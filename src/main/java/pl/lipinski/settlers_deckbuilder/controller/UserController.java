package pl.lipinski.settlers_deckbuilder.controller;

import org.springframework.web.bind.annotation.*;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginResponseDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.RegisterDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.UserDto;
import pl.lipinski.settlers_deckbuilder.service.UserService;
import pl.lipinski.settlers_deckbuilder.util.exception.EmailTakenException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;
import pl.lipinski.settlers_deckbuilder.util.exception.WrongCredentialsException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Iterable<UserDto> getAll(){
        return userService.getAll();
    }

    @GetMapping("/getbymail/{email}")
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
