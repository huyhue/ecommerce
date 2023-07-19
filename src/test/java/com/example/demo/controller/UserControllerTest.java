package com.example.demo.controller;

import com.example.demo.CommonTest;
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
import static org.junit.Assert.assertNull;
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
        CommonTest.injectObjects(userController, "userRepository", userRepository);
        CommonTest.injectObjects(userController, "cartRepository", cartRepository);
        CommonTest.injectObjects(userController, "bCryptPasswordEncoder", encoder);
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
        User u = response.getBody();
        assertEquals("huyhue", u.getUsername());
        assertEquals("passhash", u.getPassword());
    }
    
    @Test
    public void testUserByUsername(){
    	User user = new User();
        user.setUsername("huyhue");
        user.setPassword("huyhue123");
        when(userRepository.findByUsername("huyhue")).thenReturn(user);
        ResponseEntity<User> response = userController.findByUserName("huyhue");
        assertEquals(200, response.getStatusCodeValue());
        User ex = response.getBody();
        assertNotNull(ex);
        assertEquals("huyhue", ex.getUsername());
        assertEquals("huyhue123", ex.getPassword());
    }

    @Test
    public void testUserById(){
        User user = new User();
        user.setUsername("huyhue");
        user.setPassword("huyhue123");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        ResponseEntity<User> response = userController.findById(1L);
        assertEquals(200, response.getStatusCodeValue());
        User ex = response.getBody();
        assertNotNull(ex);
        assertEquals("huyhue", ex.getUsername());
        assertEquals("huyhue123", ex.getPassword());
    }

    @Test
    public void testUserFail() {
    	final ResponseEntity<User> responseByName = userController.findByUserName("notAName");
        assertNull(responseByName.getBody());
        final ResponseEntity<User> responseByID = userController.findById(2L);
        assertNull(responseByID.getBody());
    }
}
