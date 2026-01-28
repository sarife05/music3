package org.example.musiclibrary.model;

public class Podcast extends Media {

    private String host;

    public Podcast(int id, String name, int duration, String host) {
        super(id, name, duration);
        this.name = name;
        this.duration = duration;
        this.host = host;
    }

    @Override
    public String getType() {
        return "PODCAST";
    }

    @Override
    public String getCreator() {
        return host;
    }

    @Override
    public void play() {
        System.out.println("Playing podcast: " + name);
    }
    @Override
    public void validate() {
        validateBase();   // проверка name и duration

        if (host == null || host.isEmpty()) {
            throw new IllegalArgumentException("Podcast host required");
        }
    }

}
