package rh.flashcards.data.database;

import android.provider.BaseColumns;

final class Schema {

    static final class Deck implements BaseColumns {
        static final String TABLE_NAME = "deck";

        static final String ID = _ID;
        static final String NAME = "name";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + ID + " INTEGER PRIMARY KEY,"
                        + NAME + " TEXT NOT NULL)";

        static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    static final class Card implements BaseColumns {
        static final String TABLE_NAME = "card";

        static final String ID = _ID;
        static final String FRONT = "front";
        static final String BACK = "back";
        static final String DECK_ID = "deck_id";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + ID + " INTEGER PRIMARY KEY,"
                        + FRONT + " TEXT NOT NULL,"
                        + BACK + " TEXT NOT NULL,"
                        + DECK_ID + " INTEGER NOT NULL REFERENCES " + Deck.TABLE_NAME + " (" + Deck.ID + "))";

        static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
