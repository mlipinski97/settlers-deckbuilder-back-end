package pl.lipinski.settlers_deckbuilder.dao.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoginDto {
    private String email;
    private String password;
}

