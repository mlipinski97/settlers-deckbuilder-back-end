package pl.lipinski.settlers_deckbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.lipinski.settlers_deckbuilder.dao.entity.CardDeckIntersection;

import java.util.Optional;

@Repository
public interface CardDeckIntersectionRepository extends JpaRepository<CardDeckIntersection, Long> {
    Iterable<CardDeckIntersection> findAllByDeck_Id(Long deck_id);

    Optional<CardDeckIntersection> findByDeckIdAndCardId(Long deckId, Long CardId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CardDeckIntersection cdi WHERE cdi.card.id = :cardId AND cdi.deck.id = :deckId")
    void deleteByCardIdAndDeckId(@Param("cardId") Long cardId, @Param("deckId") Long deckId);

}
