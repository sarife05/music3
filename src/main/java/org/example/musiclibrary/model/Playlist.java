package org.example.musiclibrary.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist implements Validatable {

    private int id;
    private String name;
    private List<Media> items = new ArrayList<>();

    public Playlist(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addMedia(Media media) {
        items.add(media);
    }

    public List<Media> getItems() {
        return items;
    }

    @Override
    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Playlist name cannot be empty");
        }
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Playlist must contain at least one media item");
        }
    }

    public void playAll() {
        for (Media m : items) {
            m.play();   // ПОЛИМОРФИЗМ
        }
    }
}
