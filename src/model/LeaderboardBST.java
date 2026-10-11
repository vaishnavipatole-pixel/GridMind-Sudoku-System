package model;
public class LeaderboardBST {
    private LeaderboardNode root;

    public void insert(String username, int xp, int coins) {

        LeaderboardNode newNode =
                new LeaderboardNode(username, xp, coins);

        if (root == null) {
            root = newNode;
            return;
        }

        LeaderboardNode current = root;

        while (true) {

            if (xp > current.xp) {

                if (current.left == null) {
                    current.left = newNode;
                    return;
                }

                current = current.left;

            } else {

                if (current.right == null) {
                    current.right = newNode;
                    return;
                }

                current = current.right;
            }
        }
    }

    public void display() {

        if (root == null) {
            System.out.println("No players found!");
            return;
        }

        System.out.println("\n========== LEADERBOARD ==========");
        System.out.printf("%-6s %-18s %-8s %-8s%n",
                "Rank", "Username", "XP", "Coins");
        System.out.println("---------------------------------");

        displayDescending(root, new int[]{1});
    }

    private void displayDescending(
            LeaderboardNode node, int[] rank) {

        if (node == null) {
            return;
        }

        displayDescending(node.left, rank);

        System.out.printf("%-6d %-18s %-8d %-8d%n",
                rank[0], node.username, node.xp, node.coins);

        rank[0]++;

        displayDescending(node.right, rank);
    }
}


