public class Ingredient
{
   public String name;
   public int votes;
   
   public Ingredient(String _name)
   {
      name = _name;
      votes = 1;
   }
   public Ingredient(String _name, int _votes)
   {
      name = _name;
      votes = _votes;
   }

}