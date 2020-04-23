package data;

import android.provider.BaseColumns;

public class WatchedSeriasContract {
    private WatchedSeriasContract() {
    }

    public static final class WatchedSerias implements BaseColumns {
        public final static String TABLE_NAME = "watched_serias";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SERIAS_ID = "serias_id";
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_RATE = "rate";
        public final static String COLUMN_OPINION = "opinion";
    }
}
