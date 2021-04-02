package pl.lipinski.settlers_deckbuilder.dao.entity;

import lombok.*;
import pl.lipinski.settlers_deckbuilder.util.enums.Role;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "role")
    private Role role;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive;
}
