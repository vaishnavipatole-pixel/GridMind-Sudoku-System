package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;

public class ProgressDAO {
	// Get current level of user
    public int getCurrentLevel(int userId) {

        String query =
                "SELECT current_level FROM user_progress WHERE user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("current_level");
                }
            }

        } catch (SQLException e) {
            System.out.println("Unable to load progress.");
            e.printStackTrace();
        }

        return 1;
    }


    // Create progress record if user does not have one
    public void createProgress(int userId) {

        String query =
                "INSERT IGNORE INTO user_progress "
                + "(user_id, current_level, completed_levels) "
                + "VALUES (?, 1, 0)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Unable to create progress.");
            e.printStackTrace();
        }
    }


    // Save completed level and unlock next level
    public void saveProgress(int userId, int completedLevel) {

        int nextLevel = completedLevel + 1;

        String query =
                "UPDATE user_progress "
                + "SET current_level = ?, "
                + "completed_levels = ? "
                + "WHERE user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, nextLevel);
            statement.setInt(2, completedLevel);
            statement.setInt(3, userId);

            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Unable to save progress.");
            e.printStackTrace();
        }
    }
}
