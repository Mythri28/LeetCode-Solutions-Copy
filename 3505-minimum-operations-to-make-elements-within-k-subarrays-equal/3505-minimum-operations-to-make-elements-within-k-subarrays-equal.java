

class Solution {

    class MedianWindow {
        TreeMap<Integer, Integer> low = new TreeMap<>();
        TreeMap<Integer, Integer> high = new TreeMap<>();

        int lowSize = 0;
        int highSize = 0;

        long lowSum = 0;
        long highSum = 0;

        // Add one occurrence of a number to a multiset
        void addToMap(TreeMap<Integer, Integer> map, int value) {
            map.put(value, map.getOrDefault(value, 0) + 1);
        }

        // Remove one occurrence of a number from a multiset
        void removeFromMap(TreeMap<Integer, Integer> map, int value) {
            int count = map.get(value);

            if (count == 1) {
                map.remove(value);
            } else {
                map.put(value, count - 1);
            }
        }

        // Move the largest value from low to high
        void moveLowToHigh() {
            int value = low.lastKey();

            removeFromMap(low, value);
            lowSize--;
            lowSum -= value;

            addToMap(high, value);
            highSize++;
            highSum += value;
        }

        // Move the smallest value from high to low
        void moveHighToLow() {
            int value = high.firstKey();

            removeFromMap(high, value);
            highSize--;
            highSum -= value;

            addToMap(low, value);
            lowSize++;
            lowSum += value;
        }

        // Keep low's size equal to high's size,
        // or one greater than high's size
        void balance() {
            while (lowSize > highSize + 1) {
                moveLowToHigh();
            }

            while (lowSize < highSize) {
                moveHighToLow();
            }
        }

        void add(int value) {
            if (lowSize == 0 || value <= low.lastKey()) {
                addToMap(low, value);
                lowSize++;
                lowSum += value;
            } else {
                addToMap(high, value);
                highSize++;
                highSum += value;
            }

            balance();
        }

        void remove(int value) {
            if (low.containsKey(value)) {
                removeFromMap(low, value);
                lowSize--;
                lowSum -= value;
            } else {
                removeFromMap(high, value);
                highSize--;
                highSum -= value;
            }

            balance();
        }

        long getCost() {
            int median = low.lastKey();

            long leftCost = (long) median * lowSize - lowSum;
            long rightCost = highSum - (long) median * highSize;

            return leftCost + rightCost;
        }
    }

    public long minOperations(int[] nums, int x, int k) {

        int n = nums.length;
        int windowCount = n - x + 1;

        // Step 1: Calculate the cost of every size-x window
        long[] cost = new long[windowCount];

        MedianWindow window = new MedianWindow();

        // Add the first window
        for (int i = 0; i < x; i++) {
            window.add(nums[i]);
        }

        cost[0] = window.getCost();

        // Slide the window
        for (int i = x; i < n; i++) {

            window.remove(nums[i - x]);
            window.add(nums[i]);

            cost[i - x + 1] = window.getCost();
        }

        // Step 2: Dynamic programming
        long INF = Long.MAX_VALUE / 4;

        long[][] dp = new long[n + 1][k + 1];

        for (int i = 0; i <= n; i++) {
            Arrays.fill(dp[i], INF);
        }

        // Selecting zero subarrays costs zero
        for (int i = 0; i <= n; i++) {
            dp[i][0] = 0;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= k; j++) {

                // Option 1: Do not select a window ending here
                dp[i][j] = dp[i - 1][j];

                // Option 2: Select a window of size x
                if (i >= x) {

                    int start = i - x;

                    if (dp[start][j - 1] != INF) {

                        long take =
                            dp[start][j - 1] + cost[start];

                        dp[i][j] = Math.min(dp[i][j], take);
                    }
                }
            }
        }

        return dp[n][k];
    }
}
