package rh.flashcards.data;

import java.util.List;

import rh.flashcards.entity.Deck;

public interface DeckRepository {

    List<Deck> findAll();

    void create(Deck deck);

    void update(Deck deck);

    void delete(Deck deck);
}
