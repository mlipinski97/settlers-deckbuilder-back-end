package pl.lipinski.settlers_deckbuilder.dao.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "faction")
    private String faction;

    @Column(nullable = false, name = "type")
    private String type;

    @Column(name = "cost_foundation")
    private Integer costFoundation;

    @Column(name = "cost_first_resource_quantity")
    private Integer costFirstResourceQuantity;

    @Column(name = "cost_first_resource_type")
    private String costFirstResourceType;

    @Column(name = "cost_second_resource_quantity")
    private Integer costSecondResourceQuantity;

    @Column(name = "cost_second_resource_type")
    private String costSecondResourceType;

    @Column(name = "raise_first_resource_quantity")
    private Integer raiseFirstResourceQuantity;

    @Column(name = "raise_first_resource_type")
    private String raiseFirstResourceType;

    @Column(name = "raise_second_resource_quantity")
    private Integer raiseSecondResourceQuantity;

    @Column(name = "raise_second_resource_type")
    private String raiseSecondResourceType;

    @Column(name = "deal_effect")
    private String dealEffect;

    @Column(nullable = false, name = "color")
    private String color;

    @Column(nullable = false, name = "quantity")
    private Integer quantity;

    @Column(nullable = false, name = "effect")
    private String effect;

    @Column(nullable = false, name = "building_bonus")
    private String buildingBonus;

    @Column(nullable = false,name = "expansion")
    private String expansion;

}
