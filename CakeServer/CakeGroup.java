import java.util.*;
import java.io.*;
public class CakeGroup
{
   private GroupInfo info;
   long endTime;
   boolean activeGroup = true;
   CakeServer self;
   public final File ORDERS= new File("orders.txt");
   ArrayList <CakeServer.ThreadedServer> members = new ArrayList<CakeServer.ThreadedServer>();
   ArrayList<Ingredient> icings = new ArrayList<Ingredient>();
   ArrayList<Ingredient> sizes = new ArrayList<Ingredient>();
   ArrayList<Ingredient> cakeFlavs = new ArrayList<Ingredient>();
   ArrayList<Ingredient> cakeComps = new ArrayList<Ingredient>();
   ArrayList<Ingredient> toppingss = new ArrayList<Ingredient>();
   
   
   public Ingredient tally(ArrayList<Ingredient> lst)
   {
      Ingredient winner = new Ingredient("No winner",0);
      for(int i =0;i<lst.size();i++)
      {
         if(lst.get(i).votes > winner.votes)
         {
            winner = lst.get(i);
         }
      }
      return winner;
   }
   public CakeGroup(GroupInfo _info,CakeServer _self)
   {
      activeGroup = true;
      info = _info;
      self = _self;
      endTime = new Date().getTime() +  (1000*60*(info.minutes));
      //Group Timer
      Timer timer = new Timer();
      timer.schedule(new TimerTask() {
      @Override
      public void run() {
         long lnTimeLeft = endTime - new Date().getTime();
         if(lnTimeLeft <= 0)
         {
            if(members.size() > 0)
            {
            members.get(0).writeToGroupProtected(new Message("System",getResults()));
            //for(int i=0;i<members.size();i++)
            //{
               //members.get(i).disconnect();
            //}
            self.groups.remove(info.groupName);
            self.refreshGroups();
            cancel();
            }
            else
            {
               String text = getResults();
               activeGroup = false;
               self.refreshGroups();
               cancel();
            }
         }
         else
         {
               if(members.size() > 0)
               {
               members.get(0).writeToGroupProtected(new Message("System",String.format("There are %d minutes and %d seconds left%n",lnTimeLeft/60000,((lnTimeLeft/1000)%60))));
               }
         }
      }
      }, 1000,1000*(60)*(1));
      
   }
   public boolean isGroupName(String id)
   {
      return info.groupName == id;
   }
   public boolean isPassword(String pass)
   {
      return info.groupPassword.equals(pass);
   
   }
   public String getGroupName()
   {
      return info.groupName;
   }
   
   public static void main(String [] args)
   {
      //new CakeGroup();
   
   }
   public void addVoteHelper(ArrayList<Ingredient> list,String vote)
   {
      boolean newVote = true;
      for(int i = 0; i< list.size();i++)
      {
         if(list.get(i).name.equals(vote))
         {
            list.get(i).votes++;
            newVote = false;
         }
      }
      if(newVote)
      {
         list.add(new Ingredient(vote));
      }
   }
   public void addVote(Vote vote)
   {
     addVoteHelper(icings,vote.getIcing());
     addVoteHelper(sizes,vote.getSize()); 
     addVoteHelper(cakeFlavs,vote.getCakeFlav());
     addVoteHelper(cakeComps,vote.getCakeComp());  
     addVoteHelper(toppingss,vote.getToppings()); 
      
   }
   
   /**
      Removes user thread from group
   */
   public void leaveGroup(int i, String username)
   {
            
					
					
					   members.remove(i);
               
                  if(members.size() > 0)
                  {
                     members.get(0).writeToGroup(new Message(username,"is now disconnected from the group"));
                     members.get(0).sendUsers();
                  }
                  
                  //sendUsers();
                  //refreshUsers();
				
   
   }
   /**
      returns a string with the result of a completed vote
   */
   /**
      returns a string with the result of a completed vote
   */
   public String getResults()
   {
      String results;
      String infoToCSV;
      if(cakeComps.isEmpty())
      {
         results = String.format("You ran out of time without casting any votes, so no cake was ordered.");
      }
      else
      {
         Comparator<Ingredient> inComp = new IngredientComparator();
         Collections.sort(icings,inComp);
         Collections.sort(sizes,inComp);
         Collections.sort(cakeFlavs,inComp);
         Collections.sort(cakeComps,inComp);
         Collections.sort(toppingss,inComp);
         results = String.format("Congratulations you have ordered a %s %s %s with %s and %s",sizes.get(0).name,cakeFlavs.get(0).name,cakeComps.get(0).name,icings.get(0).name,toppingss.get(0).name);
         self.printGUI(results);
         try
         {
            String starter = "Group Name,Group Leader, Street Address, City, State, Zip,Cake Size,Cake Type, Cake Flavor, Icing, Toppings";
            
            infoToCSV = String.format("%s, %s, %s, %s , %s , %s ,%s,%s, %s,%s,%s",info.getGroupName(),info.leader,info.getStreetAddress(),info.getCity(),info.getState(),info.getZip(),sizes.get(0).name,cakeComps.get(0).name,cakeFlavs.get(0).name,icings.get(0).name,toppingss.get(0).name);
            if((new File("orders.csv")).exists())
            {
            BufferedWriter bw = new BufferedWriter(new FileWriter("orders.csv",true));

            bw.write( infoToCSV );
            bw.flush();
            bw.newLine();
             bw.flush();
            bw.close( );
            }
            else
            {
               BufferedWriter bw = new BufferedWriter(new FileWriter("orders.csv",true));
               bw.write( starter );
               bw.flush();
               bw.newLine();
               bw.flush();
               bw.write( infoToCSV );
               bw.flush();
               bw.newLine();
               bw.flush();
            }
            }
                     
         catch(IOException ioe)
         {
            self.printGUI("Error Writing order to file");
            System.err.println("Error Writing order to file");
         }
      } 
      return results;
   }
}