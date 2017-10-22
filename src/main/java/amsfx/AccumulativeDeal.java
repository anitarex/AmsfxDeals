package amsfx;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table
public class AccumulativeDeal {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int ID;
	private String orderingCurrency;
	private int dealCount;
	
	 public AccumulativeDeal(int ID, String orderingCurrency, int dealCount) {
	      super( );
	      this.ID = ID;
	      this.orderingCurrency = orderingCurrency;
	      this.dealCount = dealCount;
	     
	 }

	   public AccumulativeDeal( ) {
	      super();
	   }
	   public void setID(int ID) {
		      this.ID = ID;
		   }

	   public int getID( ) {
	      return ID;
	   }
	   
	   public void setOrderingCurrency(String orderingCurrency) {
	      this.orderingCurrency = orderingCurrency;
	   }
	   
	   public String getOrderingCurrency(){
		   return orderingCurrency;
	   }
	   public void setDealCount(int dealCount) {
		      this.dealCount = dealCount;
		   }

	   public int getDealCount( ) {
	      return dealCount;
	   }
	  
}
