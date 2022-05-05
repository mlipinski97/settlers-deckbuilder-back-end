package pl.lipinski.settlers_deckbuilder.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.lipinski.settlers_deckbuilder.dao.dto.DeckDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.Deck;
import pl.lipinski.settlers_deckbuilder.repository.DeckRepository;
import pl.lipinski.settlers_deckbuilder.service.DeckService;
import pl.lipinski.settlers_deckbuilder.service.UserService;
import pl.lipinski.settlers_deckbuilder.util.enums.AccessLevel;
import pl.lipinski.settlers_deckbuilder.util.enums.Role;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;
import pl.lipinski.settlers_deckbuilder.util.specification.DeckSpecification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorCode.CAN_NOT_FIND_DECK_BY_ID_ERROR_CODE;
import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorCode.USER_DONT_HAVE_PERMISSIONS_ERROR_CODE;
import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorMessage.CAN_NOT_FIND_DECK_BY_ID_ERROR_MESSAGE;
import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorMessage.USER_DONT_HAVE_PERMISSIONS_ERROR_MESSAGE;

@Service
public class DeckServiceImpl implements DeckService {
    private final DeckRepository deckRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public DeckServiceImpl(DeckRepository deckRepository, UserService userService) {
        this.deckRepository = deckRepository;
        this.userService = userService;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Iterable<DeckDto> getAll(Integer pageNumber,
                                    Integer pageSize,
                                    String deckNamePattern,
                                    String ownerEmail) {
        Pageable cardPaging = PageRequest.of(pageNumber, pageSize);

        Specification<Deck> deckSpecification = Specification
                .where(DeckSpecification.deckHasNameLike(deckNamePattern))
                .and(DeckSpecification.deckHasOwnerEmail(ownerEmail));

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();


        return deckRepository.findAll(deckSpecification, cardPaging)
                .stream()
                .filter(deck -> deck.getAccessLevel().equals(AccessLevel.PUBLIC.getAccessLevel())
                        || deck.getOwner().getEmail().equals(currentUserEmail))
                .map(d -> modelMapper.map(d, DeckDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public DeckDto findById(Long id) throws ElementNotFoundByIdException, PermissionDeniedException {
        Deck deck = deckRepository.findById(id).orElseThrow(() -> new ElementNotFoundByIdException(
                CAN_NOT_FIND_DECK_BY_ID_ERROR_MESSAGE.getMessage() + id,
                CAN_NOT_FIND_DECK_BY_ID_ERROR_CODE.getValue()
        ));
        validatePermissionByDeckAccessLevel(deck);
        return modelMapper.map(deck, DeckDto.class);
    }

    @Override
    public void setDeckPrivate(Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ElementNotFoundByIdException(
                        CAN_NOT_FIND_DECK_BY_ID_ERROR_MESSAGE.getMessage() + deckId,
                        CAN_NOT_FIND_DECK_BY_ID_ERROR_CODE.getValue()
                ));
        validatePermissionByEmail(deck.getOwner().getEmail());
        deck.setAccessLevel(AccessLevel.PRIVATE.getAccessLevel());
        deckRepository.save(deck);
    }

    @Override
    public void setDeckPublic(Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new ElementNotFoundByIdException(
                        CAN_NOT_FIND_DECK_BY_ID_ERROR_MESSAGE.getMessage() + deckId,
                        CAN_NOT_FIND_DECK_BY_ID_ERROR_CODE.getValue()
                ));
        validatePermissionByEmail(deck.getOwner().getEmail());
        deck.setAccessLevel(AccessLevel.PUBLIC.getAccessLevel());
        deckRepository.save(deck);
    }

    @Override
    public DeckDto addEmptyDeck(DeckDto deckDto) throws UserNotFoundException {
        checkBoundaries(deckDto);
        Deck deck = modelMapper.map(deckDto, Deck.class);
        return modelMapper.map(deckRepository.save(deck), DeckDto.class);
    }

    @Override
    public void deleteById(Long id) throws ElementNotFoundByIdException {
        deckRepository.findById(id).orElseThrow(() -> new ElementNotFoundByIdException(
                CAN_NOT_FIND_DECK_BY_ID_ERROR_MESSAGE.getMessage() + id,
                CAN_NOT_FIND_DECK_BY_ID_ERROR_CODE.getValue()
        ));
        deckRepository.deleteById(id);
    }

    private void validatePermissionByEmail(String email) throws PermissionDeniedException {
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

    private void validatePermissionByDeckAccessLevel(Deck deck) throws PermissionDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<? extends GrantedAuthority> authorities = new ArrayList<>(authentication.getAuthorities());
        if (!authentication.getName().equals(deck.getOwner().getEmail())
                && !deck.getAccessLevel().equals(AccessLevel.PUBLIC.getAccessLevel())
                && !authorities.get(0).getAuthority().equals(Role.ADMIN.getRole())) {
            throw new PermissionDeniedException(
                    USER_DONT_HAVE_PERMISSIONS_ERROR_MESSAGE.getMessage(),
                    USER_DONT_HAVE_PERMISSIONS_ERROR_CODE.getValue()
            );
        }
    }

    private void checkBoundaries(DeckDto deckDto) throws UserNotFoundException {
        userService.findByEmail(deckDto.getOwnerEmail());
    }
}
