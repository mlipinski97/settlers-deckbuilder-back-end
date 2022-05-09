package pl.lipinski.settlers_deckbuilder.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeckServiceImplTest {

    ModelMapper modelMapper = new ModelMapper();
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
        deckBaseEntity.setId(1L);
        //when
        when(authentication.getName()).thenReturn("testemail@test.com");
        when(authentication.getAuthorities()).thenReturn(Collections.singleton(new SimpleGrantedAuthority("USER")));
        when(deckRepository.findById(1L)).thenReturn(of(deckBaseEntity));
        //then
        DeckDto testDeck = deckService.findById(1L);
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
    @DisplayName("when call with insufficient permissions is made it throws PermissionDeniedException")
    public void whenCallWithInsufficientPermissionsIsMadeItThrowsPermissionDeniedException() {
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
    public void whenGivenIncorrectIdItThrowsElementNotFoundByIdException() {
        //given
        Long deckId = 1L;
        //then
        assertThrows(ElementNotFoundByIdException.class, () -> deckService.deleteById(deckId));
        verify(deckRepository, never()).save(any());
    }

    @Test
    @DisplayName("when given deck with existing owner it creates empty deck")
    public void whenUserExistsItCratesNewDeck() throws UserNotFoundException {
        //given
        UserDto testUserDto = new UserDto();
        testUserDto.setEmail("test@test.com");
        DeckDto testDeckDto = new DeckDto();
        testDeckDto.setOwnerEmail("test@test.com");
        testDeckDto.setOwnerId(10L);
        testDeckDto.setName("testName");
        testDeckDto.setAccessLevel(AccessLevel.PUBLIC.getAccessLevel());
        testDeckDto.setId(1L);
        Deck testDeck = modelMapper.map(testDeckDto, Deck.class);
        //when
        when(userService.findByEmail(anyString())).thenReturn(testUserDto);
        when(deckRepository.save(testDeck)).thenReturn(testDeck);
        //then
        deckService.addEmptyDeck(testDeckDto);
        ArgumentCaptor<Deck> deckArgumentCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(deckRepository).save(deckArgumentCaptor.capture());
        Deck capturedDeck = deckArgumentCaptor.getValue();
        assertEquals(testDeck, capturedDeck);
    }

    @Test
    @DisplayName("when user doesnt exist it throws whenUserDoesntExistItThrowsUserNotFoundException")
    public void whenUserDoesntExistItThrowsUserNotFoundException() throws UserNotFoundException {
        //given
        DeckDto testDeckDto = new DeckDto();
        testDeckDto.setOwnerEmail("test@test.com");
        testDeckDto.setOwnerId(10L);
        testDeckDto.setName("testName");
        testDeckDto.setAccessLevel(AccessLevel.PUBLIC.getAccessLevel());
        testDeckDto.setId(1L);
        //when
        when(userService.findByEmail(anyString())).thenThrow(UserNotFoundException.class);
        //then
        assertThrows(UserNotFoundException.class, () -> deckService.addEmptyDeck(testDeckDto));
        verify(deckRepository, never()).save(any());

    }

    @Test
    @DisplayName("when get all is called it returns all viable decks, that is: decks that user owns and all public decks with name like given phrase")
    public void whenGetAllIsCalledItReturnsAllDecksWithUserEmailAndPublicAccessLevel() {
        //given
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String userEmail = "testemail@test.com";
        User testUser = new User();
        testUser.setEmail(userEmail);
        Deck testDeck = new Deck();
        testDeck.setOwner(testUser);
        testDeck.setName("testName");
        testDeck.setAccessLevel(AccessLevel.PRIVATE.getAccessLevel());
        testDeck.setId(1L);
        List<DeckDto> expectedReturn = new ArrayList<>(Collections.singletonList(modelMapper.map(testDeck, DeckDto.class)));
        Page<Deck> testPage = new PageImpl<>(Collections.singletonList(testDeck));
        //when
        when(authentication.getName()).thenReturn(userEmail);
        when(deckRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(testPage);
        //then
        Iterable<DeckDto> capturedReturn = deckService.getAll(0, 10, "_d", userEmail);
        verify(deckRepository).findAll(any(Specification.class), any(Pageable.class));
        assertEquals(expectedReturn, capturedReturn);
    }

    @Test
    @DisplayName("when given correct Id and user have permission it sets chosen deck to private")
    public void whenGivenCorrectIdAndHavePermissionItSetsDeckToPrivate() throws ElementNotFoundByIdException, PermissionDeniedException {
        //given
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String userEmail = "testemail@test.com";
        User testUser = new User();
        testUser.setEmail(userEmail);
        Deck testDeck = new Deck();
        testDeck.setId(1L);
        testDeck.setAccessLevel(AccessLevel.PUBLIC.getAccessLevel());
        testDeck.setName("test deck name");
        testDeck.setOwner(testUser);
        //when
        when(authentication.getName()).thenReturn(userEmail);
        when(authentication.getAuthorities()).thenReturn(Collections.singleton(new SimpleGrantedAuthority("USER")));
        when(deckRepository.findById(any())).thenReturn(of(testDeck));
        //then
        deckService.setDeckPrivate(1L);
        verify(deckRepository).save(testDeck);
        assertEquals(AccessLevel.PRIVATE.getAccessLevel(), testDeck.getAccessLevel());
    }

    @Test
    @DisplayName("when given incorrect Id and user have permission when setting to privateit throws ElementNotFoundException")
    public void whenGivenIncorrectIdAndHavePermissionWhenSettingPrivateItThrowsElementNotFoundException() {
        //given
        Long wrongId = -1L;
        //then
        assertThrows(ElementNotFoundByIdException.class, () -> deckService.setDeckPrivate(wrongId));
        verify(deckRepository, never()).save(any());
    }

    @Test
    @DisplayName("when given correct Id and user doesnt have permission when setting private it throws PermissionDeniedException")
    public void whenGivenCorrectIdAndDontHavePermissionWhenSettingPrivateItThrowsPermissionDeniedException() {
        //given
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String userEmail = "testemail@test.com";
        String differentUserEmail = "differenttestemail@test.com";
        User testUser = new User();
        testUser.setEmail(userEmail);
        Deck testDeck = new Deck();
        testDeck.setId(1L);
        testDeck.setAccessLevel(AccessLevel.PUBLIC.getAccessLevel());
        testDeck.setName("test deck name");
        testDeck.setOwner(testUser);
        //when
        when(authentication.getName()).thenReturn(differentUserEmail);
        when(authentication.getAuthorities()).thenReturn(Collections.singleton(new SimpleGrantedAuthority("USER")));
        when(deckRepository.findById(1L)).thenReturn(of(testDeck));
        //then
        assertThrows(PermissionDeniedException.class, () -> deckService.setDeckPrivate(1L));
        verify(deckRepository, never()).save(any());
    }

    @Test
    @DisplayName("when given correct Id and user have permission it sets chosen deck to public")
    public void whenGivenCorrectIdAndHavePermissionItSetsDeckToPublic() throws ElementNotFoundByIdException, PermissionDeniedException {
        //given
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String userEmail = "testemail@test.com";
        User testUser = new User();
        testUser.setEmail(userEmail);
        Deck testDeck = new Deck();
        testDeck.setId(1L);
        testDeck.setAccessLevel(AccessLevel.PRIVATE.getAccessLevel());
        testDeck.setName("test deck name");
        testDeck.setOwner(testUser);
        //when
        when(authentication.getName()).thenReturn(userEmail);
        when(authentication.getAuthorities()).thenReturn(Collections.singleton(new SimpleGrantedAuthority("USER")));
        when(deckRepository.findById(any())).thenReturn(of(testDeck));
        //then
        deckService.setDeckPublic(1L);
        verify(deckRepository).save(testDeck);
        assertEquals(AccessLevel.PUBLIC.getAccessLevel(), testDeck.getAccessLevel());
    }

    @Test
    @DisplayName("when given incorrect Id and user have permission when setting public it throws ElementNotFoundException")
    public void whenGivenIncorrectIdAndHavePermissionWhenSettingPublicItThrowsElementNotFoundException() {
        //given
        Long wrongId = -1L;
        //then
        assertThrows(ElementNotFoundByIdException.class, () -> deckService.setDeckPublic(wrongId));
        verify(deckRepository, never()).save(any());
    }

    @Test
    @DisplayName("when given correct Id and user doesnt have permission when setting public it throws PermissionDeniedException")
    public void whenGivenCorrectIdAndDontHavePermissionWhenSettingPublicItThrowsPermissionDeniedException() {
        //given
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String userEmail = "testemail@test.com";
        String differentUserEmail = "differenttestemail@test.com";
        User testUser = new User();
        testUser.setEmail(userEmail);
        Deck testDeck = new Deck();
        testDeck.setId(1L);
        testDeck.setAccessLevel(AccessLevel.PUBLIC.getAccessLevel());
        testDeck.setName("test deck name");
        testDeck.setOwner(testUser);
        //when
        when(authentication.getName()).thenReturn(differentUserEmail);
        when(authentication.getAuthorities()).thenReturn(Collections.singleton(new SimpleGrantedAuthority("USER")));
        when(deckRepository.findById(1L)).thenReturn(of(testDeck));
        //then
        assertThrows(PermissionDeniedException.class, () -> deckService.setDeckPublic(1L));
        verify(deckRepository, never()).save(any());
    }

}
