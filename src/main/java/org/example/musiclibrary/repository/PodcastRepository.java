package org.example.musiclibrary.repository;

import org.example.musiclibrary.model.Podcast;
import org.example.musiclibrary.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PodcastRepository {

    // CREATE
    public void create(Podcast podcast) throws SQLException {
        String sql = "INSERT INTO podcasts (id, name, duration, host) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, podcast.getId());
            ps.setString(2, podcast.getName());
            ps.setInt(3, podcast.getDuration());
            ps.setString(4, podcast.getCreator());  // корректный getter

            ps.executeUpdate();
        }
    }

    // GET ALL
    public List<Podcast> getAll() throws SQLException {
        List<Podcast> list = new ArrayList<>();
        String sql = "SELECT * FROM podcasts";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Podcast(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("host")
                ));
            }
        }
        return list;
    }

    // GET BY ID
    public Podcast getById(int id) throws SQLException {
        String sql = "SELECT * FROM podcasts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Podcast(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("duration"),
                            rs.getString("host")
                    );
                }
            }
        }
        return null;
    }

    // UPDATE
    public void update(int id, Podcast podcast) throws SQLException {
        String sql = "UPDATE podcasts SET name = ?, duration = ?, host = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, podcast.getName());
            ps.setInt(2, podcast.getDuration());
            ps.setString(3, podcast.getCreator());
            ps.setInt(4, id);

            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM podcasts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

