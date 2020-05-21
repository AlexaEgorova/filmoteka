package data;

import android.provider.BaseColumns;

public class WatchingSeriesContract {
    private WatchingSeriesContract() {
    }

    public static final class WatchingSeries implements BaseColumns {
        public final static String TABLE_NAME = "watching_series";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SERIES_ID = "series_id";
        public final static String COLUMN_SEASON_NUM = "season_number";
        public final static String COLUMN_EPISODE_NUM = "episode_number";
    }
}
