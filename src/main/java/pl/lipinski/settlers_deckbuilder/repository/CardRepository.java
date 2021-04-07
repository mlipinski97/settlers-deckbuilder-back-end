package pl.lipinski.settlers_deckbuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lipinski.settlers_deckbuilder.dao.entity.Card;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> , JpaSpecificationExecutor<Card> {

    @Query("SELECT c FROM Card c WHERE UPPER(c.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Iterable<Card> findAllByNameLike(@Param("name") String name);
}
