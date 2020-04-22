package data;

import android.provider.BaseColumns;

public class CountriesContract {
    private CountriesContract() {
    };

    public static final class Countries implements BaseColumns {
        public final static String TABLE_NAME = "countries";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";

    }
}
