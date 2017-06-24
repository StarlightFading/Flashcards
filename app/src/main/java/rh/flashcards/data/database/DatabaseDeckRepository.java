package rh.flashcards.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rh.flashcards.data.DeckRepository;
import rh.flashcards.entity.Deck;

public class DatabaseDeckRepository implements DeckRepository {

    private final DatabaseHelper dbHelper;

    public DatabaseDeckRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    @Override
    public List<Deck> findAll() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Schema.Deck.TABLE_NAME, getColumns(), null, null, null, null, null);

        List<Deck> decks = new ArrayList<>();
        while (cursor.moveToNext()) {
            decks.add(createDeckFromCursor(cursor));
        }

        return decks;
    }

    @Override
    public void create(Deck deck) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Long id = db.insert(Schema.Deck.TABLE_NAME, null, createContentValues(deck));
        deck.setId(id);
    }

    @Override
    public void update(Deck deck) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.update(
                Schema.Deck.TABLE_NAME,
                createContentValues(deck),
                Schema.Deck.ID + "=?",
                new String[]{String.valueOf(deck.getId())}
        );
    }

    @Override
    public void delete(Deck deck) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(
                Schema.Deck.TABLE_NAME,
                Schema.Deck.ID + "=?",
                new String[]{String.valueOf(deck.getId())}
        );
    }

    @NonNull
    private String[] getColumns() {
        return new String[]{
                Schema.Deck._ID,
                Schema.Deck.NAME,
        };
    }

    @NonNull
    private Deck createDeckFromCursor(Cursor cursor) {
        Deck deck = new Deck();
        deck.setId(cursor.getLong(cursor.getColumnIndexOrThrow(Schema.Deck.ID)));
        deck.setName(cursor.getString(cursor.getColumnIndexOrThrow(Schema.Deck.NAME)));

        return deck;
    }

    @NonNull
    private ContentValues createContentValues(Deck deck) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Schema.Deck.NAME, deck.getName());

        return contentValues;
    }
}
