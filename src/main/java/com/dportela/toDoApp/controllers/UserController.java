package com.dportela.toDoApp.controllers;

import com.dportela.toDoApp.models.User;
import com.dportela.toDoApp.models.UserDetailsImpl;
import com.dportela.toDoApp.repositories.UserRepository;
import com.dportela.toDoApp.responses.UserResponse;
import com.dportela.toDoApp.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody User request_user) {
        System.out.println("Logging in user...");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request_user.getUsername(), request_user.getPassword())
        );

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String token = jwtUtils.generateToken(request_user);

        return ResponseEntity.ok(new UserResponse(userDetails.getId(), userDetails.getUsername(), jwtUtils.generateAuthorizationHeader(token)));
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody User request_user) {
        System.out.println("Signing up user...");

        if (userRepository.existsByUsername(request_user.getUsername()))
        {
            return ResponseEntity.badRequest().body("Username already used");
        }

        User user = new User(request_user.getUsername(), bCryptPasswordEncoder.encode(request_user.getPassword()));

        userRepository.save(user);

        String token = jwtUtils.generateToken(user);

        return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), jwtUtils.generateAuthorizationHeader(token)));
    }

    @GetMapping("/delete")
    public ResponseEntity deleteUser() {
        int user_id = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        userRepository.deleteById(user_id);

        return ResponseEntity.ok("User deleted");
    }
}
