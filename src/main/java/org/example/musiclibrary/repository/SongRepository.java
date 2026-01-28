package org.example.musiclibrary.repository;

import org.example.musiclibrary.model.Song;
import org.example.musiclibrary.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongRepository {

    public Song create(Song song) throws SQLException {
        String sql = "INSERT INTO songs (id, name, duration, artist) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, song.getId());
            ps.setString(2, song.getName());
            ps.setInt(3, song.getDuration());
            ps.setString(4, song.getCreator());  // использован корректный getter

            ps.executeUpdate();
        } return song;
    }

    // GET ALL
    public List<Song> getAll() throws SQLException {
        List<Song> list = new ArrayList<>();
        String sql = "SELECT * FROM songs";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Song(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("duration"),
                        rs.getString("artist")
                ));
            }
        }
        return list;
    }

    // GET BY ID
    public Song getById(int id) throws SQLException {
        String sql = "SELECT * FROM songs WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Song(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("duration"),
                            rs.getString("artist")
                    );
                }
            }
        }
        return null;
    }

    // UPDATE
    public void update(int id, Song song) throws SQLException {
        String sql = "UPDATE songs SET name = ?, duration = ?, artist = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, song.getName());
            ps.setInt(2, song.getDuration());
            ps.setString(3, song.getCreator());
            ps.setInt(4, id);

            ps.executeUpdate();
        }
    }

    // DELETE
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM songs WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
