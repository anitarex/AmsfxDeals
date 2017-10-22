package amsfx;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import com.opencsv.CSVReader;
import org.springframework.web.servlet.ModelAndView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class FileUploadController 
{
	@Autowired
	private DealProcessor dp;
	
	private static final Logger logger = Logger.getLogger(DisplayResultsController.class);
	private String saveDirectory = "C:/myserveruploads/";
	
	 @RequestMapping(value="/uploadFile" ,method = RequestMethod.GET)
	    public String viewLogin() {
	        return "UploadForm";
	    }
	
	
	@RequestMapping(value="/uploadFile.do", method = RequestMethod.POST)
	public ModelAndView handleFileUpload(HttpServletRequest request,
			@RequestParam CommonsMultipartFile fileUpload, ModelMap mp) throws Exception 
	{
		
		//DealProcessor dp = new DealProcessor(fileUpload,mp,request);
		dp.setFileUploaded(fileUpload);
		dp.setModelMap(mp);
		dp.setHttpServletRequest(request);
		
		logger.info("File Upload Controller.. Starts");
		ModelAndView mv = dp.process();
		logger.info("File Upload Controller.. Ends");
		return mv;
	
	}

}
