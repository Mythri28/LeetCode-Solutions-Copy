// class Solution {
//     public String[] createGrid(int m, int n) {
        
class Solution {
    public String[] createGrid(int m, int n) {
        char[][] grid = new char[m][n];

        for (int i = 0; i < m; i++) {
            Arrays.fill(grid[i], '#');
        }

        // Open the first column
        for (int i = 0; i < m; i++) {
            grid[i][0] = '.';
        }

        // Open the bottom row
        for (int j = 0; j < n; j++) {
            grid[m - 1][j] = '.';
        }

        String[] result = new String[m];

        for (int i = 0; i < m; i++) {
            result[i] = new String(grid[i]);
        }

        return result;
    }
}
