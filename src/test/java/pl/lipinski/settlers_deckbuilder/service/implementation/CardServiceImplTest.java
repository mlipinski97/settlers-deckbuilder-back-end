package pl.lipinski.settlers_deckbuilder.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import pl.lipinski.settlers_deckbuilder.dao.dto.CardDto;
import pl.lipinski.settlers_deckbuilder.dao.entity.Card;
import pl.lipinski.settlers_deckbuilder.repository.CardRepository;
import pl.lipinski.settlers_deckbuilder.service.CardService;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    CardRepository cardRepository;
    ModelMapper modelMapper = new ModelMapper();

    CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardServiceImpl(cardRepository);
    }

    @Test
    @DisplayName("when getAll() is called it returns all cards within given pattern by pages")
    public void whenGetAllIsCalledItReturnsAllCardsWithinGivenPattern() {
        //given
        Card testCard = new Card();
        testCard.setBuildingBonus("bonus");
        testCard.setColor("black");
        testCard.setCostFirstResourceQuantity(1);
        testCard.setCostFirstResourceType("Wood");
        testCard.setCostFoundation(1);
        testCard.setCostSecondResourceQuantity(2);
        testCard.setCostSecondResourceType("stone");
        testCard.setDealEffect("deal effect");
        testCard.setEffect("effect");
        testCard.setExpansion("base");
        testCard.setFaction("test");
        testCard.setId(1L);
        testCard.setName("test name");
        testCard.setQuantity(2);
        List<CardDto> expectedReturn = new ArrayList<>(Collections.singletonList(modelMapper.map(testCard, CardDto.class)));
        Page<Card> testPage = new PageImpl<>(Collections.singletonList(testCard));
        //when
        when(cardRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(testPage);
        //then
        Iterable<CardDto> receivedReturn = cardService.getAll(1,
                10,
                "test faction",
                "testType",
                "black",
                2,
                "testExpansion");
        verify(cardRepository).findAll(any(Specification.class), any(Pageable.class));
        assertEquals(expectedReturn, receivedReturn);
    }

    @Test
    @DisplayName("when given correct id it finds card")
    public void whenGivenCorrectIdItFindsCard() throws ElementNotFoundByIdException {
        //given
        Long testCardId = 1L;
        Card testCard = new Card();
        testCard.setBuildingBonus("bonus");
        testCard.setColor("black");
        testCard.setCostFirstResourceQuantity(1);
        testCard.setCostFirstResourceType("Wood");
        testCard.setCostFoundation(1);
        testCard.setCostSecondResourceQuantity(2);
        testCard.setCostSecondResourceType("stone");
        testCard.setDealEffect("deal effect");
        testCard.setEffect("effect");
        testCard.setExpansion("base");
        testCard.setFaction("test");
        testCard.setId(testCardId);
        testCard.setName("test name");
        testCard.setQuantity(2);
        CardDto testCardDto = modelMapper.map(testCard, CardDto.class);
        //when
        when(cardRepository.findById(testCardId)).thenReturn(of(testCard));
        //then
        CardDto receivedCardDto = cardService.findById(testCardId);
        verify(cardRepository).findById(testCardId);
        assertEquals(testCardDto, receivedCardDto);
    }

    @Test
    @DisplayName("when given non-existing card Id it throws ElementNotFoundByIdException")
    public void whenGivenNonExistingIdItThrowsElementNotFoundByIdException() {
        //given
        Long wrongId = -1L;
        //then
        assertThrows(ElementNotFoundByIdException.class, () -> cardService.findById(wrongId));
        verify(cardRepository).findById(wrongId);
    }

    @Test
    public void whenGivenCardNameItReturnsListOfCardWithinNamePattern() {
        //given
        String testCardName = "testName";
        Long testCardId = 1L;
        Card testCard = new Card();
        testCard.setBuildingBonus("bonus");
        testCard.setColor("black");
        testCard.setCostFirstResourceQuantity(1);
        testCard.setCostFirstResourceType("Wood");
        testCard.setCostFoundation(1);
        testCard.setCostSecondResourceQuantity(2);
        testCard.setCostSecondResourceType("stone");
        testCard.setDealEffect("deal effect");
        testCard.setEffect("effect");
        testCard.setExpansion("base");
        testCard.setFaction("test");
        testCard.setId(testCardId);
        testCard.setName(testCardName);
        testCard.setQuantity(2);
        Long testCardId2 = 2L;
        Card testCard2 = new Card();
        testCard.setBuildingBonus("bonus");
        testCard.setColor("black");
        testCard.setCostFirstResourceQuantity(1);
        testCard.setCostFirstResourceType("Wood");
        testCard.setCostFoundation(1);
        testCard.setCostSecondResourceQuantity(2);
        testCard.setCostSecondResourceType("stone");
        testCard.setDealEffect("deal effect");
        testCard.setEffect("effect");
        testCard.setExpansion("base");
        testCard.setFaction("test");
        testCard.setId(testCardId2);
        testCard.setName(testCardName + "suffix");
        testCard.setQuantity(2);
        List<Card> testCardDtoList = Arrays.asList(testCard, testCard2);
        Iterable<CardDto> expectedReturn = testCardDtoList.stream()
                .map(card -> modelMapper.map(card, CardDto.class))
                .collect(Collectors.toList());
        //when
        when(cardRepository.findAllByNameLike(testCardName)).thenReturn(testCardDtoList);
        //then
        Iterable<CardDto> receivedReturn = cardService.findAllByName(testCardName);
        assertEquals(expectedReturn, receivedReturn);
        verify(cardRepository).findAllByNameLike(testCardName);
    }

    @Test
    public void whenGivenCardDetailsNewCardIsAdded() {
        //given
        CardDto testCardDto = new CardDto();
        testCardDto.setBuildingBonus("bonus");
        testCardDto.setColor("black");
        testCardDto.setCostFirstResourceQuantity(1);
        testCardDto.setCostFirstResourceType("Wood");
        testCardDto.setCostFoundation(1);
        testCardDto.setCostSecondResourceQuantity(2);
        testCardDto.setCostSecondResourceType("stone");
        testCardDto.setDealEffect("deal effect");
        testCardDto.setEffect("effect");
        testCardDto.setExpansion("base");
        testCardDto.setFaction("test");
        testCardDto.setId(1L);
        testCardDto.setName("testCardName");
        testCardDto.setQuantity(2);
        Card expectedCard = modelMapper.map(testCardDto, Card.class);
        //when
        when(cardRepository.save(expectedCard)).thenReturn(expectedCard);
        //then
        cardService.addCard(testCardDto);
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardArgumentCaptor.capture());
        Card capturedCard = cardArgumentCaptor.getValue();
        assertEquals(expectedCard, capturedCard);
    }

    @Test
    public void whenGivenCorrectIdItDeletesTheCard() throws ElementNotFoundByIdException {
        //given
        Long testId = 1L;
        Card testCard = new Card();
        testCard.setBuildingBonus("bonus");
        testCard.setColor("black");
        testCard.setCostFirstResourceQuantity(1);
        testCard.setCostFirstResourceType("Wood");
        testCard.setCostFoundation(1);
        testCard.setCostSecondResourceQuantity(2);
        testCard.setCostSecondResourceType("stone");
        testCard.setDealEffect("deal effect");
        testCard.setEffect("effect");
        testCard.setExpansion("base");
        testCard.setFaction("test");
        testCard.setId(testId);
        testCard.setName("testCardName");
        testCard.setQuantity(2);
        //when
        when(cardRepository.findById(testId)).thenReturn(of(testCard));
        //then
        cardService.deleteById(testId);
        verify(cardRepository).deleteById(testId);
    }

    @Test
    public void whenGivenInCorrectIdItThrowsElementNotFoundByIdException() {
        //given
        Long wrongId = -1L;
        //then
        assertThrows(ElementNotFoundByIdException.class, () -> cardService.deleteById(wrongId));
        verify(cardRepository, never()).save(any());
    }

    @Test
    public void whenGivenNewCardDetailsChosenCardIsChanged() throws ElementNotFoundByIdException {
        //given
        CardDto cardDtoToBeChanged = new CardDto();
        cardDtoToBeChanged.setBuildingBonus("bonus");
        cardDtoToBeChanged.setColor("white");
        cardDtoToBeChanged.setCostFirstResourceQuantity(1);
        cardDtoToBeChanged.setCostFirstResourceType("Stone");
        cardDtoToBeChanged.setCostFoundation(1);
        cardDtoToBeChanged.setCostSecondResourceQuantity(2);
        cardDtoToBeChanged.setCostSecondResourceType("gold");
        cardDtoToBeChanged.setDealEffect("deal effect");
        cardDtoToBeChanged.setEffect("effect");
        cardDtoToBeChanged.setExpansion("base");
        cardDtoToBeChanged.setFaction("test");
        cardDtoToBeChanged.setId(1L);
        cardDtoToBeChanged.setName("changedTestCardName");
        cardDtoToBeChanged.setQuantity(2);
        Card CardToBeChanged = modelMapper.map(cardDtoToBeChanged, Card.class);
        CardDto changedCardDto = new CardDto();
        changedCardDto.setBuildingBonus("bonus");
        changedCardDto.setColor("white");
        changedCardDto.setCostFirstResourceQuantity(1);
        changedCardDto.setCostFirstResourceType("Stone");
        changedCardDto.setCostFoundation(1);
        changedCardDto.setCostSecondResourceQuantity(2);
        changedCardDto.setCostSecondResourceType("gold");
        changedCardDto.setDealEffect("deal effect");
        changedCardDto.setEffect("effect");
        changedCardDto.setExpansion("base");
        changedCardDto.setFaction("test");
        changedCardDto.setId(1L);
        changedCardDto.setName("changedTestCardName");
        changedCardDto.setQuantity(2);
        Card changedCard = modelMapper.map(changedCardDto, Card.class);
        //when
        when(cardRepository.findById(1L)).thenReturn(Optional.of(CardToBeChanged));
        when(cardRepository.save(changedCard)).thenReturn(changedCard);
        //then
        cardService.updateCardById(changedCardDto, 1L);
        ArgumentCaptor<Card> cardArgumentCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardArgumentCaptor.capture());
        Card capturedCard = cardArgumentCaptor.getValue();
        assertEquals(changedCard, capturedCard);
    }

    @Test
    public void whenGivenNewCardDetailsButIncorrectIdItThrowsElementNotFoundByIdException() {
        //given
        CardDto changedCardDto = new CardDto();
        changedCardDto.setBuildingBonus("bonus");
        changedCardDto.setColor("white");
        changedCardDto.setCostFirstResourceQuantity(1);
        changedCardDto.setCostFirstResourceType("Stone");
        changedCardDto.setCostFoundation(1);
        changedCardDto.setCostSecondResourceQuantity(2);
        changedCardDto.setCostSecondResourceType("gold");
        changedCardDto.setDealEffect("deal effect");
        changedCardDto.setEffect("effect");
        changedCardDto.setExpansion("base");
        changedCardDto.setFaction("test");
        changedCardDto.setId(1L);
        changedCardDto.setName("changedTestCardName");
        changedCardDto.setQuantity(2);
        //then
        assertThrows(ElementNotFoundByIdException.class, () -> cardService.updateCardById(changedCardDto, 1L));
        verify(cardRepository, never()).save(any());
    }
}
