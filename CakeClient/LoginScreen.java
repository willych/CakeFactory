import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginScreen 
{
   JFrame loginScreen = new JFrame();
   JLabel lblLoginName = new JLabel ("Username: ");
   JLabel lblGroupName = new JLabel ("Group name: ");
   JLabel lblPassword = new JLabel ("Group password: ");
   JTextField txtLoginName = new JTextField();
   JTextField txtGroupName = new JTextField();
   JPasswordField txtPassword = new JPasswordField();
   CakeFactoryClient cfc; 
   JButton btnEnterGroup = new JButton("Enter Group");  
   String aGroupName;

   public LoginScreen(CakeFactoryClient _cfc)
   {
       cfc = _cfc;
       //new Listener(btnEnterGroup, txtLoginName, txtGroupName, txtPassword, cfc);
       new Listener(this, cfc);
       initialize();     
   }
   
   public void initialize()
   {
      loginScreen.setTitle("Cake Factory - Login");
      loginScreen.setSize(450,450);
      loginScreen.setLayout(new BorderLayout());     
      loginScreen.setLocationRelativeTo(null);
      loginScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      loginScreen.setVisible(true);
      
      JLabel lblLogo = new JLabel("Cake Factory! :-D",SwingConstants.CENTER);
      setFont(lblLogo);
      JLabel lblLogin = new JLabel("Enter login details ",SwingConstants.CENTER);
         
      JPanel logoPanel = new JPanel();
      logoPanel.setLayout(new GridLayout(2,0));
      logoPanel.add(lblLogo);
      logoPanel.add(lblLogin);
      
      JPanel infoPanel = new JPanel(new GridLayout(3, 0));
      infoPanel.add(lblLoginName);
      infoPanel.add(txtLoginName);
      infoPanel.add(lblGroupName);
      infoPanel.add(txtGroupName);
      txtGroupName.setEditable(false);
      aGroupName = CakeFactoryClient.groupListBox.getSelectedValue().toString();
      txtGroupName.setText(aGroupName);
      
      infoPanel.add(lblPassword);
      infoPanel.add(txtPassword);
      
      JPanel btnPanel = new JPanel();
      btnPanel.add(btnEnterGroup);
           
      loginScreen.add(logoPanel, BorderLayout.NORTH);
      loginScreen.add(infoPanel, BorderLayout.CENTER);
      loginScreen.add(btnPanel, BorderLayout.SOUTH);
      
      loginScreen.pack();
      
   }
   
   public void setFont(JLabel _lblLogo)
   {
      Font font = new Font("Comic Sans MS", Font.ITALIC, 25);
      _lblLogo.setFont(font); 
      _lblLogo.setForeground(Color.ORANGE);
   }
}//end of class