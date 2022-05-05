package pl.lipinski.settlers_deckbuilder.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import pl.lipinski.settlers_deckbuilder.dao.dto.DeckDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.Deck;
import pl.lipinski.settlers_deckbuilder.dao.entity.User;
import pl.lipinski.settlers_deckbuilder.repository.DeckRepository;
import pl.lipinski.settlers_deckbuilder.security.UserDetailsImpl;
import pl.lipinski.settlers_deckbuilder.util.enums.AccessLevel;
import pl.lipinski.settlers_deckbuilder.util.enums.Role;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckServiceImplTest {

    @Mock
    AbstractAuthenticationToken authentication;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private DeckRepository deckRepository;
    @InjectMocks
    private DeckServiceImpl deckService;

    @BeforeEach
    void setUp() {
        deckService = new DeckServiceImpl(deckRepository, userService);
    }

    @Test
    @DisplayName("when deck id is provided returned deck should be correct")
    @WithMockUser(username = "user", password = "user", roles = "USER", authorities = {"USER"})
    public void whenDeckIdIsProvidedThenRetrievedDeckIsCorrect() throws ElementNotFoundByIdException, PermissionDeniedException {
        //given
        DeckDto testDeck;
        User deckBaseEntityOwner = new User();
        deckBaseEntityOwner.setEmail("testemail@test.com");
        deckBaseEntityOwner.setId(10L);
        deckBaseEntityOwner.setRole(Role.ADMIN);
        Deck deckBaseEntity = new Deck();
        deckBaseEntity.setName("deck name");
        deckBaseEntity.setAccessLevel(AccessLevel.PUBLIC.getAccessLevel());
        deckBaseEntity.setOwner(deckBaseEntityOwner);
        deckBaseEntity.setId(1L);
        //when
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("deck name");
        Set<GrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority("USER"));
        when(authentication.getAuthorities()).thenReturn(simpleGrantedAuthorities);
        when(deckRepository.findById(anyLong())).thenReturn(of(deckBaseEntity));
        testDeck = deckService.findById(1L);
        //then
        verify(deckRepository).findById(anyLong());
        assertNotNull(testDeck);
        assertEquals("deck name", testDeck.getName());
        assertEquals(10L, testDeck.getOwnerId());
        assertEquals("testemail@test.com", testDeck.getOwnerEmail());
        assertEquals(AccessLevel.PUBLIC.getAccessLevel(), testDeck.getAccessLevel());
    }
}
