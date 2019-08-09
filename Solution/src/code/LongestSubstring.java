package code;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 395. 至少有K个重复字符的最长子串
 * 
 * @author Administrator
 *
 */
public class LongestSubstring {
	public static void main(String[] args) {
		String s = "aaabb";
		int k = 3;
		int a = longestSubstring(s, k);
		System.out.println(a);
	}

	private static int longestSubstring(String s, int k) {
		int len = s.length();
		if (len < k)
			return 0;
		if (k < 2)
			return len;
		return count(s.toCharArray(), k, 0, len - 1);
	}

	private static int count(char[] charArray, int k, int index, int lastindex) {
		if (lastindex - index + 1 < k)
			return 0;
		int[] times = new int[26];// 26个小写字母
		// 统计出现频次
		for (int i = index; i <= lastindex; i++) {
			++times[charArray[i] - 'a'];
		}
		// 如果该字符出现频次小于k，则不可能出现在结果子串中
		// 分别排除，然后挪动两个指针
		while (lastindex - index + 1 >= k && times[charArray[index] - 'a'] < k) {
			++index;
		}
		while (lastindex - index + 1 >= k && times[charArray[lastindex] - 'a'] < k) {
			--lastindex;
		}
		if (lastindex - index + 1 < k)
			return 0;
		for (int i = index; i <= lastindex; ++i) {
			// 如果第i个不符合要求，切分成左右两段分别递归求得
			if (times[charArray[i] - 'a'] < k) {
				return Math.max(count(charArray, k, index, i - 1), count(charArray, k, i + 1, lastindex));
			}
		}
		return lastindex - index + 1;

	}

	public static Map<Character, Integer> getMap(char[] charArray) {
		Map<Character, Integer> map = new LinkedHashMap<>();
		for (int i = 0; i < charArray.length; i++) {
			Integer integer = map.get(charArray[i]);
			if (integer != null)
				map.put(charArray[i], integer + 1);
			else
				map.put(charArray[i], 1);
		}
		return map;
	}

}