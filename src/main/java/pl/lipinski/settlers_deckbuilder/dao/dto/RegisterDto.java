package pl.lipinski.settlers_deckbuilder.dao.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RegisterDto {
    private String email;
    private String password;
}
