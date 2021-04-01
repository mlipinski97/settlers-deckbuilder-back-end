package pl.lipinski.settlers_deckbuilder.service;

import pl.lipinski.settlers_deckbuilder.dao.dto.LoginDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginResponseDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.UserDto;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;

public interface UserService {
    UserDto findByEmail(String email) throws UserNotFoundException;
    LoginResponseDto authenticate(LoginDto loginDto) throws UserNotFoundException;
}
