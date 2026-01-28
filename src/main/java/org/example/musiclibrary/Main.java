package org.example.musiclibrary;
import org.example.musiclibrary.model.*;
import org.example.musiclibrary.service.*;
import org.example.musiclibrary.exception.*;
public class Main {
    public static void main(String[] args) {
        MediaService mediaService = new MediaService();
        SongService songService = new SongService(mediaService);
        PodcastService podcastService = new PodcastService(mediaService);
        PlaylistService playlistService = new PlaylistService();
        System.out.println("=== CREATE MEDIA ===");
        Song s1 = songService.create(new Song(10, "Bohemian Rhapsody", 354, "queen")
        );
        Podcast p1 = new Podcast(11, "Java Podcast", 1800, "Bob");
        int songId = s1.getId();
        int podcastId = p1.getId();
        System.out.println("Song id = " + songId);
        System.out.println("Podcast id = " + podcastId);
        Media m = mediaService.getById(songId);
        Song updatedSong =new Song(10, "Bohemian Rhapsody (Remastered)", 360, "queen");
        songService.update(songId, updatedSong);
        Playlist playlist = new Playlist(1, "My Favorites");
        playlistService.addMediaToPlaylist(playlist, songService.getById(songId)
        );
        playlistService.addMediaToPlaylist(  playlist,  podcastService.getById(podcastId)
        );
        podcastService.delete(podcastId);     }}
