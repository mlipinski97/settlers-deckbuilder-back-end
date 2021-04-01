package pl.lipinski.settlers_deckbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lipinski.settlers_deckbuilder.dao.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
}
