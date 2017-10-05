import java.util.*;
public class IngredientComparator implements Comparator<Ingredient> 
{

   public int compare(Ingredient i1, Ingredient i2) 
   {
		
		
		int in1 = i1.votes;
		int in2 = i2.votes;
		int diff ;
		diff =  in2 -in1;
		diff = (int) Math.signum( diff );
		return diff;
		
	}

}