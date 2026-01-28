package org.example.musiclibrary.model;
public class Song extends Media {

    private String artist;

    public Song(int id, String name, int duration, String artist) {
        super(id, name, duration);
        this.name = name;
        this.duration = duration;
        this.artist = artist;
    }

    @Override
    public String getType() {
        return "SONG";
    }

    @Override
    public String getCreator() {
        return artist;
    }

    @Override
    public void play() {
        System.out.println("Playing song: " + name);
    }

    @Override
    public void validate() {
        validateBase();   // общая проверка из Media

        if (artist == null || artist.isEmpty()) {
            throw new IllegalArgumentException("Artist is required");
        }
    }}


