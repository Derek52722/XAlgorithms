import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * 败者树
 * 
 * @author Derek Xu
 * @date 2018-05-16
 * @since 0.1
 */
public class LoserTree {

	/**
	 * 建立败者树 </br>
	 * 注意：败者树需要建立比原始数组长度大1的新数组newArray，且新数组的最后一个值为null，表示数组中的最小值。 </br>
	 * 自定义比较器需要保证任何元素与null比较时，都返回1。
	 * 
	 * @param array
	 *            原始数组
	 * @param comparator
	 *            自定义比较器
	 * @return 返回败者树下标数组
	 */
	@SuppressWarnings("unchecked")
	public static <T> int[] buildTree(T[] array, Comparator<T> comparator) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return new int[0];
		}

		int len = array.length;
		T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), len + 1);
		System.arraycopy(array, 0, newArray, 0, len);
		int[] lt = new int[len];
		init(lt, len);

		for (int i = len - 1; i >= 0; --i) {
			adjust(newArray, lt, i, comparator);
		}
		return lt;
	}

	private static void init(int[] lt, int initVal) {
		for (int i = 0; i < lt.length; ++i) {
			lt[i] = initVal;
		}
	}

	private static <T> void adjust(T[] array, int[] lt, int i, Comparator<T> comparator) {
		for (int j = (i + array.length - 1) / 2; j > 0; j = j / 2) {
			if (comparator.compare(array[i], array[lt[j]]) >= 1) {
				int temp = lt[j];
				lt[j] = i;
				i = temp;
			}
		}
		lt[0] = i;
	}

	public static void main(String[] args) {
		// Integer[] a = null;
		// Integer[] a = new Integer[] { 2 };
		// Integer[] a = new Integer[] { 1, 3 };
		Integer[] a = new Integer[] { 10, 9, 20, 6, 12 };

		int[] lt = LoserTree.buildTree(a, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				if (o1 == null) {
					return -1;
				} else if (o2 == null) {
					return 1;
				} else {
					return o1.compareTo(o2);
				}
			}
		});

		if (lt != null) {
			for (int i = 0; i < lt.length; ++i) {
				System.out.print(lt[i] + " ");
			}
			System.out.println();
		}
	}
}
