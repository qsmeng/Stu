package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 949. 给定数字能组成的最大时间
 * 
 * @author Administrator
 *
 */
public class LargestTimeFromDigits {
	public static void main(String[] args) {
		int[] a = { 1, 9, 9, 5 };
		System.out.println(largestTimeFromDigits2(a));
	}

	public static String largestTimeFromDigits4(int[] A) {
		Arrays.sort(A);
		for (int i = 3; i >= 0; i--) {
			if (A[i] > 2)
				continue;
			for (int j = 3; j >= 0; j--) {
				if (j == i || A[i] == 2 && A[j] > 3)
					continue;
				for (int k = 3; k >= 0; k--) {
					if (k == i || k == j || A[k] > 5)
						continue;
					return "" + A[i] + A[j] + ':' + A[k] + A[6 - i - j - k];
				}
			}
		}
		return "";
	}

	public static String largestTimeFromDigits3(int[] A) {
		StringBuilder sb = new StringBuilder();
		int sum = 0;
		int max = -1;
		int temp = 0;
		boolean mark = true;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i != j) {
					for (int k = 0; k < 4; k++) {
						if (k != j && k != i) {
							for (int l = 0; l < 4; l++) {
								if (i != l && j != l && k != l) {
									sum = A[l] * 1000 + A[k] * 100 + A[j] * 10 + A[i];
									temp = A[j] * 10 + A[i];
									if (sum <= 2359 && sum > max && temp < 60) {
										max = sum;
										mark = false;
									}
								}
							}
						}
					}
				}
			}
		}
		if (mark)
			return "";
		else {
			sb.append((max / 1000) + "");
			max = max % 1000;
			sb.append((max / 100) + "");
			sb.append(":");
			max = max % 100;
			sb.append((max / 10) + "");
			max = max % 10;
			sb.append(max + "");

		}
		return sb.toString();
	}

	public static String largestTimeFromDigits2(int[] arr) {
		int index = arr.length;
		if (index < 4)
			return "";
		List<Integer> list = new ArrayList<>();
		for (int x = 0; x < index; x++) {
			for (int y = 0; y < index; y++) {
				if (x == y)
					continue;
				for (int z = 0; z < index; z++) {
					if (x == z || y == z)
						continue;
					for (int n = 0; n < index; n++) {
						if (x == n || y == n || z == n)
							continue;
						String str = "" + arr[x] + arr[y] + arr[z] + arr[n];
						Integer num = Integer.parseInt(str);
						if (num == 0)
							return "00:00";
						list.add(num);
					}
				}
			}
		}
		Integer max = 0;
		for (Integer i : list) {
			if (max < i && i / 100 < 24 && i % 100 < 60)
				max = i;
		}
		if (max == 0)
			return "";
		StringBuilder sb = new StringBuilder(max.toString());
		if (sb.length() == 3)
			sb.insert(0, "0");
		sb.insert(2, ":");
		return sb.toString();
	}

	static boolean[] B = new boolean[4];// 用来表示数字是否使用过
	// 下面方法思路主要就是先贪一波，贪失败了就去把开头限定为不为2再贪一波，哈哈哈

	public static String largestTimeFromDigits(int[] A) {
		Arrays.sort(A);// 先排序方便后面findMax方法及时break
		int index = findMax(2, A);
		if (index == -1)
			return "";
		StringBuilder sb = new StringBuilder();// 用StringBuilder方便组合字符串
		sb.append(A[index]);
		B[index] = true;
		if (A[index] == 2) {
			index = findMax(3, A);
		} else {
			index = findMax(9, A);
		}
		if (index == -1)
			return "";// 这里findMax寻找失败的话是不需要回退使用notStartWith2方法的，因为如果小于等于3的都没有，找小于等于1的来代替2开头就更加找不到了。
		sb.append(A[index]);
		sb.append(":");
		B[index] = true;
		index = findMax(5, A);
		if (index == -1)
			return notStartWith2(A);// 这里寻找失败可能是太贪了，所以用notStartWith2回退一下
		sb.append(A[index]);
		B[index] = true;
		index = findMax(9, A);// 这里一定找的到
		sb.append(A[index]);
		return sb.toString();
	}

	private static String notStartWith2(int[] A) {
		Arrays.fill(B, false);
		int index = findMax(1, A);
		if (index == -1)
			return "";
		StringBuilder sb = new StringBuilder();
		sb.append(A[index]);
		B[index] = true;
		index = findMax(9, A);
		if (index == -1)
			return "";
		sb.append(A[index]);
		sb.append(":");
		B[index] = true;
		index = findMax(5, A);
		if (index == -1)
			return "";
		sb.append(A[index]);
		B[index] = true;
		index = findMax(9, A);
		if (index == -1)
			return "";
		sb.append(A[index]);
		return sb.toString();
	}

	private static int findMax(int cap, int[] A) {
		int maxIndex = -1;
		int max = -1;
		for (int i = 0; i < 4; i++) {
			if (A[i] > cap)
				break;
			if (B[i])
				continue;
			if (A[i] > max) {
				max = A[i];
				maxIndex = i;
			}
		}
		return maxIndex;
	}
}
