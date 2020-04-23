package data;

import android.provider.BaseColumns;

public class WantToWatchSeriasContract {
    private WantToWatchSeriasContract() {
    }

    public static final class WantToWatchSerias implements BaseColumns {
        public final static String TABLE_NAME = "want_to_watch";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SERIAS_ID = "serias_id";
        public final static String COLUMN_ADD_DATE = "add_date";
    }
}
