package com.mustafakaplan.aracdeposu;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mustafakaplan.entity.Advertisements;
import com.mustafakaplan.entity.Brands;
import com.mustafakaplan.entity.Cities;
import com.mustafakaplan.entity.Models;
import com.mustafakaplan.entity.Types;
import com.mustafakaplan.entity.Users;
import com.mustafakaplan.entity.Vehicles;
import com.mustafakaplan.service.AdvertisementService;
import com.mustafakaplan.service.BrandService;
import com.mustafakaplan.service.CityService;
import com.mustafakaplan.service.ModelService;
import com.mustafakaplan.service.TypeService;
import com.mustafakaplan.service.UserService;
import com.mustafakaplan.service.VehicleService;


@Controller
public class ProfileController 
{
	public static String url = "http://localhost:8085/aracdeposu";
	Long ad_id;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private BrandService brandService;
	
	@Autowired
	private ModelService modelService;
	
	@Autowired
	private TypeService typeService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private AdvertisementService advertisementService;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/car", method = RequestMethod.GET)
	public String car(Model model) 
	{
		return "car";
	}
	
	@RequestMapping(value = "/car-single", method = RequestMethod.GET)
	public String car_single(Model model) 
	{
		return "car-single";
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Model model, HttpServletRequest request) 
	{
		model.addAttribute("user", request.getSession().getAttribute("user"));
		return "profile";
	}
	
	@RequestMapping(value = "/getVehicle", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<Vehicles>> getVehicle(HttpServletRequest request)
	{
		return new ResponseEntity<>(vehicleService.getAll(), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getAdUser", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<Users>> getAdUser(HttpServletRequest request)
	{
		return new ResponseEntity<>(userService.getFindById(Long.parseLong(request.getParameter("id"))), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getCity", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<Cities>> getCity(HttpServletRequest request)
	{
		return new ResponseEntity<>(cityService.getAll(), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getBrand", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<Brands>> getBrand(HttpServletRequest request)
	{
		return new ResponseEntity<>(brandService.getAll(Integer.parseInt(request.getParameter("id"))), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getModel", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<Models>> getModel(HttpServletRequest request)
	{
		return new ResponseEntity<>(modelService.getAll(Integer.parseInt(request.getParameter("id")),Integer.parseInt(request.getParameter("id2"))), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getType", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<Types>> getType(HttpServletRequest request)
	{
		return new ResponseEntity<>(typeService.getAll(Integer.parseInt(request.getParameter("id"))), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public ResponseEntity<String> uploadImage(@RequestParam("files") MultipartFile[] files,HttpSession session)
	{   
		Advertisements oldAd = advertisementService.getFindByAdId(ad_id);
		
		if(files.length == 0)
		{
			return new ResponseEntity<>("EMPTY", HttpStatus.CREATED);
		}
		
		String path= "C:\\Users\\m06ka\\Desktop\\MASAUSTU\\spring-tool-suite-3.9.12.CI-B1344-e4.15.0-win32-x86_64\\sts-bundle\\sts-3.9.12.CI-B1344\\projects\\AracDeposu\\src\\main\\webapp\\resources\\asset\\ad_images\\";  
		
		File dir = new File(path + ad_id);
		if (!dir.exists())
		{
			dir.mkdirs();
			path += ad_id;
		}
		
		String filename = "";
		oldAd.setImg1("");
		oldAd.setImg2("");
		oldAd.setImg3("");
		oldAd.setImg4("");
		oldAd.setImg5("");
		
		for(int i=0; i<files.length; i++)
		{
			String extension = FilenameUtils.getExtension(files[i].getOriginalFilename());
			
			if(i == 0)
			{
				filename = "pp" + "." + extension;
				oldAd.setPp("asset/ad_images/" + ad_id + "/" + filename);
			}
			else
			{
				filename = "img" + i + "." + extension;
				
				if(i == 1)
				{
					oldAd.setImg1("asset/ad_images/" + ad_id + "/" + filename);
				}else if(i == 2)
				{
					oldAd.setImg2("asset/ad_images/" + ad_id + "/" + filename);
				}else if(i == 3)
				{
					oldAd.setImg3("asset/ad_images/" + ad_id + "/" + filename);
				}else if(i == 4)
				{
					oldAd.setImg4("asset/ad_images/" + ad_id + "/" + filename);
				}else if(i == 5)
				{
					oldAd.setImg5("asset/ad_images/" + ad_id + "/" + filename);
				}
			}
			
			try{  
	            byte barr[]=files[i].getBytes();  
	              
	            BufferedOutputStream bout=new BufferedOutputStream(  
	                     new FileOutputStream(path+"\\"+filename));  
	            bout.write(barr);  
	            bout.flush();  
	            bout.close();  
	              
	            }catch(Exception e){System.out.println(e);
	            return new ResponseEntity<>("ERROR", HttpStatus.CREATED);
	            }  
		}
		
		advertisementService.updateAd(oldAd);
		
        return new ResponseEntity<>("OK", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/addAd", method = RequestMethod.POST)
	public ResponseEntity<String> addAd(@RequestBody Advertisements ad, HttpServletRequest request)
	{
		advertisementService.createAd(ad);
		ad_id = ad.getAd_id();
		
		return new ResponseEntity<>("OK", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/updateAd", method = RequestMethod.POST)
	public ResponseEntity<String> updateAd(@RequestBody Advertisements ad, HttpServletRequest request)
	{
		Advertisements oldAd = advertisementService.getFindByAdId(ad.getAd_id());
		
		ad_id = oldAd.getAd_id();
		
		if(ad.getActive() == -5)
		{
			deleteImageFolder();
		}
		
		ad.setVehicle(oldAd.getVehicle());
		ad.setBrand(oldAd.getBrand());
		ad.setModel(oldAd.getModel());
		ad.setType(oldAd.getType());
		ad.setUser_id(oldAd.getUser_id());
		ad.setActive(0);
	
		ad.setPp(oldAd.getPp());
		ad.setImg1(oldAd.getImg1());
		ad.setImg2(oldAd.getImg2());
		ad.setImg3(oldAd.getImg3());
		ad.setImg4(oldAd.getImg4());
		ad.setImg5(oldAd.getImg5());
		
		advertisementService.updateAd(ad);
		
		
		
		return new ResponseEntity<>("OK", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/error_404", method = RequestMethod.GET)
	public String error(Model model)
	{
		return "error_404";
	}
	
	@RequestMapping(value = "/getCarsProfile", method = RequestMethod.POST)
	public ResponseEntity<ArrayList<Advertisements>> getCarsProfile(HttpServletRequest request)
	{ 
		return new ResponseEntity<>(advertisementService.getAllByUserId(Long.parseLong(request.getParameter("id"))), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getAdByAdId", method = RequestMethod.POST)
	public ResponseEntity<Advertisements> getAd(HttpServletRequest request)
	{
		return new ResponseEntity<>(advertisementService.getFindByAdId(Long.parseLong(request.getParameter("id"))), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/deleteImageFolder", method = RequestMethod.POST)
	public void deleteImageFolder()
	{
		String oldImageFolder = String.valueOf(ad_id);
		String path= "C:\\Users\\m06ka\\Desktop\\MASAUSTU\\spring-tool-suite-3.9.12.CI-B1344-e4.15.0-win32-x86_64\\sts-bundle\\sts-3.9.12.CI-B1344\\projects\\AracDeposu\\src\\main\\webapp\\resources\\asset\\ad_images\\";
		path += oldImageFolder;
		
		File folder = new File(path);
		
		try {
			FileUtils.deleteDirectory(folder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
