package com.ecom.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        
        User savedUser = userRepository.save(user);
        
        assertNotNull(savedUser.getId());
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        entityManager.persist(user1);
        
        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        entityManager.persist(user2);
        entityManager.flush();
        
        List<User> users = userRepository.findAll();
        
        assertEquals(2, users.size());
    }

    @Test
    void testFindUserById() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        entityManager.persist(user);
        entityManager.flush();
        
        Optional<User> foundUser = userRepository.findById(user.getId());
        
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
        assertEquals("Doe", foundUser.get().getLastName());
    }

    @Test
    void testFindUserByIdNotFound() {
        Optional<User> foundUser = userRepository.findById(999L);
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        entityManager.persist(user);
        entityManager.flush();
        
        userRepository.deleteById(user.getId());
        Optional<User> foundUser = userRepository.findById(user.getId());
        
        assertFalse(foundUser.isPresent());
    }
}
