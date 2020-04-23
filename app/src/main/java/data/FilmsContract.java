package data;

import android.provider.BaseColumns;

import java.io.Serializable;

public final class FilmsContract implements Serializable {
    private FilmsContract() {
    }

    public static final class Films implements BaseColumns, Serializable {
        public final static String TABLE_NAME = "films";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_YEAR = "year";
        public final static String COLUMN_COUNTRY = "country";
        public final static String COLUMN_AGE = "age";
        public final static String COLUMN_GANRE = "ganre";
        public final static String COLUMN_ACTOR = "actor";
        public final static String COLUMN_PRODUCER = "producer";
        public final static String COLUMN_IMDB = "imdb";
        public final static String COLUMN_KINOPOISK = "kinopoisk";
        public final static String COLUMN_WANT = "want";
        public final static String COLUMN_LINK = "trailer_link";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String[] COLUMNS = {_ID, COLUMN_NAME, COLUMN_YEAR,
                COLUMN_COUNTRY, COLUMN_AGE, COLUMN_GANRE, COLUMN_ACTOR, COLUMN_PRODUCER,
                COLUMN_IMDB, COLUMN_KINOPOISK, COLUMN_WANT, COLUMN_LINK, COLUMN_DESCRIPTION};
    }
}
