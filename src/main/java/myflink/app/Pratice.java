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

//        replace("hello    world nihao");

//        String str = new String("abc");
//        myPrint(str.toCharArray(), 0);

//        int t = new Pratice().MoreThanHalfNum_Solution(new int[] {1,3,4,5,5,5,5,5});


        System.out.println(findNum(new int[] {1,3,4,5,5,5,5,5}));
//        System.out.println(t);
//        comb(str);

    }


























    private static boolean search(int[][] array, int target) {
        int i = 0;
        int j = array[0].length - 1;
        int len = array[0].length;

        while (i <= len - 1 && j >= 0) {
            if (array[i][j] == target) {
                return true;
            } else if (array[i][j] >= target) {
                i++;
            } else {
                j--;
            }
        }
        return false;

    }


    private Node reverListXunhuan(Node head) {
        if (head == null || head.next == null) {
            return head;
        }


        Node p1 = head;
        Node p2 = p1.next;

        Node tmp = new Node("-1", null);
        tmp.next = null;

        while (p1 != null) {
            p1.next = tmp.next;
            tmp.next = p1;

            p1 = p2;
            if (p1 == null) {
                break;
            }
            p2 = p1.next;
        }

        return tmp.next;
    }


    public String reverseWords(String s) {
        if (s == null) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        Stack<String> stack = new Stack<>();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                sb.append(s.charAt(i));
            } else {
                if (sb.length() > 0) {
                    stack.push(sb.toString());
                    sb = new StringBuilder();
                }

            }
        }

        stack.push(sb.toString());

        StringBuilder rs = new StringBuilder();
        while (!stack.isEmpty()) {
            rs.append(stack.pop() + " ");
        }

        return rs.toString().trim();
    }


    /**
     * 递归方式
     *
     * @param head
     * @return
     */
    private Node reverseList(Node head) {

        if (head == null) {
            return head;
        }

        Node newHead = reverseList(head.next);
        Node curNode = newHead;

        while (curNode != null && curNode.next != null) {
            curNode = curNode.next;
        }
        curNode.next = head;
        head.next = null;

        return newHead;
    }

    class Node {
        public String data;
        public Node next;

        public Node(String data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    /**
     * 从后往前打印列表 ，递归
     *
     * @param head
     */
    private static void pintListConvse(ListNode head) {
        if (head == null) {
            return;
        }

        pintListConvse(head.next);
        System.out.println(head.val);

    }


    /**
     * 从后往前打印链表,使用栈来考虑
     *
     * @param node
     */
    private static void print01(ListNode node) {
        if (node == null) {
            return;
        }

        Stack<Integer> stack = new Stack<>();
        while (node != null) {
            stack.push(node.val);
            node = node.next;
        }

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }

    /**
     * 从后往前打印链表,使用栈来考虑
     *
     * @param node
     */
    private static List<Integer> print02(ListNode node) {
        List<Integer> list = new ArrayList<>();
        while (node != null) {
            list.add(node.val);
            node = node.next;
        }
        Collections.reverse(list);
        return list;
    }


    //用来pop
    static Stack<Integer> s0 = new Stack<>();

    //push
    static Stack<Integer> s1 = new Stack<>();

    /**
     * 用两个栈来实现一个队列
     * 队列先进先出
     */


    /**
     * 加入队列
     *
     * @param i
     * @return
     */
    private static void offer(int i) {
        s1.push(i);
    }

    private static int poll() throws Exception {
        if (s1.isEmpty() && s0.isEmpty()) {
            throw new Exception("error");
        }

        if (s0.isEmpty()) {
            while (!s1.isEmpty()) {
                s0.push(s1.pop());
            }
        }
        return s0.pop();
    }

    public int FirstNotRepeatingChar(String str) {
        if(str == null || "".equals(str)) {
            return -1;
        }

        int[] res = new int[256];

        for(int i = 0; i < str.length(); i++) {
            char s = str.charAt(i);
            int pos = (int)s;
            res[pos]++;
        }



        for(int i = 0; i < str.length(); i++) {
            char s = str.charAt(i);
            int pos = (int)s;
            if(res[pos] == 1) {
                return i;
            }
        }
        return -1;
    }

    private int findKLargestNum(int[] arr, int k) {
        if(arr == null || arr.length == 0) {
            return Integer.MIN_VALUE;
        }
        Arrays.sort(arr);

        int len = arr.length;

        while (k > 0) {
            k--;
            len--;
        }

        return arr[len+1];
    }


    /**
     * 斐波那契数列
     *
     * @param n
     * @return
     */
    private int fibonacci(int n) {
        if (n == 0)
            return 0;
        if (n == 1) {
            return 1;
        }

        int fib0 = 0;
        int res = 1;
        int fib1 = 1;

        for (int i = 2; i < n; i++) {
            res = fib0 + fib1;
            fib0 = fib1;
            fib1 = res;
        }
        return res;
    }

    /**
     * .一只青蛙一次可以跳上1级台阶，也可以跳上2级。
     * 求该青蛙跳上一个 n 级台阶总共有多少种跳法
     */
    private int getMethodNumber(int n) {
        if (n == 0)
            return 0;
        if (n == 1)
            return 1;
        if (n == 2)
            return 2;
        return getMethodNumber(n - 1) + getMethodNumber(n - 2);
    }


    private static int getNum(int n) {
        int num = 0;
        while (n != 0) {
            num++;
            n = (n - 1) & n;
        }
        return num;
    }


    private ListNode deleteNode0(ListNode head, ListNode toBeDeletd) {
        if (head == null || toBeDeletd == null) {
            return head;
        }

        if (head == toBeDeletd) {
            return null;
        } else {
            if (toBeDeletd.next == null) {

                ListNode ptr = head;
                while (ptr.next.next != null) {
                    ptr = ptr.next;
                }

                ptr.next = null;
            } else {
                toBeDeletd.val = toBeDeletd.next.val;
                toBeDeletd.next = toBeDeletd.next.next;
            }

        }


        return head;

    }


    public int MoreThanHalfNum_Solution(int [] array) {
        int len=array.length;
        if(len<1){
            return 0;
        }
        int count=0;
        Arrays.sort(array);
        int num=array[len/2];
        for(int i=0;i<len;i++){
            if(num==array[i])
                count++;
        }
        if(count<=(len/2)){
            num=0;
        }
        return num;
    }


    private static ListNode getMidNode(ListNode head) {
        if(head == null || head.next == null) {
            return head;
        }
        ListNode p0 = head;
        ListNode p1 = head;


        while (p1.next != null || p1.next.next != null) {
            p0 = p0.next;
            p1 = p1.next.next;
        }

        return p0;

    }


    private ListNode deleteNode(ListNode head, ListNode toBeDeletd) {
        if (head == null || toBeDeletd == null) {
            return head;
        }

        if (head == toBeDeletd) {
            return null;
        } else {
            if (toBeDeletd.next == null) {
                ListNode pr = head;
                while (pr.next.next != null) {
                    pr = pr.next;
                }
                pr.next = null;
            } else {
                toBeDeletd.val = toBeDeletd.next.val;
                toBeDeletd.next = toBeDeletd.next.next;
            }


        }

        return head;
    }


    /**
     * 链表中的倒数第K个节点
     *
     * @param head
     * @param k
     * @return
     */
    private ListNode findK(ListNode head, int k) {
        ListNode faster = head;
        ListNode slow = head;

        for (int i = 0; i < k - 1; i++) {
            if (faster.next != null) {
                faster = faster.next;
            } else {
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
     *
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
        if (array == null || array.length == 0) {
            return false;
        }

        int start = 0;
        int end = array.length - 1;

        while (start < end) {

            int mid = (start + end) / 2;

            if (target == array[mid]) {
                return true;
            } else if (target > array[mid]) {
                start++;
            } else {
                end--;
            }
        }
        return false;
    }

    public int removeDuplicatesArrayåå(int[] nums) {
        if(nums == null || nums.length == 0) {
            return -1;
        }

        int len = nums.length;
        int k = 0;
        //1,1,2,2,4,5,6,6
        for(int i = 1; i < len-1; i++) {
            if(nums[i] != nums[k]) {
                k++;
                nums[k] = nums[i];
            }
        }
        return k+1;
    }

    public ListNode deleteDuplicates(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode p0 = head;
        while (p0 != null && p0.next != null) {
            if (p0.val == p0.next.val) {
                p0.next = p0.next.next;
            } else {
                p0 = p0.next;
            }
        }

        return head;
    }


    public int singleNumber(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int i = 0; i < nums.length; i++) {
            if (map.containsKey(nums[i])) {
                map.remove(nums[i]);
            } else {
                map.put(nums[i], 1);
            }
        }

        return map.keySet().iterator().next();
    }


    List<List<String>> res = new ArrayList<>();
//    public List<List<String>> partition(String s) {
//        if(s==""||s.length()==0){
//            return res;
//        }
//        List<String> list=new ArrayList<>();
//        backTracing(list,s,0);
//        return res;
//    }

//    public void backTracing(List<String> list,String s, int i){
//        if(i==s.length()){
//            res.add(list);
//        }
//        for(int j=i+1;j<=s.length();++j){
//            if(isParlindrome(s.substring(i,j))){
//                list.add(s.substring(i,j));
//                backTracing(new ArrayList<String> (list),s,j);
//                list.remove(list.size()-1);
//            }
//        }
//    }



    private int searchTarget(int[] arr, int target) {
        int low = 0;
        int high = arr.length - 1;
        int mid = -1;

        while (low < high) {
            mid = low+high/2;
            if(arr[mid] > target) {
                high--;
            }else if(arr[mid] < target) {
                low++;
            }else {
                return mid;
            }
        }
        return -1;
    }

    private int searchTarget(int[] arr, int target, int low, int high) {
        if(arr == null || arr.length == 0 || low > high) {
            return -1;
        }

        int mid = (low +high)/2;

        if(arr[mid] == target) {
            return mid;
        }else if(arr[mid] > target) {
            searchTarget(arr, target, low, mid-1);
        }else if(arr[mid] < target) {
            searchTarget(arr, target,mid+1, high);
        }

        return -1;

    }

    int  NumberOf1(int n) {
        int count = 0;
        while( n != 0) {
            count++;

            n = n & (n-1);
        }

        return count;
    }


    private static void myPrint(char[] str, int i) {
        if(i >= str.length) {
            return;
        }

        if(i == str.length - 1) {
            System.out.println(String.valueOf(str));
        }else {
            for(int j = i; j < str.length; j++) {
                char tmp = str[j];
                str[j] = str[i];
                str[i] = tmp;

                myPrint(str, i+1);

                tmp = str[j];
                str[j] = str[i];
                str[i] = tmp;
            }
        }

    }

    public static void comb(String str){
        char[] chs = str.toCharArray();
        int combNum = 1 << chs.length;//组合的个数有2^n的长度
        int k;
        for(int i = 0; i<combNum; i++){
            for(int j = 0; j<chs.length; j++){
                k = 1<<j;
                if((k&i)!=0){//按位与运算，如果结果为1就输出当前位，结果为0不输出
                    System.out.print(chs[j]);
                }
            }
            System.out.println();
        }
    }

    public static void per(char[] buf, String str,int length){
        char[] chs = str.toCharArray();
        if(length == 0){
            for(int i = buf.length-1; i>=0; i--){
                System.out.print(buf[i]);
            }
            System.out.println();
            return;
        }
        for (int i = 0;i<chs.length;i++){
            buf[length-1] = chs[i];
            per(buf,str,length-1);
        }
    }


    private static String replace(String s) {
        StringBuilder sb = new StringBuilder();
        int len = s.length();
        for(int i = 0; i < len; i++) {
            if(s.charAt(i) ==  ' ') {
                sb.append("%20");
            }else {
                sb.append(s.charAt(i));
            }
        }

        System.out.println(sb.toString());
        return sb.toString();
    }




    private static String reverseSub(String s) {
        if(s == null ||  "".equals(s)) {
            return null;
        }
        String[] array = s.split(" ");
        int len = array.length;

        Stack<String> stack = new Stack<>();

        for(int i = 0; i < len; i++) {
            stack.push(array[i]);
        }

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop() + " ");
        }

        return sb.toString().trim();
    }



    private static char getFirstStr(String s) {
        char[] t = s.toCharArray();

        int size = t.length;
        int[] tt = new int[256];

        for(int i = 0; i < 256; i++) {
            tt[i] = 0;
        }
        Map<Character, List<Integer>> map = new HashMap<>();
        for(int i = 0; i < size; i++) {
            tt[t[i]] += 1;
            System.out.println(tt[t[i]]);
        }

//        for(int i = 0; i < 256; i++) {
//            System.out.println(tt[i]);
//        }

        for(int i = 0; i < size; i++) {
            if(tt[t[i]] == 1) {
                return t[i];
            }
        }

        return ' ';
    }



    public static String LCS_caculate(String s1,String s2) {
        int size1 = s1.length();
        int size2 = s2.length();
        int chess[][] = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 1; i <= size1; i++) {//根据上面提到的公式计算矩阵
            for (int j = 1; j <= size2; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    chess[i][j] = chess[i - 1][j - 1] + 1;
                } else {
                    chess[i][j] = Math.max(chess[i][j - 1], chess[i - 1][j]);
                }
            }
        }
        int i = size1;
        int j = size2;
        StringBuffer sb = new StringBuffer();
        while ((i != 0) && (j != 0)) {//利用上面得到的矩阵计算子序列，从最右下角往左上走
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                sb.append(s1.charAt(i - 1));//相同时即为相同的子串
                i--;
                j--;
            } else {
                if (chess[i][j - 1] > chess[i - 1][j]) {
                    j--;
                } else {
                    i--;
                }
            }
        }
        System.out.println((double) sb.length() / s2.length() + "," + (double) sb.length() / s1.length());
        return sb.reverse().toString();//记得反转
    }
    private static String getCommonStr(String s0, String s1) {
        if (s0 == null || s1 == null) {
            return null;
        }

        char[] ch0 = s0.toCharArray();
        char[] ch1 = s1.toCharArray();

        int rows = ch0.length;
        int cols = ch1.length;

        int[][] res = new int[rows][cols];
        int maxLen = 0;
        int pos = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (ch0[i] == ch1[j]) {
                    if (i == 0 || j == 0) {
                        res[i][j] = 1;
                    } else {
                        res[i][j] = res[i - 1][j - 1] + 1;
                    }

                } else {
                    res[i][j] = 0;
                }

                if (res[i][j] > maxLen) {
                    maxLen = res[i][j];
                    pos = i;
                }
            }
        }

        System.out.println(maxLen + "_ " + pos + "_" + s0);

        String sh = s0.substring(pos - maxLen+1, pos+1);
        return sh;
    }


    private ListNode reverseList0(ListNode head) {
        if(head == null || head.next == null) {
            return head;
        }


        ListNode re = reverseList(head.next);

        ListNode p1 = re;
        while (p1.next != null) {
            p1 = p1.next;
        }

        p1.next = head;
        head.next = null;
        return re;
    }

    /**
     * 验证是否为回文串
     *
     * @param s
     * @return
     */
    public boolean isPalindrome(String s) {
        if (s == null) {
            return false;
        }

        s = s.toLowerCase();

        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            while (i < j && !Character.isLetter(s.charAt(i))) i++;
            while (i < j && !Character.isLetter(s.charAt(j))) j--;


            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
        }

        return true;

    }

    public int singleNumber0(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        for (int i = 0; i < nums.length; i++) {
            nums[i] ^= nums[i + 1];
        }

        return map.keySet().iterator().next();
    }

    /**
     * 合并有序数组
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     * @return
     */
    public int[] merge(int[] nums1, int m, int[] nums2, int n) {
        if (nums1 == null || nums1.length == 0) {
            return nums2;
        }

        if (nums2 == null || nums2.length == 0) {
            return nums1;
        }

        int i = m - 1, j = n - 1, k = m + n - 1;
        int[] res = new int[m + n];

        while (i >= 0 && j >= 0) {
            if (nums1[i] < nums2[j]) {
                res[k] = nums2[j];
                j--;
            } else {
                res[k] = nums1[i];
                i--;
            }
            k--;
        }

        System.arraycopy(nums2, 0, nums1, 0, j + 1);

        return res;
    }

    public int removeDuplicates(int[] nums) {
        int index = 0;
        int count = 0;

        int len = nums.length;
        for (int i = 0; i < len; i++) {
            if (i == 0 || nums[i - 1] == nums[1]) {
                count++;
            } else if (nums[i - 1] != nums[i]) {
                count = 1;
            }

            if (count <= 2) {
                nums[index++] = nums[i];
            }
        }

        return index;
    }


    /**
     * 非递归
     *
     * @param head
     * @return
     */
    private ListNode reverseList_1(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }

        ListNode dummy = new ListNode(-1, null);

        ListNode p1 = head;
        ListNode p2 = p1.next;

        while (p1 != null) {
            p1.next = dummy.next;
            dummy.next = p1;

            p1 = p2;

            if (p1 == null) {
                break;
            }

            p2 = p1.next;
        }

        return dummy.next;
    }


    /**
     * 合并两个排序链表
     *
     * @param list1
     * @param list2
     * @return
     */
    public ListNode Merge(ListNode list1,ListNode list2) {
        if(list1 == null) {
            return list2;
        }

        if(list2 == null) {
            return list1;
        }

        if(list1.val < list2.val) {
            ListNode tmp =  Merge(list1.next, list2);
            list1.next = tmp;
            return list1;
        }
        ListNode tmp =  Merge(list1, list2.next);
        list2.next = tmp;

        return list2;

    }

    /**
     * 从上到下打印一棵树
     */
    private List<Integer> print(TreeNode tree) {
        List<Integer> list = new ArrayList<>();


        if (tree == null) {
            return list;
        }


        LinkedList<TreeNode> linkedList = new LinkedList<>();
        linkedList.add(tree);

        while (tree != null) {

            linkedList.poll();
            list.add(tree.val);
            if (tree.left != null) {
                linkedList.addLast(tree.left);
            }

            if (tree.right != null) {
                linkedList.addLast(tree.right);
            }
        }

        return list;

    }


    /**
     * 找出最小的K个数
     *
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

        for (int i : input) {
            if (queue.size() < k) {
                queue.add(i);
            } else {
                if (queue.peek() < i) {
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
     *
     * @param array
     * @return
     */
    public int findGreatestSumOfSubArray0(int[] array) {
        if (array == null || array.length == 0) {
            return -1;
        }

        //每个位置的最大的sum值
        int[] sum = new int[array.length];


        int curSum = array[0];

        int maxSum = curSum;

        for (int i = 0; i < array.length; i++) {
            sum[i] = sum[i - 1] > 0 ? array[i] + sum[i - 1] : array[i];

            maxSum = Math.max(maxSum, sum[i]);
        }

        return maxSum;

    }

    private int partition(int[] array, int start, int end) {
        if (array == null || start > end) {
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

    private void sort(int[] array) {
        if (array == null || array.length == 0) {
            return;
        }

        int len = array.length;
        int mid = partition(array, 0, len);

        partition(array, 0, mid);
        partition(array, mid + 1, len);
    }


    /**
     * 输入两个链表，找出他们第一个公共节点
     *
     * @param pHead1
     * @param pHead2
     * @return
     */
    private ListNode FindFirstCommonNode(ListNode pHead1, ListNode pHead2) {
        if (pHead1 == null || pHead1 == null) {
            return null;
        }

        int size1 = 0;
        int size2 = 0;
        while (pHead1 != null) {
            size1++;
            pHead1 = pHead1.next;
        }

        while (pHead2 != null) {
            size2++;
            pHead2 = pHead2.next;
        }


        if (size1 < size2) {
            for (int i = 0; i < size2 - size1; i++) {
                pHead2 = pHead2.next;
            }
        } else {
            for (int i = 0; i < size1 - size2; i++) {
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
     *
     * @param nums
     * @param k
     * @return TODO >????
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
        if (root == null) {
            return 0;
        }

        int left = getTreeDepth(root.left);
        int right = getTreeDepth(root.right);

        if (left > right) {
            return left + 1;
        } else {
            return right + 1;
        }

    }

    /**
     * 一个数字在数组中出现的次数超过一半
     * @param arr
     * @return
     */
    private static int findNum(int[] arr) {
        if(arr == null || arr.length == 0) {
            return Integer.MAX_VALUE;
        }

        int res = arr[0];
        int count = 1;
        int len = arr.length;

        for(int i = 1; i < len; i++) {
            if(count == 0) {
                res = arr[i];
                count = 1;
            }else
            if(arr[i] == res) {
                count++;
            }else {
                count --;
            }

        }

        return res;

    }

    /**
     * 无序数组
     *
     * @param data
     * @param sum
     * @return
     */
    private boolean findNumberWithSum(int[] data, int sum) {
        if (data == null || data.length == 0) {
            return false;
        }

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < data.length; i++) {
            map.put(data[i], i);
        }

        for (int i = 0; i < data.length; i++) {
            if (map.containsKey(sum - data[i])) {
                return true;
            }
        }

        return false;
    }

    /**
     * 有序数组
     *
     * @param data
     * @param sum
     * @return
     */
    private boolean findSortedNumberWithSum(int[] data, int sum) {
        if (data == null || data.length == 0) {
            return false;
        }

        int start = 0;
        int end = data.length;

        while (start < end) {
            int curSum = data[start] + data[end];

            if (curSum == sum) {
                return true;
            } else if (curSum < sum) {
                start++;
            } else {
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
     *
     * @param sum
     * @return
     */
    public List<List<Integer>> findContinuousSequence0(int sum) {

        int start = 1;
        int end = 2;

        int curSum = 3;

        while (start < end) {
            if (sum == curSum) {
                getListFromleftToright(start, end);
                end++;
                curSum += end;
            } else if (sum < curSum) {
                start++;
                curSum -= start;
            } else {
                curSum += end;
                end++;
            }
        }

        return null;
    }

    //
    public int longestSubstring(String str) {
        char[] arr = str.toCharArray();
        int[] s = new int[26];

        for(int i = 0; i < 26; i++) {
            s[i] = -1;
        }

        s[arr[0] - 'a'] = 0;
        int size = arr.length;
        int[] res = new int[size];
        int max = res[0];

        //hubaihu
        for(int i = 1; i < size; i++ ){
            int pos = s[arr[i] - 'a'];
            int dis = i - pos;
            if(pos == -1 || dis > res[i-1]) {
                res[i] = res[i-1] + 1;
            }else {
                res[i] = dis;
            }

            s[arr[i] - 'a'] = i;
            max = Math.max(max, res[i]);

        }
        return max;
    }



    /**
     * 最长的不含重复字符的字串，计算该最长字符串的长度
     * 假设该字符串中只包含从a到z的字符
     * <p>
     * 动态规划，
     * res[i]表示以s[i】字符结尾的最长的不重复字符串的长度
     * 若s[i]在前面没有出现过，那么res[i] = res[i-1] + 1
     * 若s[i]在前面出现过，判断踏上一次出现的位置pos和i的距离d与res[i-1]的大小关系
     * 若d > res[i-1]证是在res[i-1]的左侧，则res[i] = res[i-1] + 1
     * 若d<=res[i-1]，说明是在res[i-1]构成的子串中，那么res[i-1]=d;
     * <p>
     * 判断s[i]
     *
     * @param str
     * @return
     */
    public int longestSubstringWithoutDuplication(String str) {
        if (str == null || "".equals(str)) {
            return 0;
        }

        char[] chs = str.toCharArray();

        int[] s = new int[26];

        for (int i = 0; i < s.length; i++) {
            s[i] = -1;
        }

        //第一个字符比如huiaabbh
        s[chs[0] - 'a'] = 0;//第一个s[6] = 0;
        int n = chs.length;//字符串的长度
        int[] res = new int[n];

        res[0] = 1;

        int max = res[0];

        for (int i = 1; i < n; i++) {
            //当前出现的字符在哪个位置
            int pos = s[chs[i] - 'a'];// pos = s[24]
            int dis = i - pos;

            res[i] = ((pos == -1) || dis > res[i - 1]) ? res[i - 1] + 1 : dis;
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

class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
