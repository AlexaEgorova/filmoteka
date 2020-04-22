package data;

import android.provider.BaseColumns;

public class ActorsContract {
    private ActorsContract() {
    };

    public static final class Actors implements BaseColumns {
        public final static String TABLE_NAME = "actors";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_SURNAME = "surname";
        public final static String COLUMN_NICKNAME = "nickname";
        public final static String COLUMN_BIRTHDAY = "birthday";
        public final static String COLUMN_INFO = "information";

    }
}
