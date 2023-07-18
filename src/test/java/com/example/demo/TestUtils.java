package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject) {
        boolean wasPrivate = false;
        try {
            Field declaredField = target.getClass().getDeclaredField(fieldName);
            if(!declaredField.isAccessible()){
                declaredField.setAccessible(true);
                wasPrivate = true;
            }
            declaredField.set(target, toInject);
            if(wasPrivate){
                declaredField.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static User addUser() {
        User u = new User();
        u.setId(1L);
        u.setUsername("huyhue");
        u.setPassword("huyhue123");
        u.setCart(createCart(u));
        return u;
    }

    public static Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setId(1L);
        List<Item> items = addItem();
        cart.setItems(addItem());
        cart.setTotal(items.stream().map(item -> item.getPrice()).reduce(BigDecimal::add).get());
        cart.setUser(user);
        return cart;
    }

    public static List<Item> addItem() {
        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            items.add(createItem(i));
        }
        return items;
    }

    public static Item createItem(long id){
        Item i = new Item();
        i.setId(id);
        i.setPrice(BigDecimal.valueOf(id * 1.2));
        i.setName("Item " + i.getId());
        i.setDescription("ok item");
        return i;
    }

    public static List<UserOrder> addOrder(){
        List<UserOrder> list = new ArrayList<>();
        IntStream.range(0,2).forEach(i -> {
            UserOrder o = new UserOrder();
            Cart cart = createCart(addUser());
            o.setItems(cart.getItems());
            o.setTotal(cart.getTotal());
            o.setUser(addUser());
            o.setId(Long.valueOf(i));
            list.add(o);
        });
        return list;
    }
}
