package pl.lipinski.settlers_deckbuilder.controller;

import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginResponseDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.RegisterDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.UserDto;
import pl.lipinski.settlers_deckbuilder.service.UserService;
import pl.lipinski.settlers_deckbuilder.util.JwtUtil;
import pl.lipinski.settlers_deckbuilder.util.exception.EmailTakenException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;
import pl.lipinski.settlers_deckbuilder.util.exception.WrongCredentialsException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil util) {
        this.userService = userService;
        this.jwtUtil = util;
    }

    @GetMapping
    public Iterable<UserDto> getAll(){
        return userService.getAll();
    }

    @GetMapping("/getbyemail/{email}")
    public UserDto getByEmail(@PathVariable String email) throws UserNotFoundException {
        return userService.findByEmail(email);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto loginDto) throws UserNotFoundException, WrongCredentialsException {
        return userService.authenticate(loginDto);
    }

    @PatchMapping("/delete/{email}")
    public void deleteByEmail(@PathVariable String email) throws UserNotFoundException, PermissionDeniedException {
        userService.deleteByEmail(email);
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody RegisterDto registerDto) throws EmailTakenException {
        return userService.register(registerDto);
    }

}
