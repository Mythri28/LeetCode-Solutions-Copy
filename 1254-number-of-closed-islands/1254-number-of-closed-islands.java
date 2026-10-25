
class Solution {
    public int closedIsland(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        boolean[][] visited = new boolean[rows][cols];
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 0 && !visited[r][c]) {
                    boolean closed = dfs(grid, visited, r, c);

                    if (closed) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    private boolean dfs(int[][] grid, boolean[][] visited,
                        int r, int c) {
        int rows = grid.length;
        int cols = grid[0].length;

        // Outside the grid means this island touches the boundary
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return false;
        }

        // Water or an already visited cell
        if (grid[r][c] == 1 || visited[r][c]) {
            return true;
        }

        visited[r][c] = true;

        boolean up = dfs(grid, visited, r - 1, c);
        boolean down = dfs(grid, visited, r + 1, c);
        boolean left = dfs(grid, visited, r, c - 1);
        boolean right = dfs(grid, visited, r, c + 1);

        return up && down && left && right;
    }
}
