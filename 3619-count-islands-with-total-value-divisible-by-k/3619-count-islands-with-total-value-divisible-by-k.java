
class Solution {
    public int countIslands(int[][] grid, int k) {
        int rows = grid.length;
        int cols = grid[0].length;

        boolean[][] visited = new boolean[rows][cols];
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                // Found a new island
                if (grid[r][c] > 0 && !visited[r][c]) {
                    long sum = dfs(grid, visited, r, c);

                    // Check divisibility
                    if (sum % k == 0) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private long dfs(int[][] grid, boolean[][] visited,
                     int r, int c) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Outside the grid
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return 0;
        }

        // Water or already visited
        if (grid[r][c] == 0 || visited[r][c]) {
            return 0;
        }

        // Mark the cell as visited
        visited[r][c] = true;

        // Include the current cell's value
        long sum = grid[r][c];

        // Explore all four directions
        sum += dfs(grid, visited, r - 1, c);
        sum += dfs(grid, visited, r + 1, c);
        sum += dfs(grid, visited, r, c - 1);
        sum += dfs(grid, visited, r, c + 1);

        return sum;
    }
}
