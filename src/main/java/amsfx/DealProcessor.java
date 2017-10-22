package amsfx;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.stereotype.Service;


import com.opencsv.CSVReader;
import org.apache.log4j.Logger;


@Service
public class DealProcessor 
{
	 @Autowired
	 private DealDao dealDaoObj;
	 
	
	 private static final Logger logger = Logger.getLogger(DealProcessor.class);
	 
	
	CommonsMultipartFile fileUploaded;
	File myServerFile;
	ModelMap modelmap;
	Map<String, String> messages = new HashMap<String, String>();
	HttpServletRequest request;
	private int importID;
	private static long invalidDealCount;
	private static long validDealCount;
	private static Date importStartTime;
	private static Date importEndTime;
	
	//String saveDirectory="";
	public DealProcessor()
	{
		
	}
	
	public DealProcessor(CommonsMultipartFile fileUpd, ModelMap mm,HttpServletRequest req)
	{
		this.fileUploaded =fileUpd;
		this.modelmap = mm;
		this.request = req;
	}
	
	public void setFileUploaded(CommonsMultipartFile fileUpd) {
		      this.fileUploaded = fileUpd;
	}
	public CommonsMultipartFile getFileUploaded(){
		   return fileUploaded;
	}
	
	public void setModelMap(ModelMap mm) {
	      this.modelmap = mm;
	}
	public ModelMap getModelMap(){
	   return modelmap;
	}
	
	public void setHttpServletRequest(HttpServletRequest req)
	{ 
		this.request = req;
	}
	
	public HttpServletRequest getHttpServletRequest(){
		   return request;
		}
		
	
	
	
	public ModelAndView process()
	{
		importStartTime = new Date();
		invalidDealCount = 0;
		validDealCount=0;
		
		logger.info("Inside Deal Process..");
		 System.out.println("Process: validDealCount="+validDealCount);
		   System.out.println("Process: invalidDealCount="+invalidDealCount);
		//Step 1.. Check that the file is not empty
		if (isFileEmpty())
		{
			
			messages.clear();
        	messages.put("empty-file", "File is empty !!!. Please upload a valid file !!!");
        	modelmap.clear();
        	modelmap.put("messages", messages);
        	
        	logger.info("Check that the file is not empty..File is empty !!!. Please upload a valid file !!!");
        	
            return  new ModelAndView("UploadForm"  );
			
		}
		//Step 2.. Check that the file extension is .CSV
		if (! isValidCSV())
		{
			
			messages.clear();
        	messages.put("not-csv", "File is not a CSV file !!!. Please upload a valid CSV file !!!");
        	modelmap.clear();
        	modelmap.put("messages", messages);
        	logger.info("Checking that the file extension is .CSV.. File is not a CSV file !!!. Please upload a valid CSV file");
            return  new ModelAndView("UploadForm"  );
			
		}
		//Step 3. Check whether the file is already uploaded as ValidDeal
		if( doesFileExist() )
		{
			messages.clear();
        	messages.put("already-exists", "File already exists in the database !!!. Please upload a new CSV file !!!");
        	modelmap.clear();
        	modelmap.put("messages", messages);
        	logger.info("Check whether the file is already uploaded into the database.. File already exists in the database !!!. Please upload a new CSV file !!! ");
            return  new ModelAndView("UploadForm"  );
		}
		
		//Step 4...Upload the file to server
		if(! uploadFileSuccessful())
		{
			logger.info("FileUpload NOT Successful. ");
        	return  new ModelAndView("UploadForm"  );
		}
	
		//Step 5.  Read and Process the file
		logger.debug("Started..Importing the file records into the database");
		System.out.println("Started..Importing the file records into the database");
	
		readAndProcess();
		   
		saveImportDetails();
		System.out.println("Completed..Importing the file records into the database");
		logger.debug("Completed..Importing the file records into the database");
	
		messages.clear();
		messages.put("temp-success", "Saved all Deals !!!");
		modelmap.clear();
    	modelmap.put("messages", messages);
		 return  new ModelAndView("UploadForm"  );
		
	}
	
	
	public boolean  isFileEmpty()
	{
		return fileUploaded.isEmpty();
	}
	
	public boolean  isValidCSV()
	{
		String fileName = fileUploaded.getOriginalFilename();
	
		logger.debug("Check isValidCSV-" + fileName);
		if (fileName != null && (fileName.endsWith("csv") || fileName.endsWith("CSV")))
				{
					return true;
				}
				
		return false;
	}
	
	public boolean  doesFileExist()
	{
		String fileName = fileUploaded.getOriginalFilename();
		logger.debug("check in import details table if the file has been already imported: ... Filename=" + fileName);
		return dealDaoObj.importFileExists(fileName);
		// check in import details table if the file has been already imported

	}
	
