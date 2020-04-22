package data;

import android.provider.BaseColumns;

public class ActorFilmContract {
    private ActorFilmContract() {
    };

    public static final class ActorFilm implements BaseColumns {
        public final static String TABLE_NAME = "actor_film";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_ACTOR_ID = "actor_id";
        public final static String COLUMN_FILM_ID = "film_id";
    }
}
