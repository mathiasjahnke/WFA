package de.tum.bgu.lfk.weatherforecast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.gicentre.geomap.GeoMap;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.*;

@SuppressWarnings("serial")
public class WeatherForecastApp extends PApplet{
	
	private GeoMap geoMapCountries;
	private GeoMap geoMapCities;
	
	private JSONObject weather; // the whole weather object
	private JSONObject location; //the location based on the mouse clicked position; the whole location object
	
	private JSONObject condition; //the condition extracted from weather
	private PImage yahooImage; 
	
	//different fonts
	private PFont title;
	private PFont credits;
	private PFont curTempAtLoc; //current temperature at location
	private String curTemp;
	
	
	public void setup(){
		size(900,500);
		smooth();
		
		int offSetX = 10;
		int offSetY = 60;
		
		//load countries
		geoMapCountries = new GeoMap(offSetX + 0, offSetY + 0, 726, 350, this);
		geoMapCountries.readFile("C:/Users/Mathias/workspace/data/countries/cntry00");
		
		//load cities
		geoMapCities = new GeoMap(offSetX + 30, offSetY + 11, 690, 265, this);
		geoMapCities.readFile("C:/Users/Mathias/workspace/data/cities/cities");
		
		String fontName = "UniversLTStd-Light";
		title = createFont(fontName, 30, true);
		curTempAtLoc = createFont(fontName, 74, true);
		
		//start location munich
		location = getLocation(48.135125f,11.581981f);
		String woeid = getWoeid(location);
		weather = getWeather(woeid);
		condition = getCondition(weather);
		
		//web service image
		yahooImage = loadImage("https://poweredby.yahoo.com/purple.png", "png");
	}
	
	
	public void draw(){
		background(255);
		
		//draw title
		fill(0,51,89);
		textFont(title);
		textAlign(CENTER, CENTER);
		text("Weather Forecast App", width/2, 23);
		
		
		curTemp = condition.getString("temp") + "°";
		textFont(curTempAtLoc);
		textAlign(CENTER, CENTER);
		text(curTemp, 818, 100);
		
		//draw countries
		strokeWeight(1);
		fill(228, 225, 213);
		stroke(152,198,234);
		geoMapCountries.draw();
		
		//draw cities
		strokeWeight(2);
		fill(85);
		geoMapCities.draw();
		
		//draw powered by yahoo image 
		image(yahooImage, width - yahooImage.width, height - yahooImage.height);
		
	}
	
	public void mouseClicked(){
		PVector pv = geoMapCountries.screenToGeo(mouseX, mouseY);
		println(pv);
		location = getLocation(pv.x, pv.y);
		String woeid = getWoeid(location);
		
		weather = getWeather(woeid);
		condition = getCondition(weather);
		
	
	}
	
	private JSONObject getLocation(float lat, float lon){
		
		//build YQL query
		String yql1 = "select * from geo.placefinder where text=\"";
		//String woeid = "676757";
		String latLon = Float.toString(lat) + "," + Float.toString(lon);
		String yql2 = "\" AND gflags=\"R\"";
		String query = yql1 + latLon + yql2;
		println("loc: " + query);

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
		println("loc: " + json);

		//query results
		JSONObject obj = loadJSONObject(json);

		return obj;
	}
	
	private String getWoeid(JSONObject obj){
		
		String woeid;
		try{
			JSONObject res1 = obj.getJSONObject("query");
			JSONObject res2 = res1.getJSONObject("results");
			JSONObject res3 = res2.getJSONObject("Result");
			woeid = res3.getString("woeid");
		}catch (RuntimeException e){
			//e.printStackTrace();
			println("ee:" + e.getMessage());
			woeid = "676757";
		}
		
		return woeid;
	}
	
	private JSONObject getWeather(String woeid){
		
		//build YQL query
		String yql1 = "select * from weather.forecast where woeid=\"";
		//String woeid = "676757";
		//String woeid1 = "12836562";
		String yql2 = "\" AND u='c'";
		String query = yql1 + woeid + yql2;
		println("weather: " + query);
		
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
		println("weather: " + json);
		
		//query results
		JSONObject obj = loadJSONObject(json);
		
		return obj;
	}
	
	private JSONObject getCondition(JSONObject resObj){
		JSONObject res1 = resObj.getJSONObject("query");
		JSONObject res2 = res1.getJSONObject("results");
		JSONObject res3 = res2.getJSONObject("channel");
		JSONObject res4 = res3.getJSONObject("item");
		JSONObject condition = res4.getJSONObject("condition");
		println("***CONDITION***");
		println(condition);
		
		return condition;
	}
	
	private JSONArray getForecast(JSONObject resObj){
		JSONObject res1 = resObj.getJSONObject("query");
		//println(res1);
		//println("##############################");
		
		JSONObject res2 = res1.getJSONObject("results");
		//println(res2);
		
		JSONObject res3 = res2.getJSONObject("channel");
		//println(res3);
		
		JSONObject res4 = res3.getJSONObject("item");
		//println(res4);

		JSONArray forecast = res4.getJSONArray("forecast");
		//println(forecast);
		
		println("***FORECAST***");
		for(int i = 0; i < forecast.size(); i++){
			JSONObject j = forecast.getJSONObject(i);
			println(j);
		}
		
		return forecast;
	}

}
