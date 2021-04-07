package pl.lipinski.settlers_deckbuilder.dao.entity;

import lombok.*;

import javax.persistence.*;

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


    //TODO
    //ManyToMany deck <---> card | need to create intersection. Not today. Today fever is killing me. Remember to do it.
}
