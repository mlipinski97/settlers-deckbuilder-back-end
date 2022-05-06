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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.lipinski.settlers_deckbuilder.dao.dto.DeckDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.UserDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.Deck;
import pl.lipinski.settlers_deckbuilder.dao.entity.User;
import pl.lipinski.settlers_deckbuilder.repository.DeckRepository;
import pl.lipinski.settlers_deckbuilder.service.DeckService;
import pl.lipinski.settlers_deckbuilder.service.UserService;
import pl.lipinski.settlers_deckbuilder.util.enums.AccessLevel;
import pl.lipinski.settlers_deckbuilder.util.enums.Role;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;

import java.util.Collections;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckServiceImplTest {
    @Mock
    AbstractAuthenticationToken authentication;
    @Mock
    private UserService userService;
    @Mock
    private DeckRepository deckRepository;

    private DeckService deckService;

    @BeforeEach
    void setUp() {
        deckService = new DeckServiceImpl(deckRepository, userService);
    }

    @Test
    @DisplayName("when deck id is provided returned deck should be correct")
    public void whenDeckIdIsProvidedThenRetrievedDeckIsCorrect() throws ElementNotFoundByIdException, PermissionDeniedException {
        //given
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User deckBaseEntityOwner = new User();
        deckBaseEntityOwner.setEmail("testemail@test.com");
        deckBaseEntityOwner.setId(10L);
        deckBaseEntityOwner.setRole(Role.USER);
        Deck deckBaseEntity = new Deck();
        deckBaseEntity.setName("deck name");
        deckBaseEntity.setAccessLevel(AccessLevel.PRIVATE.getAccessLevel());
        deckBaseEntity.setOwner(deckBaseEntityOwner);
        deckBaseEntity.setId(-1L);
        //when
        when(authentication.getName()).thenReturn("testemail@test.com");
        when(authentication.getAuthorities()).thenReturn(Collections.singleton(new SimpleGrantedAuthority("USER")));
        when(deckRepository.findById(-1L)).thenReturn(of(deckBaseEntity));
        //then
        DeckDto testDeck = deckService.findById(-1L);
        verify(deckRepository).findById(anyLong());
        assertNotNull(testDeck);
        assertEquals("deck name", testDeck.getName());
        assertEquals(10L, testDeck.getOwnerId());
        assertEquals("testemail@test.com", testDeck.getOwnerEmail());
        assertEquals(AccessLevel.PRIVATE.getAccessLevel(), testDeck.getAccessLevel());
    }

    @Test
    @DisplayName("when wrong id is provided ElementNotFoundByIdException is thrown")
    public void whenWrongDeckIdIsProvidedElementNotFoundByIdExceptionIsThrown() {
        //given
        Long wrongId = -1L;
        //then
        assertThrows(ElementNotFoundByIdException.class, () -> deckService.findById(wrongId));
    }

    @Test
    @DisplayName("when call with insufficient permissions is made call is denied")
    public void whenCallWithInsufficientPermissionsIsMadeCallIsDenied() {
        //given
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User deckBaseEntityOwner = new User();
        deckBaseEntityOwner.setEmail("testemail@test.com");
        deckBaseEntityOwner.setId(10L);
        deckBaseEntityOwner.setRole(Role.USER);
        Deck deckBaseEntity = new Deck();
        deckBaseEntity.setName("deck name");
        deckBaseEntity.setAccessLevel(AccessLevel.PRIVATE.getAccessLevel());
        deckBaseEntity.setOwner(deckBaseEntityOwner);
        deckBaseEntity.setId(1L);
        //when
        when(authentication.getName()).thenReturn("differenttestemail@test.com");
        when(authentication.getAuthorities()).thenReturn(Collections.singleton(new SimpleGrantedAuthority("USER")));
        when(deckRepository.findById(1L)).thenReturn(of(deckBaseEntity));
        //then
        assertThrows(PermissionDeniedException.class, () -> deckService.findById(1L));
        verify(deckRepository).findById(anyLong());


    }

    @Test
    @DisplayName("when given correct deck ID it deletes that deck")
    public void whenGivenCorrectIdItDeletesChosenDeck() throws ElementNotFoundByIdException {
        //given
        Deck deckBaseEntity = new Deck();
        deckBaseEntity.setId(1L);
        Long deckId = 1L;
        //when
        when(deckRepository.findById(anyLong())).thenReturn(of(deckBaseEntity));
        //then
        deckService.deleteById(deckId);
        verify(deckRepository).deleteById(deckId);
    }

    @Test
    @DisplayName("when given correct deck ID it deletes that deck")
    public void whenGivenIncorrectIdItThrowsElementNotFoundByIdException() throws ElementNotFoundByIdException {
        //given
        Long deckId = 1L;
        //then
        assertThrows(ElementNotFoundByIdException.class, () -> deckService.deleteById(deckId));
    }

    @Test
    @Disabled
    @DisplayName("when given deck with exisiting owner it creates empty deck")
    public void whenUserExistsItCratesNewDeck() throws UserNotFoundException {
        //given
        UserDto testUserDto = new UserDto();
        testUserDto.setEmail("test@test.com");
        DeckDto testDeckDto = new DeckDto();
        testDeckDto.setOwnerEmail("test@test.com");
        //when
        when(userService.findByEmail(anyString())).thenReturn(testUserDto);
        //then
        /*
        TODO: save tracking to be added
         */
    }
}
