package pl.lipinski.settlers_deckbuilder.service;

import com.google.common.collect.Lists;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.lipinski.settlers_deckbuilder.dao.dto.CardDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.Card;
import pl.lipinski.settlers_deckbuilder.dao.entity.CardDeckIntersection;
import pl.lipinski.settlers_deckbuilder.dao.entity.Deck;
import pl.lipinski.settlers_deckbuilder.repository.CardDeckIntersectionRepository;
import pl.lipinski.settlers_deckbuilder.repository.CardRepository;
import pl.lipinski.settlers_deckbuilder.repository.DeckRepository;
import pl.lipinski.settlers_deckbuilder.util.exception.CardDeckIntersectionPKViolationException;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;

import java.util.stream.Collectors;

import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorCode.*;
import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorMessage.*;

@Service
public class CardDeckIntersectionServiceImpl implements CardDeckIntersectionService {

    private final DeckService deckService;
    private final CardDeckIntersectionRepository cardDeckIntersectionRepository;
    private final ModelMapper modelMapper;
    private final CardRepository cardRepository;
    private final DeckRepository deckRepository;

    public CardDeckIntersectionServiceImpl(DeckService deckService,
                                           CardDeckIntersectionRepository cardDeckIntersectionRepository,
                                           CardRepository cardRepository,
                                           DeckRepository deckRepository) {
        this.deckService = deckService;
        this.cardDeckIntersectionRepository = cardDeckIntersectionRepository;
        this.cardRepository = cardRepository;
        this.deckRepository = deckRepository;
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

    @Override
    public void deleteCardFromDeck(Long cardId, Long deckId) throws ElementNotFoundByIdException {
        cardDeckIntersectionRepository.findByDeckIdAndCardId(deckId, cardId)
                .orElseThrow(() -> new ElementNotFoundByIdException(
                                CAN_NOT_FIND_CARD_DECK_INTERSECTION_BY_ID_ERROR_MESSAGE.getMessage(),
                                CAN_NOT_FIND_CARD_DECK_INTERSECTION_BY_ID_ERROR_CODE.getValue()
                        )
                );
        cardDeckIntersectionRepository.deleteByCardIdAndDeckId(cardId, deckId);
    }

    @Override
    public void addCardToDeck(Long cardId, Long deckId) throws ElementNotFoundByIdException, CardDeckIntersectionPKViolationException {
        checkForPKViolation(cardId, deckId);
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new ElementNotFoundByIdException(
                CAN_NOT_FIND_CARD_BY_ID_ERROR_MESSAGE.getMessage() + cardId,
                CAN_NOT_FIND_CARD_BY_ID_ERROR_CODE.getValue()
        ));
        Deck deck = deckRepository.findById(deckId).orElseThrow(() -> new ElementNotFoundByIdException(
                CAN_NOT_FIND_DECK_BY_ID_ERROR_MESSAGE.getMessage() + deckId,
                CAN_NOT_FIND_DECK_BY_ID_ERROR_CODE.getValue()
        ));
        CardDeckIntersection cdi = new CardDeckIntersection();
        cdi.setCard(card);
        cdi.setDeck(deck);
        cardDeckIntersectionRepository.save(cdi);
    }

    private void checkForPKViolation(Long cardId, Long deckId) throws CardDeckIntersectionPKViolationException {
        if(cardDeckIntersectionRepository.findByDeckIdAndCardId(deckId, cardId).isPresent()){
            throw new CardDeckIntersectionPKViolationException(
                    CARD_DECK_INTERSECTION_ALREADY_EXISTS_ERROR_MESSAGE.getMessage(),
                    CARD_DECK_INTERSECTION_ALREADY_EXISTS_ERROR_CODE.getValue());
        }
    }

}
