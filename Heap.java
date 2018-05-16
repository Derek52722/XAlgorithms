import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * 堆排序算法 包括：建堆、插入元素（堆尾）、删除元素（堆顶）、堆排序
 * 
 * @author Derek Xu
 * @date 2018-05-15
 * @since 0.1
 */
public class Heap {

	/**
	 * 堆排序(包含建堆过程)。大顶堆时，排序结果为由小到大 小顶堆时，排序结果为由大到小
	 * 
	 * @param type
	 *            堆类型：大顶堆、小顶堆
	 * @param array
	 *            待排序数组
	 * @param comparator
	 *            自定义比较器
	 */
	public static <T> void sort(HeapType type, T[] array, Comparator<T> comparator) {
		if (array == null || array.length == 0) {
			return;
		}
		// 1. 建堆
		buildHeap(type, array, comparator);

		// 2. 排序：调整堆结构+交换堆顶元素与末尾元素
		for (int j = array.length - 1; j > 0; j--) {
			swap(array, 0, j);// 将堆顶元素与末尾元素进行交换
			adjustHeap(type, array, 0, j, comparator);// 重新对堆进行调整
		}

	}

	/**
	 * 向已经建好的堆中插入元素
	 * 
	 * @param type
	 *            堆类型：大顶堆、小顶堆
	 * @param array
	 *            堆数组
	 * @param elem
	 *            待插入元素
	 * @param comparator
	 *            自定义比较器
	 * @return 插入元素并调整后的新堆
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] insert(HeapType type, T[] array, T elem, Comparator<T> comparator) {
		T[] newArray;
		if (array == null || array.length == 0) {
			newArray = (T[]) Array.newInstance(elem.getClass(), 1);
			newArray[0] = elem;
			return newArray;
		}
		newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length + 1);
		newArray[newArray.length - 1] = elem; // 插入堆尾
		System.arraycopy(array, 0, newArray, 0, array.length);

		// 自底向上调整
		int index = newArray.length - 1;
		boolean finished = false;
		for (int i = (newArray.length - 1) / 2; i >= 0 && index != 0; i = (i - 1) / 2) {
			switch (type) {
			case BIG_HEAP: { // 大顶堆
				if (comparator.compare(newArray[index], newArray[i]) <= -1) { // 新插入的值比父节点小，则不调整
					finished = true;
				} else {
					swap(newArray, index, i);
					index = i;
				}
				break;
			}
			case SMALL_HEAP: { // 小顶堆
				if (comparator.compare(newArray[index], newArray[i]) >= 1) { // 新插入的值比父节点大，则不调整
					finished = true;
				} else {
					swap(newArray, index, i);
					index = i;
				}
				break;
			}
			default: {
				throw new IllegalArgumentException("HeapType非法");
			}
			}
			if (finished) {
				break;
			}
		}
		return newArray;
	}

	/**
	 * 删除堆顶元素
	 * 
	 * @param type
	 *            堆类型：大顶堆、小顶堆
	 * @param array
	 *            堆数组
	 * @param comparator
	 *            自定义比较器
	 * @return 删除堆顶元素并调整后的新堆
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] delete(HeapType type, T[] array, Comparator<T> comparator) {
		T[] newArray;
		if (array == null) {
			return null;
		}
		if (array.length == 0) {
			newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), 0);
			return newArray;
		}
		// 交换堆顶和堆尾元素
		swap(array, 0, array.length - 1);
		newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length - 1);
		System.arraycopy(array, 0, newArray, 0, newArray.length);

		// 自顶向下调整堆
		boolean finished = false;
		for (int i = 0; i < newArray.length / 2 - 1; i = i * 2 + 1) {
			int index = i * 2 + 1;
			switch (type) {
			case BIG_HEAP: { // 大顶堆
				if (index < newArray.length - 1) {
					if (index + 1 < newArray.length && comparator.compare(newArray[index], newArray[index + 1]) <= -1) { // 左子节点小于右子节点
						++index;
					}
					if (comparator.compare(newArray[index], newArray[i]) <= -1) { // 子节点比父节点小，则不调整
						finished = true;
					} else {
						swap(newArray, index, i);
						index = i;
					}
				}
				break;
			}
			case SMALL_HEAP: { // 小顶堆
				if (index < newArray.length - 1) {
					if (index + 1 < newArray.length && comparator.compare(newArray[index], newArray[index + 1]) >= 1) { // 左子节点大于右子节点
						++index;
					}
					if (comparator.compare(newArray[index], newArray[i]) >= 1) { // 子节点比父节点大，则不调整
						finished = true;
					} else {
						swap(newArray, index, i);
						index = i;
					}
				}
				break;
			}
			default: {
				throw new IllegalArgumentException("HeapType非法");
			}
			}
			if (finished) {
				break;
			}
		}
		return newArray;
	}

	/**
	 * 建堆
	 * 
	 * @param type
	 *            堆类型：大顶堆、小顶堆
	 * @param array
	 *            待建堆数组
	 * @param comparator
	 *            自定义比较器
	 */
	public static <T> void buildHeap(HeapType type, T[] array, Comparator<T> comparator) {
		if (array == null || array.length == 0) {
			return;
		}
		int len = array.length;
		for (int i = len / 2 - 1; i >= 0; --i) { // 从最后一个非叶子节点开始向上建堆
			adjustHeap(type, array, i, len, comparator);
		}
	}

