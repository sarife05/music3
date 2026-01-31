package org.example.musiclibrary.repository;

import org.example.musiclibrary.exception.DatabaseOperationException;
import org.example.musiclibrary.exception.ResourceNotFoundException;
import org.example.musiclibrary.model.*;
import org.example.musiclibrary.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of MediaRepository using JDBC.
 * Follows SRP: Handles only database operations for Media.
 * Uses PreparedStatements to prevent SQL injection.
 */
public class MediaRepositoryImpl implements MediaRepository {

    @Override
    public Media create(Media entity) throws DatabaseOperationException {
        String sql = """
            INSERT INTO media (name, duration, type, creator, album, genre, price, host, episode_number, category)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, entity.getName());
            pstmt.setInt(2, entity.getDuration());
            pstmt.setString(3, entity.getType().name());
            pstmt.setString(4, entity.getCreator());

            // Song-specific fields
            if (entity instanceof Song song) {
                pstmt.setString(5, song.getAlbum());
                pstmt.setString(6, song.getGenre());
                pstmt.setDouble(7, song.getPrice());
                pstmt.setNull(8, Types.VARCHAR);
                pstmt.setInt(9, 0);
                pstmt.setNull(10, Types.VARCHAR);
            }
            // Podcast-specific fields
            else if (entity instanceof Podcast podcast) {
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setDouble(7, 0.0);
                pstmt.setString(8, podcast.getHost());
                pstmt.setInt(9, podcast.getEpisodeNumber());
                pstmt.setString(10, podcast.getCategory());
            }

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating media failed, no rows affected");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                }
            }

            return entity;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create media", e);
        }
    }

    @Override
    public List<Media> getAll() throws DatabaseOperationException {
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                mediaList.add(mapResultSetToMedia(rs));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve all media", e);
        }

        return mediaList;
    }

    @Override
    public Media getById(int id) throws ResourceNotFoundException, DatabaseOperationException {
        String sql = "SELECT * FROM media WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMedia(rs);
                } else {
                    throw new ResourceNotFoundException("Media", id);
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve media by ID", e);
        }
    }

    @Override
    public Media update(int id, Media entity) throws ResourceNotFoundException, DatabaseOperationException {
        // Check if exists
        if (!exists(id)) {
            throw new ResourceNotFoundException("Media", id);
        }

        String sql = """
            UPDATE media 
            SET name = ?, duration = ?, creator = ?, album = ?, genre = ?, 
                price = ?, host = ?, episode_number = ?, category = ?
            WHERE id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, entity.getName());
            pstmt.setInt(2, entity.getDuration());
            pstmt.setString(3, entity.getCreator());

            if (entity instanceof Song song) {
                pstmt.setString(4, song.getAlbum());
                pstmt.setString(5, song.getGenre());
                pstmt.setDouble(6, song.getPrice());
                pstmt.setNull(7, Types.VARCHAR);
                pstmt.setInt(8, 0);
                pstmt.setNull(9, Types.VARCHAR);
            } else if (entity instanceof Podcast podcast) {
                pstmt.setNull(4, Types.VARCHAR);
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setDouble(6, 0.0);
                pstmt.setString(7, podcast.getHost());
                pstmt.setInt(8, podcast.getEpisodeNumber());
                pstmt.setString(9, podcast.getCategory());
            }

            pstmt.setInt(10, id);
            pstmt.executeUpdate();

            entity.setId(id);
            return entity;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update media", e);
        }
    }

    @Override
    public boolean delete(int id) throws ResourceNotFoundException, DatabaseOperationException {
        if (!exists(id)) {
            throw new ResourceNotFoundException("Media", id);
        }

        String sql = "DELETE FROM media WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete media", e);
        }
    }

    @Override
    public boolean exists(int id) throws DatabaseOperationException {
        String sql = "SELECT COUNT(*) FROM media WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to check media existence", e);
        }

        return false;
    }

    @Override
    public List<Media> findByType(Media.MediaType type) throws DatabaseOperationException {
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE type = ? ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(mapResultSetToMedia(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find media by type", e);
        }

        return mediaList;
    }

    @Override
    public List<Media> findByCreator(String creator) throws DatabaseOperationException {
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE LOWER(creator) = LOWER(?) ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, creator);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(mapResultSetToMedia(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to find media by creator", e);
        }

        return mediaList;
    }

    @Override
    public List<Media> searchByName(String keyword) throws DatabaseOperationException {
        List<Media> mediaList = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + keyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mediaList.add(mapResultSetToMedia(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to search media by name", e);
        }

        return mediaList;
    }

    @Override
    public boolean existsByNameAndTypeAndCreator(String name, Media.MediaType type, String creator)
            throws DatabaseOperationException {
        String sql = "SELECT COUNT(*) FROM media WHERE LOWER(name) = LOWER(?) AND type = ? AND LOWER(creator) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type.name());
            pstmt.setString(3, creator);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to check media existence", e);
        }

        return false;
    }

    /**
     * Helper method to map ResultSet to Media object (polymorphic instantiation)
     */
    private Media mapResultSetToMedia(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int duration = rs.getInt("duration");
        String type = rs.getString("type");
        String creator = rs.getString("creator");

        if ("SONG".equals(type)) {
            String album = rs.getString("album");
            String genre = rs.getString("genre");
            double price = rs.getDouble("price");
            return new Song(id, name, duration, creator, album, genre, price);
        } else if ("PODCAST".equals(type)) {
            String host = rs.getString("host");
            int episodeNumber = rs.getInt("episode_number");
            String category = rs.getString("category");
            return new Podcast(id, name, duration, creator, host, episodeNumber, category);
        }

        throw new SQLException("Unknown media type: " + type);
    }
}