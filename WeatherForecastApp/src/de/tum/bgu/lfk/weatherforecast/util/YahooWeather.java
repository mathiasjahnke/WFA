package de.tum.bgu.lfk.weatherforecast.util;

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
	
	JSONObject condition;
	JSONArray forecast;
	
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
	
	
	public void update(float lat, float lon){
		
		super.update(lat, lon);
		
		
		
	}

}
