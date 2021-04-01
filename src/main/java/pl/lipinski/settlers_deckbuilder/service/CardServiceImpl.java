package pl.lipinski.settlers_deckbuilder.service;

import org.springframework.stereotype.Service;
import pl.lipinski.settlers_deckbuilder.dao.entity.Card;
import pl.lipinski.settlers_deckbuilder.repository.CardRepository;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Iterable<Card> getAll() {
        return cardRepository.findAll();
    }
}
