package pl.lipinski.settlers_deckbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lipinski.settlers_deckbuilder.dao.entity.Deck;

@Repository
public interface DeckRepository extends JpaRepository<Deck, Long>, JpaSpecificationExecutor<Deck> {

    @Query("SELECT d FROM Deck d WHERE d.owner.email = :userEmail AND UPPER(d.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Iterable<Deck> findAllByNameLikeAndUserEmail(@Param("name") String name, @Param("userEmail") String userEmail);

    @Query("SELECT d FROM Deck d WHERE d.owner.email = :email")
    Iterable<Deck> findAllByUserEmail(@Param("email") String email);
}
