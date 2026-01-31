package org.example.musiclibrary.controller;

import org.example.musiclibrary.exception.*;
import org.example.musiclibrary.model.*;
import org.example.musiclibrary.service.MediaService;
import org.example.musiclibrary.service.PlaylistService;

import java.util.List;

/**
 * Controller for Music Library operations.
 * Follows SRP: Single responsibility is coordinating requests between user and services.
 * Follows DIP: Depends on service interfaces, not implementations.
 * NO business logic - only delegates to services.
 */
public class MusicLibraryController {

    private final MediaService mediaService;
    private final PlaylistService playlistService;

    /**
     * Constructor injection demonstrating DIP
     */
    public MusicLibraryController(MediaService mediaService, PlaylistService playlistService) {
        this.mediaService = mediaService;
        this.playlistService = playlistService;
    }

    // ==================== MEDIA OPERATIONS ====================

    public Media createSong(String name, int duration, String creator, String album, String genre, double price) {
        try {
            Song song = new Song(name, duration, creator, album, genre);
            song.setPrice(price);
            Media created = mediaService.createMedia(song);
            System.out.println("✓ Song created successfully: " + created.getName());
            return created;
        } catch (InvalidInputException | DatabaseOperationException e) {
            System.err.println("✗ Failed to create song: " + e.getMessage());
            return null;
        }
    }

    public Media createPodcast(String name, int duration, String creator, String host, int episodeNumber, String category) {
        try {
            Podcast podcast = new Podcast(name, duration, creator, host, episodeNumber, category);
            Media created = mediaService.createMedia(podcast);
            System.out.println("✓ Podcast created successfully: " + created.getName());
            return created;
        } catch (InvalidInputException | DatabaseOperationException e) {
            System.err.println("✗ Failed to create podcast: " + e.getMessage());
            return null;
        }
    }

    public List<Media> getAllMedia() {
        try {
            return mediaService.getAllMedia();
        } catch (DatabaseOperationException e) {
            System.err.println("✗ Failed to retrieve media: " + e.getMessage());
            return List.of();
        }
    }

    public Media getMediaById(int id) {
        try {
            return mediaService.getMediaById(id);
        } catch (ResourceNotFoundException | DatabaseOperationException e) {
            System.err.println("✗ Failed to retrieve media: " + e.getMessage());
            return null;
        }
    }

    public Media updateMedia(int id, Media media) {
        try {
            Media updated = mediaService.updateMedia(id, media);
            System.out.println("✓ Media updated successfully: " + updated.getName());
            return updated;
        } catch (ResourceNotFoundException | InvalidInputException | DatabaseOperationException e) {
            System.err.println("✗ Failed to update media: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteMedia(int id) {
        try {
            mediaService.deleteMedia(id);
            System.out.println("✓ Media deleted successfully");
            return true;
        } catch (ResourceNotFoundException | DatabaseOperationException e) {
            System.err.println("✗ Failed to delete media: " + e.getMessage());
            return false;
        }
    }

    public List<Media> getMediaByType(Media.MediaType type) {
        try {
            return mediaService.getMediaByType(type);
        } catch (DatabaseOperationException e) {
            System.err.println("✗ Failed to retrieve media by type: " + e.getMessage());
            return List.of();
        }
    }

    public List<Media> searchMedia(String keyword) {
        try {
            return mediaService.searchMediaByName(keyword);
        } catch (DatabaseOperationException e) {
            System.err.println("✗ Failed to search media: " + e.getMessage());
            return List.of();
        }
    }

    // ==================== PLAYLIST OPERATIONS ====================

    public Playlist createPlaylist(String name, String description) {
        try {
            Playlist playlist = new Playlist(name, description);
            Playlist created = playlistService.createPlaylist(playlist);
            System.out.println("✓ Playlist created successfully: " + created.getName());
            return created;
        } catch (InvalidInputException | DatabaseOperationException e) {
            System.err.println("✗ Failed to create playlist: " + e.getMessage());
            return null;
        }
    }

    public List<Playlist> getAllPlaylists() {
        try {
            return playlistService.getAllPlaylists();
        } catch (DatabaseOperationException e) {
            System.err.println("✗ Failed to retrieve playlists: " + e.getMessage());
            return List.of();
        }
    }

    public Playlist getPlaylistById(int id) {
        try {
            return playlistService.getPlaylistById(id);
        } catch (ResourceNotFoundException | DatabaseOperationException e) {
            System.err.println("✗ Failed to retrieve playlist: " + e.getMessage());
            return null;
        }
    }

    public boolean addMediaToPlaylist(int playlistId, int mediaId) {
        try {
            playlistService.addMediaToPlaylist(playlistId, mediaId);
            System.out.println("✓ Media added to playlist successfully");
            return true;
        } catch (ResourceNotFoundException | DatabaseOperationException e) {
            System.err.println("✗ Failed to add media to playlist: " + e.getMessage());
            return false;
        }
    }

    public boolean removeMediaFromPlaylist(int playlistId, int mediaId) {
        try {
            playlistService.removeMediaFromPlaylist(playlistId, mediaId);
            System.out.println("✓ Media removed from playlist successfully");
            return true;
        } catch (DatabaseOperationException e) {
            System.err.println("✗ Failed to remove media from playlist: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePlaylist(int id) {
        try {
            playlistService.deletePlaylist(id);
            System.out.println("✓ Playlist deleted successfully");
            return true;
        } catch (ResourceNotFoundException | DatabaseOperationException e) {
            System.err.println("✗ Failed to delete playlist: " + e.getMessage());
            return false;
        }
    }

    // ==================== DISPLAY OPERATIONS ====================

    public void displayAllMedia() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              ALL MEDIA IN LIBRARY                      ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        List<Media> allMedia = getAllMedia();
        if (allMedia.isEmpty()) {
            System.out.println("  (No media found)");
        } else {
            for (Media media : allMedia) {
                System.out.println("  [" + media.getId() + "] " + media.toString());
            }
        }
    }

    public void displayMediaInfo(int id) {
        Media media = getMediaById(id);
        if (media != null) {
            media.displayInfo();
        }
    }

    public void displayAllPlaylists() {
        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out.println("║              ALL PLAYLISTS                             ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");

        List<Playlist> allPlaylists = getAllPlaylists();
        if (allPlaylists.isEmpty()) {
            System.out.println("  (No playlists found)");
        } else {
            for (Playlist playlist : allPlaylists) {
                System.out.println("  [" + playlist.getId() + "] " + playlist.toString());
            }
        }
    }

    public void displayPlaylistInfo(int id) {
        Playlist playlist = getPlaylistById(id);
        if (playlist != null) {
            playlist.print();
        }
    }
}