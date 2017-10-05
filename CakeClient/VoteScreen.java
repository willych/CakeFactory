import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.Timer;
import java.util.TimerTask;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class VoteScreen
{
   JFrame voteFrame = new JFrame();
   JPanel pnlVote = new JPanel();
   JTextArea chatArea = new JTextArea(20,30);
   JTextArea msgArea = new JTextArea(2,20);
   JTextArea usersArea = new JTextArea(22,20);
   
   JButton btnSend = new JButton("Send");
   JButton btnSendVotes = new JButton("Send Votes");
   JMenuBar menuBar = new JMenuBar();
   JMenu fileMenu = new JMenu("File");
   JMenuItem cakeMItem = new JMenuItem("Cake Menu");
   JMenuItem exitMItem = new JMenuItem("Exit");
   JLabel lblTimeLeft = new JLabel("Time Left:");
   JLabel lblUsers = new JLabel("Connected Users");
   JLabel lblFrosting = new JLabel("Frosting");
   JLabel lblSize = new JLabel("Size");
   JLabel lblFlavor = new JLabel("Flavor");
   JLabel lblComposition = new JLabel("Composition");
   JLabel lblToppings = new JLabel("Toppings");
   JRadioButton chocoFrosting = new JRadioButton("Chocolate Frosting");
   JRadioButton strawFrosting = new JRadioButton("Strawberry Frosting");
   JRadioButton butterFrosting = new JRadioButton("Butterscotch Frosting");
   JRadioButton smallSize = new JRadioButton("Small");
   JRadioButton mediumSize = new JRadioButton("Medium");
   JRadioButton largeSize = new JRadioButton("Large");
   JRadioButton chocolateFlavor = new JRadioButton("Chocolate");
   JRadioButton strawberryFlavor = new JRadioButton("Strawberry");
   JRadioButton vanillaFlavor = new JRadioButton("Vanilla");
   String[] strComp = {"","Regular Cake", "Ice Cream Cake", "Cheese Cake", "Strawberry Cake", "Chocolate Cake" };
   JComboBox<String> compList = new JComboBox<>(strComp);
   JRadioButton strawberryTop = new JRadioButton("Strawberry Toppings");
   JRadioButton fruitTop = new JRadioButton("Fruit Toppings");
   ButtonGroup frostingGroup = new ButtonGroup();
   ButtonGroup sizeGroup = new ButtonGroup();
   ButtonGroup flavorGroup = new ButtonGroup();
   ButtonGroup toppingsGroup = new ButtonGroup();
   ArrayList userArray = new ArrayList<String>();
   long endTime;
   
   CakeFactoryClient cfc; 
   public ObjectOutputStream output;
   public ObjectInputStream input;
   String clientName;

   public VoteScreen(CakeFactoryClient _cfc, String _clientName)
   {
       cfc = _cfc;
       cfc.setVisible(false);
       input = cfc.input;
       output = cfc.output;
       boolean keepRunning2 = true;
       while(keepRunning2)
       {
       try{
       Object inputObject = input.readObject();
       if (inputObject instanceof Long)
       {
         endTime = (long)inputObject;  
         System.out.println("time" + endTime);
         keepRunning2=false;
         
       }
       else
       {
         System.out.println("dont work");
       }
       }
       catch(Exception ioe)
       {
       
       }
       }
       clientName = _clientName;
       initialize(); 
       ThreadedClient tc = new ThreadedClient();
       tc.start();    
   }
   public void makeVisible()
   {
      cfc.setVisible(true);
      
   }
   public void initialize()
   {
      voteFrame.setTitle("Cake Factory Group");
      voteFrame.setSize(300,300);
      voteFrame.setLayout(new BorderLayout());     
      voteFrame.setLocationRelativeTo(null);
      voteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      voteFrame.setVisible(true);
      //voteFrame.setJMenuBar(menuBar);
      
      fileMenu.add(cakeMItem);
      fileMenu.add(exitMItem);
      menuBar.add(fileMenu);
      
      cakeMItem.addActionListener(new Listener(this));
      exitMItem.addActionListener(new Listener(this));
      compList.setSelectedIndex(0);
      
      frostingGroup.add(chocoFrosting);
      frostingGroup.add(strawFrosting);
      frostingGroup.add(butterFrosting);
   
      sizeGroup.add(smallSize);
      sizeGroup.add(mediumSize);
      sizeGroup.add(largeSize);
   
      flavorGroup.add(chocolateFlavor);
      flavorGroup.add(strawberryFlavor);
      flavorGroup.add(vanillaFlavor);
      
      toppingsGroup.add(strawberryTop);
      toppingsGroup.add(fruitTop);
      
      JPanel pnlChat = new JPanel();
      pnlChat.setLayout(new BorderLayout());
      
      pnlVote.setLayout(new GridLayout(0,1));
      //Time Area
      JPanel timePanel = new JPanel();
      timePanel.add(lblTimeLeft);
      timePanel.add(new Countdown());
      
      //timePanel.add(lblCurrTime);
      
      //Chat area
      JPanel chatPanel = new JPanel();
      chatArea.setEditable(false);
      chatArea.setLineWrap(true);
      chatArea.setWrapStyleWord(true);
      chatPanel.add(chatArea);
      JScrollPane chatScroll = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      chatPanel.add(chatScroll);
      
      //Message box
      JPanel msgPanel = new JPanel();
      msgArea.setLineWrap(true);
      msgArea.setWrapStyleWord(true);
      msgArea.requestFocus();
      msgPanel.add(msgArea);       
      JScrollPane msgScroll = new JScrollPane(msgArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      msgPanel.add(msgScroll);
      msgPanel.add(btnSend);
      
      btnSend.addActionListener(new Listener(this));
      btnSendVotes.addActionListener(new Listener(this));
      
      pnlChat.add(chatPanel, BorderLayout.NORTH);
      pnlChat.add(msgPanel, BorderLayout.SOUTH);
      
      //Voting Panel
      pnlVote.add(lblFrosting);
      JPanel frostPanel = new JPanel();
      frostPanel.add(chocoFrosting);
      frostPanel.add(strawFrosting);
      frostPanel.add(butterFrosting);
      pnlVote.add(frostPanel);
      
      pnlVote.add(lblSize);
      JPanel sizePanel = new JPanel();
      sizePanel.add(smallSize);
      sizePanel.add(mediumSize);
      sizePanel.add(largeSize);
      pnlVote.add(sizePanel);
      
      pnlVote.add(lblFlavor);
      JPanel flavorPanel = new JPanel();
      flavorPanel.add(chocolateFlavor);
      flavorPanel.add(strawberryFlavor);
      flavorPanel.add(vanillaFlavor);
      pnlVote.add(flavorPanel);
      
      pnlVote.add(lblComposition);
      pnlVote.add(compList);
      
      pnlVote.add(lblToppings);
      JPanel toppingsPanel = new JPanel();
      toppingsPanel.add(strawberryTop);
      toppingsPanel.add(fruitTop);
      pnlVote.add(toppingsPanel);
      
      pnlVote.add(btnSendVotes);
      
      JPanel pnlUsers = new JPanel();
      pnlUsers.setLayout(new BorderLayout());
      JPanel usersPanel = new JPanel();
      usersArea.setEditable(false);
      usersArea.setLineWrap(true);
      usersArea.setWrapStyleWord(true);
       
      JScrollPane userScroll = new JScrollPane(usersArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
      JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      usersPanel.add(userScroll);
      pnlUsers.add(usersPanel, BorderLayout.SOUTH);
      pnlUsers.add(lblUsers, BorderLayout.NORTH);
      
      voteFrame.add(timePanel, BorderLayout.NORTH);
      voteFrame.add(pnlChat, BorderLayout.WEST);
      voteFrame.add(pnlVote, BorderLayout.CENTER);
      voteFrame.add(pnlUsers, BorderLayout.EAST);
      voteFrame.pack();
   }
   
    protected class Countdown extends JPanel
    {
         protected JLabel timeLabel;
         protected int interval = 1000;
         
         protected Timer timer;
         
         public Countdown()
         {
               timeLabel = new JLabel();
               timeLabel.setFont(new Font(" ", Font.BOLD, 11));
               timeLabel.setForeground(Color.RED);
               this.add(timeLabel);
                            
               timer = new Timer(interval,
                  new ActionListener()
                  {
                     public void actionPerformed(ActionEvent e)
                     {
                        doCount();
                     }
                  });              
               timer.setRepeats(true);        
               timer.setInitialDelay(0); 
               timer.start();                   
         }
         
          public void doCount()
          {
             long lnTimeLeft = endTime - new Date().getTime();
             int seconds = (int) (lnTimeLeft/1000) % 60;
             int minutes = (int) lnTimeLeft/60000;
             
             /**
             if(seconds <= 0)
             {
               seconds = 60;
               minutes--;
             }
             
             
             seconds--;
             */
             String theTime = String.format("%d:%02d", minutes, seconds);            
             timeLabel.setText(theTime);
             
             if(lnTimeLeft <= 0)
             {
                 timer.stop();
                 disableVotes();
             }      
          }    
    }//end of Countdown class


   public void sendText()
   {
      try
      {
         String outMessage = msgArea.getText(); 
                             
         Message msgInfo = new Message(clientName, outMessage);
         output.writeObject(msgInfo);
         output.flush();
         System.out.println("message sent");
         msgArea.setText("");
         msgArea.requestFocus();
      }
      catch(UnknownHostException uhe)
      {
         JOptionPane.showMessageDialog(null, "Unable to connect to specified host", "Message", 
            JOptionPane.INFORMATION_MESSAGE);
      }
      catch(SocketException se)
      {
         JOptionPane.showMessageDialog(null, "Message cannot be sent \nServer is now unavailable", "Sending Failed", 
            JOptionPane.INFORMATION_MESSAGE);
      }
      catch(IOException ioe)
      {
         JOptionPane.showMessageDialog(null, "IOExceptioneewrfew communicating with host", "Message", 
            JOptionPane.INFORMATION_MESSAGE);     
      }
      catch(NullPointerException npe)
      {
         JOptionPane.showMessageDialog(null, "NullPointerException communicating with host", "Message", 
            JOptionPane.INFORMATION_MESSAGE);  
      }                       
   } 
   public void sendVotes()
   {
      try
      {
         String icing = "";
         String size = "";
         String cakeFlav = "";
         String cakeComp = "";
         String toppings = "";

         if(frostingGroup.getSelection()==null)
         {
            JOptionPane.showMessageDialog(null, "Please select a frosting.", "Error", JOptionPane.ERROR_MESSAGE);
         }
         else if(sizeGroup.getSelection()==null)
         {
            JOptionPane.showMessageDialog(null, "Please select a size.", "Error", JOptionPane.ERROR_MESSAGE);
         }
         else if(flavorGroup.getSelection()==null)
         {
            JOptionPane.showMessageDialog(null, "Please select a flavor.", "Error", JOptionPane.ERROR_MESSAGE);
         }
         else if((String)compList.getSelectedItem()=="")
         {
            JOptionPane.showMessageDialog(null, "Please select a composition.", "Error", JOptionPane.ERROR_MESSAGE);
         }
         else if(toppingsGroup.getSelection()==null)
         {
            JOptionPane.showMessageDialog(null, "Please select a topping.", "Error", JOptionPane.ERROR_MESSAGE);
         }
         else{
           for (Enumeration<AbstractButton> buttons = frostingGroup.getElements(); buttons.hasMoreElements();) {
               AbstractButton button = buttons.nextElement();
               if (button.isSelected()) {
                   icing = button.getText();
               }
           }
           for (Enumeration<AbstractButton> buttons = sizeGroup.getElements(); buttons.hasMoreElements();) {
               AbstractButton button = buttons.nextElement();
               if (button.isSelected()) {
                   size = button.getText();
               }
           }
           for (Enumeration<AbstractButton> buttons = flavorGroup.getElements(); buttons.hasMoreElements();) {
               AbstractButton button = buttons.nextElement();
               if (button.isSelected()) {
                   cakeFlav = button.getText();
               }
           }
           cakeComp = (String)compList.getSelectedItem();
           for (Enumeration<AbstractButton> buttons = toppingsGroup.getElements(); buttons.hasMoreElements();) {
               AbstractButton button = buttons.nextElement();
               if (button.isSelected()) {
                   toppings = button.getText();
               }
           }
           Vote votesObject = new Vote(icing,size,cakeFlav,cakeComp,toppings);
           output.writeObject(votesObject);
           output.flush();
           disableVotes();
                    
           JOptionPane.showMessageDialog(null, "Votes Sent", "Message", 
              JOptionPane.INFORMATION_MESSAGE);
          /*  
           boolean keepRunning = true;  
           while(keepRunning)
             {
               Object replyMessage = input.readObject();
               if(replyMessage instanceof String)
               {
                  String replyString = (String)replyMessage;
                  if(replyString.equals(StringMessages.APPROVED))
                  {
                     System.out.println("works");
                     keepRunning = false;
                  }
                  else if(replyString.equals(StringMessages.DENIED))
                  {
                     JOptionPane.showMessageDialog(null, "ERROR", "Error", JOptionPane.ERROR_MESSAGE);
                     keepRunning = false;
                  }
               }
             }
             */
        }  
      }
      catch(UnknownHostException uhe)
      {
         JOptionPane.showMessageDialog(null, "Unable to connect to specified host", "Message", 
            JOptionPane.INFORMATION_MESSAGE);
      }
      catch(SocketException se)
      {
         JOptionPane.showMessageDialog(null, "Message cannot be sent \nServer is now unavailable", "Sending Failed", 
            JOptionPane.INFORMATION_MESSAGE);
      }
      catch(IOException ioe)
      {
         JOptionPane.showMessageDialog(null, "IOExceptioneewrfew communicating with host", "Message", 
            JOptionPane.INFORMATION_MESSAGE);     
      }
      catch(NullPointerException npe)
      {
         JOptionPane.showMessageDialog(null, "NullPointerException communicating with host", "Message", 
            JOptionPane.INFORMATION_MESSAGE);  
      }
      /*
      catch(ClassNotFoundException cnfe)
      {
         System.out.println("Class not found");
      } 
      */                    
   }
   
   public void disableVotes()
   {
           chocoFrosting.setEnabled(false);
           strawFrosting.setEnabled(false);
           butterFrosting.setEnabled(false);
           smallSize.setEnabled(false);
           mediumSize.setEnabled(false);
           largeSize.setEnabled(false);
           chocolateFlavor.setEnabled(false);
           strawberryFlavor.setEnabled(false);
           vanillaFlavor.setEnabled(false);
           compList.setEnabled(false);
           strawberryTop.setEnabled(false);
           fruitTop.setEnabled(false);
           btnSendVotes.setEnabled(false);
             
   }
   
   public class ThreadedClient extends Thread
      {
         public ThreadedClient()
         { 
         }
         public void run()
         {
            boolean keepRunning = true;
            try
            {
              while(keepRunning)
               {
                  Object inputObject;
                  inputObject = input.readObject();
                  System.out.println("reading object");
                  if(inputObject instanceof Message)
                  {
                     Message sender = (Message)inputObject;
                     chatArea.append(sender.getName() + ":  " + sender.getMessage() + "\n");
                  } 

                  else if(inputObject instanceof ArrayList)
                  {
                     StringBuilder builder = new StringBuilder();
                   
                     userArray = (ArrayList)inputObject;
                     for(int i=0;i<userArray.size();i++)
                     {
                        builder.append("\n");
                        builder.append(userArray.get(i));
                     }
                     String strUsers = builder.toString();
                     int length = usersArea.getText().length();
                     usersArea.replaceRange(strUsers,0,length);
                  }
               }        
            }
            catch(UnknownHostException uhe)
            {
                JOptionPane.showMessageDialog(null, "Unable to connect to specified host", "Message", 
                    JOptionPane.INFORMATION_MESSAGE);   
            }
            catch(SocketException se)
            {
                JOptionPane.showMessageDialog(null, "Currently not connected", "Message", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            }
            catch(IOException ioe)
            {
                System.out.println(input);
                JOptionPane.showMessageDialog(null, "IOException communicating with host", "Message", 
                    JOptionPane.INFORMATION_MESSAGE);     
            } 
            catch(Exception e)
            {
               System.out.println("Exception detected \n" + e.getMessage());
            }
         }
      }
   
}//End of VoteScreen