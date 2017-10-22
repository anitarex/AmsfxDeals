package amsfx;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.MappedSuperclass;

import java.util.Date;

import javax.persistence.Column;


//@Entity
//@Table
@MappedSuperclass
public class Deal {
	@Id
	private String dealID;
	private String fromCurrency;
	private String toCurrency;
	private double dealAmount;
	private Date dealDate;
	private String sourceFileName;
	
	
	 public Deal(String dID, String fCurrency, String tCurrency, double dAmount, Date dDate, String sFileName) {
	      super( );
	      this.dealID = dID;
	      this.fromCurrency = fCurrency;
	      this.toCurrency = tCurrency;
	      this.dealAmount = dAmount;
	      this.dealDate = dDate;
	      this.sourceFileName = sFileName;
	 }

	   public Deal( ) {
	      super();
	   }

	   public String getDealID( ) {
	      return dealID;
	   }
	   
	   public void setDealID(String dID) {
	      this.dealID = dID;
	   }
	   
	   public String getFromCurrency(){
		   return fromCurrency;
	   }
	   
	   public void setFromCurrency(String fCurrency) {
		      this.fromCurrency = fCurrency;
		   }
	   public String getToCurrency(){
		   return toCurrency;
	   }
	   
	   public void setToCurrency(String tCurrency) {
		      this.toCurrency = tCurrency;
		   }
	   public double getDealAmount(){
		   return dealAmount;
	   }
	   
	   public void setDealAmount(double dAmount) {
		      this.dealAmount = dAmount;
		   }
	   
	   public Date getDealDate(){
		   return dealDate;
	   }
	   
	   public void setDealDate(Date dDate) {
		      this.dealDate = dDate;
		   }
	   public String getSourceFileName( ) {
		      return sourceFileName;
		   }
		   
	   public void setSourceFileName(String sFileName) {
		      this.sourceFileName = sFileName;
		   }
	   
}
