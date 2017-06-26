package rh.flashcards.entity;

public class Card {

    private Long id;

    private String front;

    private String back;

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
}
