package de.tum.bgu.lfk.weatherforecast.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import processing.core.PApplet;
import processing.data.JSONObject;

/**
 * to query yahoo woeid based on lattitude and longitude from Yahoo ugeo.reversegeocode.
 * to update and retrieve woeid update() has to be called.
 * 
 * @author Mathias Jahnke, Technische Universit&auml;t M&uuml;nchen, <a href="http://www.lfk.bgu.tum.de">Chair of Cartography</a>
 * @version 0.0.1
 * @since 26.06.2015
 *
 */
public class YahooWoeid {
	
	private PApplet p;
	private String woeid;
	
	/**
	 * convenience constructor
	 * @param p PApplet
	 */
	public YahooWoeid(PApplet p){
		this.p = p;
		this.woeid = "56210702"; //valentin karlstadt museum, munich, germany
 	}
	
	//**********Getter Setter***************
	/**
	 * 
	 * @return PApplet
	 */
	public PApplet getP(){
		return this.p;
	}
	
	/**
	 * 
	 * @param p PApplet
	 */
	public void setP(PApplet p){
		this.p = p;
	}

	/**
	 * 
	 * @return String
	 */
	public String getWoeid() {
		return woeid;
	}

	/**
	 * 
	 * @param woeid String
	 */
	public void setWoeid(String woeid) {
		this.woeid = woeid;
	}
	
	//**********Private Methods***************
	/**
	 * 
	 * @param lat latitude
	 * @param lon longitude
	 * @return processing.core.JSONObject
	 */
	private JSONObject getWOEID(float lon, float lat){
		
		//build YQL query
		String query = "select * from ugeo.reversegeocode where latitude=" + Float.toString(lat) + 
				" and longitude=" + Float.toString(lon) + " and appname='your-assigned-appname'";
		
		//encode the YQL string for web usage
		try{
			query = URLEncoder.encode(query, "UTF-8");

		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}finally{

		}
		//println(query);

		//build URL with YQL string
		String json1 = "http://query.yahooapis.com/v1/public/yql?q=";
		String json2 = "&format=json";
		String json = json1 + query + json2;

		//query results
		JSONObject obj = p.loadJSONObject(json);

		return obj;
	}
	
	
	//**********Public Methods***************
	/**
	 * update the woeid. retrieves the new woeid from Yahoo tables (ugeo.reversegeocode) based on longitude and latitude values. 
	 * if a woeid is found it overrides the old woeid, if not the old woeid stays.
	 * @param lat latitude (north-south)
	 * @param lon longitude (east-west)
	 * @return true if new woeid could be retrieved otherwise false
	 */
	public boolean update(float lon, float lat){
		
		JSONObject obj = getWOEID(lon, lat);
		
		String woeid;
		
		//save the old woeid
		woeid = this.woeid;
		
		boolean updateSuccessful;
		
		try{
			JSONObject res1 = obj.getJSONObject("query");
			JSONObject res2 = res1.getJSONObject("results");
			JSONObject res3 = res2.getJSONObject("result");
			JSONObject locations = res3.getJSONObject("locations");
			JSONObject woe = locations.getJSONObject("woe"); 
			
			this.woeid = woe.getString("id");
			
			updateSuccessful = true;
		}catch (RuntimeException e){
			System.out.println("Something went wrong with YahooLocation.update()");
			System.out.println("RE:" + e.getMessage());
			this.woeid = woeid;
			updateSuccessful = false;
		}
		return updateSuccessful;
	}


}
