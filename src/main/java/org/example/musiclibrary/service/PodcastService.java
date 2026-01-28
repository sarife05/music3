package org.example.musiclibrary.service;

import org.example.musiclibrary.model.Podcast;
import org.example.musiclibrary.model.Media;
import org.example.musiclibrary.exception.*;

import java.util.ArrayList;
import java.util.List;

public class PodcastService {

    private final MediaService mediaService;
    public PodcastService(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    public Podcast create(Podcast podcast) {
        if (!"PODCAST".equals(podcast.getType())) {
            throw new InvalidInputException("Invalid type for Podcast");
        }

        mediaService.create(podcast);
        return podcast;
    }


    public List<Podcast> getAll() {
        List<Media> all = mediaService.getAll();
        List<Podcast> podcasts = new ArrayList<>();

        for (Media m : all) {
            if ("PODCAST".equals(m.getType())) {
                podcasts.add((Podcast) m);
            }
        }

        return podcasts;
    }


    public Podcast getById(int id) {
        Media media = mediaService.getById(id);

        if (!"PODCAST".equals(media.getType())) {
            throw new ResourceNotFoundException("Media with id " + id + " is not a Podcast");
        }

        return (Podcast) media;
    }


    public void update(int id, Podcast podcast) {
        if (!"PODCAST".equals(podcast.getType())) {
            throw new InvalidInputException("Invalid type for Podcast");
        }

        mediaService.update(id, podcast);
    }


    public void delete(int id) {
        Media media = mediaService.getById(id);

        if (!"PODCAST".equals(media.getType())) {
            throw new ResourceNotFoundException("Media with id " + id + " is not a Podcast");
        }

        mediaService.delete(id);
    }
}

