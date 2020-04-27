package com.example.filmoteka;


public enum CODE_RETURN {
    COUNTRY(1),
    GENRE(2),
    PRODUCER(3),
    ACTOR(4),
    WANT_TO_WATCH(5),
    WATCHED(6);

    private final int value;

    CODE_RETURN(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}