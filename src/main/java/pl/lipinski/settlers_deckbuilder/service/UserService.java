package pl.lipinski.settlers_deckbuilder.service;

import pl.lipinski.settlers_deckbuilder.dao.dto.LoginDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginResponseDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.RegisterDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.UserDto;
import pl.lipinski.settlers_deckbuilder.util.exception.EmailTakenException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;
import pl.lipinski.settlers_deckbuilder.util.exception.WrongCredentialsException;

public interface UserService {

    Iterable<UserDto> getAll();

    UserDto findByEmail(String email) throws UserNotFoundException;

    LoginResponseDto authenticate(LoginDto loginDto) throws UserNotFoundException, WrongCredentialsException;

    void deleteByEmail(String email) throws UserNotFoundException, PermissionDeniedException;

    UserDto register(RegisterDto registerDto) throws EmailTakenException;

}
