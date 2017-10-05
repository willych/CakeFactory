import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.util.*;
import java.net.*;
import java.io.*;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class CakeFactoryClient extends JFrame
{  
   //Vector of groups
   Vector<String> groupNamesList = new Vector<String>();
   
   //GUI variables
   static JList groupListBox;
      
   //Network Variable
   Socket socket;
   String host = "localhost"; //for now
   int port = 16789;
   ObjectOutputStream output ;
   ObjectInputStream input;
   
   JFrame spFrame = new JFrame();
   JPanel pnlHost = new JPanel();
   JPanel pnlPort = new JPanel();
   JLabel lblHost = new JLabel("Host:");
   //JLabel lblPort = new JLabel("Port:");
   JTextArea txtHost = new JTextArea(1,9);
   //JTextArea txtPort = new JTextArea(1,9);
   JButton btnSendSP = new JButton("Send Socket");

   public CakeFactoryClient()
   {     
      spFrame.setLayout(new GridLayout(2,0));
      spFrame.setVisible(true);
      spFrame.setLocationRelativeTo(null);
      spFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
         
      pnlHost.add(lblHost);
      pnlHost.add(txtHost);
      //pnlPort.add(lblPort);
      //pnlPort.add(txtPort);
         
      spFrame.add(pnlHost);
      //spFrame.add(pnlPort);
      spFrame.add(btnSendSP);
      spFrame.pack();
         
      btnSendSP.addActionListener(new Listener(this));
   }
   
   public void start(String _host)//,String _port)
   {
      try
      {
         host = _host;
         port = 16789; //Integer.parseInt(_port);
         socket = new Socket(host, port);
         output = new ObjectOutputStream(socket.getOutputStream());
         input = new ObjectInputStream(socket.getInputStream());
         initializeClient(); 
         System.out.println("Main screen connected!");
         
         output.writeObject(StringMessages.REFRESH);
         Object list = input.readObject();
         groupNamesList = (Vector<String>) list;
         System.out.println(list);

         groupListBox.validate();
      }
      catch(UnknownHostException uhe)
      {
         JOptionPane.showMessageDialog(null, "Host not known", "Error", 
            JOptionPane.ERROR_MESSAGE);
      }
      catch(SocketException se)
      { 
         JOptionPane.showMessageDialog(null, "Server is currently unavailable", "Error", 
            JOptionPane.ERROR_MESSAGE); 
      }
      catch(IOException ioe)
      {
         System.out.println("IO Exception occurred.");
      }
      catch(Exception e)
      {
         System.out.println("Exception occurred");
         e.printStackTrace();
      }              
   }
   
   public void initializeClient()
   {
      //initial frame setup
      this.setTitle("Cake Client");
      this.setSize(500, 400);
      this.setLayout(new BorderLayout());
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(EXIT_ON_CLOSE);
                        
      //Logo text/graphics   
      JLabel lblLogo = new JLabel("Cake Factory! :-D",SwingConstants.CENTER);
      setFont(lblLogo);        
      JPanel logoPanel = new JPanel();
      logoPanel.setLayout(new GridLayout(1,0));
      logoPanel.add(lblLogo);
            
      //Buttons
      JButton btnCreateGroup = new JButton("Create New Group");
      JButton btnRefresh = new JButton("Refresh");
      JButton btnJoin = new JButton("Join");
      
      //Listbox and its components
      groupListBox = new JList(groupNamesList);
      groupListBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      groupListBox.setLayoutOrientation(JList.VERTICAL); 
      JScrollPane listScroller = new JScrollPane(groupListBox);
      listScroller.setPreferredSize(new Dimension(20, 20));
      Border border = BorderFactory.createLineBorder(Color.orange);
      groupListBox.setBorder(border);
      
      //Middle panel
      JLabel lblSelect = new JLabel("Please select a group to join: ", SwingConstants.CENTER);       
      JPanel midPanel = new JPanel(new BorderLayout());
      JPanel buttonsPanel = new JPanel();      
      buttonsPanel.add(btnCreateGroup);
      buttonsPanel.add(btnRefresh);
      midPanel.add(buttonsPanel, BorderLayout.NORTH);
      midPanel.add(lblSelect, BorderLayout.CENTER);    
      midPanel.add(groupListBox, BorderLayout.SOUTH);
      
      //Join Button panel
      JPanel joinPanel = new JPanel();
      joinPanel.add(btnJoin);
     
      //Listeners
      //new Listener(btnCreateGroup,output,input);
      btnCreateGroup.addActionListener(new Listener(this));
      btnRefresh.addActionListener(new Listener(this));
      //new Listener(btnJoin);
      btnJoin.addActionListener(new Listener(this));
     
      btnRefresh.doClick();

      this.add(lblLogo, BorderLayout.NORTH);
      this.add(midPanel, BorderLayout.CENTER);
      this.add(joinPanel, BorderLayout.SOUTH);
      
      //final frame set up 
       try //this block is to automatically set the look and feel of swing components according to system's theme
       {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       } catch (Exception e1) {
         System.err.println("Exception occurred while getting system's UI \n");
         e1.printStackTrace();
       }
       SwingUtilities.updateComponentTreeUI(this);
  
      this.setVisible(true);
      this.pack();     
   }
        
   public void setFont(JLabel _lblLogo)
   {
      Font font = new Font("Comic Sans MS", Font.ITALIC, 25);
      _lblLogo.setFont(font); 
      _lblLogo.setForeground(Color.ORANGE);
   }
   public void hideMainFrame()
   {
      this.setVisible(false);
   }
   public void showMainFrame()
   {
      this.setVisible(true);
   }
   
   public static void main(String[] args)
   {
      new CakeFactoryClient();
   }//end of main

}//end of class