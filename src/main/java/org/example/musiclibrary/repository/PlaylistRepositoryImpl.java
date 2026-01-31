package org.example.musiclibrary.repository;

import org.example.musiclibrary.exception.DatabaseOperationException;
import org.example.musiclibrary.exception.ResourceNotFoundException;
import org.example.musiclibrary.model.*;
import org.example.musiclibrary.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of PlaylistRepository using JDBC.
 * Follows SRP: Handles only database operations for Playlist.
 */
public class PlaylistRepositoryImpl implements PlaylistRepository {

    private final MediaRepositoryImpl mediaRepository = new MediaRepositoryImpl();

    @Override
    public Playlist create(Playlist entity) throws DatabaseOperationException {
        String sql = "INSERT INTO playlists (name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getDescription());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating playlist failed, no rows affected");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }

            // Add media items if any
            for (Media media : entity.getItems()) {
                if (media.getId() > 0) {
                    addMediaToPlaylist(entity.getId(), media.getId());
                }
            }

            return entity;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create playlist", e);
        }
    }

    @Override
    public List<Playlist> getAll() throws DatabaseOperationException {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM playlists ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                List<Media> items = getPlaylistMedia(id);

                playlists.add(new Playlist(id, name, description, items));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve all playlists", e);
        }

        return playlists;
    }

    @Override
    public Playlist getById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "SELECT * FROM playlists WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    List<Media> items = getPlaylistMedia(id);

                    return new Playlist(id, name, description, items);
                } else {
                    throw new ResourceNotFoundException("Playlist", id);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve playlist by ID", e);
        }
    }

    @Override
    public Playlist update(int id, Playlist entity) throws ResourceNotFoundException, DatabaseOperationException {
        if (!exists(id)) {
            throw new ResourceNotFoundException("Playlist", id);
        }

        String sql = "UPDATE playlists SET name = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getName());
            pstmt.setString(2, entity.getDescription());
            pstmt.setInt(3, id);

            pstmt.executeUpdate();
            entity.setId(id);

            return entity;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update playlist", e);
        }
    }

    @Override
    public boolean delete(int id) throws ResourceNotFoundException, DatabaseOperationException {
        if (!exists(id)) {
            throw new ResourceNotFoundException("Playlist", id);
        }

        String sql = "DELETE FROM playlists WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete playlist", e);
        }
    }

    @Override
    public boolean exists(int id) throws DatabaseOperationException {
        String sql = "SELECT COUNT(*) FROM playlists WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to check playlist existence", e);
        }

        return false;
    }

    @Override
    public void addMediaToPlaylist(int playlistId, int mediaId) throws DatabaseOperationException {
        String sql = "INSERT OR IGNORE INTO playlist_items (playlist_id, media_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, mediaId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to add media to playlist", e);
        }
    }

    @Override
    public void removeMediaFromPlaylist(int playlistId, int mediaId) throws DatabaseOperationException {
        String sql = "DELETE FROM playlist_items WHERE playlist_id = ? AND media_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, mediaId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to remove media from playlist", e);
        }
    }

    @Override
    public List<Media> getPlaylistMedia(int playlistId) throws DatabaseOperationException {
        List<Media> mediaList = new ArrayList<>();
        String sql = """
            SELECT m.* FROM media m
            INNER JOIN playlist_items pi ON m.id = pi.media_id
            WHERE pi.playlist_id = ?
            ORDER BY pi.position, m.id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playlistId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    Media media = mediaRepository.getById(id);
                    mediaList.add(media);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to get playlist media", e);
        } catch (ResourceNotFoundException e) {
            // This shouldn't happen if database integrity is maintained
            throw new DatabaseOperationException("Media referenced in playlist not found", e);
        }

        return mediaList;
    }

    @Override
    public boolean existsByName(String name) throws DatabaseOperationException {
        String sql = "SELECT COUNT(*) FROM playlists WHERE LOWER(name) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to check playlist existence by name", e);
        }

        return false;
    }

    @Override
    public Playlist findByName(String name) throws DatabaseOperationException {
        String sql = "SELECT * FROM playlists WHERE LOWER(name) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String playlistName = rs.getString("name");
                    String description = rs.getString("description");
                    List<Media> items = getPlaylistMedia(id);

                    return new Playlist(id, playlistName, description, items);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find playlist by name", e);
        }

        return null;
    }
}