

class Solution {
    private int[][] up;
    private int[] depth;
    private int[] tin;
    private int[] tout;
    private int[] prefix;
    private int[] parent;
    private int log;
    private Fenwick fenwick;
    private char[] chars;

    public List<Boolean> palindromePath(
            int n, int[][] edges, String s, String[] queries) {

        // 1. Build the tree
        List<Integer>[] graph = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            graph[u].add(v);
            graph[v].add(u);
        }

        chars = s.toCharArray();

        // 2. Prepare binary lifting tables
        log = 1;
        while ((1 << log) <= n) {
            log++;
        }

        up = new int[log][n];
        depth = new int[n];
        tin = new int[n];
        tout = new int[n];
        prefix = new int[n];
        parent = new int[n];

        // 3. Iterative DFS: obtain parent, depth and Euler order
        int[] stack = new int[n];
        int[] order = new int[n];
        int top = 0;
        int timer = 0;

        stack[top++] = 0;
        parent[0] = 0;

        while (top > 0) {
            int node = stack[--top];

            tin[node] = timer;
            order[timer++] = node;

            for (int child : graph[node]) {
                if (child == parent[node]) {
                    continue;
                }

                parent[child] = node;
                up[0][child] = node;
                depth[child] = depth[node] + 1;

                for (int j = 1; j < log; j++) {
                    up[j][child] =
                        up[j - 1][up[j - 1][child]];
                }

                stack[top++] = child;
            }
        }

        // Root is its own ancestor
        for (int j = 0; j < log; j++) {
            up[j][0] = 0;
        }

        // 4. Calculate root-to-node prefix masks
        int[] subtreeSize = new int[n];
        Arrays.fill(subtreeSize, 1);

        for (int node : order) {
            if (node == 0) {
                prefix[node] = bit(chars[node]);
            } else {
                prefix[node] =
                    prefix[parent[node]] ^ bit(chars[node]);
            }
        }

        // Reverse order ensures children are processed before parents
        for (int i = n - 1; i > 0; i--) {
            int node = order[i];
            subtreeSize[parent[node]] += subtreeSize[node];
        }

        for (int node = 0; node < n; node++) {
            tout[node] = tin[node] + subtreeSize[node] - 1;
        }

        // 5. Fenwick tree stores changes to prefix masks
        fenwick = new Fenwick(n + 1);

        List<Boolean> answer = new ArrayList<>();

        // 6. Process queries in order
        for (String q : queries) {
            String[] parts = q.split(" ");

            if (parts[0].equals("update")) {
                int node = Integer.parseInt(parts[1]);
                char newChar = parts[2].charAt(0);

                int oldBit = bit(chars[node]);
                int newBit = bit(newChar);
                int delta = oldBit ^ newBit;

                if (delta != 0) {
                    // Update every root-prefix mask in this subtree
                    fenwick.rangeXor(
                        tin[node], tout[node], delta
                    );

                    chars[node] = newChar;
                }

            } else {
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);

                int ancestor = lca(u, v);

                int maskU = currentPrefix(u);
                int maskV = currentPrefix(v);

                int pathMask =
                    maskU ^ maskV ^ bit(chars[ancestor]);

                // True when at most one bit is set
                answer.add(
                    (pathMask & (pathMask - 1)) == 0
                );
            }
        }

        return answer;
    }

    private int bit(char c) {
        return 1 << (c - 'a');
    }

    private int currentPrefix(int node) {
        return prefix[node] ^ fenwick.query(tin[node]);
    }

    private int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }

        // Bring u to the same depth as v
        int difference = depth[u] - depth[v];

        for (int j = 0; j < log; j++) {
            if ((difference & (1 << j)) != 0) {
                u = up[j][u];
            }
        }

        if (u == v) {
            return u;
        }

        // Lift both nodes until their parents are equal
        for (int j = log - 1; j >= 0; j--) {
            if (up[j][u] != up[j][v]) {
                u = up[j][u];
                v = up[j][v];
            }
        }

        return up[0][u];
    }

    private static class Fenwick {
        private final int[] tree;

        Fenwick(int size) {
            tree = new int[size + 1];
        }

        // XOR difference at a zero-based position
        private void add(int index, int value) {
            for (int i = index + 1;
                 i < tree.length;
                 i += i & -i) {
                tree[i] ^= value;
            }
        }

        // XOR of all difference values up to index
        int query(int index) {
            int result = 0;

            for (int i = index + 1;
                 i > 0;
                 i -= i & -i) {
                result ^= tree[i];
            }

            return result;
        }

        // Apply XOR to the inclusive range [left, right]
        void rangeXor(int left, int right, int value) {
            add(left, value);
            add(right + 1, value);
        }
    }
}
