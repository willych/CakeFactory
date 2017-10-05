/**
A multi threaded server program to manage instant messaging between multiple
chat clients

Authors:
Willy Choi
Dervent Wright
Andrew Marone
Abdulaziz Alomari
*/
import java.net.*;
import java.io.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class CakeServer
{
   
   //GUI Attributes
   JTextArea usersArea = new JTextArea(20,30);
   JTextArea groupsArea = new JTextArea(20,30);
   JTextArea messagesArea = new JTextArea(20,30);
   JLabel lblUsers = new JLabel("Users");
   JLabel lblGroups = new JLabel("Groups");
   JLabel lblMessages = new JLabel("Messages");
   
   //Hashtable to store groups
   Hashtable<String,CakeGroup> groups = new Hashtable<String,CakeGroup>();
   //Array list to hold references to clients
   ArrayList<ThreadedServer> allUsers =  new ArrayList<ThreadedServer>();
   //Objects reserved for synchronization
   Object keeper = new Object();
   Object userkeeper = new Object();  
   public final String USERS = "users";
   public final String GROUPS = "groups";
   public final String MESSAGES = "messages";
   public static void main(String[] args)
   {
      new CakeServer();
   }
   
   /**
      Creates new thread for each client that connects to the server
   */
   public CakeServer()
   {
      ServerSocket ss = null;  
      initializeGUI();
		try {
			System.out.println("getLocalHost: "+InetAddress.getLocalHost() );
		    System.out.println("getByName:    "+InetAddress.getByName("localhost") );

		    ss = new ServerSocket(16789);
		    Socket cs = null;
            //Loops to create multiple threads
		    while(true){ 
				cs = ss.accept(); 				
				ThreadedServer ts = new ThreadedServer(cs,this);
                String name = ts.getName();
				printGUI(name + "'s Client Connected!");
				ts.start(); 
			} 
		}
		catch(BindException be ) {
			System.out.println("Server already running on this computer, stopping.");
		}
		catch( IOException ioe ) {
			System.out.println("IO Error");
		}
   }
   /**
      Initialize server GUI
   */
   public void initializeGUI()
   {
		JFrame serverFrame = new JFrame();
      
		serverFrame.setTitle("Server GUI");
		serverFrame.setSize(450,450);
		serverFrame.setLayout(new BorderLayout());     
		serverFrame.setLocationRelativeTo(null);
		serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		serverFrame.setVisible(true);
      
		//Users area
		JPanel usersPanel = new JPanel();
		usersArea.setEditable(false);
		usersArea.setLineWrap(true);
		usersArea.setWrapStyleWord(true);
		usersPanel.add(lblUsers);
		usersPanel.add(usersArea);
		JScrollPane usersScroll = new JScrollPane(usersArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		usersPanel.add(usersScroll);

		//Groups area
		JPanel groupsPanel = new JPanel();
		groupsArea.setEditable(false);
		groupsArea.setLineWrap(true);
		groupsArea.setWrapStyleWord(true);
		groupsArea.add(lblGroups);
		groupsPanel.add(groupsArea);
		JScrollPane groupsScroll = new JScrollPane(groupsArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		groupsPanel.add(groupsScroll);
      
		//Messages Area
		JPanel messagesPanel = new JPanel();
		messagesArea.setEditable(false);
		messagesArea.setLineWrap(true);
		messagesArea.setWrapStyleWord(true);
		messagesPanel.add(lblMessages);
		messagesPanel.add(messagesArea);
		JScrollPane messagesScroll = new JScrollPane(messagesArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		messagesPanel.add(messagesScroll);

		serverFrame.add(usersPanel,BorderLayout.WEST);
		serverFrame.add(groupsPanel,BorderLayout.CENTER);
		serverFrame.add(messagesPanel,BorderLayout.EAST);
		serverFrame.pack();
   }
   
   /**
      Print String into selected server GUI field
   */
   public void printGUI(String message)
   {
        messagesArea.append(message);
        messagesArea.append(String.format("%n"));
   }
   
   public void refreshUsers()
   {
		usersArea.setText("");
		for(int i =0;i<allUsers.size();i++)
		{
			usersArea.append(allUsers.get(i).username +" -  of the Group " + allUsers.get(i).groupName);
			usersArea.append(String.format("%n"));
         System.out.println(allUsers.get(i).username +" -  of the Group " + allUsers.get(i).groupName);
		}
      
   }
   public void refreshGroups()
		{
            
            groupsArea.setText("");
            if(!groups.isEmpty())
            {
            Enumeration<String> _keys = groups.keys();
                                       
            while(_keys.hasMoreElements())
            {
               String current = _keys.nextElement();
               if(groups.get(current).activeGroup)
               {
               System.out.println();
               groupsArea.append(String.format("%s%n",current));
               }   
            } 
            }  
		}
   
	/**
      Inner class, a thread that listens for input from an individual client
	*/
	class ThreadedServer extends Thread{
		CakeServer self;
      private Socket cs;
		boolean connected = true;
		boolean initializing = true;
		boolean voted = false;
		String username;
		String groupName = null;
		ObjectInputStream ois;
		ObjectOutputStream oos;
      
		/**
         Constructor for Threaded Server
         Sets client socket variable
		*/
		public ThreadedServer( Socket cs,CakeServer _self ) {
			self = _self;
         this.cs = cs;
			//allUsers.add(this);
		}
      
		/**
         returns username
		*/
		public String getUsername()
		{
			return username;
		}
      
		/**
			Returns cs
		*/
		public Socket getCs()
		{
			return cs;
		}
		
		public boolean isConnected()
		{
			return connected;
		}
      
		
		/**
		Removes user from list of current groups
		notifies group that user is disconnected
		removes user from group user list
		*/
		public void disconnect()
		{
			synchronized(keeper)
			{      
            Thread t = Thread.currentThread();
            String name = t.getName();
			   printGUI(name + "'s Connection Lost"); 
            connected = false;
            if(groupName != null)
            {
               synchronized(keeper)
				   {
               int index = -1;
					for(int i = 0;i < groups.get(groupName).members.size();i++)
					{
						if(groups.get(groupName).members.get(i).getCs() == cs)
						{
							index = i;
                     
                     //groups.get(groupName).members.remove(i);
						}
                  }
                  if(index != -1)
                  {
                     groups.get(groupName).leaveGroup(index,groupName);
                  }
                  //writeToGroup(new Message(username,"is now disconnected from the group"));
                  //sendUsers();
                  //refreshUsers();
				   }
              
            }
            synchronized(userkeeper)
            {
               int index = -1;
               for(int i=0;i<allUsers.size();i++)
               {  
					if(allUsers.get(i).getCs() == cs)
					{
						System.out.println("cs Matches");
                  System.out.println(allUsers.size());
                  index = i;
                  
                  
					}
               }
               if(index != -1)
               {
                  allUsers.remove(index);
               }
               refreshUsers();
            }
         }
      }
		/**
         Writes an object to each member of client's group
		*/
		public void writeToGroup(Object  _sendObject)
		{
            Object sendObject = _sendObject;
            //ObjectOutputStream oos;
		    PrintWriter opw;
            for(int i =0;i<groups.get(groupName).members.size();i++)
            {
            System.out.println("Sent0");
               ThreadedServer ts = groups.get(groupName).members.get(i);
               if(ts.isConnected())
               {
					try{
						Socket cs = ts.getCs();
                  System.out.println("Sent1");
			            //ts.oos = new ObjectOutputStream(cs.getOutputStream());
											        
			            ts.oos.writeObject(sendObject);
                     System.out.println("Sent2");
			            ts.oos.flush();
						}
					catch( IOException e ) { 
						System.out.println("IO Error for Sending Message"); 
			        }
               }
            }
      }
      public void writeToGroupProtected(Object obj)
      {
         synchronized(keeper)
         {
            writeToGroup(obj);
         }
      }
   /**
      Sends an object to single thread client
   */
   public void writeToClient(Object sendObject)
   {
      //ObjectOutputStream oos;
      synchronized(keeper)
      {
         try
         {
            //oos = new ObjectOutputStream(cs.getOutputStream());
            oos.writeObject(sendObject);
            oos.flush();
         }
         catch(IOException ioe)
         {
            disconnect();
         }
      }
   } 
   
   public void sendUsers()
   {
      //writeToGroup(StringMessages.SENDING_USERS);
      ArrayList<String> users = new ArrayList<String>();
      //allUsers.add(this);
      //refreshUsers();
      if(groups.get(groupName).members.size() > 0)
      {
         users.add(groups.get(groupName).members.get(0).getUsername());

         for(int i =1;i<groups.get(groupName).members.size();i++)
         {
            users.add(groups.get(groupName).members.get(i).getUsername());
         }
         writeToGroup(users);
      }
   }
      
      /**
         Handles client connection
      */
		public void run() {
			
			String clientMessage;
			Object clientObject;
			try {
				ois = new ObjectInputStream(cs.getInputStream());
				oos = new ObjectOutputStream(cs.getOutputStream());
			 
			while(initializing){		
				clientObject = ois.readObject();	
				System.out.println("gotten");
				//If creating group
				if(clientObject instanceof GroupInfo){      
				 synchronized(keeper)
				 {
				   GroupInfo group = (GroupInfo)clientObject;
				   username = group.leader;
				   
				   if(groups.containsKey(group.getGroupName()))
				   {
					 printGUI("group already exists");
					 oos.writeObject("Group already exists");
					 oos.flush();
				   }
				   else
				   {
					 groups.put(group.groupName,new CakeGroup(group,self));
					 //w CakeGroup(group);
					 groupName = group.groupName;
					 groups.get(group.groupName).members.add(this);
					 oos.writeObject(StringMessages.APPROVED);
					 oos.flush();
                try
                {
                wait(1000);
                
                }
                catch(Exception e)
                {
                
                }
                System.out.println(groups.get(group.groupName).endTime);
                    writeToClient(groups.get(group.groupName).endTime); 
					 refreshGroups();
                allUsers.add(this);
                refreshUsers();
                boolean going = true;
                long end;
                /*
                while(going)
                {
                  end = groups.get(group.groupName).endTime;
                  if(end != null)
                  {
                    System.out.println(groups.get(group.groupName).endTime);
                    writeToClient(groups.get(group.groupName).endTime); 
                    going = false;
                  }   
                
                }
                */
                //oos.flush();
					 initializing = false;
				   }
				 }
				}
				//If joining group
				else if(clientObject instanceof JoinGroup)
				{
				   synchronized(keeper)
				   {
					  System.out.println("JoinGroup object received");
					  JoinGroup join = (JoinGroup)clientObject;
					  System.out.println("Password " +join.password);
					  System.out.println("Username " + join.username);
					  System.out.println("group " + join.groupName);
					  if(groups.containsKey(join.groupName))
					  {
						 if(groups.get(join.groupName).isPassword(join.password))
						 {
                        boolean repeated = false;
                        for(int i = 0; i<groups.get(join.groupName).members.size();i++)
                        {
                           if(join.username.toUpperCase().equals(groups.get(join.groupName).members.get(i).username.toUpperCase()))
                           {
                              repeated = true;
                           }
                        
                        }
                        if(repeated)
                        {
                           oos.writeObject("A user with the name " + join.username + " already exists in this group. Please select a unique username");
							      oos.flush();
                        }
                        else
                        {
                        username = join.username;
							   groups.get(join.groupName).members.add(this);
							   oos.writeObject(StringMessages.APPROVED);
							   oos.flush();
							   groupName = join.groupName;
							   
							   allUsers.add(this);
                       
                         writeToClient(groups.get(join.groupName).endTime);
                         refreshGroups();
                         refreshUsers();
							   initializing = false;
                        }
						 }
						 else
						 {
							oos.writeObject("You have entered an incorrect password");
							oos.flush();
							System.err.println("You have entered an incorrect password");
						 }
					  }
					  else
					  {
						 String errorMessage = join.groupName + " is not an active group";
						 writeToClient(errorMessage);
						 System.err.println(errorMessage);
					  }
					}
				}
					  else if(clientObject instanceof String)
					  {
						 String clientString = (String)clientObject;
						 printGUI(clientString);
						 if(clientString.equals(StringMessages.REFRESH))
						 {
							synchronized(keeper){
                     Enumeration<String> _keys = groups.keys();
						 
							Vector<String> keys = new Vector<String>();
										   
							while(_keys.hasMoreElements())
							{
							   String current = _keys.nextElement();
                        if(groups.get(current).activeGroup)
                        {
                        keys.add(current);
                        }   
							}
							oos.writeObject(keys);
							System.out.println("That Stuff happens");
							oos.flush();
						 }
                   }
					  }
			}
			
           synchronized(keeper)
           {
            sendUsers();
           } 
		   
           //after in group, listen for input. Message of voting object.
           System.out.println("before");
           while(connected)
           {
           System.out.println("after");
            clientObject = ois.readObject();
            //Handle message objects
            if(clientObject instanceof Message)
            {
               Message newMessage = (Message)clientObject;
               synchronized(keeper)
               {
					writeToGroup(newMessage);
               }
            }
            //Handle  Vote objects
            else if(clientObject instanceof Vote)
            {
               synchronized(keeper)
               {
                  if(!voted)
                  {
                     System.out.println("Votes recieved");
                     Vote newVote = (Vote)clientObject;
                  
                     groups.get(groupName).addVote(newVote);
                     voted = true;
                     //writeToGroup(StringMessages.APPROVED);
                  }
                  else
                  {
                     //writeToGroup(StringMessages.DENIED);
                     //writeToClient("You have already voted"); 
                        
                  }
               }
            }
            else if( clientObject instanceof GroupInfo)
            {      
				 synchronized(keeper)
				 {
               //disconnect();
				   GroupInfo group = (GroupInfo)clientObject;
				   username = group.leader;
				   
				   if(groups.containsKey(group.getGroupName()))
				   {
					 printGUI("group already exists");
					 oos.writeObject("Group already exists");
					 oos.flush();
				   }
				   else
				   {
					 groups.put(group.groupName,new CakeGroup(group,self));
					 //w CakeGroup(group);
					 groupName = group.groupName;
					 groups.get(group.groupName).members.add(this);
					 oos.writeObject(StringMessages.APPROVED);
					 oos.flush();
                try
                {
                wait(1000);
                
                }
                catch(Exception e)
                {
                
                }
                System.out.println(groups.get(group.groupName).endTime);
                    writeToClient(groups.get(group.groupName).endTime); 
					 refreshGroups();
                allUsers.add(this);
                refreshUsers();
                boolean going = true;
                long end;
                /*
                while(going)
                {
                  end = groups.get(group.groupName).endTime;
                  if(end != null)
                  {
                    System.out.println(groups.get(group.groupName).endTime);
                    writeToClient(groups.get(group.groupName).endTime); 
                    going = false;
                  }   
                
                }
                */
                //oos.flush();
					 initializing = false;
				   }
				 }
				}

				//If joining group
				else if(clientObject instanceof JoinGroup)
				{
				   disconnect();
               synchronized(keeper)
				   {
					  System.out.println("JoinGroup object received");
					  JoinGroup join = (JoinGroup)clientObject;
					  System.out.println("Password " +join.password);
					  System.out.println("Username " + join.username);
					  System.out.println("group " + join.groupName);
					  if(groups.containsKey(join.groupName))
					  {
						 if(groups.get(join.groupName).isPassword(join.password))
						 {
                        boolean repeated = false;
                        for(int i = 0; i<groups.get(join.groupName).members.size();i++)
                        {
                           if(join.username.toUpperCase().equals(groups.get(join.groupName).members.get(i).username.toUpperCase()))
                           {
                              repeated = true;
                           }
                        
                        }
                        if(repeated)
                        {
                           oos.writeObject("A user with the name " + join.username + " already exists in this group. Please select a unique username");
							      oos.flush();
                        }
                        else
                        {
                        username = join.username;
							   groups.get(join.groupName).members.add(this);
							   oos.writeObject(StringMessages.APPROVED);
							   oos.flush();
							   groupName = join.groupName;
							   
							   allUsers.add(this);
                       
                         writeToClient(groups.get(join.groupName).endTime);
                         refreshGroups();
                         refreshUsers();
							   initializing = false;
                        }
						 }
						 else
						 {
							oos.writeObject("You have entered an incorrect password");
							oos.flush();
							System.err.println("You have entered an incorrect password");
						 }
					  }
					  else
					  {
						 String errorMessage = " is not an active group";
						 writeToClient(errorMessage);
						 System.err.println(errorMessage);
					  }
					}
				}
            //Handle different string commands
            else if(clientObject instanceof String)
            {
               String clientString = (String)clientObject;
               
               if(clientString.equals(StringMessages.REFRESH))
						 {
                   synchronized(keeper){
							Enumeration<String> _keys = groups.keys();
						 
							Vector<String> keys = new Vector<String>();
										   
							while(_keys.hasMoreElements())
							{
							   keys.add(_keys.nextElement());   
							}
							oos.writeObject(keys);
							System.out.println("That Stuff happens");
							oos.flush();
				      }
               }
            }
            }
           }
			catch( IOException e ) { 
            disconnect();  
			}
         catch(ClassNotFoundException cnfe)
         {
            System.err.println("Wrong object type sent by client");
         }
		} 
	 } 
}