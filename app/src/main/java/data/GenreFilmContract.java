package data;

import android.provider.BaseColumns;

public class GenreFilmContract {
    private GenreFilmContract() {
    }

    public static final class GenreFilm implements BaseColumns {
        public final static String TABLE_NAME = "genre_film";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GENRE_ID = "genre_id";
        public final static String COLUMN_FILM_ID = "film_id";
    }
}
