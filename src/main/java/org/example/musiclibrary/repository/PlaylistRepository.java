package org.example.musiclibrary.repository;

import org.example.musiclibrary.model.Playlist;
import org.example.musiclibrary.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistRepository {

    // CREATE
    public void create(Playlist playlist) throws SQLException {
        String sql = "INSERT INTO playlists (id, name) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playlist.getId());
            ps.setString(2, playlist.getName());
            ps.executeUpdate();
        }
    }

    // GET ALL
    public List<Playlist> getAll() throws SQLException {
        List<Playlist> list = new ArrayList<>();
        String sql = "SELECT * FROM playlists";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Playlist(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        }
        return list;
    }

    // GET BY ID
    public Playlist getById(int id) throws SQLException {
        String sql = "SELECT * FROM playlists WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Playlist(
                            rs.getInt("id"),
                            rs.getString("name")
                    );
                }
            }
        }
        return null;
    }

    // UPDATE
    public void update(int id, Playlist playlist) throws SQLException {
        String sql = "UPDATE playlists SET name = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, playlist.getName());
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM playlists WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
