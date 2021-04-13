package pl.lipinski.settlers_deckbuilder.dao.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DeckDto {
    private Long id;
    private String name;
    private Long ownerId;
    private String ownerEmail;
    private String accessLevel;
}
