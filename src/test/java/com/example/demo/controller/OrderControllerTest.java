package com.example.demo.controller;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.example.demo.CommonTest.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setup(){
        User u = addUser();
        when(userRepository.findByUsername("huyhue")).thenReturn(u);
        when(orderRepository.findByUser(any())).thenReturn(addOrder());
    }

    @Test
    public void testSubmit(){
        ResponseEntity<UserOrder> response = orderController.submit("huyhue");
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        
        assertEquals(addUser().getId(), order.getUser().getId());
        assertEquals(addItem(), order.getItems());
    }

    @Test
    public void testSubmitFail(){
        ResponseEntity<UserOrder> response = orderController.submit("asasas");
        
        assertNull( response.getBody());
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void testOrderBYUser(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("huyhue");

        List<UserOrder> orders = response.getBody();
        assertEquals(addOrder().size(), orders.size());
        assertEquals(200, response.getStatusCodeValue());
    }
}
