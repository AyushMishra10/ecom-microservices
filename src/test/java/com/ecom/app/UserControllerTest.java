package com.ecom.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        
        List<User> users = Arrays.asList(user1, user2);
        when(userService.fetchAllUsers()).thenReturn(users);
        
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
        
        verify(userService, times(1)).fetchAllUsers();
    }

    @Test
    void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        
        when(userService.fetchUser(1L)).thenReturn(Optional.of(user));
        
        mockMvc.perform(get("/api/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
        
        verify(userService, times(1)).fetchUser(1L);
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        when(userService.fetchUser(999L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/api/999"))
                .andExpect(status().isNotFound());
        
        verify(userService, times(1)).fetchUser(999L);
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        
        doNothing().when(userService).addUser(any(User.class));
        
        mockMvc.perform(post("/api/addUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User added Successfully"));
        
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        User updatedUser = new User();
        updatedUser.setFirstName("JohnUpdated");
        updatedUser.setLastName("DoeUpdated");
        
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(true);
        
        mockMvc.perform(put("/api/updateUser/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(content().string("User updated successfully"));
        
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        User updatedUser = new User();
        updatedUser.setFirstName("JohnUpdated");
        updatedUser.setLastName("DoeUpdated");
        
        when(userService.updateUser(eq(999L), any(User.class))).thenReturn(false);
        
        mockMvc.perform(put("/api/updateUser/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isNotFound());
        
        verify(userService, times(1)).updateUser(eq(999L), any(User.class));
    }
}
