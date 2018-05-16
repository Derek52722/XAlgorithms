import java.util.Comparator;

/**
 * 快速排序
 * 
 * @author Derek Xu
 * @date 2018-05-16
 * @since 0.1
 */
public class QuickSort {

	/**
	 * 对数组进行排序（由小到大），元素大小可由自定义的比较器定义
	 * 
	 * @param array
	 * @param comparator
	 */
	public static <T> void sort(T[] array, Comparator<T> comparator) {
		if (array == null || array.length == 0) {
			return;
		}
		int left = 0, right = array.length - 1;
		quickSort(array, left, right, comparator);
	}

	private static <T> void quickSort(T[] array, int left, int right, Comparator<T> comparator) {
		if (left > right) {
			return;
		}

		T elem = array[left];
		int l = left, r = right;
		while (l < r) {
			while (l < r && comparator.compare(array[r], elem) >= 0) {
				--r;
			}
			while (l < r && comparator.compare(array[l], elem) <= 0) {
				++l;
			}
			if (l < r) {
				T temp = array[l];
				array[l] = array[r];
				array[r] = temp;
			}
		}
		array[left] = array[l];
		array[l] = elem;

		quickSort(array, left, l - 1, comparator);
		quickSort(array, l + 1, right, comparator);
	}

	public static void main(String[] args) {
		// Integer[] a = null;
		// Integer[] a = new Integer[] { 1, 1, 1 };
		Integer[] a = new Integer[] { 1, 4, 1, 6, 9, 0, 2 };

		QuickSort.sort(a, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});

		if (a != null) {
			for (int i = 0; i < a.length; ++i) {
				System.out.print(a[i] + " ");
			}
			System.out.println();
		}
	}

}
