package org.example.basedomain.Controller;

import jakarta.validation.Valid;
import org.example.basedomain.Dto.LoginRequest;
import org.example.basedomain.Dto.UserDetailsDTO;
import org.example.basedomain.Entity.UserEntity;
import org.example.basedomain.Service.UserService;
import org.example.basedomain.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create")
    public ResponseEntity<UserEntity> create(@Valid @RequestBody UserEntity user)  {
        log.info("User created"+" "+user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest user) {
        log.info("login user"+" "+user);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtil.genrateToken(user.getUsername(),15);
        return ResponseEntity.ok(token);
    }
    @GetMapping("/userslist")
    public ResponseEntity<List<UserDetailsDTO>> getAllUsers() {
        log.info("getAllUsers");
        List<UserDetailsDTO> userDetailsDTOS = userService.getListOfUser();
        return ResponseEntity.ok(userDetailsDTOS);
    }

    @GetMapping("/info")
    public ResponseEntity<UserDetailsDTO> getUserInfo(@RequestParam String username) {
        UserEntity user = userService.loadUserByUsername(username);
        UserDetailsDTO dto = new UserDetailsDTO(user.getUsername(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(dto);
    }
}
