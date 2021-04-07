package pl.lipinski.settlers_deckbuilder.util.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.lipinski.settlers_deckbuilder.dao.entity.Card;

public class CardSpecification {

    public static Specification<Card> cardHasColor(String cardColor) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (cardColor == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                return criteriaBuilder.equal(root.get("color"), cardColor);
            }
        };
    }

    public static Specification<Card> cardHasFaction(String cardFaction) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (cardFaction == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                return criteriaBuilder.equal(root.get("faction"), cardFaction);
            }
        };
    }

    public static Specification<Card> cardHasType(String cardType) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (cardType == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                return criteriaBuilder.equal(root.get("type"), cardType);
            }
        };
    }

    public static Specification<Card> cardHasQuantity(Integer cardQuantity) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (cardQuantity == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                return criteriaBuilder.equal(root.get("quantity"), cardQuantity);
            }
        };
    }

    public static Specification<Card> cardHasExpansion(String cardExpansion) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (cardExpansion == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            } else {
                return criteriaBuilder.equal(root.get("expansion"), cardExpansion);
            }
        };
    }
}
