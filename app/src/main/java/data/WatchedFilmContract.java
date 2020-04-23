package data;

import android.provider.BaseColumns;

public class WatchedFilmContract {
    private WatchedFilmContract() {
    }

    public static final class Watched implements BaseColumns {
        public final static String TABLE_NAME = "watched_film";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FILM_ID = "film_id";
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_RATE = "rate";
        public final static String COLUMN_OPINION = "opinion";
    }
}
