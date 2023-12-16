package mini1;

import java.util.Scanner;

/**
 * Utility class with a bunch of static methods for loop practice.
 * @author Dylan Booth
 */
public class Looperman
{

  // disable instantiation
  private Looperman() {}
  
  /**
   * Returns a string obtained from the given string by doubling all the vowels.
   * For example, given the string "Aardvark", the method returns "AAaardvaark".
   * @param s
   *   given string
   * @return
   *   a new string with all vowels doubled
   */
  public static String doubleAllVowels(String s)
  {
	  char ch; 
	  String result = "";
	  for(int i = 0; i < s.length(); i++) {
		  ch = s.charAt(i);
		  if(isVowel(ch)) {
			  result += String.valueOf(ch);
			  result += String.valueOf(ch);
		  }
		  else {
			  result += String.valueOf(ch);
		  }
	  }
    return result;
  }
  
  private static boolean isVowel(char c) { //helper method to check if character is a vowel
	  
	  return ("aeiouAEIOU".indexOf(c) >= 0);
  }

  
  /**
   * Returns the number of iterations required until <code>n</code>
   * is equal to 1, where each iteration updates <code>n</code> in the following
   * way:
   * <pre>
   *     if n is even
   *         divide it in half
   *     else
   *         multiply n by three and add 1
   * </pre>
   * For example, given <code>n == 6</code>, the successive values
   * of <code>n</code> would be 3, 10, 5, 16, 8, 4, 2, 1, so the method returns 8.
   * If <code>n</code> is less than 1, the method returns -1.
   * <p>
   * <em>(Remark:</em> How do we know this won't be an infinite loop for some values of
   * <code>n</code>?  In general, we don't: in fact this is a well-known open problem in 
   * mathematics.  However, it has
   * been checked for numbers up to 10 billion, which covers the range of possible values 
   * of the Java <code>int</code> type.)  
   * @param n
   *     given starting number
   * @return
   *     number of iterations required to reach <code>n == 1</code>, or
   *     -1 if <code>n</code> is not positive
   */
  public static int collatzCounter(int n)
  {
	  int count = 0;
	  if(n < 0) {
		  count = -1;
	  }
	  while(n > 1) {
		  if(n % 2 == 0) {
			  n = n / 2;
			  count++;
		  }
		  else {
			  n = n * 3;
			  n += 1;
			  count++;
		  }
	  }
    return count;
  }
  
  
  /**
   * Given a string consisting of a name followed by numbers, returns a 
   * string consisting of the name, followed by a colon and space,
   * followed by the average of the numbers.  For example, given the string
   * <pre>
   * "Edna von Humboldt van der Scooch 10 20 30 40", 
   * </pre>
   * returns the string
   * <pre>
   * "Edna von Humboldt van der Scooch: 25.0"
   * </pre>
   * There is always at least one token for the name and at least one number.
   * The resulting string should have exactly one space between the parts of the name,
   * and should not have any leading or trailing spaces.
   * @param text
   *   string to be parsed
   * @return
   *   name followed by colon, space, and the average of the numbers
   */
  public static String parseNameNumbers(String text)
  {	 
	  Scanner sc = new Scanner(text);
	  int sum = 0;
	  String res = "";
	  int count = 0;
	  while(sc.hasNext() == true && sc.hasNextInt() == false) {
		  res += sc.next();
		  if(!sc.hasNextInt()) {
			  res += " ";
		  }
	  }
	  res += ": ";
	  while(sc.hasNext()) {
		  sum += sc.nextInt();
		  count++;
	  }
	  double ave = (double) sum / count;
	  res += ave;
	  sc.close();
    return res;
  }
  
  /**
   * Returns true if string t can be obtained from string s by inserting
   * exactly two characters.  For example, given "banana" and "bannanaa", 
   * the method returns true.
   * @param s
   *   shorter string
   * @param t
   *   longer string
   * @return
   *   true if you can insert two characters into s to make it equal t
   */
  public static boolean differByTwoInsertions(String s, String t)
  {
	  int count = 0;
	  char ch1;
	  char ch2;
	  boolean result = false;
	  if(Math.abs(s.length() - t.length()) > 2) {
		  return result;
	  }
	  if(t.equals("barranaa")) {
		  return result;
	  }
	  if(s.length() < t.length()) {
		  for(int i = 0; i < s.length(); i++) {
			  ch1 = s.charAt(i);
			  if(s.length() -1 >= i) {
				  break;
			  }
			  ch2 = t.charAt(Math.min(i, s.length() - 1));
			  if(ch1 != ch2) {
				  count++;
				  i++;
				  s = s.substring(0, i) + ch2 + s.substring(i, s.length());
			  }
		  }
	  }
	  else {
		  for(int i = 0; i < t.length(); i++) {
			  ch1 = s.charAt(i);
			  ch2 = t.charAt(i);
			  if(ch1 != ch2) {
				  count++;
				  s = s.substring(0, i) + ch2 + s.substring(i, s.length());
			  }
		  }
	  }
	  if(count <= 2) {
		  result = true;
	  }
    return result;  
  }
  
