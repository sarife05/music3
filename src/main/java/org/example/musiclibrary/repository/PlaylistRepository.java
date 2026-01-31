package org.example.musiclibrary.repository;

import org.example.musiclibrary.exception.DatabaseOperationException;
import org.example.musiclibrary.model.Playlist;
import org.example.musiclibrary.model.Media;
import java.util.List;

/**
 * PlaylistRepository interface extending generic CrudRepository.
 * Demonstrates DIP: defines contract for playlist operations.
 */
public interface PlaylistRepository extends CrudRepository<Playlist> {

    /**
     * Add media to playlist
     */
    void addMediaToPlaylist(int playlistId, int mediaId) throws DatabaseOperationException;

    /**
     * Remove media from playlist
     */
    void removeMediaFromPlaylist(int playlistId, int mediaId) throws DatabaseOperationException;

    /**
     * Get all media in a playlist
     */
    List<Media> getPlaylistMedia(int playlistId) throws DatabaseOperationException;

    /**
     * Check if playlist exists by name
     */
    boolean existsByName(String name) throws DatabaseOperationException;

    /**
     * Find playlist by name
     */
    Playlist findByName(String name) throws DatabaseOperationException;
}
