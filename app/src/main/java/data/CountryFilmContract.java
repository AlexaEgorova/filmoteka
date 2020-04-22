package data;

import android.provider.BaseColumns;

public class CountryFilmContract {
    private CountryFilmContract() {
    };

    public static final class CountryFilm implements BaseColumns {
        public final static String TABLE_NAME = "country_film";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_COUNTRY_ID = "country_id";
        public final static String COLUMN_FILM_ID = "film_id";
    }
}