	public boolean  uploadFileSuccessful()
	{
		//Make or check the file upload folder 	
	    String rootPath = request.getSession().getServletContext().getRealPath("/");
	    File dir = new File(rootPath + File.separator + "fxDealsUploads" + File.separator);
	    System.out.println("Get Absolute Path for the file to be uploaded="+dir.getAbsolutePath());
	    logger.info("Get Absolute Path for the file to be uploaded="+ dir.getAbsolutePath());
	    
	    if (!dir.exists()) 
	    {
	            dir.mkdirs();
	    }
		
		myServerFile = new File(dir + File.separator + fileUploaded.getOriginalFilename());
		if (!fileUploaded.getOriginalFilename().equals("")) 
		{
			logger.info("Start copying the file...=" +dir + File.separator + fileUploaded.getOriginalFilename());
			try
			{
			fileUploaded.transferTo(myServerFile);
			}
			catch(IllegalStateException ise)
			{
				String ex1 =ise.toString();
				messages.put("file-upload-ils", "File upload not successful !!!. " + ex1);
				modelmap.put("messages", messages);
				ise.printStackTrace();
				logger.error("File upload not successful !!!." + ise.getStackTrace() );
				return false;
	        }
			catch(IOException ioe)
			{
				String ex1 =ioe.toString();
				messages.put("file-upload-ils", "File upload not successful !!!. " + ex1);
				modelmap.put("messages", messages);
				ioe.printStackTrace();
				logger.error("File upload not successful !!!." + ioe.getStackTrace() );
				return false;
	        }
			return true;
		
		}
		
		

		
				
		return false;
	}
	
	private void readAndProcess()
	{
	   	 List<ValidDeal> validDeals = new ArrayList();
         List<InvalidDeal> inValidDeals = new ArrayList();
         
        logger.debug("Inside readAndProcess. Check each deal whether valid or Invalid ");
		for(String[] eachCSVRecordline:readUploadedCSVFile(myServerFile, "Pass the file Name")) 
        {
        	// check the deal is valid or invalid
        	Deal csvLineDeal =  extractEachRowData(eachCSVRecordline);
        	
        	//System.out.println("****VALID OR INVALID CHECKING");
        	
        /*	System.out.println("csvLineDeal FromCurrency==.." +csvLineDeal.getFromCurrency());
        	System.out.println("csvLineDeal ToCurrency==.." +csvLineDeal.getToCurrency());
        	System.out.println("csvLineDeal DealDate==.." +csvLineDeal.getDealDate());
        	System.out.println("csvLineDeal DealAmount==.." +csvLineDeal.getDealAmount());
       */	
        	if(StringUtils.isEmpty(csvLineDeal.getFromCurrency()) ||
        	   StringUtils.isEmpty(csvLineDeal.getToCurrency()) ||
        	   StringUtils.isEmpty(csvLineDeal.getDealDate()) ||
        	   StringUtils.isEmpty(csvLineDeal.getDealAmount())||
        	   StringUtils.isEmpty(csvLineDeal.getDealID()) ||
        	   dealDaoObj.existsDealId(csvLineDeal.getDealID()))
        	{
        		//System.out.println("This is an Invalid deal");
        		// invalid deals
        	      		
        		InvalidDeal invD = new InvalidDeal(csvLineDeal.getDealID(), 
        				csvLineDeal.getFromCurrency(), 
        				csvLineDeal.getToCurrency(),
        				csvLineDeal.getDealAmount(),
        				csvLineDeal.getDealDate(),
        				csvLineDeal.getSourceFileName());
        		
        		inValidDeals.add(invD);
        	}
        	else
        	{
        		// valid deals
        		//System.out.println("This is a valid deal");

        		
        		ValidDeal valD = new ValidDeal(csvLineDeal.getDealID(), 
        				csvLineDeal.getFromCurrency(), 
        				csvLineDeal.getToCurrency(),
        				csvLineDeal.getDealAmount(),
        				csvLineDeal.getDealDate(),
        				csvLineDeal.getSourceFileName());
        		
        		validDeals.add(valD);
        		
        		
        	}
        	
        }//for
		
		 
		if(validDeals.size() > 0)
		{
			saveValidDeals(validDeals);
			
		}
        if(inValidDeals.size() > 0)
        {
        	saveInvalidDeals(inValidDeals);
        	
        }
        invalidDealCount = inValidDeals.size(); 
        validDealCount= validDeals.size();
		
	}
	
	
    private List<String[]> readUploadedCSVFile(File serverUploadedFile, String fileName)
    {
    	
    	logger.debug("Reading CSV File..");
    	List<String[]> lines = null;
    	try {
            //read file
            //CSVReader(fileReader, ',', '\'', 1) means
            //using separator , and using single quote ' . Skip first line when read
        	//logger.logInfo(" reading CSV file");
    		System.out.println("Reading CSV File..");
            	FileReader objFileReader = new FileReader(serverUploadedFile);
                CSVReader objCSVReader = new CSVReader(objFileReader, ',');
                lines = objCSVReader.readAll();
                //System.out.println(lines[0]. +".." +lines[1]);	
            	
            
        } catch (IOException e) {
        	logger.info("Reading CSV File..readUploadedCSVFile() "+e.getMessage());
        	logger.error("Reading CSV File..readUploadedCSVFile() "+e.getMessage());
        } 
    	
    	return lines;
    }
    
