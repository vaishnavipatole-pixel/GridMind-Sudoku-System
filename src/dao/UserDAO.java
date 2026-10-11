package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.LeaderboardBST;
import database.DatabaseConnection;
import model.User;

public class UserDAO {
	public boolean registerUser(User user) {
		 // Username validation: only alphabets allowed
	    if (!user.getUsername().matches("[a-zA-Z]+")) {
	        System.out.println(
	            "Invalid username! Username must contain only alphabets."
	        );
	        return false;
	    }    
        String checkQuery =
                "SELECT user_id FROM users WHERE username = ?";

        String insertQuery =
                "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            if (connection == null) {
                return false;
            }

            // Check if username already exists
            try (PreparedStatement checkStatement =
                         connection.prepareStatement(checkQuery)) {

                checkStatement.setString(1, user.getUsername());

                try (ResultSet resultSet =
                             checkStatement.executeQuery()) {

                    if (resultSet.next()) {
                        System.out.println(
                            "Username already exists."
                        );
                        return false;
                    }
                }
            }

            // Insert new user
            try (PreparedStatement insertStatement =
                         connection.prepareStatement(insertQuery)) {

                insertStatement.setString(
                        1,
                        user.getUsername()
                );

                insertStatement.setString(
                        2,
                        user.getPassword()
                );

                int rows =
                        insertStatement.executeUpdate();

                if (rows > 0) {
                    System.out.println(
                        "Registration successful!"
                    );
                    return true;
                }
            }

        } catch (SQLException e) {

            System.out.println(
                "Registration failed."
            );

            e.printStackTrace();
        }

        return false;
    }


