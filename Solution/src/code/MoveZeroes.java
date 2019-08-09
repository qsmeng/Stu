package code;

/**
 * 283. 移动零
 * 
 * @author Administrator
 *
 */
public class MoveZeroes {
	public static void main(String[] args) {
		int[] nums = { 0, 1, 0, 3, 12 };
		// System.out.println(Arrays.toString(moveZeroes(nums)));
		moveZeroes2(nums);
	}

	// 读写双标 一次遍历一次补零
	public static int[] moveZeroes(int[] nums) {
		int j = 0;
		int l = nums.length;
		for (int i = 0; i < l; i++) {
			if (nums[i] != 0) {
				nums[j++] = nums[i];
			}
		}
		while (j < l) {
			nums[j++] = 0;
		}
		return nums;
	}

	public static void moveZeroes2(int[] nums) {
		int c = 0, temp = 0;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] == 0) {
				c++;
			} else {
				temp = nums[i - c];
				nums[i - c] = nums[i];
				nums[i] = temp;
			}
		}
	}
	// System.out.println(Arrays.toString(nums));

}
