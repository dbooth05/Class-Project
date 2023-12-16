import java.io.FileNotFoundException;
import java.util.ArrayList;

import hw3.SnakeUtil;

public class SimpleFileTests
{

  public static void main(String[] args) throws FileNotFoundException
  {
    ArrayList<String[]> result = SnakeUtil.createDescriptorsFromFile("testfile.txt");
    System.out.println(result.size());
    for (String[] arr : result)
    {
      for (String s : arr)
      {
        System.out.println(s);
      }
      System.out.println("---");
    }
  }

}
