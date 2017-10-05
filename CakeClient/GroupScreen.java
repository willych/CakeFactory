import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.util.*;
import java.net.*;
import java.io.*;

public class GroupScreen
{  
   //Global GUI Variables
   JFrame groupScreen = new JFrame();
   
   JLabel lblCreateGroup = new JLabel ("Create a Group", SwingConstants.CENTER);
   JLabel lblGroupName = new JLabel("Group name:");
   JLabel lblGroupPassword = new JLabel("Group password:");
   JLabel lblReenter = new JLabel("Reenter password:");
   JLabel lblstAddress = new JLabel("Street Address:");
   JLabel lblCity = new JLabel("City:");
   JLabel lblState = new JLabel("State:");
   JLabel lblZip = new JLabel("Zip:");
   JLabel lblNumber = new JLabel("Contact number:");
   JLabel lblTime = new JLabel("Time Limit");
   JButton btnCreate = new JButton("Create Group");
      
   JTextField txtGroupName = new JTextField();
   JPasswordField txtGroupPassword = new JPasswordField();
   JPasswordField txtReenter = new JPasswordField();
   JTextField txtstAddress = new JTextField();
   JTextField txtCity = new JTextField();
   JTextField txtZip = new JTextField();
   JTextField txtNumber = new JTextField(10);
   JTextField txtTime = new JTextField();
   
   JTextField txtLeader = new JTextField();
   JLabel lblLeader = new JLabel ("Leader");
       
   String divider = "----------";
   String[] statesList = {"Select a state",divider,"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI",
      "MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
   JComboBox cbxState = new JComboBox<String>(statesList);
   
   //Client network Variables
   Socket socket;
   String host = "localhost";
   public ObjectOutputStream output ;
   public ObjectInputStream input;
   CakeFactoryClient cfc;
          
   public GroupScreen(CakeFactoryClient _cfc)
   {  
      cfc = _cfc;
     //output = _output;
     //input = _input;
     initialize();              
   }
   
                  
   public void initialize()
   {    
      groupScreen.setTitle("Cake Factory - New Group");
      groupScreen.setSize(450,450);
      groupScreen.setLayout(new BorderLayout());     
      groupScreen.setLocationRelativeTo(null);
      groupScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      groupScreen.setVisible(true);
         
      JLabel lblLogo = new JLabel("Cake Factory! :-D",SwingConstants.CENTER);
      setFont(lblLogo);
         
      JPanel logoPanel2 = new JPanel();
      logoPanel2.setLayout(new GridLayout(2,0));
      logoPanel2.add(lblLogo);
      logoPanel2.add(lblCreateGroup);
        
      JPanel infoPanel = new JPanel();
      infoPanel.setLayout(new GridLayout(10, 0));
      infoPanel.add(lblGroupName);
      infoPanel.add(txtGroupName);
      infoPanel.add(lblGroupPassword);
      infoPanel.add(txtGroupPassword);
      infoPanel.add(lblReenter);
      infoPanel.add(txtReenter);
      infoPanel.add(lblstAddress);
      infoPanel.add(txtstAddress);
      infoPanel.add(lblCity);
      infoPanel.add(txtCity);
      infoPanel.add(lblState);
      infoPanel.add(cbxState);
      infoPanel.add(lblZip);
      infoPanel.add(txtZip);
      infoPanel.add(lblNumber);
      infoPanel.add(txtNumber);
      infoPanel.add(lblLeader);
      infoPanel.add(txtLeader);
      infoPanel.add(lblTime);
      infoPanel.add(txtTime);
         
      JPanel btnPanel = new JPanel();
      btnPanel.add(btnCreate);
         
      groupScreen.add(logoPanel2, BorderLayout.NORTH);
      groupScreen.add(infoPanel, BorderLayout.CENTER);
      groupScreen.add(btnPanel, BorderLayout.SOUTH);
         
      groupScreen.pack(); 
      btnCreate.addActionListener(new Listener(this));             
   }
   
   public void setFont(JLabel _lblLogo)
   {
      Font font = new Font("Comic Sans MS", Font.ITALIC, 25);
      _lblLogo.setFont(font); 
      _lblLogo.setForeground(Color.ORANGE);
   }
   public void disposeFrame()
   {
      groupScreen.dispose();
   }

}