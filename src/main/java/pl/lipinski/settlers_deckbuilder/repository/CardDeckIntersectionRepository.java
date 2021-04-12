package pl.lipinski.settlers_deckbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lipinski.settlers_deckbuilder.dao.entity.CardDeckIntersection;

@Repository
public interface CardDeckIntersectionRepository extends JpaRepository<CardDeckIntersection, Long> {
    Iterable<CardDeckIntersection> findAllByDeck_Id(Long deck_id);
}
