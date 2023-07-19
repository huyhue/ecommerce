package com.example.demo;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.example.demo.controller.CartControllerTest;
import com.example.demo.controller.ItemControllerTest;
import com.example.demo.controller.OrderControllerTest;
import com.example.demo.controller.UserControllerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {CartControllerTest.class, UserControllerTest.class,
                ItemControllerTest.class, OrderControllerTest.class}
)
public class ECommerceApplicationTests {


}
