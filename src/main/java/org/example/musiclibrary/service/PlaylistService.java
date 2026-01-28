package org.example.musiclibrary.service;

import org.example.musiclibrary.model.Media;
import org.example.musiclibrary.model.Playlist;

public class PlaylistService {
    public void addMediaToPlaylist(Playlist playlist, Media media) {
        playlist.addMedia(media);
    }
}
