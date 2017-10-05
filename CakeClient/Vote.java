import java.io.*;
public class Vote implements Serializable
{
   public String icing;
   public String size;
   public String cakeFlav;
   public String cakeComp;
   public String toppings;
   
   public Vote(String _icing,String _size,String _cakeFlav,String _cakeComp,String _toppings)
   {
      icing = _icing;
      size = _size;
      cakeFlav = _cakeFlav;
      cakeComp = _cakeComp;
      toppings = _toppings;
   }
   
   public String getIcing()
   {
      return icing;
   }
   
   public String getSize()
   {
      return size;
   }
   
   public String getCakeFlav()
   {
      return cakeFlav;
   }
   
   public String getCakeComp()
   {
      return cakeComp;
   }
   
   public String getToppings()
   {
      return toppings;
   }
}