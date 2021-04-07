package pl.lipinski.settlers_deckbuilder.controller;

import org.springframework.web.bind.annotation.*;
import pl.lipinski.settlers_deckbuilder.dao.dto.CardDto;
import pl.lipinski.settlers_deckbuilder.service.CardServiceImpl;
import pl.lipinski.settlers_deckbuilder.util.exception.ElementNotFoundByIdException;

@RestController
@RequestMapping("/api/card")
public class CardController {
    private final CardServiceImpl cardService;

    public CardController(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public Iterable<CardDto> findAll(@RequestParam("page") Integer pageNumber,
                                     @RequestParam("size") Integer pageSize,
                                     @RequestParam(value = "faction", required = false) String faction,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = "color", required = false) String color,
                                     @RequestParam(value = "quantity", required = false) Integer quantity,
                                     @RequestParam(value = "expansion", required = false) String expansion) {
        return cardService.getAll(pageNumber, pageSize, faction, type, color, quantity, expansion);
    }

    @GetMapping("/{id}")
    public CardDto findById(@PathVariable Long id) throws ElementNotFoundByIdException {
        return cardService.findById(id);
    }

    @GetMapping("/getbyname/{name}")
    public Iterable<CardDto> findAllByNameLike(@PathVariable String name){
        return cardService.findAllByName(name);
    }

    @PostMapping
    public CardDto addCard(@RequestBody CardDto cardDto){
        return cardService.addCard(cardDto);
    }

    @PatchMapping
    public CardDto updateCard(@RequestBody CardDto cardDto){
        return cardService.updateCard(cardDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable Long id) throws ElementNotFoundByIdException {
        cardService.deleteById(id);
    }
}
