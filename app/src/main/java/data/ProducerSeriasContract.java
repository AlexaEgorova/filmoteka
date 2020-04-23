package data;

import android.provider.BaseColumns;

public class ProducerSeriasContract {
    private ProducerSeriasContract() {
    }

    public static final class ProducerSerias implements BaseColumns {
        public final static String TABLE_NAME = "producer_serias";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PRODUCER_ID = "producer_id";
        public final static String COLUMN_SERIAS_ID = "serias_id";
    }
}
