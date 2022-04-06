package com.example.cookieshop.controllers;

import com.example.cookieshop.models.Basket;
import com.example.cookieshop.models.Cookie;
import com.example.cookieshop.repositories.CookieRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CookieController {
    CookieRepository repo = new CookieRepository();

    @GetMapping("/")
    public String index(HttpSession session){
        return "index";
    }

    @GetMapping("/basket")
    public String showBasket(HttpSession session, Model basketModel){
        basketModel.addAttribute(session.getAttribute("basket"));
        return "basket";
    }

    @GetMapping("/shop")
    public String showShop(HttpSession session, Model cookieModel){
        cookieModel.addAttribute("cookies",repo.getAllCookies());
        return "shop";
    }

    @GetMapping("/addToBasket")
    public String add(HttpSession session, @RequestParam int id, Model cookieModel){
        //get cookie with id
        Cookie cookie= repo.getCookieById(id);


        //get basket from session
        Basket basket = (Basket) session.getAttribute("basket");

        //get cookielist from basket
        List<Cookie> cookies = basket.getCookieList();
        //add cookie to cookielist
        cookies.add(cookie);
        //set basket to cookielist
        basket.setCookieList(cookies);

        //add basket to model for Thymeleaf
        //cookieModel.addAttribute("basket", basket);

        //add basket to session
        session.setAttribute("basket", cookies);
        return "redirect:/";
    }
}
