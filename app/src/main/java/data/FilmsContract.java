package data;

import android.provider.BaseColumns;

public final class FilmsContract {
    private FilmsContract() {
    };

    public static final class Films implements BaseColumns {
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
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_LINK = "trailer_link";
    }
}
