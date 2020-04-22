package data;

import android.provider.BaseColumns;

public class ProducersContract {
    private ProducersContract() {
    }

    public static final class Producers implements BaseColumns {
        public final static String TABLE_NAME = "producers";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_SURNAME = "surname";
        public final static String COLUMN_INFO = "information";
    }
}
