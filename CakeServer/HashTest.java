import java.util.*;
public class HashTest
{
   public static void main(String []args)
   {
      Hashtable<String,String> hashy = new Hashtable<String,String>();
      hashy.put("one","two");
      Enumeration<String> keys = hashy.keys();
      System.out.println(hashy.toString());
      String out = "";
      while(keys.hasMoreElements())
      {
         out = out +  "," + keys.nextElement() ;
      }
      System.out.println(out);
   }

}