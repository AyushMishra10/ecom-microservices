package com.ecom.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
    }

    @Test
    void testFetchAllUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        
        List<User> users = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(users);
        
        List<User> result = userService.fetchAllUsers();
        
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFetchUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        Optional<User> result = userService.fetchUser(1L);
        
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        assertEquals("Doe", result.get().getLastName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testFetchUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        Optional<User> result = userService.fetchUser(999L);
        
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void testAddUser() {
        userService.addUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserSuccess() {
        User updatedUser = new User();
        updatedUser.setFirstName("JohnUpdated");
        updatedUser.setLastName("DoeUpdated");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        
        boolean result = userService.updateUser(1L, updatedUser);
        
        assertTrue(result);
        assertEquals("JohnUpdated", user.getFirstName());
        assertEquals("DoeUpdated", user.getLastName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdateUserNotFound() {
        User updatedUser = new User();
        updatedUser.setFirstName("JohnUpdated");
        updatedUser.setLastName("DoeUpdated");
        
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        boolean result = userService.updateUser(999L, updatedUser);
        
        assertFalse(result);
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }
}
