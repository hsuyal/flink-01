package myflink.app;

/**
 * Created by xumingyang on 2019/8/2.
 */
import java.util.*;

/**
 * Created by xumingyang on 2019/7/12.
 */
public class Pratice {
    public static void main(String[] args) {
        ListNode tail = new ListNode(1, null);
        ListNode tail1 = new ListNode(2, tail);
        ListNode tail2 = new ListNode(3, tail1);
        ListNode tail3 = new ListNode(4, tail2);


        pintListConvse(tail3);
    }

    /**
     * 从后往前打印列表 ，递归
     * @param head
     */
    private static void pintListConvse(ListNode head) {
        if(head  == null) {
            return;
        }

        pintListConvse(head.next);
        System.out.println(head.val);

    }


    /**
     * 从后往前打印列表
     * @param head
     */
    private static void print(ListNode head) {
        if(head == null) {
            return ;
        }
        Stack<Integer> stack = new Stack();
        while (head != null) {
            stack.push(head.val);

            ListNode tmp = head.next;
            head = tmp;
        }

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }

    }


    static Stack<Integer> s0 = new Stack<>();
    static Stack<Integer> s1 = new Stack<>();

    /**
     * 用两个栈来实现一个队列
     * 队列先进先出
     */


    /**
     * 加入队列
     * @param i
     * @return
     */
    private static void offer(int i) {

        s1.push(i);

    }

    private static int poll() throws Exception {
        if (s1.isEmpty() && s0.isEmpty()) {
            throw new Exception("null");
        }


        while (!s0.isEmpty() && s1.isEmpty()) {
            s1.push(s0.pop());
        }

        return s1.pop();
    }


    private ListNode deleteNode0(ListNode head, ListNode toBeDeletd) {
        if(head == null || toBeDeletd == null) {
            return head;
        }

        if(head == toBeDeletd) {
            return null;
        }else {
            if(toBeDeletd.next == null) {

                ListNode ptr = head;
                while (ptr.next.next != null) {
                    ptr = ptr.next;
                }

                ptr.next = null;
            }else {
                toBeDeletd.val = toBeDeletd.next.val;
                toBeDeletd.next = toBeDeletd.next.next;
            }

        }


        return head;

    }


    private ListNode deleteNode(ListNode head, ListNode toBeDeletd) {
        if (head == null || toBeDeletd == null) {
            return head;
        }

        if(head == toBeDeletd) {
            return null;
        }else {
            if(toBeDeletd.next == null) {
                ListNode pr = head;
                while (pr.next.next != null) {
                    pr = pr.next;
                }
                pr.next = null;
            }else {
                toBeDeletd.val = toBeDeletd.next.val;
                toBeDeletd.next = toBeDeletd.next.next;
            }


        }

        return head;
    }


    /**
     * 链表中的倒数第K个节点
     * @param head
     * @param k
     * @return
     */
    private ListNode findK(ListNode head, int k) {
        ListNode faster = head;
        ListNode slow = head;

        for(int i = 0; i < k-1; i++) {
            if(faster.next != null) {
                faster = faster.next;
            }else {
                return null;
            }
        }

        while (faster.next != null) {
            faster = faster.next;
            slow = slow.next;
        }

        return slow;
    }

    /**
     * 反转链表 递归解法
     * @param head
     * @return
     */
    private ListNode reverseList(ListNode head) {
        if (head == null) {
            return null;
        }

        ListNode cur = reverseList(head.next);

        ListNode ptr = cur;

        while (ptr.next != null) {
            ptr = ptr.next;
        }

        ptr.next = head;
        head.next = null;
        return cur;
    }



    private boolean search(int[] array, int target) {
        if(array == null || array.length == 0) {
            return false;
        }

        int start = 0;
        int end = array.length -  1;

        while (start < end) {

            int mid =  (start+end)/2;

            if(target == array[mid]) {
                return true;
            }else if(target > array[mid]) {
                start++;
            }else {end--;}
        }
        return false;
    }


    /**
     * 非递归
     * @param head
     * @return
     */
    private ListNode reverseList_1(ListNode head) {
        if(head == null || head.next == null) {
            return null;
        }

        ListNode dummy = new ListNode(-1, null);

        ListNode p1 = head;
        ListNode p2 = p1.next;

        while (p1 != null) {
            p1.next = dummy.next;
            dummy.next =  p1;

            p1 = p2;

            if(p1 == null) {
                break;
            }

            p2  = p1.next;
        }

        return dummy.next;
    }


    /**
     * 合并两个排序链表
     * @param list1
     * @param list2
     * @return
     */
    public ListNode merge(ListNode list1, ListNode list2) {

        if(list1 == null) {
            return list2;
        }

        if(list2 == null) {
            return list1;
        }

        if(list1.val < list2.val) {
            ListNode mergedList  = merge(list1.next, list2);
            list1.next = mergedList;
            return list1;
        }
        ListNode mm = merge(list1, list2.next);
        list2.next = mm;
        return list2;
    }

    /**
     * 从上到下打印一棵树
     */
    private List<Integer> print(TreeNode tree) {
        List<Integer> list = new ArrayList<>();


        if(tree == null) {
            return list;
        }


        LinkedList<TreeNode> linkedList = new LinkedList<>();
        linkedList.add(tree);

        while (tree != null) {

            linkedList.poll();
            list.add(tree.val);
            if(tree.left != null) {
                linkedList.addLast(tree.left);
            }

            if(tree.right != null) {
                linkedList.addLast(tree.right);
            }
        }

        return list;

    }


    /**
     * 找出最小的K个数
     * @param input
     * @param k
     * @return
     */
    private List<Integer> GetLeastNumbers_Solution(int[] input, int k) {
        List<Integer> res = new ArrayList<>();


        if (input == null || input.length == 0 || input.length < k || k < 1) {
            return res;
        }


        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.reverseOrder());

        for(int i : input) {
            if(queue.size() < k) {
                queue.add(i);
            }else {
                if(queue.peek() <  i) {
                    queue.poll();
                    queue.add(i);
                }
            }
        }

        res.addAll(queue);
        return res;
    }


    /**
     * 求连续数组的最大和
     * @param array
     * @return
     */
    public int findGreatestSumOfSubArray0(int[] array) {
        if(array ==  null  || array.length  == 0) {
            return -1;
        }

        //每个位置的最大的sum值
        int[] sum = new int[array.length];


        int curSum = array[0];

        int maxSum = curSum;

        for(int i = 0; i < array.length; i++) {
            sum[i] = sum[i-1] > 0 ? array[i] + sum[i-1] : array[i];

            maxSum = Math.max(maxSum, sum[i]);
        }

        return maxSum;

    }

    private int partition(int[] array, int start, int end) {
        if(array == null || start > end) {
            return -1;
        }

        int tmp = array[0];
        while (start < end) {
            while (start < end && tmp <= array[end]) {
                end--;
            }
            array[start] = array[end];

            while (start < end && tmp > array[start]) {
                start++;
            }

            array[end] = array[start];
        }

        array[start] = tmp;
        return start;
    }

    private void sort(int[]  array) {
        if(array == null || array.length == 0) {
            return;
        }

        int len = array.length;
        int mid = partition(array, 0, len);

        partition(array, 0, mid);
        partition(array, mid+1, len);
    }


    /**
     * 输入两个链表，找出他们第一个公共节点
     * @param pHead1
     * @param pHead2
     * @return
     */
    private ListNode FindFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        if(pHead1 == null || pHead1 == null) {
            return null;
        }

        int size1 = 0;
        int size2  = 0;
        while (pHead1 != null) {
            size1++;
            pHead1 = pHead1.next;
        }

        while (pHead2 != null) {
            size2++;
            pHead2 = pHead2.next;
        }


        if(size1 < size2) {
            for(int i = 0; i < size2-size1; i++) {
                pHead2 = pHead2.next;
            }
        }else  {
            for(int i = 0; i < size1-size2; i++) {
                pHead1 = pHead1.next;
            }
        }
        while (pHead1 != null && pHead2 != null && pHead1 != pHead2) {
            pHead1 = pHead1.next;
            pHead2 = pHead2.next;
        }

        return pHead1;
    }

    /**
     * 一个数字在排序数组中出现的次数
     * @param nums
     * @param k
     * @return
     * TODO >????
     */
    private int getNumberOfK(int[] nums, int k) {
//        if(nums == null || nums.length == 0) {
//            return -1;
//        }
//
//        int start = 0;
//        int end = nums.length;
//
//        while (start < end) {
//            int mid = (start+end)/2;
//
//            if()
//        }
//    }
        return 0;
    }

    private int getTreeDepth(TreeNode root) {
        if(root == null) {
            return 0;
        }

        int left = getTreeDepth(root.left);
        int right = getTreeDepth(root.right);

        if(left > right) {
            return left+1 ;
        }else {
            return right+1;
        }

    }

    /**
     * 无序数组
     * @param data
     * @param sum
     * @return
     */
    private boolean findNumberWithSum(int[] data, int sum) {
        if(data == null || data.length == 0) {
            return false;
        }

        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0 ; i < data.length; i++) {
            map.put(data[i], i);
        }

        for(int i= 0; i < data.length; i++) {
            if(map.containsKey(sum-data[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 有序数组
     * @param data
     * @param sum
     * @return
     */
    private boolean findSortedNumberWithSum(int[] data, int sum) {
        if(data == null || data.length == 0) {
            return false;
        }

        int start = 0;
        int end = data.length;

        while (start < end) {
            int curSum = data[start] + data[end];

            if(curSum == sum) {
                return true;
            }else if(curSum < sum) {
                start++;
            }else {
                end--;
            }
        }

        return false;
    }

    private void printNums(int from, int end) {
        while (from <= end) {
            System.out.println(from);
        }
    }


    /**
     * 和为S的连续正数序列
     * @param sum
     * @return
     */
    public List<List<Integer>> findContinuousSequence0(int sum) {

        int start = 1;
        int end = 2;

        int curSum = 3;

        while (start < end) {
            if(sum == curSum) {
                getListFromleftToright(start, end);
                end++;
                curSum += end;
            }else if(sum < curSum) {
                start++;
                curSum -=  start;
            }else {
                curSum += end;
                end++;
            }
        }

        return null;
    }


    /**
     * 最长的不含重复字符的字串，计算该最长字符串的长度
     *假设该字符串中只包含从a到z的字符
     *
     * 动态规划，
     * res[i]表示以s[i】字符结尾的最长的不重复字符串的长度
     * 若s[i]在前面没有出现过，那么res[i] = res[i-1] + 1
     * 若s[i]在前面出现过，判断踏上一次出现的位置pos和i的距离d与res[i-1]的大小关系
     * 若d > res[i-1]证是在res[i-1]的左侧，则res[i] = res[i-1] + 1
     * 若d<=res[i-1]，说明是在res[i-1]构成的子串中，那么res[i-1]=d;
     *
     * 判断s[i]
     *
     * @param str
     * @return
     */
    public int longestSubstringWithoutDuplication(String str) {
        if(str == null || "".equals(str)) {
            return 0;
        }

        char[] chs = str.toCharArray();

        int[] s = new int[26];

        for(int i = 0 ; i < s.length; i++) {
            s[i] = -1;
        }

        //第一个字符比如huiaabbh
        s[chs[0] - 'a'] = 0;//第一个s[6] = 0;
        int n = chs.length;//字符串的长度
        int[] res = new int[n];

        res[0] = 1;

        int max = res[0];

        for(int i = 1;  i < n; i++) {
            //当前出现的字符在哪个位置
            int pos = s[chs[i] - 'a'];// pos = s[24]
            int dis = i - pos;

            res[i] = ((pos == -1) ||  dis > res[i-1]) ? res[i-1] + 1 : dis;
            s[chs[i] - 'a'] = i;
            max = Math.max(max, res[i]);
        }
        return max;
    }


    private List<Integer> getListFromleftToright(int left, int right) {

        List<Integer> tempList = new ArrayList<>();
        for (int i = left; i <= right; i++) {
            tempList.add(i);
        }

        return tempList;
    }
}



class TreeNode {
    int val = 0;
    TreeNode left = null;
    TreeNode right = null;

    public TreeNode(int val) {
        this.val = val;

    }
}

class ListNode{
    public int val;
    public ListNode next;

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
