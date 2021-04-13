package pl.lipinski.settlers_deckbuilder.dao.entity.key;

import lombok.*;
import pl.lipinski.settlers_deckbuilder.dao.entity.Card;
import pl.lipinski.settlers_deckbuilder.dao.entity.Deck;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CardDeckIntersectionId implements Serializable {
    private Card card;
    private Deck deck;
}
