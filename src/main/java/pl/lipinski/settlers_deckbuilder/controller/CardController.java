package pl.lipinski.settlers_deckbuilder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lipinski.settlers_deckbuilder.dao.entity.Card;
import pl.lipinski.settlers_deckbuilder.service.CardServiceImpl;

@RestController
@RequestMapping("/api/card")
public class CardController {
    private final CardServiceImpl cardService;

    public CardController(CardServiceImpl cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public Iterable<Card> findAll(){
        return cardService.getAll();
    }
}
