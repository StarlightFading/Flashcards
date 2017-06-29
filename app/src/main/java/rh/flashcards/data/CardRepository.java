package rh.flashcards.data;

import java.util.List;

import rh.flashcards.entity.Card;
import rh.flashcards.entity.Deck;

public interface CardRepository {

    List<Card> findForDeck(Deck deck);

    void create(Card card, Deck deck);

    void update(Card card);

    void delete(Card card);
}
