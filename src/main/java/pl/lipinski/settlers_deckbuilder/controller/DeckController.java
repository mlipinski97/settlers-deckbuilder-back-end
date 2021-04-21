package pl.lipinski.settlers_deckbuilder.controller;

import org.springframework.web.bind.annotation.*;
import pl.lipinski.settlers_deckbuilder.dao.dto.CardDto;
import pl.lipinski.settlers_deckbuilder.dao.dto.DeckDto;
import pl.lipinski.settlers_deckbuilder.service.CardDeckIntersectionService;
import pl.lipinski.settlers_deckbuilder.service.DeckService;
import pl.lipinski.settlers_deckbuilder.util.exception.CardDeckIntersectionPKViolationException;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;
import pl.lipinski.settlers_deckbuilder.util.exception.PermissionDeniedException;
import pl.lipinski.settlers_deckbuilder.util.exception.UserNotFoundException;

@RestController
@RequestMapping("/api/deck")
public class DeckController {
    private final DeckService deckService;
    private final CardDeckIntersectionService cardDeckIntersectionService;

    public DeckController(DeckService deckService, CardDeckIntersectionService cardDeckIntersectionService) {
        this.deckService = deckService;
        this.cardDeckIntersectionService = cardDeckIntersectionService;
    }

    @GetMapping("/findbyid/{id}")
    public DeckDto findById(@PathVariable Long id) throws ElementNotFoundByIdException, PermissionDeniedException {
        return deckService.findById(id);
    }

    @GetMapping
    public Iterable<DeckDto> findAll(@RequestParam("page") Integer pageNumber,
                                     @RequestParam("size") Integer pageSize,
                                     @RequestParam(value = "deckNamePattern", required = false) String deckNamePattern,
                                     @RequestParam(value = "ownerEmail", required = false) String ownerEmail) {
        return deckService.getAll(pageNumber, pageSize, deckNamePattern, ownerEmail);
    }

    @PostMapping
    public DeckDto addEmptyDeck(@RequestBody DeckDto deckDto) throws UserNotFoundException {
        return deckService.addEmptyDeck(deckDto);
    }

    @DeleteMapping("/{id}")
    public void deleteDeck(@PathVariable Long id) throws ElementNotFoundByIdException {
        deckService.deleteById(id);
    }

    @PatchMapping("/setpublic/{deckId}")
    public void setDeckPublic(@PathVariable Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException {
        deckService.setDeckPublic(deckId);
    }

    @PatchMapping("/setprivate/{deckId}")
    public void setDeckPrivate(@PathVariable Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException {
        deckService.setDeckPrivate(deckId);
    }

    @GetMapping("/getcontent/{deckId}")
    public Iterable<CardDto> getDeckContent(@PathVariable Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException {
        return cardDeckIntersectionService.findAllCardsInDeckByDeckId(deckId);
    }

    @DeleteMapping("/removecardfromdeck/{cardId}/{deckId}")
    public void deleteCardFromDeck(@PathVariable Long cardId, @PathVariable Long deckId) throws ElementNotFoundByIdException {
        cardDeckIntersectionService.deleteCardFromDeck(cardId, deckId);
    }

    @PostMapping("/addcardtodeck/{cardId}/{deckId}")
    public void addCardToDeck(@PathVariable Long cardId, @PathVariable Long deckId) throws ElementNotFoundByIdException, PermissionDeniedException, CardDeckIntersectionPKViolationException {
        cardDeckIntersectionService.addCardToDeck(cardId, deckId);
    }

    @PostMapping("/copydeck/{deckId}")
    public void copyDeckById(@PathVariable Long deckId, @RequestBody DeckDto deckDto) throws UserNotFoundException, ElementNotFoundByIdException, CardDeckIntersectionPKViolationException {
        cardDeckIntersectionService.copyDeckById(deckId, deckDto);
    }
}
