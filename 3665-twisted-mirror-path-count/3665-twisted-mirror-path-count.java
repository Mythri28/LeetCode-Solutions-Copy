
class Solution {
    public int uniquePaths(int[][] grid) {

        int m = grid.length;
        int n = grid[0].length;

        int MOD = 1_000_000_007;

        // Special case: only one cell
        if (m == 1 && n == 1) {
            return 1;
        }

        // Ways to enter a cell from the left
        int[][] right = new int[m][n];

        // Ways to enter a cell from above
        int[][] down = new int[m][n];

        // Robot starts at (0, 0)
        // It can initially move right or down.
        if (n > 1) {
            right[0][1] = 1;
        }

        if (m > 1) {
            down[1][0] = 1;
        }

        // Process cells in row-major order
        for (int i = 0; i < m; i++) {

            for (int j = 0; j < n; j++) {

                // Starting cell is already processed
                if (i == 0 && j == 0) {
                    continue;
                }

                if (grid[i][j] == 1) {

                    // Mirror: entered while moving right
                    // Reflection sends robot down
                    if (i + 1 < m) {
                        down[i + 1][j] =
                            (down[i + 1][j] + right[i][j]) % MOD;
                    }

                    // Mirror: entered while moving down
                    // Reflection sends robot right
                    if (j + 1 < n) {
                        right[i][j + 1] =
                            (right[i][j + 1] + down[i][j]) % MOD;
                    }

                } else {

                    // Empty cell: robot can move either way
                    int total =
                        (right[i][j] + down[i][j]) % MOD;

                    // Move right
                    if (j + 1 < n) {
                        right[i][j + 1] =
                            (right[i][j + 1] + total) % MOD;
                    }

                    // Move down
                    if (i + 1 < m) {
                        down[i + 1][j] =
                            (down[i + 1][j] + total) % MOD;
                    }
                }
            }
        }

        // Count all ways reaching the destination
        return (right[m - 1][n - 1]
              + down[m - 1][n - 1]) % MOD;
    }
}
