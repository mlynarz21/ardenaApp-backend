package com.mlynarz.ardena.Repository;

import com.mlynarz.ardena.model.Role;
import com.mlynarz.ardena.model.RoleName;
import com.mlynarz.ardena.repository.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HorseRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void whenFindByName_thenReturnRole() {
        // given
        Role userRole = new Role(RoleName.ROLE_USER);
        entityManager.persist(userRole);
        entityManager.flush();

        // when
        Role found = roleRepository.findByName(RoleName.ROLE_USER).get();

        // then
        assertEquals(RoleName.ROLE_USER, found.getName());
    }

    @Test
    public void whenFindByNameOtherRole_thenReturnEmpty() {
        // given
        Role userRole = new Role(RoleName.ROLE_USER);
        entityManager.persist(userRole);
        entityManager.flush();

        // when
        Optional<Role> found = roleRepository.findByName(RoleName.ROLE_ADMIN);

        // then
        assertFalse(found.isPresent());
    }

}


