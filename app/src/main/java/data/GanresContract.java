package data;

import android.provider.BaseColumns;

public class GanresContract {
    private GanresContract() {
    };

    public static final class Ganres implements BaseColumns {
        public final static String TABLE_NAME = "ganres";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_INFO = "information";
    }
}
