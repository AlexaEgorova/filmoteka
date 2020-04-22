package data;

import android.provider.BaseColumns;

public class GanreFilmContract {
    private GanreFilmContract() {
    }

    public static final class GanreFilm implements BaseColumns {
        public final static String TABLE_NAME = "ganre_film";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GANRE_ID = "ganre_id";
        public final static String COLUMN_FILM_ID = "film_id";
    }
}
