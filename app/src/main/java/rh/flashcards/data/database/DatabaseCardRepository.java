package rh.flashcards.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import rh.flashcards.data.CardRepository;
import rh.flashcards.entity.Card;
import rh.flashcards.entity.Deck;

public class DatabaseCardRepository implements CardRepository {

    private final DatabaseHelper dbHelper;

    public DatabaseCardRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    public List<Card> findForDeck(Deck deck) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        cursor = db.query(
                Schema.Card.TABLE_NAME,
                getColumns(),
                Schema.Card.DECK_ID + "=?",
                new String[]{String.valueOf(deck.getId())},
                null,
                null,
                null);

        List<Card> cards = new ArrayList<>();
        while (cursor.moveToNext()) {
            cards.add(createCardFromCursor(cursor));
        }

        return cards;
    }

    @Override
    public List<Card> findCardsForStudying(Deck deck, LocalDate studyDate) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        cursor = db.query(
                Schema.Card.TABLE_NAME,
                getColumns(),
                Schema.Card.DECK_ID + "=?"
                        + " AND (julianday(?) - julianday(" + Schema.Card.FRONT_REVIEWED + ") >= " + Schema.Card.FRONT_SCORE
                        + " OR julianday(?) - julianday(" + Schema.Card.BACK_REVIEWED + ") >= " + Schema.Card.BACK_SCORE
                        + " OR " + Schema.Card.FRONT_REVIEWED + " is NULL"
                        + " OR " + Schema.Card.BACK_REVIEWED + " is NULL" + ")",
                new String[]{String.valueOf(deck.getId()), formatDate(studyDate), formatDate(studyDate)},
                null,
                null,
                null);

        List<Card> cards = new ArrayList<>();
        while (cursor.moveToNext()) {
            cards.add(createCardFromCursor(cursor));
        }

        return cards;
    }

    @Override
    public void create(Card card, Deck deck) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Long id = db.insert(Schema.Card.TABLE_NAME, null, createContentValues(card, deck));
        card.setId(id);
    }

    @Override
    public void update(Card card) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(
                Schema.Card.TABLE_NAME,
                createContentValues(card),
                Schema.Card.ID + "=?",
                new String[]{String.valueOf(card.getId())});
    }

    @Override
    public void delete(Card card) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Schema.Card.TABLE_NAME, Schema.Card.ID + "=?", new String[]{String.valueOf(card.getId())});
    }

    private ContentValues createContentValues(Card card, Deck deck) {
        ContentValues values = createContentValues(card);
        values.put(Schema.Card.DECK_ID, deck.getId());

        return values;
    }

    private ContentValues createContentValues(Card card) {
        ContentValues values = new ContentValues();
        values.put(Schema.Card.FRONT, card.getFront());
        values.put(Schema.Card.BACK, card.getBack());
        values.put(Schema.Card.FRONT_SCORE, card.getFrontScore());
        values.put(Schema.Card.BACK_SCORE, card.getBackScore());

        values.put(Schema.Card.FRONT_REVIEWED, formatDate(card.getFrontReviewed()));
        values.put(Schema.Card.BACK_REVIEWED, formatDate(card.getBackReviewed()));

        return values;
    }

    private Card createCardFromCursor(Cursor cursor) {
        Card card = new Card();
        card.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Schema.Card.ID)));
        card.setFront(cursor.getString(cursor.getColumnIndexOrThrow(Schema.Card.FRONT)));
        card.setBack(cursor.getString(cursor.getColumnIndexOrThrow(Schema.Card.BACK)));
        card.setFrontScore(cursor.getInt(cursor.getColumnIndexOrThrow(Schema.Card.FRONT_SCORE)));
        card.setBackScore(cursor.getInt(cursor.getColumnIndexOrThrow(Schema.Card.BACK_SCORE)));
        card.setFrontReviewed(parseDate(cursor.getString(cursor.getColumnIndexOrThrow(Schema.Card.FRONT_REVIEWED))));
        card.setBackReviewed(parseDate(cursor.getString(cursor.getColumnIndexOrThrow(Schema.Card.BACK_REVIEWED))));

        return card;
    }

    private String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }

        return date.format(DateTimeFormatter.ISO_DATE);
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }

        return LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
    }

    private String[] getColumns() {
        return new String[]{
                Schema.Card.ID,
                Schema.Card.FRONT,
                Schema.Card.BACK,
                Schema.Card.FRONT_SCORE,
                Schema.Card.BACK_SCORE,
                Schema.Card.FRONT_REVIEWED,
                Schema.Card.BACK_REVIEWED,
        };
    }
}
