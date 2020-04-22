package data;

import android.provider.BaseColumns;

public class WantToWatchContract {
    private WantToWatchContract() {
    };

    public static final class WantToWatch implements BaseColumns {
        public final static String TABLE_NAME = "want_to_watch";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_FILM_ID = "film_id";
        public final static String COLUMN_ADD_DATE = "add_date";
    }
}
