package rh.flashcards.data;

import org.threeten.bp.LocalDate;

import java.util.List;

import rh.flashcards.entity.Card;
import rh.flashcards.entity.Deck;

public interface CardRepository {

    List<Card> findForDeck(Deck deck);

    List<Card> findCardsForStudying(Deck deck, LocalDate studyDate);

    void create(Card card, Deck deck);

    void update(Card card);

    void delete(Card card);
}
