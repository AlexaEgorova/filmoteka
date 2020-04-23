package data;

import android.provider.BaseColumns;

public class ActorSeriasContract {
    private ActorSeriasContract() {
    }

    public static final class ActorSerias implements BaseColumns {
        public final static String TABLE_NAME = "actor_serias";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ACTOR_ID = "actor_id";
        public final static String COLUMN_SERIAS_ID = "serias_id";
    }
}
