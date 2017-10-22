package amsfx;

import java.util.Date;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
@Table
public class ImportDetails {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int ID;
	private String sourceFileName;
	private Date  startTime;
	private Date  endTime;
	private long validDealCount;
	private long invalidDealCount;
	
	 public ImportDetails(int ID,String sourceFileName, Date startTime , Date endTime, long validDealCount, long invalidDealCount)
	 {
	      super();
	      this.ID = ID;
	      this.sourceFileName = sourceFileName;
	      this.startTime = startTime;
	      this.endTime = endTime;
	      this.validDealCount = validDealCount;
	      this.invalidDealCount = invalidDealCount;
	      
	 }

	   public ImportDetails() {
	      super();
	   }
	   public void setID(int ID) {
		      this.ID = ID;
		   }

	   public int getID() {
	      return ID;
	   }
	   
	   public void setSourceFileName(String sourceFileName) {
	      this.sourceFileName = sourceFileName;
	   }
	   
	   public String getSourceFileName(){
		   return sourceFileName;
	   }
	   public void setStartTime(Date startTime) {
		      this.startTime = startTime;
	   }
		   
	   public Date getStartTime(){
			   return startTime;
		}
	   public void setEndTime(Date endTime) {
		      this.endTime = endTime;
	   }
		   
	   public Date getEndTime(){
			   return endTime;
		}
	   public void setValidDealCount(long validDealCount) {
		      this.validDealCount = validDealCount;
		   }

	   public long getValidDealCount() {
	      return validDealCount;
	   }
	   public void setInvalidDealCount(long invalidDealCount) {
		      this.invalidDealCount = invalidDealCount;
		   }

	   public long getInvalidDealCount() {
	      return invalidDealCount;
	   }
	  
	  
}
