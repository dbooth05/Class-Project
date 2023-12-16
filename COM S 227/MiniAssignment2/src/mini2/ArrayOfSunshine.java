package mini2;
import java.util.ArrayList;
public class ArrayOfSunshine
{
  
  // disable instantiation
  private ArrayOfSunshine() {}
  
  /**
   * Returns the longest string that is a prefix of
   * all strings in the given array.  For example, 
   * if the array is ["foo", "four", "football"], the
   * method returns "fo".  If the array is empty, returns
   * an empty string.
   * @author Dylan Booth
   * @param arr
   *   any array of strings.
   * @return
   *   longest common prefix of the given strings
   */
  public static String longestCommonPrefix(String[] arr)
  {
	  String prefix = ""; //base string to build on
	  char ch; //temporary character value
	  int count = 0;
	  if(arr.length == 0) {
		  return prefix;
	  }
	  for(int i = 1; i < arr[0].length(); i++) {
		  ch = arr[0].charAt(i);
		  for(int j = 0; j < arr.length; j++) {
			  if(ch == arr[j].charAt(i)) {
				  count++;
			  }
		  }
		  if(count == arr.length) {
			  prefix += ch;
			  count = 0;
		  }
		  else {
			  return prefix;
		  }	  }
    return prefix;
  }
  
  /**
   * Modifies the given array by exchanging the first half
   * with the second half.  If the array has odd length, the
   * center element is not moved.  For example, given array
   * <pre>
   * [2, 4, 6, 99, 1, 2, 3]
   * </pre>
   * after swapFirstLast the array
   * contains
   * <pre>
   * [1, 2, 3, 99, 2, 4, 6]
   * </pre>
   * Note this method modifies the given array and returns
   * void.
   * @param arr
   *   the int array to be modified
   */
  public static void swapFirstLast(int[] arr)
  {
	  int temp; //a int to hold a temporary value at an array
	  if(arr.length % 2 == 0) {
		  for(int i = 0; i < arr.length / 2; i++) {
			  temp = arr[i];
			  arr[i] = arr[arr.length / 2 + i];
			  arr[arr.length / 2 + i] = temp;
		  }
	  }
	  else {
		  for(int i = 0; i < arr.length / 2; i++) {
			  if(i == arr.length / 2 + 1) {
				  
			  }
			  else {
				  temp = arr[i];
				  arr[i] = arr[(arr.length + 1) / 2 + i];
				  arr[(arr.length + 1) / 2 + i] = temp;
			  }
		  }
	  }
  }
  
  /**
   * Rearrange the elements of arr according to the given list of
   * indices.  After calling this method, arr[i] should be
   * the value formerly located at arr[swizzler[i]].
   * For example, if swizzler is [3, 0, 3, 1] and arr is 
   * <pre>
   * [10, 20, 30, 40]
   * </pre>
   * and
   * then after this method executes, arr is
   * <pre>
   * [40, 10, 40, 20].
   * </pre>  
   * If the swizzler length
   * does not match the array length, or if it contains any
   * number that is out of range as an index in arr, 
   * the method does nothing.  Note that this method 
   * modifies the given array and returns void.
   * @param arr
   *   the int array to be modified
   * @param swizzler
   *   array of indices indicating new positions of elements
   */
  public static void swizzle(int[] arr, int[] swizzler)
  {
	  int [] temp = new int[arr.length]; //temporary array to hold the values or arr[]
	  for(int j = 0; j < arr.length; j++) { //sets temp array equal to arr
		  temp[j] = arr[j];
	  }
	  
	  if(isValid(arr, swizzler)) {
		  for(int i = 0; i < arr.length; i++) {
			  arr[i] = temp[swizzler[i]]; //sets the new value at i in the array
		  }
	  }
  }
  
  private static boolean isValid(int [] arr, int [] swizzler) { //checks to see if the swizzler array is a valid array
	  
	  for(int i = 0; i < swizzler.length; i++) {
		  if(swizzler[i] > arr.length - 1 || swizzler[i] < 0 || arr.length != swizzler.length) {
			  return false;
		  }
	  }
	  return true;
  }
  
  /**
   * Creates a new array from the given one by adding up groups
   * of k adjacent entries.  The length of the returned array
   * is always <code>arr.length / k</code> (integer division), 
   * and its ith element is the sum
   * <p>
   * arr[ik] + arr[ik + 1] + arr[ik + 2] + ... + arr[ik + (k - 1)]
   * <p>
   * If the array length is not an exact multiple
   * of k, the excess elements are ignored.  For example, if
   * arr is [1, 2, 3, 4, 5, 6, 7] and k is 3, the 
   * result is [6, 15].
   * @param arr
   *   any int array
   * @param k
   *   number of adjacent elements in each group
   * @return
   *   new array obtained by adding up groups of adjacent elements
   */
  public static int[] condense(int[] arr, int k)
  {
	  int [] total = new int[arr.length / k]; //a new array to return the totals
	  int temp = 0; //temporary int to hold values
	  for(int i = 0; i < total.length; i++) {
		  for(int j = 0; j < k; j++) {
			  temp += arr[j + i*k];
		  }
		  total[i] = temp;
		  temp = 0;
	  }
	  return total;
  }
  
