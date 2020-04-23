package data;

import android.provider.BaseColumns;

public class CountrySeriasContract {
    private CountrySeriasContract() {
    }

    public static final class CountrySerias implements BaseColumns {
        public final static String TABLE_NAME = "country_serias";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_COUNTRY_ID = "country_id";
        public final static String COLUMN_SERIAS_ID = "serias_id";
    }
}
