package pl.lipinski.settlers_deckbuilder.service;


import pl.lipinski.settlers_deckbuilder.dao.dto.DeckDto;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;

public interface DeckService {
    DeckDto findById(Long id) throws ElementNotFoundByIdException, PermissionDeniedException;
    Iterable<DeckDto> getAll(Integer pageNumber, Integer pageSize, String deckNamePattern, String ownerEmail);
    void setDeckPrivate(Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException;
    void setDeckPublic(Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException;
    DeckDto addEmptyDeck(DeckDto deckDto) throws UserNotFoundException;
    void deleteById(Long id) throws ElementNotFoundByIdException;
}
