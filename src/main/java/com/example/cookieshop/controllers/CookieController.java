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
        if (session.getAttribute("cookieref") == null) session.setAttribute("cookieref", new Cookie(1,"Ostekage", 5));
        return "index";
    }

    @GetMapping("/basket")
    public String basket(HttpSession session, Model basketModel){
        Basket basket = (Basket) session.getAttribute("basket");
        if (basket==null) basket = new Basket(new ArrayList<>());
        session.setAttribute("basket", basket);

        //price
        int totalPrice = 0;
        for (Cookie cookie:basket.getCookieList()) totalPrice += cookie.getPrice();
        basketModel.addAttribute("totalPrice", totalPrice);
        return "basket";
    }

    @GetMapping("/shop")
    public String shop(HttpSession session, Model cookieModel){
        cookieModel.addAttribute("cookies",repo.getAllCookies());
        return "shop";
    }

    @GetMapping("/addToBasket")
    public String add(HttpSession session, @RequestParam int id){
        //get cookie with id
        Cookie cookie = repo.getCookieById(id);

        //get basket from session
        Basket basket = (Basket) session.getAttribute("basket");

        //get cookielist from basket
        if (basket==null) basket = new Basket(new ArrayList<>());
        List<Cookie> cookies = basket.getCookieList();

        //add cookie to cookielist
        cookies.add(cookie);

        //set basket to cookielist
        basket.setCookieList(cookies);

        //add basket to session
        session.setAttribute("basket", basket);
        return "redirect:/";
    }

    //setAttribute
    @GetMapping("/setcookie")
    public String setCookie(HttpSession session){
        Cookie myCookie = new Cookie(43, "Chokolademuffin", 16);
        session.setAttribute("cookieref", myCookie);
        return "index";
    }

    //getAttribute
    @GetMapping("/getcookie")
    public String getCookie(HttpSession session){
        Cookie cookie = (Cookie) session.getAttribute("cookieref");
        cookie.setName("Mighty Chokolate Donut");
        session.setAttribute("cookieref", cookie);
        return "index";
    }
}
