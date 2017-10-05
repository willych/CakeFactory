import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.net.*;

//Use settings file
//.properties 
public class Listener implements ActionListener
{
   //Global Variables
   JButton button;
   JMenuItem menu;
   CakeFactoryClient cfc;
   LoginScreen ls;
   
   //Group object variables
   JTextField name;
   JTextField user;
   JTextField address;
   JTextField city;
   JComboBox state;
   JTextField zip;
   JTextField number;
   JPasswordField pass;
   JPasswordField confirmPass;
   JTextField leader;
   JTextField time;
   
   //Login object variables
   JTextField username;
   JTextField groupName;
   JPasswordField groupPass;
   
   //Vectors and list
   static Vector<String> groupNamesList;
   JList groupListBox;
   
   //Client network Variables
   Socket socket;
   String host = "SNOOPY23";
   ObjectOutputStream output ;
   ObjectInputStream input;

   //For validation variables
   String divider = "----------";

   //Parameterized constructor, accepts 
   public Listener(CakeFactoryClient _cfc)
   {
      cfc = _cfc;
      output = cfc.output;
      input = cfc.input;
   }
   //Generic Constructor for Menu
   public Listener(JMenuItem _menu,VoteScreen _vScreen)
   {
      vScreen = _vScreen;
      menu = _menu;
      menu.addActionListener(this);
   }

   //Constructor for login screen
   public Listener(LoginScreen _ls, CakeFactoryClient _cfc)
   {
      cfc = _cfc;
      output = cfc.output;
      input = cfc.input;
      ls = _ls;
      button = ls.btnEnterGroup;
      username = ls.txtLoginName;
      groupName = ls.txtGroupName;
      groupPass = ls.txtPassword;
      button.addActionListener(this);  
   }

   GroupScreen gScreen;
   public Listener(GroupScreen _gScreen)
   {
      gScreen = _gScreen;
      cfc = gScreen.cfc;
      output = cfc.output;
      input = cfc.input;
   }
   VoteScreen vScreen;
   public Listener(VoteScreen _vScreen)
   {
      vScreen = _vScreen;
      output = vScreen.output;
      input = vScreen.input;
   }
     
