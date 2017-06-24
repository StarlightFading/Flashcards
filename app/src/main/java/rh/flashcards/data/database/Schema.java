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
}
