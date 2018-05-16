import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Boyer-Moore 字符串匹配算法，比KMP快
 * 
 * @author Derek Xu
 * @date 2018-05-03
 * @since 0.1
 */
public class BMAlgorithm {

	public static void main(String[] args) {
		String source = "的的的凤飞飞单独的";
		String pattern = "的";
		List<Integer> matchList = match(source, pattern);
		if (matchList != null && matchList.size() > 0) {
			for (int i = 0; i < matchList.size(); ++i) {
				System.out.printf("Find at index: %d%n", matchList.get(i));
			}
		}
	}

	public static List<Integer> match(String source, String pattern) {
		if (source == null || "".equals(source) || pattern == null || "".equals(pattern)) {
			return Collections.emptyList();
		}
		List<Integer> matchList = new LinkedList<>();
		Map<Character, Integer> bmBcMap = preBmBc(pattern);
		int[] bmGs = preBmGs(pattern);

		int sourceLen = source.length();
		int patternLen = pattern.length();
		int sPointer = 0, pPointer = 0;
		while (sPointer <= sourceLen - patternLen) {
			// 模式串从后向前匹配
			for (pPointer = patternLen - 1; pPointer >= 0
					&& source.charAt(sPointer + pPointer) == pattern.charAt(pPointer); --pPointer) {
				// do nothing
			}
			// 找到一个匹配子串
			if (pPointer < 0) {
				matchList.add(sPointer);
				sPointer += patternLen;
			} else {
				// 取移动位数的最大值
				Integer bmBcLen = bmBcMap.get(source.charAt(sPointer + pPointer));
				if (bmBcLen == null) {
					bmBcLen = patternLen;
				} else {
					bmBcLen = bmBcLen - patternLen + 1 + pPointer;
				}
				sPointer += Math.max(bmGs[pPointer], bmBcLen);
			}
		}
		return matchList;
	}

	/**
	 * 坏字符bc表
	 * 
	 * @return
	 */
	private static Map<Character, Integer> preBmBc(String pattern) {
		if (pattern == null || "".equals(pattern)) {
			return Collections.emptyMap();
		}
		int len = pattern.length();
		Map<Character, Integer> bmBcMap = new HashMap<>(len);
		char[] charArray = pattern.toCharArray();
		for (int i = 0; i < len; ++i) {
			// 存放字符距离尾部的长度
			bmBcMap.put(charArray[i], len - 1 - i);
		}
		return bmBcMap;
	}

	/**
	 * 好后缀gs表 <br/>
	 * <br/>
	 * 有三种情况 <br/>
	 * 1.模式串中有子串匹配上好后缀 <br/>
	 * 2.模式串中没有子串匹配上好后缀，但找到一个最大前缀 <br/>
	 * 3.模式串中没有子串匹配上好后缀，但找不到一个最大前缀 <br/>
	 * 所以按顺序处理3->2->1情况处理 <br/>
	 * 
	 * @param pattern
	 * @return
	 */
	private static int[] preBmGs(String pattern) {
		if (pattern == null || "".equals(pattern)) {
			return new int[0];
		}
		int len = pattern.length();
		int[] bmGs = new int[len];
		int[] suffix = suffix(pattern);

		int i, j;
		// 情况3
		for (i = 0; i < len; ++i) {
			bmGs[i] = len;
		}
		// 情况2
		for (i = len - 1; i >= 0; --i) {
			if (suffix[i] == i + 1) { // 找到前缀
				for (j = 0; j < len - 1; ++j) {
					if (bmGs[j] == len) { // 保证每个位置至多只能被修改一次
						bmGs[j] = len - 1 - i;
					}
				}
			}
		}
		// 情况1
		for (i = 0; i < len - 1; ++i) {
			bmGs[len - 1 - suffix[i]] = len - 1 - i;
		}
		return bmGs;
	}

	/**
	 * 获取suffix表
	 * 
	 * @param pattern
	 * @return
	 */
	private static int[] suffix(String pattern) {
		if (pattern == null || "".equals(pattern)) {
			return new int[0];
		}
		int len = pattern.length();
		int[] suffix = new int[len];
		suffix[len - 1] = len;

		int i, j;
		for (i = len - 2; i >= 0; --i) {
			for (j = i; j >= 0 && pattern.charAt(j) == pattern.charAt(len - 1 - i + j); --j)
				;
			suffix[i] = i - j;
		}
		return suffix;
	}
}
