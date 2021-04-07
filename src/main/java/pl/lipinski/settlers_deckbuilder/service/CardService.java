package pl.lipinski.settlers_deckbuilder.service;


import pl.lipinski.settlers_deckbuilder.dao.dto.CardDto;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;

public interface CardService {
    Iterable<CardDto> getAll(Integer pageNumber,
                             Integer pageSize,
                             String faction,
                             String type,
                             String color,
                             Integer quantity,
                             String expansion);

    CardDto findById(Long id) throws ElementNotFoundByIdException;

    Iterable<CardDto> findAllByName(String name);

    CardDto addCard(CardDto cardDto);

    void deleteById(Long id) throws ElementNotFoundByIdException;

    CardDto updateCard(CardDto cardDto) throws ElementNotFoundByIdException;
    /*
    Iterable<Card> findByColor();
    Iterable<Card> findByNumberOfCopies();
    Iterable<Card> findByFaction();
    Iterable<Card> findByType();
    Iterable<Card> findByExpansion();
    Iterable<Card> findByQuantity();*/
}
