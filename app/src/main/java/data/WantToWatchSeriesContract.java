package data;

import android.provider.BaseColumns;

public class WantToWatchSeriesContract {
    private WantToWatchSeriesContract() {
    }

    public static final class WantToWatchSeries implements BaseColumns {
        public final static String TABLE_NAME = "want_to_watch_series";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SERIES_ID = "series_id";
        public final static String COLUMN_ADD_DATE = "add_date";
    }
}