    //Button Listener  
   public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Create New Group"))
		{
			new GroupScreen(cfc);
		}
      else if (e.getActionCommand().equals("Cake Menu"))
      {

         vScreen.voteFrame.dispose();
         vScreen.makeVisible();          
      }
      else if (e.getActionCommand().equals("Exit"))
      {
         System.exit(0);
      }
		else if (e.getActionCommand().equals("Refresh"))//get arraylist from server
		{
          try
          {              
             Object arrayListNames = null;
             output.writeObject(StringMessages.REFRESH);
             boolean keepRunning = true;
             while(keepRunning)
             {
               
               arrayListNames = input.readObject();
               System.out.println("one");
               if(arrayListNames instanceof Vector)
               {
                  groupNamesList = (Vector<String>) arrayListNames;
                  cfc.groupListBox.setListData(groupNamesList);
                  keepRunning = false;
               }
             }
             arrayListNames = null;
          }
          catch(Exception e2)
          {
            System.out.println("Exception occurred \n");
            e2.printStackTrace();
         }   
		}
      else if(e.getActionCommand().equals("Join"))
      {
         if(CakeFactoryClient.groupListBox.getSelectedIndex()< 0)
         {
            JOptionPane.showMessageDialog(null, "You must select a group name first", "Select Name", 
               JOptionPane.INFORMATION_MESSAGE);
         }
         else
         {
            new LoginScreen(cfc);
         }
      }
      else if(e.getActionCommand().equals("Create Group"))
      {
         try
         {
            String txtName = gScreen.txtGroupName.getText();
            String charPass = String.valueOf(gScreen.txtGroupPassword.getPassword());//parsed to string
            String txtPass = String.valueOf(gScreen.txtReenter.getPassword());
            String txtAddress = gScreen.txtstAddress.getText();
            String txtCity = gScreen.txtCity.getText();
            String txtState = String.valueOf(gScreen.cbxState.getSelectedItem());
            String txtZip = gScreen.txtZip.getText();
            String txtNumber = gScreen.txtNumber.getText();
            String txtLeader = gScreen.txtLeader.getText();
            String txtTime = gScreen.txtTime.getText();
            
            //Validation of all fields
            if(txtName.isEmpty() || txtName.equals(""))
            {
               JOptionPane.showMessageDialog(null, "Please enter a group name.", "Error", JOptionPane.ERROR_MESSAGE);
               name.requestFocus();//highlight too
            }
            else if (txtName.length() < 2)
            {
               JOptionPane.showMessageDialog(null, "Group name must be at least 2 characters.", "Error", JOptionPane.ERROR_MESSAGE);
               name.requestFocus();  
            }
            else if(charPass.isEmpty() || charPass.equals(""))
            {
               JOptionPane.showMessageDialog(null, "Please enter a password.", "Error", JOptionPane.ERROR_MESSAGE);
               pass.requestFocus();
            }
            else if(charPass.length() < 5)
            {
               JOptionPane.showMessageDialog(null, "Password must be at least 5 characters.", "Error", JOptionPane.ERROR_MESSAGE);
               pass.requestFocus();
            }
            else if(!txtPass.equals(charPass))
            {
               JOptionPane.showMessageDialog(null, "Passwords do not match.","Mismatch", JOptionPane.ERROR_MESSAGE);
               confirmPass.requestFocus();
            }
            else if(txtAddress.isEmpty() || txtAddress.equals(""))
            {
               JOptionPane.showMessageDialog(null, "Please enter a street address.", "Error", JOptionPane.ERROR_MESSAGE);
               address.requestFocus();
            }
            else if(txtCity.isEmpty() || txtName.equals(""))
            {
               JOptionPane.showMessageDialog(null, "Please enter a city name.", "Error", JOptionPane.ERROR_MESSAGE);
               city.requestFocus();
            }
            else if(txtState.equals(divider) || txtState.equals("Select a state"))
            {
               JOptionPane.showMessageDialog(null, "Please select a state.", "Select State", JOptionPane.ERROR_MESSAGE);
               state.requestFocus();   
            }
            else if(txtZip.isEmpty() || txtZip.equals("") || txtZip.length() < 5)
            {
                JOptionPane.showMessageDialog(null, "Please enter a valid zip code.", "Invalid Zip", JOptionPane.ERROR_MESSAGE);
                zip.requestFocus();
            }
            else if(txtNumber.isEmpty() || txtNumber.equals("") || txtNumber.length() != 10)
            {
               JOptionPane.showMessageDialog(null, "Please enter a valid contact number.", "Invalid Number", JOptionPane.ERROR_MESSAGE);
               number.requestFocus();
            }
            else if(txtLeader.isEmpty() || txtLeader.equals(""))
            {
               JOptionPane.showMessageDialog(null, "Please enter your leader name.", "Error", JOptionPane.ERROR_MESSAGE);
               leader.requestFocus();
            }
            else if(txtTime.isEmpty() || txtTime.equals(""))
            {
               JOptionPane.showMessageDialog(null, "Please enter a time.", "Error", JOptionPane.ERROR_MESSAGE);
               time.requestFocus();
            }
            else //Create object here
            { 
               long longTime = Long.valueOf(txtTime);
               GroupInfo gi = new GroupInfo(txtName,txtPass,txtAddress,txtCity,txtState,txtZip,txtNumber, txtLeader,longTime);
               System.out.println(txtName + " added");
               try
               {
                  output.writeObject(gi);
                  output.flush();
                  System.out.println("Group Object sent to server!");
               }
               catch(IOException ioe)
               {
                  System.out.println("IOException Occurred");
               }
               JOptionPane.showMessageDialog(null, "Group successfully created!", "Group Created", JOptionPane.INFORMATION_MESSAGE);    
            }
            gScreen.disposeFrame();
                         
             Object replyMessage = null;
             boolean keepRunning = true;
             while(keepRunning)
             {
               replyMessage = input.readObject();
               if(replyMessage instanceof String)
               {
                  String replyString = (String)replyMessage;
                  if(replyString.equals(StringMessages.APPROVED))
                  {
                     keepRunning = false;
                     new VoteScreen(cfc,txtLeader);
                  }
                  else
                  {
                     String errorMessage = replyMessage.toString();
                     JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                     keepRunning = false;
                  }
               }
             
             replyMessage = null;
          }
            
         }        

            
         
         catch(Exception npe)
         {
         
         }
      }
      else if(e.getActionCommand().equals("Enter Group"))
      {
         //get data from each field
         String txtName = username.getText();
         String txtGroupName = groupName.getText();
         String txtPassword = String.valueOf(groupPass.getPassword());
                 
         //validate fields
         if(txtName.isEmpty() || txtName.equals(""))
         {
            JOptionPane.showMessageDialog(null, "Please enter a username", "Error", JOptionPane.ERROR_MESSAGE);
            username.requestFocus();   
         }
         else if(txtPassword.isEmpty() || txtPassword.equals(""))
         {
            JOptionPane.showMessageDialog(null, "Please enter a valid group password.", "Error", JOptionPane.ERROR_MESSAGE);
            groupPass.requestFocus();
         }
         
         else //make joingroup object and send to server here
         {
            JoinGroup aJoinGroup = new JoinGroup(txtName, txtGroupName, txtPassword);
            System.out.println("two");
            try
            {
               output.writeObject(aJoinGroup);
               output.flush();
               System.out.println("Join Group Object sent to server!");
            }
            catch(IOException ioe)
            {
               System.out.println("IOException Occurred");
            }
            username.setText("");
            groupPass.setText("");
            
            try
            {              
             Object replyMessage = null;
             boolean keepRunning = true;
             while(keepRunning)
             {
               replyMessage = input.readObject();
               if(replyMessage instanceof String)
               {
                  String replyString = (String)replyMessage;
                  if(replyString.equals(StringMessages.APPROVED))
                  {
                     keepRunning = false;
                     ls.loginScreen.dispose();
                     new VoteScreen(cfc,txtName);
                  }
                  else
                  {
                     String errorMessage = replyMessage.toString();
                     JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                     keepRunning = false;
                  }
               }
             }
             replyMessage = null;
          }
          catch(Exception e2)
          {
            System.out.println("Exception occurred \n");
            e2.printStackTrace();
          }   
         }         
      } 
      else if (e.getActionCommand().equals("Send"))
      {
         vScreen.sendText();
      }  
      else if (e.getActionCommand().equals("Send Votes"))
      {
         vScreen.sendVotes();
      }    
      else if (e.getActionCommand().equals("Send Socket"))
      {
         String txtHost = cfc.txtHost.getText();
         //String txtPort = cfc.txtPort.getText();
                 
         //validate fields
         if(txtHost.isEmpty() || txtHost.equals(""))
         {
            JOptionPane.showMessageDialog(null, "Please enter a host.", "Error", JOptionPane.ERROR_MESSAGE);
            cfc.txtHost.requestFocus();   
         }
         /*
         else if(txtPort.isEmpty() || txtPort.equals(""))
         {
            JOptionPane.showMessageDialog(null, "Please enter a port.", "Error", JOptionPane.ERROR_MESSAGE);
            cfc.txtPort.requestFocus();
         }
         */
         else
         {
            cfc.spFrame.dispose();
            cfc.start(txtHost); //,txtPort);
         }
 
      }    
   } 
}