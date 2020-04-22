package data;

import android.provider.BaseColumns;

public class ProducerFilmContract {
    private ProducerFilmContract() {
    }

    public static final class ProducerFilm implements BaseColumns {
        public final static String TABLE_NAME = "producer_film";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCER_ID = "producer_id";
        public final static String COLUMN_FILM_ID = "film_id";
    }
}
