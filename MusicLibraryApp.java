import java.sql.*;

public class MusicLibraryApp {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/asik3";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) {
        try (Connection conn = connectToDatabase()) {
            createTables(conn);
            insertMusic(conn, "Bohemian Rhapsody", "Queen", "A Night at the Opera", 1975);
            updateMusic(conn, 1, "Bohemian Rhapsody", "Queen", "Greatest Hits", 1981);
            deleteMusic(conn, 1);
            listMusic(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection connectToDatabase() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void createTables(Connection conn) throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS music (
                id SERIAL PRIMARY KEY,
                title VARCHAR(100) NOT NULL,
                artist VARCHAR(100) NOT NULL,
                album VARCHAR(100),
                year INT
            )
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    private static void insertMusic(Connection conn, String title, String artist, String album, int year) throws SQLException {
        String insertSQL = "INSERT INTO music (title, artist, album, year) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
            pstmt.setString(3, album);
            pstmt.setInt(4, year);
            pstmt.executeUpdate();
        }
    }

    private static void updateMusic(Connection conn, int id, String title, String artist, String album, int year) throws SQLException {
        String updateSQL = "UPDATE music SET title = ?, artist = ?, album = ?, year = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, artist);
            pstmt.setString(3, album);
            pstmt.setInt(4, year);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        }
    }

    private static void deleteMusic(Connection conn, int id) throws SQLException {
        String deleteSQL = "DELETE FROM music WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private static void listMusic(Connection conn) throws SQLException {
        String selectSQL = "SELECT * FROM music";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                System.out.printf("ID: %d, Title: %s, Artist: %s, Album: %s, Year: %d\n",
                        rs.getInt("id"), rs.getString("title"), rs.getString("artist"),
                        rs.getString("album"), rs.getInt("year"));
            }
        }
    }
}
