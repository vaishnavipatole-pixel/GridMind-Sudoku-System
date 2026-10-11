package model;

public class LeaderboardNode {
	public String username;
    public int xp;
    public int coins;

    public LeaderboardNode left;
    public LeaderboardNode right;

    public LeaderboardNode(String username, int xp, int coins) {
        this.username = username;
        this.xp = xp;
        this.coins = coins;
        this.left = null;
        this.right = null;
    }
}
