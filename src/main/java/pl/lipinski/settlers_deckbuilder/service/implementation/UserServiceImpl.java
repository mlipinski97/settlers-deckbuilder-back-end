package pl.lipinski.settlers_deckbuilder.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginResponseDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.RegisterDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.UserDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.User;
import pl.lipinski.settlers_deckbuilder.repository.UserRepository;
import pl.lipinski.settlers_deckbuilder.service.UserService;
import pl.lipinski.settlers_deckbuilder.util.JwtUtil;
import pl.lipinski.settlers_deckbuilder.util.enums.Role;
import pl.lipinski.settlers_deckbuilder.util.exception.EmailTakenException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;
import pl.lipinski.settlers_deckbuilder.util.exception.WrongCredentialsException;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorCode.*;
import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorMessage.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = new ModelMapper();
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Iterable<UserDto> getAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> modelMapper.map(u, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email,
                        CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE.getValue()));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public LoginResponseDto authenticate(LoginDto loginDto) throws UserNotFoundException, WrongCredentialsException {
        try {
            UsernamePasswordAuthenticationToken upAuthToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
            authenticationManager.authenticate(upAuthToken);
        } catch (Exception e) {
            throw new WrongCredentialsException(WRONG_CREDENTIALS_ERROR_MESSAGE.getMessage(),
                    WRONG_CREDENTIALS_ERROR_CODE.getValue());
        }
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(loginDto.getEmail(),
                        CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE.getValue()));
        String token = jwtUtil.generateToken(user);
        return new LoginResponseDto(token);
    }

    @Override
    public void deleteByEmail(String email) throws UserNotFoundException, PermissionDeniedException {
        validatePermission(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        email,
                        CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE.getValue()
                ));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public UserDto register(RegisterDto registerDto) throws EmailTakenException {
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new EmailTakenException(
                    EMAIL_ALREADY_TAKEN_ERROR_MESSAGE.getMessage() + registerDto.getEmail(),
                    EMAIL_ALREADY_TAKEN_ERROR_CODE.getValue()
            );
        }
        User newUser = modelMapper.map(registerDto, User.class);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole(Role.USER);
        newUser.setIsActive(true);
        return modelMapper.map(userRepository.save(newUser), UserDto.class);
    }

    private void validatePermission(String email) throws PermissionDeniedException {
        ArrayList<? extends GrantedAuthority> authorities = new ArrayList<>(SecurityContextHolder
                .getContext().getAuthentication().getAuthorities());
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals(email)
                && !authorities.get(0).getAuthority().equals(Role.ADMIN.getRole())) {
            throw new PermissionDeniedException(
                    USER_DONT_HAVE_PERMISSIONS_ERROR_MESSAGE.getMessage(),
                    USER_DONT_HAVE_PERMISSIONS_ERROR_CODE.getValue()
            );
        }
    }

}
