package de.tum.bgu.lfk.weatherforecast.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * to query yahoo weather informationbased on woeid from Yahoo geo.placefinder and weather.forecast.
 * stores basic location and weather information like country, state, city, woeid, condition and a 5 day forecast.
 * 
 * @author Mathias Jahnke, Technische Universit&auml;t M&uuml;nchen, <a href="http://www.lfk.bgu.tum.de">Chair of Cartography</a>
 * @version 0.0.1
 * @since 26.06.2015
 *
 */
public class YahooWeather extends YahooLocation{
	
	private JSONObject weather;
	private JSONObject condition;
	private JSONArray forecast;
	
	
	//**********Constructors***************
	/**
	 * constructor
	 * @param p PApplet
	 */
	public YahooWeather(PApplet p){
		super(p);
	}
	
	/**
	 * convenience constructor
	 * @param lat latitude
	 * @param lon longitude
	 * @param p PApplet
	 */
	public YahooWeather(float lat, float lon, PApplet p){
		super(lat, lon, p);
	}
	
	//**********Getter Setter***************
	/**
	 * 
	 * @return JSONObject
	 */
	public JSONObject getWeather(){
		return weather;
	}
	
	//**********Private Methods***************
	
	private JSONObject getWeather(String woeid){
		
		//build YQL query
		String yql1 = "select * from weather.forecast where woeid=\"";
		String yql2 = "\" AND u='c'";
		String query = yql1 + woeid + yql2;
		//println("weather: " + query);
		
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
		//println("weather: " + json);
		
		//query results
		JSONObject obj = getP().loadJSONObject(json);
		
		return obj;
	}
	
	//**********Public Methods***************
	@Override
	public void update(float lat, float lon){
		
		super.update(lat, lon);
		weather = getWeather(getWoeid());
		
	}

}