public void populateLeaderboard(LeaderboardBST bst) {
    String sql = "SELECT username, xp, coins FROM users";

    try (Connection con = DatabaseConnection.getConnection()) {

        if (con == null) {
            System.out.println("Database connection failed!");
            return;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                int xp = rs.getInt("xp");
                int coins = rs.getInt("coins");

                bst.insert(username, xp, coins);
            }
        }

    } catch (SQLException e) {
        System.out.println(
            "Error loading leaderboard: " + e.getMessage()
        );
    }
}

	
    public User loginUser(
            String username,
            String password) {

        String query =
                "SELECT user_id, username, password "
                + "FROM users "
                + "WHERE username = ? AND password = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            if (connection == null) {
                return null;
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(query)) {

                statement.setString(1, username);
                statement.setString(2, password);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        return new User(
                            resultSet.getInt("user_id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                        );
                    }
                }
            }

        } catch (SQLException e) {

            System.out.println(
                "Login failed."
            );

            e.printStackTrace();
        }

        return null;
    }
    public void addXP(int userId, int xp) {

        String query =
                "UPDATE users SET xp = xp + ? WHERE user_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(query)) {

            statement.setInt(1, xp);
            statement.setInt(2, userId);

            statement.executeUpdate();

            System.out.println(
                "⭐ XP earned: +" + xp
            );

        } catch (SQLException e) {

            System.out.println(
                "Unable to update XP."
            );

            e.printStackTrace();
        }
    }
        public void addCoins(int userId, int coins) {

            String query =
                    "UPDATE users SET coins = coins + ? WHERE user_id = ?";

            try (Connection connection =
                         DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {

                statement.setInt(1, coins);
                statement.setInt(2, userId);

                statement.executeUpdate();

                System.out.println(
                    "🪙 Coins earned: +" + coins
                );

            } catch (SQLException e) {

                System.out.println(
                    "Unable to update coins."
                );

                e.printStackTrace();
            }
        }
        public void updateStreak(int userId) {

            String query =
                    "SELECT current_streak, longest_streak, last_played_date "
                    + "FROM users WHERE user_id = ?";

            try (Connection connection =
                         DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {

                statement.setInt(1, userId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        int currentStreak =
                                resultSet.getInt("current_streak");

                        int longestStreak =
                                resultSet.getInt("longest_streak");

                        java.sql.Date lastPlayedDate =
                                resultSet.getDate("last_played_date");

                        java.sql.Date today =
                                java.sql.Date.valueOf(
                                        java.time.LocalDate.now()
                                );

                        if (lastPlayedDate == null) {

                            currentStreak = 1;

                        } else {

                            java.time.LocalDate lastDate =
                                    lastPlayedDate.toLocalDate();

                            java.time.LocalDate todayDate =
                                    today.toLocalDate();

                            long days =
                                    java.time.temporal.ChronoUnit.DAYS
                                    .between(lastDate, todayDate);

                            if (days == 1) {
                                currentStreak++;
                            }
                            else if (days > 1) {
                                currentStreak = 1;
                            }
                        }

                        if (currentStreak > longestStreak) {
                            longestStreak = currentStreak;
                        }

                        String updateQuery =
                                "UPDATE users "
                                + "SET current_streak = ?, "
                                + "longest_streak = ?, "
                                + "last_played_date = ? "
                                + "WHERE user_id = ?";

                        try (PreparedStatement updateStatement =
                                     connection.prepareStatement(updateQuery)) {

                            updateStatement.setInt(1, currentStreak);
                            updateStatement.setInt(2, longestStreak);
                            updateStatement.setDate(3, today);
                            updateStatement.setInt(4, userId);

                            updateStatement.executeUpdate();

                            System.out.println(
                                "🔥 Current Streak: "
                                + currentStreak + " day(s)"
                            );

                            System.out.println(
                                "🏆 Longest Streak: "
                                + longestStreak + " day(s)"
                            );
                        }
                    }
                }

            } catch (SQLException e) {

                System.out.println(
                    "Unable to update streak."
                );

                e.printStackTrace();
            }
        }

public boolean deductCoins(int userId, int coins) {

    String query =
            "UPDATE users SET coins = coins - ? "
            + "WHERE user_id = ? AND coins >= ?";

    try (Connection connection =
                 DatabaseConnection.getConnection();
         PreparedStatement statement =
                 connection.prepareStatement(query)) {

        statement.setInt(1, coins);
        statement.setInt(2, userId);
        statement.setInt(3, coins);

        int rows = statement.executeUpdate();

        if (rows > 0) {
            System.out.println(
                "🪙 " + coins + " coins deducted from your account."
            );
            return true;
        }

        System.out.println("Not enough coins in your account!");
        return false;

    } catch (SQLException e) {
        System.out.println("Unable to deduct coins.");
        e.printStackTrace();
        return false;
    }
}

        
        public void displayProfile(int userId) {

            String query =
                    "SELECT username, xp, coins, current_streak, longest_streak "
                    + "FROM users WHERE user_id = ?";

            try (Connection connection =
                         DatabaseConnection.getConnection();
                 PreparedStatement statement =
                         connection.prepareStatement(query)) {

                statement.setInt(1, userId);

                try (ResultSet resultSet =
                             statement.executeQuery()) {

                    if (resultSet.next()) {

                        String username =
                                resultSet.getString("username");

                        int xp =
                                resultSet.getInt("xp");

                        int coins =
                                resultSet.getInt("coins");

                        int currentStreak =
                                resultSet.getInt("current_streak");

                        int longestStreak =
                                resultSet.getInt("longest_streak");

                        System.out.println();
                        System.out.println(
                            "================================="
                        );
                        System.out.println(
                            "          MY PROFILE"
                        );
                        System.out.println(
                            "=================================");

                        System.out.println(
                            "Username       : " + username
                        );

                        System.out.println(
                            "XP             : " + xp
                        );

                        System.out.println(
                            "Coins          : " + coins
                        );

                        System.out.println(
                            "Current Streak : "
                            + currentStreak + " day(s)"
                        );

                        System.out.println(
                            "Longest Streak : "
                            + longestStreak + " day(s)"
                        );

                        System.out.println(
                            "================================="
                        );
                    }
                }

            } catch (SQLException e) {

                System.out.println(
                    "Unable to load profile."
                );

                e.printStackTrace();
            }
        }
    }


