package data;


import android.provider.BaseColumns;

public final class FilmsContract {
    private FilmsContract() {
    };

    public static final class AddMovie implements BaseColumns {
        public final static String TABLE_NAME = "movies";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_COUNTRY = "country";
        public final static String COLUMN_YEAR = "year";
        public final static String COLUMN_DESCRIPTION = "description";
    }
}
