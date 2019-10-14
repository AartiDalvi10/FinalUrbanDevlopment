package com.location.api.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.location.api.model.AreaDTO;
import com.location.api.model.SearchObjectDetailDTO;

/**
 * Handles requests for the Employee service.
 */
@RestController
@Component
public class AreaController {
	
	private static final Logger logger = LoggerFactory.getLogger(AreaController.class);
	private static final String  API_KEY="AIzaSyCInknKpnR2nuTw-Xgn9fvpI5Tc9hSFZSk";
	private static  final String locationApiUrl="https://maps.googleapis.com/maps/api/geocode/json";
	private static  final String locationSearchObjectApiUrl="https://maps.googleapis.com/maps/api/place/nearbysearch/json";
	static int density=10000;
	
	
	 @RequestMapping(value = "/", method = RequestMethod.GET)
	  public static String getMessage() {
	  return "Hello HI";
	 }
	
		  
	  @RequestMapping(value = "/getLocationAndSuggestionDetail", method = RequestMethod.GET)
	  public static String getLocationAndSuggestionDetail(@RequestParam String location,@RequestParam String searchObject) {
		String locationJson=null;
		String jsonInString=null;
		
			try {
				if(location!=null && !location.isEmpty())
				locationJson=getCordinates(location);
				
				String locationCordinates=locationJson.substring(locationJson.indexOf("\"location\" :"),locationJson.indexOf("\"location_type\" :"));
				String lat=locationCordinates.substring(locationCordinates.indexOf("\"lat\" :"), locationCordinates.indexOf(",")).split(":")[1].trim();
				String lng=locationCordinates.substring(locationCordinates.indexOf("\"lng\" :"), locationCordinates.lastIndexOf("}")).split(":")[1].trim();
				
				String searchObjectJson=getSearchObjectCordinates(lat,lng,searchObject);
				
			    String [] searchObjectJsonResult = locationJson.substring(locationJson.indexOf("\"results\" : [")+"\"results\" : [".length(),locationJson.lastIndexOf("]")).trim().replaceAll("\\n","").replaceAll(" ","").split("\\}\\,\\{");
				
	            JSONParser parser = new JSONParser();
	            Object obj=null;
				try {
					obj = parser.parse(searchObjectJson);
				} catch (org.json.simple.parser.ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            JSONObject jb = (JSONObject) obj;

	            //now read
	            JSONArray jsonObject1 = (JSONArray) jb.get("results");
	            
	            Set<SearchObjectDetailDTO> searchObjectDetailDTOLst = new HashSet();
	            
	    		for(int i=0,j=0;i<jsonObject1.size() ;i++,j++) {
	    			SearchObjectDetailDTO searchObjectDetailDTO = new SearchObjectDetailDTO();
	    			System.out.println( "total school "+ jsonObject1.size());
	    			
	    			JSONObject jsonObject2 = (JSONObject) jsonObject1.get(i);
	    			JSONObject jsonObject3 = (JSONObject) jsonObject2.get("geometry");
	    			JSONArray type = (JSONArray) jsonObject2.get("types");
	    			System.out.println("****************type" +type);
	    			
	    			if(type.contains(searchObject.toLowerCase()) &&  type.contains("point_of_interest")  && type.contains("establishment")) {
	    				
	    				
	    				String name = (String) jsonObject2.get("name");
	    				searchObjectDetailDTO.setName(name);
	    				//if(!name.contains("coaching")) {
	    				System.out.println("name = "+name);
	    				String address =(String) jsonObject2.get("vicinity");
	    				searchObjectDetailDTO.setAddress(address);
	    				System.out.println("address=  "+address );
	    				
	    				JSONObject searchObjectLocation = (JSONObject) jsonObject3.get("location");

	    				System.out.println("Lat = " + searchObjectLocation.get("lat"));
	    				System.out.println("Lng = " + searchObjectLocation.get("lng"));
	    				searchObjectDetailDTO.setLat(searchObjectLocation.get("lat").toString());
	    				searchObjectDetailDTO.setLng(searchObjectLocation.get("lng").toString());
	    				//}
	    			}
	    			searchObjectDetailDTOLst.add(searchObjectDetailDTO);
	    		}
	    		
	    		AreaDTO areaDTO = new AreaDTO();
	    		areaDTO.setLocation(location);
	    		areaDTO.setSearchObjectType(searchObject.toLowerCase());
	    		areaDTO.setLocationLat(lat);
	    		areaDTO.setLocationLng(lng);
	    		areaDTO.setSearchObjectDetail(searchObjectDetailDTOLst);
	    		
	    		List<String> schoolSuggestion = new ArrayList();
	    		schoolSuggestion.add("Need More Schools ");
	    		schoolSuggestion.add("Survey to check kids exempted from education");
	    		schoolSuggestion.add("More nursery play group is required");
	    		
	    		List<String> hospitalSuggestion = new ArrayList();
	    		hospitalSuggestion.add("Need More Hospitals");
	    		hospitalSuggestion.add("Improve facilities of existing hospitals");
	    		
	    		List<String> parkSuggestion = new ArrayList();
	    		parkSuggestion.add("Need More park");
	    		
	    		if(density/searchObjectDetailDTOLst.size()>1000 && searchObject.toLowerCase().equals("school")) {
	    			areaDTO.setSuggestion(schoolSuggestion);	
	    		}else if(density/searchObjectDetailDTOLst.size()>1000 && searchObject.toLowerCase().equals("hospital")) {
	    			areaDTO.setSuggestion(hospitalSuggestion);	
	    		}else if(density/searchObjectDetailDTOLst.size()>1000 && searchObject.toLowerCase().equals("park")) {
	    			areaDTO.setSuggestion(parkSuggestion);	
	    		}
	    			
	    		ObjectMapper mapper = new ObjectMapper();
	    		 jsonInString = mapper.writeValueAsString(areaDTO);
			} catch (IOException e) {
				
				e.printStackTrace();
			} 
		  
		  return jsonInString;
	  }
	  public static String getSearchObjectCordinates(String lat,String lng,String searchObject)throws IOException{
 
		    
		  InputStream inputStream = null;
			String json = "";
			String url1= locationSearchObjectApiUrl+"?location="+lat+","+lng+"&radius=500&types="+searchObject.toLowerCase()+"&key="+API_KEY;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(
						url1	);
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				inputStream = entity.getContent();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
				StringBuilder sbuild = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sbuild.append(line);
				}
				inputStream.close();
				json = sbuild.toString();
			} catch (Exception e) {
			}
			return json;
  }
	  
	   public static String getCordinates(String location) throws IOException{
	 	   String thisLine;
		   String finalUrl=locationApiUrl+"?key="+API_KEY+"&address="+location;
		   URL url = new URL(finalUrl);
		 		    BufferedReader theHTML = new BufferedReader(new InputStreamReader(url.openStream()));
		    String locationString="";
		    while ((thisLine = theHTML.readLine()) != null)
		    	locationString+=thisLine;
		    	
		    theHTML.close();
		    
		    return locationString;
		 
		}

	  
	}
	 


