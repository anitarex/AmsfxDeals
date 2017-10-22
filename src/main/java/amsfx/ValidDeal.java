package amsfx;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import java.util.Date;

@Entity
@Table
public class ValidDeal extends Deal
{	

	 public ValidDeal(String dID, String fCurrency, String tCurrency, double dAmount, Date dDate, String sFileName)
	 {
	      super(dID,  fCurrency,  tCurrency,  dAmount, dDate, sFileName);
	
	 }

	   public ValidDeal( ) 
	   {
	      super();
	   }

}
