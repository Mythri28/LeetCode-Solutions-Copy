class Solution {
    public int shortestPath(int n, int[][] edges, String labels, int k) {

        // Step 1: Build directed graph
        List<int[]>[] graph = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int weight = edge[2];

            graph[u].add(new int[]{v, weight});
        }

        // Step 2: Distance array
        long[][] dist = new long[n][k + 1];

        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Long.MAX_VALUE);
        }

        // Step 3: Priority Queue
        // {cost, node, consecutiveCount}
        PriorityQueue<long[]> pq = new PriorityQueue<>(
            (a, b) -> Long.compare(a[0], b[0])
        );

        // Starting node has one occurrence of its label
        dist[0][1] = 0;
        pq.offer(new long[]{0, 0, 1});

        // Step 4: Dijkstra
        while (!pq.isEmpty()) {

            long[] current = pq.poll();

            long cost = current[0];
            int node = (int) current[1];
            int count = (int) current[2];

            // Ignore outdated states
            if (cost != dist[node][count]) {
                continue;
            }

            // Destination reached with minimum cost
            if (node == n - 1) {
                return (int) cost;
            }

            // Explore neighboring nodes
            for (int[] edge : graph[node]) {

                int nextNode = edge[0];
                int weight = edge[1];

                int nextCount;

                // Same character
                if (labels.charAt(node) ==
                    labels.charAt(nextNode)) {

                    nextCount = count + 1;

                } else {
                    // Different character
                    nextCount = 1;
                }

                // Cannot exceed k consecutive characters
                if (nextCount > k) {
                    continue;
                }

                long newCost = cost + weight;

                // Relaxation
                if (newCost < dist[nextNode][nextCount]) {

                    dist[nextNode][nextCount] = newCost;

                    pq.offer(new long[]{
                        newCost,
                        nextNode,
                        nextCount
                    });
                }
            }
        }

        // No valid path exists
        return -1;
    }
}
