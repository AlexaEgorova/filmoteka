package data;

import android.provider.BaseColumns;

public class CountrySeriesContract {
    private CountrySeriesContract() {
    }

    public static final class CountrySeries implements BaseColumns {
        public final static String TABLE_NAME = "country_series";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_COUNTRY_ID = "country_id";
        public final static String COLUMN_SERIES_ID = "series_id";
    }
}
