package org.example.musiclibrary.service;

import org.example.musiclibrary.model.Song;
import org.example.musiclibrary.model.Media;
import org.example.musiclibrary.exception.*;

import java.util.ArrayList;
import java.util.List;

public class SongService {

    private final MediaService mediaService;
    public SongService(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public Song create(Song song) {
        if (!"SONG".equals(song.getType())) {
            throw new InvalidInputException("Invalid type for Song");
        }

        mediaService.create(song);
        return song;
    }


    public List<Song> getAll() {
        List<Media> all = mediaService.getAll();
        List<Song> songs = new ArrayList<>();

        for (Media m : all) {
            if ("SONG".equals(m.getType())) {
                songs.add((Song) m); // полиморфизм
            }
        }

        return songs;
    }


    public Song getById(int id) {
        Media media = mediaService.getById(id);

        if (!"SONG".equals(media.getType())) {
            throw new ResourceNotFoundException("Media with id " + id + " is not a Song");
        }

        return (Song) media;
    }


    public void update(int id, Song song) {
        if (!"SONG".equals(song.getType())) {
            throw new InvalidInputException("Invalid type for Song");
        }

        mediaService.update(id, song);
    }


    public void delete(int id) {
        Media media = mediaService.getById(id);

        if (!"SONG".equals(media.getType())) {
            throw new ResourceNotFoundException("Media with id " + id + " is not a Song");
        }

        mediaService.delete(id);
    }
}

