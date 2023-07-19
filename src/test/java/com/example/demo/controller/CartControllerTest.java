package com.example.demo.controller;

import static com.example.demo.CommonTest.addUser;
import static com.example.demo.CommonTest.addItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {
    @InjectMocks
    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setup(){
        when(userRepository.findByUsername("huyhue")).thenReturn(addUser());
        when(itemRepository.findById(any())).thenReturn(Optional.of(addItem(1)));
    }

    @Test
    public void testAddToCart() {
        ModifyCartRequest modify = new ModifyCartRequest();
        modify.setUsername("huyhue");
        modify.setItemId(1L);
        modify.setQuantity(2);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(modify);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        Cart cartAdd = responseEntity.getBody();
        assertEquals(cartAdd.getItems().size(), 4);
    }
    
    @Test
    public void testRemoveFromCart() {
        Item item1 = new Item(1L, "Round Widget", BigDecimal.valueOf(2.99), "A widget that is round");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        ModifyCartRequest modify = new ModifyCartRequest();
        modify.setUsername("huyhue");
        modify.setItemId(1L);
        modify.setQuantity(1);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modify);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        Cart cartRemove = responseEntity.getBody();
        assertEquals(cartRemove.getItems().size(), 1);
    }
    
    @Test
    public void testFailByUsername(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("failhuyhue");
        request.setItemId(1L);
        request.setQuantity(1);
        when(userRepository.findByUsername("failhuyhue")).thenReturn(null);
        ResponseEntity<Cart> responseUserMissing = cartController.removeFromcart(request);
        assertNotNull(responseUserMissing);
        assertEquals(404, responseUserMissing.getStatusCodeValue());
    }
}
