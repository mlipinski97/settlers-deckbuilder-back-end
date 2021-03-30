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

    @Column(nullable = false, name = "color")
    private String color;

    @Column(nullable = false, name = "number_of_copies")
    private Integer number_of_copies;

    @Column(nullable = false, name = "affiliation")
    private String affiliation;

    @Column(nullable = false, name = "type")
    private String type;

    @Column(nullable = false, name = "effect")
    private String effect;

    @Column(name = "building_bonus")
    private String building_bonus;

    @Column(nullable = false, name = "cost")
    private String cost;

    @Column(nullable = false, name = "expansion")
    private String expansion;
}
