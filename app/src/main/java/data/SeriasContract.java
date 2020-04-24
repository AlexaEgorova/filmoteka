package data;

import android.provider.BaseColumns;

public final class SeriasContract {
    private SeriasContract() {
    }

    public static final class Serias implements BaseColumns {
        public final static String TABLE_NAME = "serias";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_START_YEAR = "year";
        public final static String COLUMN_SEASONS_NUM = "seasons_num";
        public final static String COLUMN_EP_DURATION = "episode_duration";
        public final static String COLUMN_EP_IN_SEASON_NUM = "episodes_in_season_num";
        // 0 - don't know
        // 1 - been showing
        // 2 - finished
        // 3 - closed
        public final static String COLUMN_STATE = "state";
        public final static String COLUMN_COUNTRY = "country";
        public final static String COLUMN_AGE = "age";
        public final static String COLUMN_GANRE = "ganre";
        public final static String COLUMN_ACTOR = "actor";
        public final static String COLUMN_PRODUCER = "producer";
        public final static String COLUMN_IMDB = "imdb";
        public final static String COLUMN_KINOPOISK = "kinopoisk";

        // 0 - don't add to collection
        // 1 - want to watch
        // 2 - been watching
        // 3 - watched
        public final static String COLUMN_WANT = "want";
        public final static String COLUMN_LINK = "trailer_link";
        public final static String COLUMN_DESCRIPTION = "description";

        public final static String[] COLUMNS = {_ID, COLUMN_NAME, COLUMN_START_YEAR,
                COLUMN_SEASONS_NUM, COLUMN_EP_DURATION,  COLUMN_EP_IN_SEASON_NUM, COLUMN_STATE,
                COLUMN_COUNTRY, COLUMN_AGE, COLUMN_GANRE, COLUMN_ACTOR, COLUMN_PRODUCER,
                COLUMN_IMDB, COLUMN_KINOPOISK, COLUMN_WANT, COLUMN_LINK, COLUMN_DESCRIPTION};
    }
}
