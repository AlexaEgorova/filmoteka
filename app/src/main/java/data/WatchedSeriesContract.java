package data;

import android.provider.BaseColumns;

public class WatchedSeriesContract {
    private WatchedSeriesContract() {
    }

    public static final class WatchedSeries implements BaseColumns {
        public final static String TABLE_NAME = "watched_series";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SERIES_ID = "series_id";
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_RATE = "rate";
        public final static String COLUMN_OPINION = "opinion";
    }
}
