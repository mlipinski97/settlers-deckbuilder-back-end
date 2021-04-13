package pl.lipinski.settlers_deckbuilder.service;

import pl.lipinski.settlers_deckbuilder.dao.dto.CardDto;
import pl.lipinski.settlers_deckbuilder.util.exception.CardDeckIntersectionPKViolationException;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;

public interface CardDeckIntersectionService {
    Iterable<CardDto> findAllCardsInDeckByDeckId(Long deckId) throws ElementNotFoundByIdException,
            PermissionDeniedException;
    void deleteCardFromDeck(Long cardId, Long deckId) throws ElementNotFoundByIdException;
    void addCardToDeck(Long cardId, Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException, CardDeckIntersectionPKViolationException;
}
