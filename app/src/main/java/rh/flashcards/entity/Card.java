package rh.flashcards.entity;

import org.threeten.bp.LocalDate;

import java.io.Serializable;

public class Card implements Serializable {

    private Long id;

    private String front;

    private String back;

    private Deck deck;

    private int frontScore;

    private int backScore;

    private LocalDate frontReviewed;

    private LocalDate backReviewed;

    public Card() {
    }

    public Card(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public int getFrontScore() {
        return frontScore;
    }

    public void setFrontScore(int frontScore) {
        this.frontScore = frontScore;
    }

    public int getBackScore() {
        return backScore;
    }

    public void setBackScore(int backScore) {
        this.backScore = backScore;
    }

    public LocalDate getFrontReviewed() {
        return frontReviewed;
    }

    public void setFrontReviewed(LocalDate frontReviewed) {
        this.frontReviewed = frontReviewed;
    }

    public LocalDate getBackReviewed() {
        return backReviewed;
    }

    public void setBackReviewed(LocalDate backReviewed) {
        this.backReviewed = backReviewed;
    }
}
