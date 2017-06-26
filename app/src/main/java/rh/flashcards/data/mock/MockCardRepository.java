package rh.flashcards.data.mock;

import java.util.ArrayList;
import java.util.List;

import rh.flashcards.data.CardRepository;
import rh.flashcards.entity.Card;
import rh.flashcards.entity.Deck;

public class MockCardRepository implements CardRepository {

    @Override
    public List<Card> findForDeck(Deck deck) {
        List<Card> cards = new ArrayList<>();

        cards.add(new Card("사자", "lion"));
        cards.add(new Card("책", "book"));
        cards.add(new Card("사전", "dictionary"));

        return cards;
    }
}
