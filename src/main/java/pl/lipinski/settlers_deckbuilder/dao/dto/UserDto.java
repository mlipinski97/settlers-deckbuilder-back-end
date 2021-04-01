package pl.lipinski.settlers_deckbuilder.dao.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {
    public Long id;
    public String email;
    public String role;
}
