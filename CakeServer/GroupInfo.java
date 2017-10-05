import java.io.*;

/**
 * Class that creates an object for a new group
 * @authors Dervent Wright, Willy Choi, Abdulaziz Alomari, Andrew Marone
 * @version 4/20/2014
 */

public class GroupInfo implements Serializable
{
   //Global Variables
   public String streetAddress;
   public String city;
   public String state;
   public String zip;
   public String contactNo;
   public String groupName;
   public String groupPassword;
   public String leader;
   public long minutes;
   
   public GroupInfo(String _groupName, String _groupPassword,String _streetAddress, 
      String _city, String _state, String _zip, String _contactNo,String _leader,long _minutes)      
   {
      groupName = _groupName;
      groupPassword = _groupPassword; 
      streetAddress = _streetAddress;
      city = _city;
      state = _state;
      zip = _zip;
      contactNo = _contactNo; 
      leader = _leader;
      minutes = _minutes;
   }
   
   public long getMinutes()
   {
      return minutes;
   }
   
   public String getGroupName()
   {
      return groupName;
   }
   
   public String getGroupPassword()
   {
      return groupPassword;
   }
   
   public String getStreetAddress()
   {
      return streetAddress;
   }
   
   public String getCity()
   {
      return city;
   }
   
   public String getState()
   {
      return state;
   }
   
   public String getZip()
   {
      return zip;
   }
   
   public String getcontactNo()
   {
      return contactNo;
   }     
}
