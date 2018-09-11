package com.mlynarz.ardena.service;

import com.mlynarz.ardena.exception.BadRequestException;
import com.mlynarz.ardena.exception.ResourceNotFoundException;
import com.mlynarz.ardena.model.*;
import com.mlynarz.ardena.payload.Response.PagedResponse;
import com.mlynarz.ardena.payload.Request.EventRequest;
import com.mlynarz.ardena.payload.Response.EventResponse;
import com.mlynarz.ardena.payload.Request.VoteRequest;
import com.mlynarz.ardena.repository.EventRepository;
import com.mlynarz.ardena.repository.UserRepository;
import com.mlynarz.ardena.repository.VoteRepository;
import com.mlynarz.ardena.security.jwt.UserPrincipal;
import com.mlynarz.ardena.util.AppConstants;
import com.mlynarz.ardena.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    public PagedResponse<EventResponse> getAllEvents(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Events
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Event> events = eventRepository.findAll(pageable);

        if (events.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), events.getNumber(),
                    events.getSize(), events.getTotalElements(), events.getTotalPages(), events.isLast());
        }

        // Map Events to EventResponses containing vote counts and event creator details
        List<Long> eventIds = events.map(Event::getId).getContent();
        Map<Long, Long> optionVoteCountMap = getOptionVoteCountMap(eventIds);
        Map<Long, Long> eventUserVoteMap = getEventUserVoteMap(currentUser, eventIds);
        Map<Long, User> creatorMap = getEventCreatorMap(events.getContent());

        List<EventResponse> eventResponses = events.map(event -> ModelMapper.mapEventToEventResponse(event,
                optionVoteCountMap,
                creatorMap.get(event.getCreatedBy()),
                eventUserVoteMap == null ? null : eventUserVoteMap.getOrDefault(event.getId(), null))).getContent();

        return new PagedResponse<>(eventResponses, events.getNumber(),
                events.getSize(), events.getTotalElements(), events.getTotalPages(), events.isLast());
    }

    public PagedResponse<EventResponse> getEventsCreatedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all events created by the given username
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Event> events = eventRepository.findByCreatedBy(user.getId(), pageable);

        if (events.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), events.getNumber(),
                    events.getSize(), events.getTotalElements(), events.getTotalPages(), events.isLast());
        }

        // Map Event to EventResponses containing vote counts and event creator details
        List<Long> eventIds = events.map(Event::getId).getContent();
        Map<Long, Long> optionVoteCountMap = getOptionVoteCountMap(eventIds);
        Map<Long, Long> eventUserVoteMap = getEventUserVoteMap(currentUser, eventIds);

        List<EventResponse> eventResponses = events.map(event -> ModelMapper.mapEventToEventResponse(event,
                optionVoteCountMap,
                user,
                eventUserVoteMap == null ? null : eventUserVoteMap.getOrDefault(event.getId(), null))).getContent();

        return new PagedResponse<>(eventResponses, events.getNumber(),
                events.getSize(), events.getTotalElements(), events.getTotalPages(), events.isLast());
    }

    public PagedResponse<EventResponse> getEventsVotedBy(String username, UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // Retrieve all eventIds in which the given username has voted
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Page<Long> userVotedEventIds = voteRepository.findVotedEventIdsByUserId(user.getId(), pageable);

        if (userVotedEventIds.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), userVotedEventIds.getNumber(),
                    userVotedEventIds.getSize(), userVotedEventIds.getTotalElements(),
                    userVotedEventIds.getTotalPages(), userVotedEventIds.isLast());
        }

        // Retrieve all event details from the voted eventIds.
        List<Long> eventIds = userVotedEventIds.getContent();

        Sort sort = new Sort(Sort.Direction.DESC, "createdAt");
        List<Event> events = eventRepository.findByIdIn(eventIds, sort);

        // Map Events to EventResponses containing vote counts and event creator details
        Map<Long, Long> optionVoteCountMap = getOptionVoteCountMap(eventIds);
        Map<Long, Long> eventUserVoteMap = getEventUserVoteMap(currentUser, eventIds);
        Map<Long, User> creatorMap = getEventCreatorMap(events);

        List<EventResponse> eventResponses = events.stream().map(event -> ModelMapper.mapEventToEventResponse(event,
                optionVoteCountMap,
                creatorMap.get(event.getCreatedBy()),
                eventUserVoteMap == null ? null : eventUserVoteMap.getOrDefault(event.getId(), null))).collect(Collectors.toList());

        return new PagedResponse<>(eventResponses, userVotedEventIds.getNumber(), userVotedEventIds.getSize(), userVotedEventIds.getTotalElements(), userVotedEventIds.getTotalPages(), userVotedEventIds.isLast());
    }

    public Event createEvent(EventRequest eventRequest) {
        Event event = new Event();
        event.setDescription(eventRequest.getDescription());

        eventRequest.getOptions().forEach(optionRequest -> event.addOption(new Option(optionRequest.getText())));

        event.setEventDate(eventRequest.getEventDate());

        return eventRepository.save(event);
    }

    public EventResponse getEventById(Long eventId, UserPrincipal currentUser) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new ResourceNotFoundException("Event", "id", eventId));

        // Retrieve Vote Counts of every option belonging to the current event
        List<OptionVoteCount> votes = voteRepository.countByEventIdGroupByOptionId(eventId);

        Map<Long, Long> optionVotesMap = votes.stream()
                .collect(Collectors.toMap(OptionVoteCount::getOptionId, OptionVoteCount::getVoteCount));

        // Retrieve event creator details
        User creator = userRepository.findById(event.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", event.getCreatedBy()));

        // Retrieve vote done by logged in user
        Vote userVote = null;
        if (currentUser != null) {
            userVote = voteRepository.findByUserIdAndEventId(currentUser.getId(), eventId);
        }

        return ModelMapper.mapEventToEventResponse(event, optionVotesMap,
                creator, userVote != null ? userVote.getOption().getId() : null);
    }

    public EventResponse castVoteAndGetUpdatedEvent(Long eventId, VoteRequest voteRequest, UserPrincipal currentUser) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (event.getEventDate().isBefore(Instant.now())) {
            throw new BadRequestException("Sorry! This Event has already expired");
        }

        User user = userRepository.getOne(currentUser.getId());

        Option selectedOption = event.getOptions().stream()
                .filter(option -> option.getId().equals(voteRequest.getOptionId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Option", "id", voteRequest.getOptionId()));

        Vote vote = new Vote();
        vote.setEvent(event);
        vote.setUser(user);
        vote.setOption(selectedOption);

        try {
            vote = voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) {
            logger.info("User {} has already voted in Event {}", currentUser.getId(), eventId);
            throw new BadRequestException("Sorry! You have already cast your vote in this event");
        }

        //-- Vote Saved, Return the updated Event Response now --

        // Retrieve Vote Counts of every option belonging to the current event
        List<OptionVoteCount> votes = voteRepository.countByEventIdGroupByOptionId(eventId);

        Map<Long, Long> optionVotesMap = votes.stream()
                .collect(Collectors.toMap(OptionVoteCount::getOptionId, OptionVoteCount::getVoteCount));

        // Retrieve event creator details
        User creator = userRepository.findById(event.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", event.getCreatedBy()));

        return ModelMapper.mapEventToEventResponse(event, optionVotesMap, creator, vote.getOption().getId());
    }


    private void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }

    private Map<Long, Long> getOptionVoteCountMap(List<Long> eventIds) {
        // Retrieve Vote Counts of every Option belonging to the given eventIds
        List<OptionVoteCount> votes = voteRepository.countByEventIdInGroupByOptionId(eventIds);

        return votes.stream().collect(Collectors.toMap(OptionVoteCount::getOptionId, OptionVoteCount::getVoteCount));
    }

    private Map<Long, Long> getEventUserVoteMap(UserPrincipal currentUser, List<Long> eventIds) {
        // Retrieve Votes done by the logged in user to the given eventIds
        Map<Long, Long> eventUserVoteMap = null;
        if (currentUser != null) {
            List<Vote> userVotes = voteRepository.findByUserIdAndEventIdIn(currentUser.getId(), eventIds);

            eventUserVoteMap = userVotes.stream()
                    .collect(Collectors.toMap(vote -> vote.getEvent().getId(), vote -> vote.getOption().getId()));
        }
        return eventUserVoteMap;
    }

    Map<Long, User> getEventCreatorMap(List<Event> events) {
        // Get Event Creator details of the given list of events
        List<Long> creatorIds = events.stream()
                .map(Event::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
}
