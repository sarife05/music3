package org.example.musiclibrary.repository;

import org.example.musiclibrary.model.Media;
import org.example.musiclibrary.model.Song;
import org.example.musiclibrary.model.Podcast;
import org.example.musiclibrary.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MediaRepository {

    // CREATE
    public void create(Media media) throws SQLException {
        String sql = "INSERT INTO media (id, name, duration, type, creator) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, media.getId());
            ps.setString(2, media.getName());
            ps.setInt(3, media.getDuration());
            ps.setString(4, media.getType());

            // Для SONG -> artist, для PODCAST -> host
            ps.setString(5, media.getCreator());

            ps.executeUpdate();
        }
    }

    // GET ALL
    public List<Media> getAll() throws SQLException {
        List<Media> list = new ArrayList<>();
        String sql = "SELECT * FROM media";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRowToMedia(rs));
            }
        }
        return list;
    }

    // GET BY ID
    public Media getById(int id) throws SQLException {
        String sql = "SELECT * FROM media WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToMedia(rs);
                }
            }
        }
        return null;
    }

    // UPDATE
    public void update(int id, Media media) throws SQLException {
        String sql = "UPDATE media SET name = ?, duration = ?, type = ?, creator = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, media.getName());
            ps.setInt(2, media.getDuration());
            ps.setString(3, media.getType());
            ps.setString(4, media.getCreator());
            ps.setInt(5, id);

            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM media WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Полиморфное создание объекта
    private Media mapRowToMedia(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int duration = rs.getInt("duration");
        String type = rs.getString("type");
        String creator = rs.getString("creator");

        return switch (type) {
            case "SONG" -> new Song(id, name, duration, creator);
            case "PODCAST" -> new Podcast(id, name, duration, creator);
            default -> throw new IllegalArgumentException("Unknown media type: " + type);
        };
    }
}