  /**
   * Determines whether arr is a permutation.  We define
   * a permutation as an array in which each number 
   * 0 through n - 1 appears exactly once, where n is 
   * the length of the array.
   * @param arr
   *   an int array
   * @return
   *   true if the given array is a permutation, false otherwise
   */
  public static boolean isPermutation(int arr[])
  {
	  boolean result = true; //boolean to track if arr is a permutation
	  for(int i = 0; i < arr.length - 1; i++) {
		  int temp = arr[i]; //temp value to hold
		  for(int j = 0; j < arr.length; j++) {
			  if(i == j) {
				  continue;
			  }
			  else if(temp == arr[j]) {
				  result = false;
				  return result;
			  }
			  else if(arr[j] < 0 || arr[j] > arr.length -1) {
				  result = false;
				  return result;
			  }
			  
		  }
	  }
    return result;
  }
  
  /**
   * Returns the cycle in arr beginning with the given starting index.
   * A <em>cycle</em> in an array is a sequence of indices 
   * i<sub>0</sub>, i<sub>1</sub>, i<sub>2</sub>, ...i<sub>k</sub>
   * for some k &gt;= 0, such that:
   * <p>
   * i<sub>1</sub> = arr[i<sub>0</sub>], i<sub>2</sub> = arr[i<sub>1</sub>], 
   * ..., and i<sub>0</sub> = arr[i<sub>k</sub>].  
   * <p>The sequence
   * of indices is returned as an int array with the given start index
   * at position 0.  
   * For example, given [2, 4, 0, 1, 3] and start = 3, the method
   * returns [3, 1, 4].  (Note that if arr[start] == start, then
   * the resulting cycle has length 1.)
   * Returns null if the given array is not
   * a permutation, or if the start index is out of bounds in arr.
   * @param arr
   *   an int array
   * @param start
   *   starting point for finding a cycle
   * @return
   *   array containing cycle indices, beginning with given start index
   *   
   */
  public static int[] findOneCycle(int[] arr, int start)
  {
	  ArrayList<Integer> cycle = new ArrayList<Integer> (); //arraylist to add keep sequence
	  cycle.add(start); //adding the start number
	  if(start >= arr.length || isPermutation(arr) == false) { //checking if possible or permutation
		  return null;
	  }
	  
	  int temp = arr[start]; //temporary int to hold next index
	  
	  for(int i = 0; i < arr.length && temp != start; i++) {
		  if(temp == start && i > 0) {
			  break;
		  }
		  else {
			  cycle.add(temp);
			  temp = arr[temp];
		  }
	  }
	  //turning arraylist to an array
	  int [] result = new int[cycle.size()]; //array to be returned
	  for(int i = 0; i < cycle.size(); i++) {
		  result[i] = cycle.get(i);
	  }
	  
    return result;
  }
  
  /**
   * Returns a list of all cycles in the given array.
   * For example, given [2, 1, 3, 0, 5, 4], the resulting
   * list should contain [0, 2, 3], [1], and [4, 5].
   * The cycles in the resulting list should be disjoint; that is,
   * for example, 
   * the list should not contain [2, 3, 0] in addition to
   * [0, 2, 3], even though both arrays describe the same cycle.
   * The order of cycles in the list is not specified.
   * Returns an empty list if arr is not a permutation.
   * @param arr
   *   an int array
   * @return
   *   list of all cycles
   */
  public static ArrayList<int[]> findAllCycles(int[] arr)
  {
	 ArrayList<int[]> result = new ArrayList<int[]> (); //arraylist to hold result value
	 
	 if(isPermutation(arr) == false) { //check to see if array is permutation, if so return empty array
		 return result;
	 }
	 for(int i = 0; i < arr.length; i++) {
		 if(hasOccured(i, result)) { //calling of helper method to check array
			 continue;
		 }
		 else {
	 		result.add(findOneCycle(arr, i));
		 }
	 }
	 return result;
  }
  
  private static boolean hasOccured(int n, ArrayList<int[]> arr) { //helper method to find if a value has been used 
	  for(int i = 0; i < arr.size(); i++) {
		  int [] temp = arr.get(i);
		  for(int j = 0; j < temp.length; j++) {
			  if(temp[j] == n) {
				  return true;
			  }
		  }  
	  }	  
	  return false;
  }
}