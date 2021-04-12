package pl.lipinski.settlers_deckbuilder.service;

import com.google.common.collect.Lists;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.lipinski.settlers_deckbuilder.dao.dto.CardDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.Card;
import pl.lipinski.settlers_deckbuilder.repository.CardRepository;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;
import pl.lipinski.settlers_deckbuilder.util.specification.CardSpecification;

import java.util.stream.Collectors;

import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorCode.CAN_NOT_FIND_CARD_BY_ID_ERROR_CODE;
import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorCode.CAN_NOT_FIND_USER_BY_ID_ERROR_CODE;
import static pl.lipinski.settlers_deckbuilder.util.enums.ErrorMessage.CAN_NOT_FIND_CARD_BY_ID_ERROR_MESSAGE;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ModelMapper modelMapper;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Iterable<CardDto> getAll(Integer pageNumber,
                                    Integer pageSize,
                                    String faction,
                                    String type,
                                    String color,
                                    Integer quantity,
                                    String expansion) {
        Pageable cardPaging = PageRequest.of(pageNumber, pageSize);

        Specification<Card> cardSpecification = Specification
                .where(CardSpecification.cardHasFaction(faction))
                .and(CardSpecification.cardHasType(type))
                .and(CardSpecification.cardHasColor(color))
                .and(CardSpecification.cardHasQuantity(quantity))
                .and(CardSpecification.cardHasExpansion(expansion));

        return cardRepository
                .findAll(cardSpecification, cardPaging)
                .stream()
                .map(card -> modelMapper.map(card, CardDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CardDto findById(Long id) throws ElementNotFoundByIdException {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundByIdException(
                        CAN_NOT_FIND_CARD_BY_ID_ERROR_MESSAGE.getMessage() + id,
                        CAN_NOT_FIND_CARD_BY_ID_ERROR_CODE.getValue()
                ));
        return modelMapper.map(card, CardDto.class);
    }

    @Override
    public Iterable<CardDto> findAllByName(String name) {
        return Lists.newArrayList(cardRepository.findAllByNameLike(name))
                .stream()
                .map(c -> modelMapper.map(c, CardDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CardDto addCard(CardDto cardDto) {
        Card card = modelMapper.map(cardDto, Card.class);
        return modelMapper.map(cardRepository.save(card), CardDto.class);
    }

    @Override
    public void deleteById(Long id) throws ElementNotFoundByIdException {
        cardRepository.findById(id).orElseThrow(() -> new ElementNotFoundByIdException(
                CAN_NOT_FIND_CARD_BY_ID_ERROR_MESSAGE.getMessage() + id,
                CAN_NOT_FIND_USER_BY_ID_ERROR_CODE.getValue()
                ));
        cardRepository.deleteById(id);
    }

    @Override
    public CardDto updateCard(CardDto cardDto) {
        Card card = modelMapper.map(cardDto, Card.class);
        return modelMapper.map(cardRepository.save(card), CardDto.class);
    }

}
