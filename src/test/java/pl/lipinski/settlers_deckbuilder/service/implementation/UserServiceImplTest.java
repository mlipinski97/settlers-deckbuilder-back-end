package pl.lipinski.settlers_deckbuilder.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lipinski.settlers_deckbuilder.dao.dto.LoginDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.UserDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.User;
import pl.lipinski.settlers_deckbuilder.repository.UserRepository;
import pl.lipinski.settlers_deckbuilder.service.UserService;
import pl.lipinski.settlers_deckbuilder.util.JwtUtil;
import pl.lipinski.settlers_deckbuilder.util.enums.Role;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;
import pl.lipinski.settlers_deckbuilder.util.exception.WrongCredentialsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    ModelMapper modelMapper = new ModelMapper();

    @Mock
    AbstractAuthenticationToken authentication;
    @Mock
    UserRepository userRepository;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtil jwtUtil;

    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository,
                authenticationManager,
                jwtUtil,
                passwordEncoder);
    }

    //this as well as tested method getAll() is only for testing/admin work, normals user has no way to access it
    @Test
    @DisplayName("when get all is called it returns all elements")
    public void whenGetAllIsCalledItReturnsAllElements() {
        //given
        List<User> testUserList = new ArrayList<>();
        testUserList.add(new User(1L, "test1email@test.com", "password", Role.USER, true));
        testUserList.add(new User(2L, "test2email@test.com", "password", Role.ADMIN, true));
        testUserList.add(new User(3L, "test3email@test.com", "password", Role.USER, true));
        List<UserDto> testUserDtoList = testUserList.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        //when
        when(userRepository.findAll()).thenReturn(testUserList);
        //then
        List<UserDto> receivedUserList = StreamSupport
                .stream(userService.getAll().spliterator(), false)
                .collect(Collectors.toList());
        assertEquals(receivedUserList, testUserDtoList);
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("when findByEmail is called and there is matching email user object is returned")
    public void whenFindByEmailIsCalledAndThereIsMatchingUserIsReturned() throws UserNotFoundException {
        //given
        String testEmail = "test@test.com";
        User testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setId(1L);
        testUser.setRole(Role.USER);
        testUser.setIsActive(true);
        testUser.setPassword("password");
        //when
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        //then
        UserDto receivedUserDto = userService.findByEmail(testEmail);
        verify(userRepository).findByEmail(testEmail);
        assertEquals(receivedUserDto.getEmail(), testUser.getEmail());
        assertEquals(receivedUserDto.getRole(), testUser.getRole().getRole());
        assertEquals(receivedUserDto.getId(), testUser.getId());
        assertEquals(receivedUserDto.getIsActive(), testUser.getIsActive());
    }

    @Test
    @DisplayName("when findByEmail is called and there is no matching email it throws UserNotFoundException")
    public void whenFindByEmailIsCalledAndThereIsNoMatchingUserItThrowsUserNotFoundException() {
        //given
        String testEmail = "test@test.com";
        //then
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(testEmail));
        verify(userRepository).findByEmail(testEmail);
    }

    @Test
    @DisplayName("when given correct credentials it authenticates the user")
    public void whenGivenCorrectCredentialsItAuthenticates() throws UserNotFoundException, WrongCredentialsException {
        //given
        String testEmail = "test@test.com";
        String testPassword = "password";
        User testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setPassword(testPassword);
        LoginDto testLoginDto = new LoginDto();
        testLoginDto.setEmail(testEmail);
        testLoginDto.setPassword(testPassword);
        //when
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        //then
        userService.authenticate(testLoginDto);
        verify(jwtUtil).generateToken(any());
        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail(testEmail);
    }

    @Test
    @DisplayName("when given email that has no matching account in database it throws UserNotFoundException")
    public void whenGivenNonMatchingEmailItThrowsUserNotFoundException() {
        //given
        String testEmail = "test@test.com";
        String testPassword = "password";
        User testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setPassword(testPassword);
        LoginDto testLoginDto = new LoginDto();
        testLoginDto.setEmail(testEmail);
        testLoginDto.setPassword(testPassword);
        //when
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        //then
        assertThrows(UserNotFoundException.class, () -> userService.authenticate(testLoginDto));
        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByEmail(testEmail);
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    @Disabled
    @DisplayName("when given wrong authentication credentials it throws WrongCredentialsException")
    public void whenGivenWrongCredentialsItWrongCredentialsException() {
        //given
        String testEmail = "test@test.com";
        String testPassword = "password";
        String wrongPassword = "wrongPassword";
        String wrongEmail= "wrongtest@test.com";
        User testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setPassword(testPassword);
        LoginDto testLoginDto = new LoginDto();
        testLoginDto.setEmail(testEmail);
        testLoginDto.setPassword(wrongPassword);
        //when
        when(authenticationManager.authenticate(any())).thenThrow(RuntimeException.class);
        //then
        assertThrows(WrongCredentialsException.class, () -> userService.authenticate(testLoginDto));
        verify(authenticationManager).authenticate(any());
        verify(userRepository, never()).findByEmail(testEmail);
        verify(jwtUtil, never()).generateToken(any());
    }
}
