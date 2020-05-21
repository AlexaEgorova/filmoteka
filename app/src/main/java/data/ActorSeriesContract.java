package data;

import android.provider.BaseColumns;

public class ActorSeriesContract {
    private ActorSeriesContract() {
    }

    public static final class ActorSeries implements BaseColumns {
        public final static String TABLE_NAME = "actor_series";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ACTOR_ID = "actor_id";
        public final static String COLUMN_SERIES_ID = "series_id";
    }
}
