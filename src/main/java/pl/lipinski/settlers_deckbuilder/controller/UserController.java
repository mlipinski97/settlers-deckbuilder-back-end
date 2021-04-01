package pl.lipinski.settlers_deckbuilder.controller;

import org.springframework.web.bind.annotation.*;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginResponseDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.UserDto;
import pl.lipinski.settlers_deckbuilder.service.UserService;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getbymail/{email}")
    public UserDto getByEmailLike(@PathVariable String email) throws UserNotFoundException {
        return userService.findByEmail(email);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto loginDto) throws UserNotFoundException {
        return userService.authenticate(loginDto);
    }
}
