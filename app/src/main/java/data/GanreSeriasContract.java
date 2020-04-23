package data;

import android.provider.BaseColumns;

public class GanreSeriasContract {
    private GanreSeriasContract() {
    }

    public static final class GanreSerias implements BaseColumns {
        public final static String TABLE_NAME = "ganre_serias";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GANRE_ID = "ganre_id";
        public final static String COLUMN_SERIAS_ID = "serias_id";
    }
}
