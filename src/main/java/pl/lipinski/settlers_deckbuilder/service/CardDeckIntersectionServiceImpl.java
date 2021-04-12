package pl.lipinski.settlers_deckbuilder.service;

import com.google.common.collect.Lists;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.lipinski.settlers_deckbuilder.dao.dto.CardDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.CardDeckIntersection;
import pl.lipinski.settlers_deckbuilder.dao.entity.Deck;
import pl.lipinski.settlers_deckbuilder.repository.CardDeckIntersectionRepository;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;

import java.util.stream.Collectors;

@Service
public class CardDeckIntersectionServiceImpl implements CardDeckIntersectionService{

    private final DeckService deckService;
    private final CardDeckIntersectionRepository cardDeckIntersectionRepository;
    private final ModelMapper modelMapper;

    public CardDeckIntersectionServiceImpl(DeckService deckService,
                                           CardDeckIntersectionRepository cardDeckIntersectionRepository) {
        this.deckService = deckService;
        this.cardDeckIntersectionRepository = cardDeckIntersectionRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Iterable<CardDto> findAllCardsInDeckByDeckId(Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException {
        deckService.findById(deckId);
        return Lists.newArrayList(cardDeckIntersectionRepository.findAllByDeck_Id(deckId))
                .stream()
                .map(CardDeckIntersection::getCard)
                .map(card -> modelMapper.map(card, CardDto.class))
                .collect(Collectors.toList());
    }

}
