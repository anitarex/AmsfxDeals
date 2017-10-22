package amsfx;

import java.util.ArrayList;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


@Repository
@Transactional
public class DealDao 
{// Injected database connection:
    @PersistenceContext private EntityManager em;
    
    //flushing batch size
	private int batchSize=1000;
	private static final Logger logger = Logger.getLogger(DealDao.class);
 
	
	//Get the Summary of an imported file from Import Details
    public List<ImportDetails> getImportSummary(String sourceFileName) {
        TypedQuery<ImportDetails> query = em.createQuery(
            "SELECT d FROM ImportDetails d where SourceFileName=:sourceFileName ", ImportDetails.class);
        query.setParameter("sourceFileName", sourceFileName);
        return query.getResultList();
    }
	
	 // Retrieves all the valid deals:
    public List<ValidDeal> getValidDeals(String sourceFileName) {
        TypedQuery<ValidDeal> query = em.createQuery(
            "SELECT d FROM ValidDeal d where sourceFileName=:sourceFileName ORDER BY d.dealID", ValidDeal.class);
        query.setParameter("sourceFileName", sourceFileName);
        return query.getResultList();
    }
	// check in the valid deal table
	public boolean fileExists(String sourceFileName)
	{
		boolean fileExists = false;
		Query query1 = em.createQuery("Select count(*) from ValidDeal where sourceFileName=:sourceFileName");
		query1.setParameter("sourceFileName", sourceFileName);
		Long result = (Long) query1.getSingleResult();
		
		if (result.longValue() > 0)
			fileExists = true;
		else 
			fileExists = false;
				
		return fileExists;	    
	}
	
	// check in the valid deal table
	public boolean existsDealId(String dealID)
	{
		boolean dealIDExists = false;
		Query query1 = em.createQuery("Select count(*) from ValidDeal where dealID=:dealID");
		query1.setParameter("dealID", dealID);
		Long result = (Long) query1.getSingleResult();
		
		if (result.longValue() > 0)
			dealIDExists = true;
		else 
			dealIDExists = false;
				
		return dealIDExists;	    
	}
	
	// check in the import details table
	public boolean importFileExists(String sourceFileName)
	{
		boolean importFileExists = false;
		Query query1 = em.createQuery("Select count(*) from ImportDetails where SourceFileName=:sourceFileName");
		query1.setParameter("sourceFileName", sourceFileName);
		Long result = (Long) query1.getSingleResult();
		
		if (result.longValue() > 0)
			importFileExists = true;
		else 
			importFileExists = false;
				
		return importFileExists;	    
	}
	
	public <T extends InvalidDeal> Collection<T> batchInvalidSave(Collection<T> invRecords) {
		logger.info("Inside Batch Invalid Save...");
		return saveRecords(invRecords);
	}
	public <T extends ValidDeal> Collection<T> batchValidSave(Collection<T> vRecords, Collection<AccumulativeDeal> accumulativeRecords)
	{
		logger.info("Inside Batch Valid Save...");
		saveAccumulativeDeals(accumulativeRecords);
		return saveRecords(vRecords);
	}
	
	
	private <T extends Deal>Collection<T> saveRecords(Collection<T> records) {
		logger.info("Inside Save Records...");
		 
		
		 int i = 0;
		 for(T t: records)
		 {
			 i++;
			 em.persist(t);
			 
			 // flushing and clearing the cache
			 			// if (i % Integer.parseInt(batchSize) == 0) {
			 if (i % batchSize == 0){
				 System.out.println("Flush and clear: iteration=" +i);
				 em.flush(); //flush the batch of insert
				 em.clear(); //release memory
			 }
		 }
		
		 return records;

	}

	private void saveAccumulativeDeals(Collection<AccumulativeDeal> accRecords)
	{
		logger.info("Inside Accumulative Save...");
		
		Iterator it = accRecords.iterator();
	    int i = 0;
	    while(it.hasNext())
	    { 
	        i++;
	        AccumulativeDeal accumulativeDealRecord = (AccumulativeDeal)it.next();
	        
	        Query query2= em.createQuery("from AccumulativeDeal where orderingCurrency=:orderingCurrency");
	        query2.setParameter("orderingCurrency", accumulativeDealRecord.getOrderingCurrency());
	       
	        List<AccumulativeDeal> aDeals =  query2.getResultList();
	        
	        if(aDeals.size() > 0)
	        {
	        	AccumulativeDeal aDeal = aDeals.get(0);
	        	aDeal.setDealCount(aDeal.getDealCount()+ accumulativeDealRecord.getDealCount());
	        	em.persist(aDeal);
	        }
	        else
	        {
	        	em.persist(accumulativeDealRecord);
	        	
	        }
	    }
	}
	
	public int dbInsertImportDetails(ImportDetails impDetail)
	{	
		logger.info("Inside dbInsertImportDetails...");
		em.persist(impDetail);
		int ImpID = impDetail.getID();
		return ImpID ;
	}
	
}
	
	/*
	private  <T extends AccumulativeDeal>Collection<T> addAllCurrentCachedAccDeals(Collection<T> records)
	{
		 int i = 0;
		 for(T t: records)
		 {
			 i++;
			 em.persist(t);
			 
			 // flushing and clearing the cache
			 			// if (i % Integer.parseInt(batchSize) == 0) {
			 if (i % batchSize == 0){
				 System.out.println("addAllCurrentCachedAccDeals Inside if Flush and clear: iteration=" +i);
				 em.flush(); //flush the batch of insert
				 em.clear(); //release memory
			 }
		 }
		
		 return records;
		
	}
	
	private void saveAccumulativeDeals(Collection<AccumulativeDeal> accRecords)
	{
		System.out.println("Inside Accumulative Save...");
		
        Query getAllDBAccDealsquery= em.createQuery("from AccumulativeDeal ");
             
        List<AccumulativeDeal> getAllDBAccDeals =  getAllDBAccDealsquery.getResultList();
        
        if (getAllDBAccDeals.isEmpty())
        {
        	addAllCurrentCachedAccDeals(accRecords);
        
        }
        else
        {
         //do SomethingLater
        }
		
       
		
	}
	*/

/*public void dbUpdateImportDetails(int impID, long validDealCount, long invalidDealCount)
{
	   TypedQuery<ImportDetails> query = em.createQuery(
	            "SELECT i FROM ImportDetails i where ID=:impID", ImportDetails.class);
	        query.setParameter("impID", impID);
	     
        ImportDetails importDetail= query.getSingleResult();
        Date importEndTime = new Date();
        importDetail.setEndTime(importEndTime);
        importDetail.setValidDealCount(validDealCount);
        importDetail.setInvalidDealCount(invalidDealCount);
        
	        em.persist(importDetail);
	
}
*/

