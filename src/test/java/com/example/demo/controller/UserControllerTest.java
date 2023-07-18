package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void testAddUser(){
        when(encoder.encode("huyhue123")).thenReturn("passhash");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("huyhue");
        request.setPassword("huyhue123");
        request.setConfirmPassword("huyhue123");
        ResponseEntity<User> response = userController.createUser(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("huyhue", user.getUsername());
        assertEquals("passhash", user.getPassword());
    }

    @Test
    public void testUserById(){
        User user = new User();
        user.setUsername("huyhue");
        user.setPassword("huyhue123");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User ex = response.getBody();
        assertNotNull(ex);
        assertEquals(1L, ex.getId());
        assertEquals("huyhue", ex.getUsername());
        assertEquals("huyhue123", ex.getPassword());
    }

    @Test
    public void testUserByUsername(){
    	User user = new User();
        user.setUsername("huyhue");
        user.setPassword("huyhue123");
        when(userRepository.findByUsername("huyhue")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("huyhue");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        User ex = response.getBody();
        assertNotNull(ex);
        assertEquals("huyhue", ex.getUsername());
        assertEquals("huyhue123", ex.getPassword());
    }

}
