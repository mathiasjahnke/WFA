package de.tum.bgu.lfk.weatherforecast.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import processing.core.PApplet;
import processing.data.JSONObject;

/**
 * to query yahoo location information based on lat and lon from Yahoo geo.placefinder.
 * stores basic location information lite country, state, city and woeid.
 * to update and retrieve location information update() has to be called.
 * 
 * @author Mathias Jahnke, Technische Universit&auml;t M&uuml;nchen, <a href="http://www.lfk.bgu.tum.de">Chair of Cartography</a>
 * @version 0.0.1
 * @since 26.06.2015
 *
 */
public class YahooLocation {
	
	private PApplet p;
	private String city;
	private String state;
	private String country;
	private String woeid;
	
	/**
	 * convenience constructor
	 * @param p PApplet
	 */
	public YahooLocation(PApplet p){
		this.p = p;
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
	public String getCity() {
		return city;
	}

	/**
	 * 
	 * @param city String
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 
	 * @return String
	 */
	public String getState() {
		return state;
	}

	/**
	 * 
	 * @param state String
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 
	 * @return String
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * 
	 * @param country String
	 */
	public void setCountry(String country) {
		this.country = country;
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
	private JSONObject getLocation(float lat, float lon){
		
		//build YQL query
		String yql1 = "select * from geo.placefinder where text=\"";
		String latLon = Float.toString(lat) + "," + Float.toString(lon);
		String yql2 = "\" AND gflags=\"R\"";
		String query = yql1 + latLon + yql2;

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
	 * update the YhaooLocation object. retrieves the new location from geo.placefinder based on lat lon values. 
	 * Overrides the current information.
	 * @param lat latitude
	 * @param lon longitude
	 */
	public void update(float lat, float lon){
		
		JSONObject obj = getLocation(lat, lon);
		
		String country, state, city, woeid;
		
		//save the old state
		country = this.country;
		state = this.state;
		city = this.city;
		woeid = this.woeid;
		
		//TODO indicator if update() was able to retrieve a valid location or woeid
		try{
			JSONObject res1 = obj.getJSONObject("query");
			JSONObject res2 = res1.getJSONObject("results");
			JSONObject res3 = res2.getJSONObject("Result");
			this.country = res3.getString("country");
			this.state = res3.getString("state");
			this.city = res3.getString("city");
			this.woeid = res3.getString("woeid");
		}catch (RuntimeException e){
			System.out.println("Something went wrong with YahooLocation.update()");
			System.out.println("RE:" + e.getMessage());
			this.country = country;
			this.state = state;
			this.city = city;
			this.woeid = woeid;
		}
		
	}


}
