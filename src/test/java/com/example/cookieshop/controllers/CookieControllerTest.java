package com.example.cookieshop.controllers;

import com.example.cookieshop.models.Basket;
import com.example.cookieshop.models.Cookie;
import com.example.cookieshop.repositories.CookieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
//WebMvcTest only tests controller
//mock services and repositories
//faster than SpringBootTest
//use when only want to test controller
//@WebMvcTest(CookieController.class)
//@ExtendWith(MockitoExtension.class)
//SpringBootTest initializes complete application context
//slower than WebMvcTest
//use when integration testing and want to include testing af services and repositories
@SpringBootTest
//Initialize a MockMvc used to simulate calls to the controller and make assertions about the results
@AutoConfigureMockMvc
public class CookieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CookieRepository cookieRepository;

    @BeforeEach
    void setUp() {
        // Mocking data for testing - Mock repository with default behavior
        Cookie cookie1 = new Cookie(1, "Cookie1", 10);
        Cookie cookie2 = new Cookie(2, "Cookie2", 20);
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(cookie1);
        cookies.add(cookie2);
        //set up behavior for mock repository - when getAllCookies() is called it returns the cookies collection
        when(cookieRepository.getAllCookies()).thenReturn(cookies);
    }

    @Test
    void testIndexPage() throws Exception {
        //MockMvcBuilder creates HTTP request (GET, POST, PUT, DELETE...)
        //perform() execute the request and returns resultActions that can be used to chain expectations
        //MockMvcResultMatchers is used to match expectations with .andExpect()
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"));
    }

    @Test
    void testShopPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/shop"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("shop"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cookies"));
    }

    @Test
    void testAddToBasket() throws Exception {
        //MockMvcRequestBuilders can use method chaining to build the request like .get().param().sessionAttr()
        mockMvc.perform(MockMvcRequestBuilders.get("/addToBasket").param("id", "1").sessionAttr("basket", new Basket(new ArrayList<>())))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/basket"));
    }

    @Test
    void testBasketPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/basket").sessionAttr("basket", new Basket(new ArrayList<>())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("basket"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("totalPrice"));
    }

}

