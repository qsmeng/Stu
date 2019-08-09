package code;

/**
 * 376. 摆动序列
 * 
 * @author Administrator
 *
 */
public class WiggleMaxLength {
	public static void main(String[] args) {
		int[] nums = { 1, 1, 1 };
		int a = wiggleMaxLength(nums);
		System.out.println(a);
	}

	public static int wiggleMaxLength(int[] nums) {
		int n = nums.length;
		if (n < 2) {
			return n;
		}
		int up = 1;
		int down = 1;
		for (int i = 1; i < n; i++) {
			System.out.println(i);
			if (nums[i] > nums[i - 1]) {
				up = down + 1;
			} else if (nums[i] < nums[i - 1]) {
				down = up + 1;
			}
		}
		return Math.max(up, down);
	}

	class Node {
		// 上一次是增的个数
		public int up;
		// 上一次是减的个数
		public int down;
		// 增的最后一个值
		public int max;
		// 减的最后一个值
		public int min;
	}

	public int wiggleMaxLength2(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		if (nums.length == 1) {
			return 1;
		}

		Node node = new Node();
		node.up = 1;
		node.down = 1;
		node.max = nums[0];
		node.min = nums[0];

		for (int i = 1; i < nums.length; i++) {
			if (nums[i] > node.max) {
				node.max = nums[i];
			} else if (nums[i] < node.max) {
				if (node.up + 1 > node.down) {
					node.down = node.up + 1;
					node.min = nums[i];
				}
			}
			if (nums[i] < node.min) {
				node.min = nums[i];
			} else if (nums[i] > node.min) {
				if (node.down + 1 > node.up) {
					node.up = node.down + 1;
					node.max = nums[i];
				}
			}
			// System.out.println("index:" + i);
			// System.out.println("up:" + node.up);
			// System.out.println("down:" + node.down);
		}
		return Math.max(node.up, node.down);
	}

	public static int	DOWN	= -1;
	public static int	UP		= 1;
	public static int	INIT	= 0;

	public int wiggleMaxLength3(int[] nums) {
		int state = INIT;
		int res = 1;
		if (nums.length == 0)
			return 0;
		if (nums.length == 1)
			return 1;
		for (int i = 1; i < nums.length; i++) {
			switch (state) {
			case -1:
				if (nums[i] > nums[i - 1]) {
					res++;
					state = UP;
				}
				break;
			case 1:
				if (nums[i] < nums[i - 1]) {
					res++;
					state = DOWN;
				}
				break;
			case 0:
				if (nums[i] > nums[i - 1]) {
					state = UP;
					res++;
				} else if (nums[i] < nums[i - 1]) {
					state = DOWN;
					res++;
				}
				break;
			}
		}
		return res;
	}
}
