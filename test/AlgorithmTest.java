import org.junit.Test;

import java.util.Arrays;

/**
 * 说明：基础算法测试
 * 创建人：Ajie
 * 创建时间：2019年11月4日15:22:46
 */
public class AlgorithmTest {

    /**
     * 功能描述：冒泡排序
     * @author Ajie
     * @date 2019/11/4 0004
     */
    @Test
    public void bubbleSort() {
        long startTime, endTime;
        // 初始化数组，随机100以内的数
        int[] arr1 = {19,16,11,8,5,3};
        int[] arr = new int[19];
        for (int i = 0; i < 19; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        System.out.println("初始化的数组："+Arrays.toString(arr1));
        startTime = System.currentTimeMillis() / 1000;
        // 统计比较次数
        int count = 0;
        // 第一轮比较
        for (int i = 0; i < arr1.length-1; i++) {
            // 如果有元素进行了交换就 变成false 这样可以优化算法
            boolean flag = true;
            // 第二轮比较
            for (int j = 0; j < arr1.length - 1 - i; j++) {
                if (arr1[j] > arr1[j + 1]) {
                    // 交换位置
                    int temp = arr1[j];
                    arr1[j] = arr1[j + 1];
                    arr1[j + 1] = temp;
                    flag = false;
                }
                count++;
            }
            // 所有元素已经排序完成就跳出循环
            if (flag) {
                break;
            }
        }
        endTime = System.currentTimeMillis() / 1000;
        System.out.println("经过冒泡排序后的数组："+Arrays.toString(arr1));
        System.out.println("一共比较了:" + count + " 次,耗时：" + (endTime - startTime) + " 毫秒");
    }

    /**
     * 功能描述：选择排序
     * @author Ajie
     * @date 2019/11/4 0004
     */
    @Test
    public void selectionSort() {
        long startTime, endTime;
        // 初始化数组，随机100以内的数
        int[] arr = new int[19];
        for (int i = 0; i < 19; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        System.out.println("初始化的数组："+Arrays.toString(arr));
        startTime = System.currentTimeMillis() / 1000;
        // 第一轮比较
        for (int i = 0; i < arr.length-1; i++) {
            // 记录元素下标
            int index = i;
            // 第二轮比较
            for (int j = i+1; j < arr.length; j++) {
                if (arr[j] < arr[index]) {
                    // 保存最小元素下标
                    index = j;
                }
            }
            // 此时已经找到最小元素下标
            // 将最小元素与前面的元素交换位置
            int temp = arr[index];
            arr[index] = arr[i];
            arr[i] = temp;
        }
        endTime = System.currentTimeMillis() / 1000;
        System.out.println("经过选择排序后的数组："+Arrays.toString(arr));
        System.out.println("耗时：" + (endTime - startTime) + " 毫秒");
    }

    /**
     * 功能描述：插入排序
     * @author Ajie
     * @date 2019/11/4 0004
     */
    @Test
    public void insertionSort() {
        long startTime, endTime;
        // 初始化数组，随机100以内的数
        int[] arr = new int[19];
        for (int i = 0; i < 19; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        System.out.println("初始化的数组："+Arrays.toString(arr));
        startTime = System.currentTimeMillis() / 1000;
        for (int i = 1; i < arr.length; i++) {
            // 定义待插入的数
            int insertValue = arr[i];
            // 找到待插入数的前一个数下标
            int insertIndex = i - 1;
            // 如果待插入元素小于前一个元素，就将前一个元素后移
            while (insertIndex >= 0 && insertValue < arr[insertIndex]) {
                arr[insertIndex + 1] = arr[insertIndex];
                // 如果待插入的元素值大于前一个元素，那么就不会进入while循环，这样insertIndex + 1之后的位置仍然是自己所在的位置，所以赋值后值不改变
                insertIndex--;
            }
            // 交换位置
            arr[insertIndex + 1] = insertValue;
        }
        endTime = System.currentTimeMillis() / 1000;
        System.out.println("经过插入排序后的数组："+Arrays.toString(arr));
        System.out.println("耗时：" + (endTime - startTime) + " 毫秒");
    }

    /**
     * 功能描述：希尔排序
     * @author Ajie
     * @date 2019/11/4 0004
     */
    @Test
    public void shellSort() {
        long startTime, endTime;
        // 初始化数组，随机100以内的数
        int[] arr = new int[19];
        for (int i = 0; i < 19; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        System.out.println("初始化的数组："+Arrays.toString(arr));
        startTime = System.currentTimeMillis() / 1000;
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            // 对数组元素进行分组
            for (int i = gap; i < arr.length; i++) {
                // 遍历各数组中的元素
                for (int j = i - gap; j >= 0; j -= gap) {
                    // 交换元素
                    if (arr[j] > arr[j+gap]) {
                        int temp = arr[j];
                        arr[j] = arr[j + gap];
                        arr[j + gap] = temp;
                    }
                }
            }
        }
        endTime = System.currentTimeMillis() / 1000;
        System.out.println("经过希尔排序后的数组："+Arrays.toString(arr));
        System.out.println("耗时：" + (endTime - startTime) + " 毫秒");
    }

    /**
     * 功能描述：快速排序
     * @author Ajie
     * @date 2019/11/4 0004
     * @param arr -数组
     * @param left -左下标
     * @param right -右下标
     */
    public static void quickSort(int[] arr, int left, int right) {
        int l = left;// 左下标
        int r = right;// 右下标
        int pivot = arr[(left + right) / 2];// 找到中间的值
        // 将比pivot小的值放在其左边，比pivot大的值放在其右边
        while (l < r) {
            // 在pivot左边寻找，直至找到大于等于pivot的值才退出
            while (arr[l] < pivot) {
                l += 1;// 将l右移一位
            }
            // 在pivot右边寻找，直至找到小于等于pivot的值才退出
            while (arr[r] > pivot) {
                r -= 1;// 将r左移一位
            }
            if (l >= r) {
                // 左右下标重合，寻找完毕，退出循环
                break;
            }
            // 交换元素
            int temp = arr[l];
            arr[l] = arr[r];
            arr[r] = temp;
            //倘若发现值相等的情况，则没有比较的必要，直接移动下标即可

            // 如果交换完后，发现arr[l]==pivot，此时应将r左移一位
            if (arr[l] == pivot) {
                r -= 1;
            }
            // 如果交换完后，发现arr[r]==pivot，此时应将l右移一位
            if (arr[r] == pivot) {
                l += 1;
            }
        }
        // 如果l==r，要把这两个下标错开，否则会出现无限递归，导致栈溢出的情况
        if (l == r) {
            l += 1;
            r -= 1;
        }
        // 向左递归
        if (left < r) {
            quickSort(arr, left, r);
        }
        // 向右递归
        if (right > l) {
            quickSort(arr, l, right);
        }
    }

    /**
     * 功能描述：测试快速排序
     * @author Ajie
     * @date 2019/11/4 0004
     */
    public static void main(String[] args) {
        int[] arr = new int[19];
        for (int i = 0; i < 19; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        System.out.println("初始化数组："+Arrays.toString(arr));
        // 调用快速排序方法
        quickSort(arr, 0, arr.length - 1);
        System.out.println("快速排序后的数组："+Arrays.toString(arr));
    }

    /**
     * 功能描述：归并算法之 分解
     * @author Ajie
     * @date 2019/11/4 0004
     */
    public static void mergeSort(int[] arr, int left, int right, int[] temp) {
        // 分解
        if (left < right) {
            int mid = (left + right) / 2;// 中间索引
            // 向左递归进行分解
            mergeSort(arr, left, mid, temp);
            // 向右递归进行分解
            mergeSort(arr, mid + 1, right, temp);// mid + 1，中间位置的后一个位置才是右边序列的开始位置
            // 每分解一轮便合并一轮
            merge(arr, left, right, mid, temp);
        }
    }

    /**
     * 合并的方法
     *
     * @param arr   待排序的数组
     * @param left  左边有序序列的初始索引
     * @param right 中间索引
     * @param mid   右边有序序列的初始索引
     * @param temp  做中转的数组
     */
    public static void merge(int[] arr, int left, int right, int mid, int[] temp) {
        int i = left; // 初始化i，左边有序序列的初始索引
        int j = mid + 1;// 初始化j，右边有序序列的初始索引(右边有序序列的初始位置即为中间位置的后一个位置)
        int t = 0;// 指向temp数组的当前索引，初始为0
        // 先把左右两边的数据(已经有序)按规则填充到temp数组
        // 直到左右两边的有序序列，有一边处理完成为止
        while (i <= mid && j <= right) {
            // 如果左边有序序列的当前元素小于或等于右边有序序列的当前元素，就将左边的元素填充到temp数组中
            if (arr[i] <= arr[j]) {
                temp[t] = arr[i];
                t++;// 索引后移
                i++;// i后移
            } else {
                // 反之，将右边有序序列的当前元素填充到temp数组中
                temp[t] = arr[j];
                t++;// 索引后移
                j++;// j后移
            }
        }
        // 把有剩余数据的一边的元素填充到temp中
        while (i <= mid) {
            // 此时说明左边序列还有剩余元素
            // 全部填充到temp数组
            temp[t] = arr[i];
            t++;
            i++;
        }
        while (j <= right) {
            // 此时说明左边序列还有剩余元素
            // 全部填充到temp数组
            temp[t] = arr[j];
            t++;
            j++;
        }
        // 将temp数组的元素复制到原数组
        t = 0;
        int tempLeft = left;
        while (tempLeft <= right) {
            arr[tempLeft] = temp[t];
            t++;
            tempLeft++;
        }
    }

    /**
     * 功能描述：测试归并排序
     * @author Ajie
     * @date 2019/11/4 0004
     */
    @Test
    public void mergeTest() {
        int[] arr = new int[19];
        for (int i = 0; i < 19; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        int[] temp = new int[arr.length];
        System.out.println("初始化数组："+Arrays.toString(arr));
        // 调用归并排序算法
        mergeSort(arr, 0, arr.length-1, temp);
        System.out.println("并归排序后的数组："+Arrays.toString(arr));
    }

    /**
     * 功能描述：基数排序
     * @author Ajie
     * @date 2019/11/4 0004
     * @param arr -需要排序的数组
     */
    public static void raixSort(int[] arr) {
        // 得到数组中最大的数
        int max = arr[0];// 假设第一个数就是数组中的最大数
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        // 得到最大数是几位数
        // 通过拼接一个空串将其变为字符串进而求得字符串的长度，即为位数
        int maxLength = (max + "").length();

        // 定义一个二维数组，模拟桶，每个桶就是一个一维数组
        // 为了防止放入数据的时候桶溢出，我们应该尽量将桶的容量设置得大一些
        int[][] bucket = new int[10][arr.length];
        // 记录每个桶中实际存放的元素个数
        // 定义一个一维数组来记录每个桶中每次放入的元素个数
        int[] bucketElementCounts = new int[10];

        // 通过变量n帮助取出元素位数上的数
        for (int i = 0, n = 1; i < maxLength; i++, n *= 10) {
            for (int j = 0; j < arr.length; j++) {
                // 针对每个元素的位数进行处理
                int digitOfElement = arr[j] / n % 10;
                // 将元素放入对应的桶中
                // bucketElementCounts[digitOfElement]就是桶中的元素个数，初始为0，放在第一位
                bucket[digitOfElement][bucketElementCounts[digitOfElement]] = arr[j];
                // 将桶中的元素个数++
                // 这样接下来的元素就可以排在前面的元素后面
                bucketElementCounts[digitOfElement]++;
            }
            // 按照桶的顺序取出数据并放回原数组
            int index = 0;
            for (int k = 0; k < bucket.length; k++) {
                // 如果桶中有数据，才取出放回原数组
                if (bucketElementCounts[k] != 0) {
                    // 说明桶中有数据，对该桶进行遍历
                    for (int l = 0; l < bucketElementCounts[k]; l++) {
                        // 取出元素放回原数组
                        arr[index++] = bucket[k][l];
                    }
                }
                // 每轮处理后，需要将每个bucketElementCounts[k]置0
                bucketElementCounts[k] = 0;
            }
        }
    }

    /**
     * 功能描述：测试基数排序
     * @author Ajie
     * @date 2019/11/4 0004
     */
    @Test
    public void raixSortTest() {
        int[] arr = new int[19];
        for (int i = 0; i < 19; i++) {
            arr[i] = (int) (Math.random() * 100);
        }
        System.out.println("初始化数组："+Arrays.toString(arr));
        // 调用基数排序算法
        raixSort(arr);
        System.out.println("基数排序后的数组："+Arrays.toString(arr));
    }


}