    Deal extractEachRowData(String[] recordLine)
    {
    	Deal lineDeal = new Deal();
    	lineDeal.setDealID(recordLine[0]);
    	lineDeal.setFromCurrency(recordLine[1]);
    	lineDeal.setToCurrency(recordLine[2]);
    	
    	SimpleDateFormat formatter1= new SimpleDateFormat("dd/MM/yyyy");  
    	try
    	{   
    		if((recordLine[3]!= null) && !(recordLine[3].equalsIgnoreCase("")))
    		{
    			
    			lineDeal.setDealDate(formatter1.parse(recordLine[3]));
    			
    		}
    		else
    		{
    			lineDeal.setDealDate(null);
    		}
    	}
    	catch(ParseException pe)
    	{ 
    		System.out.println(pe.toString());
 		
    		logger.error("Reading each Row :Deal Date: ParseException  "+pe.getStackTrace());
    		
    	}
    	try
    	{
    		lineDeal.setDealAmount(Double.parseDouble(recordLine[4]));
    	}
    	catch(NumberFormatException ne)
    	{
    		lineDeal.setDealAmount(0.0);
    		logger.error("Reading each Row :Deal Amount: Number Format exception. set to 0.  "+ne.getStackTrace());
    	}
    	lineDeal.setSourceFileName(fileUploaded.getOriginalFilename());
    	
    	return lineDeal;
    	
    }
	
    void saveInvalidDeals(List<InvalidDeal> myInvalidDeals)
    {
    	logger.info("Save InValid Deals");
    	
    	dealDaoObj.batchInvalidSave(myInvalidDeals);
    	
    }
    
    void saveValidDeals(List<ValidDeal> myValidDeals)
    {
    	
    	logger.info("Save Valid Deals");
   
        /***Calculate Accumulative Deals ***/
    	Map<String, Integer> accumulativeValues = new HashMap(); 
    	for(ValidDeal vDealLine: myValidDeals){
    		
    		if(accumulativeValues.containsKey(vDealLine.getFromCurrency()))
    		{
				int value = Integer.parseInt(String.valueOf((accumulativeValues.get(vDealLine.getFromCurrency()))));
				accumulativeValues.put(vDealLine.getFromCurrency(), ++value);
			}
			else
			{
				accumulativeValues.put(vDealLine.getFromCurrency(), 1);
			}
   
    	}
    	List<AccumulativeDeal> accumulativeDealsList = new ArrayList();
		for (Object key : accumulativeValues.keySet()) 
		{
			AccumulativeDeal accumulativeDeal = new AccumulativeDeal();
			accumulativeDeal.setDealCount(new Integer(String.valueOf(accumulativeValues.get(key))));
			accumulativeDeal.setOrderingCurrency(key.toString());
			accumulativeDealsList.add(accumulativeDeal);
		   // System.out.println("Key = " + key + " - " + accumulativeValues.get(key));
		}
    	
    	
    	
    	dealDaoObj.batchValidSave(myValidDeals , accumulativeDealsList);
    	
    	
   	
    }
    

   void saveImportDetails()
   {
	   logger.info("Inside method insertImportDetails..");
	   String fileName = fileUploaded.getOriginalFilename();
	   ImportDetails impDetailsObj = new ImportDetails();
	   impDetailsObj.setStartTime(importStartTime);
	   impDetailsObj.setSourceFileName(fileName);
	   
	   Date importEndTime = new Date();
	   impDetailsObj.setEndTime(importEndTime);
	   impDetailsObj.setValidDealCount(validDealCount);
	   impDetailsObj.setInvalidDealCount(invalidDealCount);
	   
	
	   
	   dealDaoObj.dbInsertImportDetails(impDetailsObj);
   }
  
    
}


/*  void insertImportDetails()
{
	   System.out.println("Inside method insertImportDetails..");
	   String fileName = fileUploaded.getOriginalFilename();
	   Date importStartTime = new Date();
	   ImportDetails impDetailsObj = new ImportDetails();
	   impDetailsObj.setStartTime(importStartTime);
	   impDetailsObj.setSourceFileName(fileName);
	   importID=dealDaoObj.dbInsertImportDetails(impDetailsObj);
}

void updateImportDetails()
{
	   System.out.println("Inside method updateImportDetails..");
	   dealDaoObj.dbUpdateImportDetails(importID,validDealCount,invalidDealCount);
}
*/
