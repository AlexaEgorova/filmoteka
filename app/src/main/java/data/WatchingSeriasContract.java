package data;

import android.provider.BaseColumns;

public class WatchingSeriasContract {
    private WatchingSeriasContract() {
    }

    public static final class WatchingSerias implements BaseColumns {
        public final static String TABLE_NAME = "watching_serias";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_SERIAS_ID = "serias_id";
        public final static String COLUMN_SEASON_NUM = "season_number";
        public final static String COLUMN_EPISODE_NUM = "episode_number";
    }
}