	private static <T> void adjustHeap(HeapType type, T[] array, int index, int len, Comparator<T> comparator) {
		T tmp = array[index];
		for (int i = index * 2 + 1; i < len; i = i * 2 + 1) { // 从index的左节点开始
			switch (type) {
			case BIG_HEAP: { // 大顶堆
				if (i + 1 < len && comparator.compare(array[i], array[i + 1]) <= -1) { // 如果左子节点小于右子节点,i指向右子节点
					++i;
				}
				if (comparator.compare(array[i], tmp) >= 1) { // 如果子节点大于父节点，赋值给父节点
					array[index] = array[i];
					index = i;
				}
				break;
			}
			case SMALL_HEAP: { // 小顶堆
				if (i + 1 < len && comparator.compare(array[i], array[i + 1]) >= 1) { // 如果左子节点大于右子节点,i指向右子节点
					++i;
				}
				if (comparator.compare(array[i], tmp) <= -1) { // 如果子节点小于父节点，赋值给父节点
					array[index] = array[i];
					index = i;
				}
				break;
			}
			default: {
				throw new IllegalArgumentException("HeapType非法");
			}
			}
		}
		array[index] = tmp;
	}

	private static <T> void swap(T[] array, int index1, int index2) {
		T tmp = array[index1];
		array[index1] = array[index2];
		array[index2] = tmp;
	}

	public enum HeapType {
		BIG_HEAP, // 大顶堆
		SMALL_HEAP // 小顶堆
	}

	public static void main(String[] args) {
		// Integer[] a = null;
		Integer[] a = new Integer[] { 1, 1, 1 };
		// Integer[] a = new Integer[] { 1, 4, 1, 6, 9, 0, 2 };

		/*
		 * 堆排序
		 */
		Heap.sort(HeapType.SMALL_HEAP, a, new Comparator<Integer>() {
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

		/*
		 * 建堆
		 */
		Heap.buildHeap(HeapType.SMALL_HEAP, a, new Comparator<Integer>() {
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

		/*
		 * 插入元素
		 */
		Integer[] b = Heap.insert(HeapType.SMALL_HEAP, a, 7, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		for (int i = 0; i < b.length; ++i) {
			System.out.print(b[i] + " ");
		}
		System.out.println();

		/*
		 * 删除元素
		 */
		Integer[] c = Heap.delete(HeapType.SMALL_HEAP, a, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		if (c != null) {
			for (int i = 0; i < c.length; ++i) {
				System.out.print(c[i] + " ");
			}
			System.out.println();
		}
	}
}
