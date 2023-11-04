package com.nurfet.fetchapi.controller;

import com.nurfet.fetchapi.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Objects;

@Controller
public class MainController {

    @GetMapping("/")
    public String mainPage(Model model, HttpSession session, @Nullable Authentication auth) {
        return getPage(model, session, auth);
    }


    public String getPage(Model model, HttpSession session, @Nullable Authentication auth) {

        if (Objects.isNull(auth)) {
            model.addAttribute("authenticatedName", session.getAttribute("authenticatedName"));
            session.removeAttribute("authenticatedName");

            model.addAttribute("authenticationException", session.getAttribute("authenticationException"));
            session.removeAttribute("authenticationException");

            return "login-page";
        }

        User user = (User) auth.getPrincipal();
        model.addAttribute("user", user);

        if (user.hasRole("ROLE_ADMIN")) {
            return "index";
        }

        if (user.hasRole("ROLE_USER")) {
            return "index";
        }

        return "access-denied-page";
    }
}
