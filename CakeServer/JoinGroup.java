import java.io.*;

public class JoinGroup implements Serializable
{
   String username;
   String groupName;
   String password;
   
   public JoinGroup(String user, String group, String pass)
   {
      username = user;
      groupName = group;
      password = pass;
   }
   
   public String getUsername()
   {
      return username;
   }
   
   public String getgroupName()
   {
      return groupName;
   }
   
   public String getPassword()
   {
      return password;
   }

}