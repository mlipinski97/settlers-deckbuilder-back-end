package pl.lipinski.settlers_deckbuilder.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginResponseDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.UserDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.User;
import pl.lipinski.settlers_deckbuilder.repository.UserRepository;
import pl.lipinski.settlers_deckbuilder.util.JwtUtil;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;

import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorCode.CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDto findByEmail(String email) throws UserNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email,
                        CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE.getValue()));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public LoginResponseDto authenticate(LoginDto loginDto) throws UserNotFoundException {
        try {
            UsernamePasswordAuthenticationToken upAuthtoken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
            authenticationManager.authenticate(upAuthtoken);
        } catch (Exception e) {
            throw new UserNotFoundException(loginDto.getEmail(),
                    CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE.getValue());
        }
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(loginDto.getEmail(),
                        CAN_NOT_FIND_USER_BY_EMAIL_ERROR_CODE.getValue()));
        String token = jwtUtil.generateToken(user);
        return new LoginResponseDto(token);
    }

}
