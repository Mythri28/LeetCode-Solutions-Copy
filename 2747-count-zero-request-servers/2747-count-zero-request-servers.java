
import java.util.Arrays;

class Solution {
    public int[] countServers(int n, int[][] logs, int x, int[] queries) {

        // Sort logs by time
        Arrays.sort(logs, (a, b) -> Integer.compare(a[1], b[1]));

        int q = queries.length;

        // Store query time and its original index
        int[][] sortedQueries = new int[q][2];

        for (int i = 0; i < q; i++) {
            sortedQueries[i][0] = queries[i];
            sortedQueries[i][1] = i;
        }

        // Sort queries by time
        Arrays.sort(sortedQueries,
            (a, b) -> Integer.compare(a[0], b[0]));

        int[] answer = new int[q];

        // Number of requests for each server in the current window
        int[] serverCount = new int[n + 1];

        int activeServers = 0;

        // Sliding window pointers for logs
        int left = 0;
        int right = 0;

        for (int i = 0; i < q; i++) {

            int queryTime = sortedQueries[i][0];
            int originalIndex = sortedQueries[i][1];

            int startTime = queryTime - x;

            // Add logs whose time is <= queryTime
            while (right < logs.length &&
                   logs[right][1] <= queryTime) {

                int serverId = logs[right][0];

                if (serverCount[serverId] == 0) {
                    activeServers++;
                }

                serverCount[serverId]++;
                right++;
            }

            // Remove logs whose time is < startTime
            while (left < right &&
                   logs[left][1] < startTime) {

                int serverId = logs[left][0];

                serverCount[serverId]--;

                if (serverCount[serverId] == 0) {
                    activeServers--;
                }

                left++;
            }

            // Servers that received no requests
            answer[originalIndex] = n - activeServers;
        }

        return answer;
    }
}
