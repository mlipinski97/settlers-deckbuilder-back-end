package pl.lipinski.settlers_deckbuilder.util.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.lipinski.settlers_deckbuilder.dao.entity.Deck;

public class DeckSpecification {

    public static Specification<Deck> deckHasOwnerEmail(String ownerEmail) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (ownerEmail == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                return criteriaBuilder.equal(root.get("owner").get("email"), ownerEmail);
            }
        };
    }

    public static Specification<Deck> deckHasNameLike(String deckNamePattern) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (deckNamePattern == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                return criteriaBuilder.like(root.get("name"), "%" + deckNamePattern + "%");
            }
        };
    }
}
