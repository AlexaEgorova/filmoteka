package data;

import android.provider.BaseColumns;

public class GenresContract {
    private GenresContract() {
    }

    public static final class Genres implements BaseColumns {
        public final static String TABLE_NAME = "genres";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_INFO = "information";
    }
}
