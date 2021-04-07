package pl.lipinski.settlers_deckbuilder.dao.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CardDto {
    private Long id;
    private String name;
    private String faction;
    private String type;
    private Integer costFoundation;
    private Integer costFirstResourceQuantity;
    private String costFirstResourceType;
    private Integer costSecondResourceQuantity;
    private String costSecondResourceType;
    private Integer raiseFirstResourceQuantity;
    private String raiseFirstResourceType;
    private Integer raiseSecondResourceQuantity;
    private String raiseSecondResourceType;
    private String dealEffect;
    private String color;
    private Integer quantity;
    private String effect;
    private String buildingBonus;
    private String expansion;
}