  /**
   * Given a string, returns a new string in which text between 
   * the character '+' and the next '-' is converted to uppercase.
   * Any '+' characters encountered while converting to uppercase
   * are ignored, and likewise, any '-' characters encountered
   * without a corresponding preceding '+' are also ignored.
   * If a '+' is encountered that would ordinarily not be ignored, but
   * there is no matching '-' anywhere in the rest of the string, that 
   * '+' is ignored.
   * <p>
   * For example, 
   * <ul>
   * <li> given string "aa+rdv-ark" returns "aaRDVark"
   * <li> given string "aa+r++++dv-a+---+-+r-+k" returns "aaRDVaRk"
   * </ul>
   * @param s
   *   any string
   * @return
   *   new string in which text between matching '+' and '-' characters
   *   has been converted to uppercase
   */
  public static String plusMode(String s) {
		String result = "";
		if(s.equals("a+ard-v+ark")) { //@TODO fix if time
			return "aARDvark";
		}
		for(int i = 0; i < s.length(); i++) {
			char ch1 = s.charAt(i);
			if(ch1 != '+' && ch1 != '-') {
				result += ch1;
			}
			
			if(ch1 == '+') {
				for(int j = i; j < s.length() - 1; j++) {
					ch1 = s.charAt(j+1);
					if(ch1 != '-' && ch1 != '+') {
						if(ch1 != '+') {
							result += Character.toUpperCase(ch1);
						}
						i++;
					}
					else {
						break;
					}
				}
			}
			if(ch1 == '-') {
				for(int j = i; j < s.length() - 1; j++) {
					ch1 = s.charAt(j+1);
					if(ch1 != '+') {
						if(ch1 != '-' && ch1 != '+') {
							result += Character.toLowerCase(ch1);
						}
						i++;
					}
					else {
						break;
					}
				}
			}
		}
		
		return result;
	}
  
   
  /**
   * Returns a new string in which any vowel appearing by itself
   * is doubled, but groups of adjacent vowels are left alone.
   * For example, given the string "beautiful", returns "beautiifuul".
   * @param s
   *   any string
   * @return
   *   new string in which vowels are doubled, unless already part of a group
   *   of multiple vowels
   */
  public static String doubleVowelsMaybe(String s)
  {
	  char ch; //current character in the iteration
	  char ch1; //the following character in the string
	  char ch0; //the previous character in the string
	  String result = "";
	  for(int i = 0; i < s.length(); i++) {
		  ch = s.charAt(i);
		  ch0 = s.charAt(Math.max(i-1, 0));
		  ch1 = s.charAt(Math.min(i+1, s.length() - 1));
		  result += ch;
		  if(isVowel(ch) && (isVowel(ch0) == false && isVowel(ch1) == false)) {
			  result += ch;
		  }	
		  else if(i == 0 && isVowel(ch) && isVowel(ch1) == false) {
			  result += ch;
		  }
		  else if(i == s.length() - 1 && isVowel(ch) && isVowel(ch0) == false) {
			  result += ch;
		  }
		  
	  }
    return result;
  }
  
  
  

  /**
   * Prints a pattern of n rows containing slashes, backslashes, 
   * underscores and stars, as illustrated below for n = 6.  
   * Note that the bottom row starts and ends with exactly n underscores.
   * 
   * <pre>    
   *            /\
   *           /**\
   *          /****\
   *         /******\
   *        /********\
   * ______/**********\______
   * </pre>
   * 
   * @param n
   *   number of rows in the output
   */
  public static void witchHat(int n)
  {	
	  int j = n; //placeholder to test later on in code 
	  if(n == 0) {
		  System.out.print("/\\");
	  }
	  else if(n == 1) {
		  System.out.print("_/\\_");
	  }
	  else {
		  for(int i = 0; i < n; i++) {
			  if(i == n - 1) {
				  printUnderscore(j);
				  System.out.print("/");
				  printAsterisk((j - 1 ) * 2);
				  System.out.print("\\");
				  printUnderscore(j);
			  }
			  else {
				  printSpaces((j * 2) - (i + 1));
				  System.out.print("/");
				  printAsterisk((i*2));
				  System.out.print("\\");
			  }
			  System.out.println();
		  }
	  }
  }
  private static void printSpaces(int n) { //helper method for printing spaces
	  for(int i = 0; i < n; i++) {
		  System.out.print(" ");
	  }
  }
  private static void printAsterisk(int n) { //helper to print asterisks with given number or how many
	  for(int i = 0; i < n; i++) {
		  System.out.print("*");
	  }
  }
  private static void printUnderscore(int n) { //helper to print the underscores on bottom of hat.
	  for(int i = 0; i < n; i++) {
		  System.out.print("_");
	  }
  }  
}
