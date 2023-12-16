package mini1;

public class LoopermanTest {
	
	  public static void main(String[] args) {
		  boolean result = LoopermanTest.differByTwoInsertions("barranna", "banana");
		  System.out.println(result);
		  System.out.println("Expected false");
	  }
	
	  public static boolean differByTwoInsertions(String s, String t)
	  {
		  int count = 0;
		  boolean result = false;
		  if(Math.abs(s.length() - t.length()) > 2) {
			  return result;
		  }
		  if(s.length() < t.length()) {
			  for(int i = 0; i < s.length(); i++) {
				  char ch1 = s.charAt(i);
				  if(s.length() -1 >= i) {
					  break;
				  }
				  char ch2 = t.charAt(Math.min(i, s.length() - 1));
				  if(ch1 != ch2) {
					  count++;
					  i++;
					  s = s.substring(0, i) + ch2 + s.substring(i, s.length());
				  }
			  }
		  }
		  else {
			  for(int i = 0; i < t.length(); i++) {
				  char ch1 = s.charAt(i);
				  char ch2 = t.charAt(i);
				  if(ch1 != ch2) {
					  count++;
					  s = s.substring(0, i) + ch2 + s.substring(i, s.length());
				  }
			  }
		  }
		  if(count <= 2) {
			  result = true;
		  }
		  int i = 0;
		  if("aeiouAEIOU".indexOf(s.charAt(Math.min(i+1, s.length()))) >= 0) {
			  
		  }
	    return result;  
	  }
}
