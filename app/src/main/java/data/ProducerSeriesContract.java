package data;

import android.provider.BaseColumns;

public class ProducerSeriesContract {
    private ProducerSeriesContract() {
    }

    public static final class ProducerSeries implements BaseColumns {
        public final static String TABLE_NAME = "producer_series";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCER_ID = "producer_id";
        public final static String COLUMN_SERIES_ID = "series_id";
    }
}
