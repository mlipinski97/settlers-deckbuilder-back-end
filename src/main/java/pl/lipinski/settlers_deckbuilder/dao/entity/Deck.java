package pl.lipinski.settlers_deckbuilder.dao.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "deck")
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User owner;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "access_level")
    private String accessLevel;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL)
    List<CardDeckIntersection> cardDeckIntersections;

}
