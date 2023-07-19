package com.example.demo.controller;

import static com.example.demo.CommonTest.addItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {
	@InjectMocks
	private ItemController itemController;

	@Mock
	private ItemRepository itemRepository;

	@Before
    public void setup(){
        when(itemRepository.findById(1L)).thenReturn(Optional.of(addItem(1)));
        when(itemRepository.findAll()).thenReturn(addItem());
        when(itemRepository.findByName("item")).thenReturn(Arrays.asList(addItem(1), addItem(2)));
    }

	@Test
	public void testListItem() {
		ResponseEntity<List<Item>> response = itemController.getItems();
		assertEquals(200, response.getStatusCodeValue());
		List<Item> items = response.getBody();
		assertEquals(addItem(), items);
	}
	
	@Test
	public void testItemById() {
		ResponseEntity<Item> response = itemController.getItemById(1L);
		Item item = response.getBody();
		assertEquals(addItem(1L), item);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
	}
	
	@Test
	public void testItemInvalid() {
		ResponseEntity<List<Item>> response = itemController.getItemsByName("asasas");
		assertNull(response.getBody());
		assertEquals(404, response.getStatusCodeValue());
		
		ResponseEntity<Item> responseID = itemController.getItemById(3L);
        assertEquals(404, responseID.getStatusCodeValue());
        assertNull(responseID.getBody());
	}
	
	@Test
	public void testItemByName() {
		ResponseEntity<List<Item>> response = itemController.getItemsByName("item");
		
		List<Item> items = Arrays.asList(addItem(1), addItem(2));
		assertEquals(addItem(), items);
		assertEquals(200, response.getStatusCodeValue());
	}

}
