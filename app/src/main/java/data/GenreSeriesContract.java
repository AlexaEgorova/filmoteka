package data;

import android.provider.BaseColumns;

public class GenreSeriesContract {
    private GenreSeriesContract() {
    }

    public static final class GenreSeries implements BaseColumns {
        public final static String TABLE_NAME = "genre_series";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GENRE_ID = "genre_id";
        public final static String COLUMN_SERIES_ID = "series_id";
    }
}
