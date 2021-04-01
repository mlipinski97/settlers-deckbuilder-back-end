package pl.lipinski.settlers_deckbuilder.service;


import pl.lipinski.settlers_deckbuilder.dao.entity.Card;

public interface CardService {
    Iterable<Card> getAll();
}
