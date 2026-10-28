
class Solution {
    public int minCost(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        boolean[][][] dp = new boolean[m][n][1024];

        // Starting cell
        dp[0][0][grid[0][0]] = true;

        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {

                // Skip the starting cell
                if (row == 0 && col == 0) {
                    continue;
                }

                // Reach from above
                if (row > 0) {
                    for (int x = 0; x < 1024; x++) {
                        if (dp[row - 1][col][x]) {
                            int newXor = x ^ grid[row][col];
                            dp[row][col][newXor] = true;
                        }
                    }
                }

                // Reach from the left
                if (col > 0) {
                    for (int x = 0; x < 1024; x++) {
                        if (dp[row][col - 1][x]) {
                            int newXor = x ^ grid[row][col];
                            dp[row][col][newXor] = true;
                        }
                    }
                }
            }
        }

        // Find the smallest reachable XOR at the destination
        for (int x = 0; x < 1024; x++) {
            if (dp[m - 1][n - 1][x]) {
                return x;
            }
        }

        return -1;
    }
}
