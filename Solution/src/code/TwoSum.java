package code;

import java.util.HashMap;
import java.util.Map;

/**
 * 1.两数之和
 * 
 * @author Administrator
 *
 */
public class TwoSum {
	public static void main(String[] args) {
		int[] nums = { -1, -2, -3, -4, -5 };
		int target = -8;
		int[] a = twoSum3(nums, target);
		System.out.println(a);
	}

	public static int[] twoSum(int[] nums, int target) {
		for (int i = 0; i < nums.length - 1; i++) {
			for (int j = i + 1; j < nums.length; j++) {
				if (target == nums[j] + nums[i]) {
					return new int[] { i, j };
				}
			}
		}
		return null;
	}

	public static int[] twoSum2(int[] nums, int target) {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		int complement = 0;
		for (int i = 0; i < nums.length; i++) {
			complement = target - nums[i];
			if (map.containsKey(nums[i])) {
				if (nums[map.get(nums[i])] == complement)
					return new int[] { i, map.get(nums[i]) };
			} else {
				map.put(nums[i], i);
				map.put(complement, i);
			}

		}
		throw new IllegalArgumentException("No two sum solution");

	}

	public static int[] twoSum3(int[] nums, int target) {
		int indexArrayMax = 4095;
		int[] indexArrays = new int[indexArrayMax + 1];
		for (int i = 0; i < nums.length; i++) {
			int diff = target - nums[i];
			int index = diff & indexArrayMax;
			if (indexArrays[index] != 0) {
				return new int[] { indexArrays[index] - 1, i };
			}
			indexArrays[nums[i] & indexArrayMax] = i + 1;
		}
		throw new IllegalArgumentException("No two sum value");
	}
}
