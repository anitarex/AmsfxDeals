package amsfx;
import javax.persistence.Query;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;


@Controller
public class DisplayResultsController {
	
	 @Autowired
	 private DealDao displayDao;
	 
	 Map<String, String> dmessages = new HashMap<String, String>();
	 ModelMap modelMap1;
	 
	 
	 private static final Logger logger = Logger.getLogger(DisplayResultsController.class);
	 
	
	 
	 @RequestMapping(value="/results", method = RequestMethod.GET)
	 public ModelAndView getFileName(HttpServletRequest request) 
	 {
		
		 System.out.println("Inside getFileName Method");
		 return new ModelAndView("UploadForm.jsp", "displayDao", displayDao);
	 }
	 
	 @RequestMapping(value="/results", method = RequestMethod.POST)
	 public ModelAndView importSummary(HttpServletRequest request, ModelMap modelMap1)
	     {
	    	
	    	 logger.info("Inside import Summary Method");
		
	    	
	        String sourceFileName = request.getParameter("sourceFileName");
	        
	        if(displayDao.importFileExists(sourceFileName))
	        {	 
	        	
	       	 	logger.info("Import file Exists");
	        	return new ModelAndView("DisplaySummary", "displayDao", displayDao);
	        }
	        else
	        {	dmessages.put("filename-notexists", "There is no imported file with the file name you have entered. "
	        		+ "Please enter the correct file name");
	        	modelMap1.put("dmessages", dmessages);
	        	
	        	 logger.info("filename-not exists: There is no imported file with the file name you have entered. "
	        		+ "Please enter the correct file name");
	        	return  new ModelAndView("UploadForm");
	        }
	       
	    }
	 

     
     public ModelAndView validResults(HttpServletRequest request, ModelMap modelMap1)
     {
     	
    	System.out.println("Inside validResults() Method");
        String sourceFileName = request.getParameter("sourceFileName");
        
        if(displayDao.fileExists(sourceFileName))
        {	       
        	return new ModelAndView("DisplayResults", "displayDao", displayDao);
        }
        else
        {	dmessages.put("filename-notexists", "There is no deal stored against the file name you have entered. "
        		+ "Please enter the correct file name");
        	modelMap1.put("dmessages", dmessages);
        	return  new ModelAndView("UploadForm");
        }
       
    }
	    
	

}
