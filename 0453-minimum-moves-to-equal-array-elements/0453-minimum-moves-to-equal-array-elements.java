class Solution {
    public int minMoves(int[] nums) {
        int min=nums[0];
        for(int i=1;i<nums.length;i++){
            min=Math.min(nums[i],min);
        }
        int moves=0;
        for(int i=0;i<nums.length;i++){
moves+=nums[i]-min;
        }
        return moves;
        
    }
}