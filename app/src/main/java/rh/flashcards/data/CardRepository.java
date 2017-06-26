package rh.flashcards.data;

import java.util.List;

import rh.flashcards.entity.Card;
import rh.flashcards.entity.Deck;

public interface CardRepository {

    public List<Card> findForDeck(Deck deck);
}
