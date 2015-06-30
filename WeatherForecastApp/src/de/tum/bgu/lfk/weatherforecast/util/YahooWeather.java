package de.tum.bgu.lfk.weatherforecast.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 * to query yahoo weather informationbased on woeid from Yahoo geo.placefinder and weather.forecast.
 * stores basic location and weather information like country, state, city, woeid, condition and a 5 day forecast.
 * to retrieve data update() has to be called
 * 
 * @author Mathias Jahnke, Technische Universit&auml;t M&uuml;nchen, <a href="http://www.lfk.bgu.tum.de">Chair of Cartography</a>
 * @version 0.0.1
 * @since 26.06.2015
 *
 */
public class YahooWeather extends YahooLocation{
	
	private JSONObject weather;
	
	//Condition
	private String date;
	private String temp;
	private String code;
	private String text;
	
	private LinkedList<YahooForecast> forecast;
	
	
	//**********Constructors***************
	/**
	 * convenience constructor
	 * @param p PApplet
	 */
	public YahooWeather(PApplet p){
		super(p);
		date = null;
		temp = null;
		code = null;
		text = null;
		forecast = new LinkedList<YahooForecast>();
	}
	
	//**********Getter Setter***************
	/**
	 * 
	 * @return JSONObject
	 */
	public JSONObject getWeather(){
		return weather;
	}
	
	public String getDate() {
		return date;
	}

	/**
	 * Set the date String from the JSON output
	 * replaces abbreviations of month (e.g. Jun to July) and weekdays (e.g. Mon to Monday) 
	 * @param date a String representing the date
	 */
	public void setDate(String date) {
		//Replace month
		if(date.matches(".*Jun.*")){
			date = date.replaceAll("Jun", "June");
		}else if(date.matches(".*Jul.*")){
			date = date.replaceAll("Jul", "July");
		}
		
		//replace weekday
		if(date.matches(".*Sun.*")){
			date = date.replaceAll("Sun", "Sunday");
		}else if(date.matches(".*Mon.*")){
			date = date.replaceAll("Mon", "Monday");
		}else if(date.matches(".*Tue.*")){
			date = date.replaceAll("Tue", "Tuesday");
		}else if(date.matches(".*Wed.*")){
			date = date.replaceAll("Wed", "Wednesday");
		}else if(date.matches(".*Thu.*")){
			date = date.replaceAll("Thu", "Thursday");
		}else if(date.matches(".*Fri.*")){
			date = date.replaceAll("Fri", "Friday");
		}else if(date.matches(".*Sat.*")){
			date = date.replaceAll("Sat", "Saturday");
		}
		this.date = date;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * return a LinkedList of YahooForecast objects.
	 * @return LinkedList
	 */
	public LinkedList<YahooForecast> getForecast(){
		return forecast;
	}

	//**********Private Methods***************
	private JSONObject queryWeather(String woeid){
		
		//build YQL query
		String yql1 = "select * from weather.forecast where woeid=\"";
		String yql2 = "\" AND u='c'";
		String query = yql1 + woeid + yql2;
		
		//encode the YQL string for web usage
		try{
			query = URLEncoder.encode(query, "UTF-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}finally{
			
		}
		
		//build URL with YQL string
		String json1 = "http://query.yahooapis.com/v1/public/yql?q=";
		String json2 = "&format=json";
		String json = json1 + query + json2;
		
		//query results
		JSONObject obj = getP().loadJSONObject(json);
		return obj;
	}
	
	/**
	 * retrieves the condition from the weather object 
	 * @param resObj the whole weather object
	 * @return 
	 */
	private void setCondition(JSONObject resObj){
		JSONObject res1 = resObj.getJSONObject("query");
		JSONObject res2 = res1.getJSONObject("results");
		JSONObject res3 = res2.getJSONObject("channel");
		JSONObject res4 = res3.getJSONObject("item");
		JSONObject condition = null;
		try{
			condition = res4.getJSONObject("condition");
			setDate(condition.getString("date"));
			this.temp = condition.getString("temp");
			this.code = condition.getString("code");
			this.text = condition.getString("text");
		}catch(RuntimeException e){
			System.out.println(e.getMessage());
			System.out.println(resObj);
		}
	}
	
	/**
	 * retrieves the 5 day forecast from the whole weather object
	 * @param resObj
	 */
	private void setForecast(JSONObject resObj){
		JSONObject res1 = resObj.getJSONObject("query");
		JSONObject res2 = res1.getJSONObject("results");
		JSONObject res3 = res2.getJSONObject("channel");
		JSONObject res4 = res3.getJSONObject("item");
		
		
		try{
			JSONArray forecastArray = res4.getJSONArray("forecast");
			YahooForecast yf;
			for(int i = 0; i < forecastArray.size(); i++){
				JSONObject j = forecastArray.getJSONObject(i);
				yf = new YahooForecast(j.getString("date"), j.getString("high"), j.getString("low"), j.getString("code"), j.getString("text"), j.getString("day"), "C");
				this.forecast.add(yf);
			}
		}catch(RuntimeException e){
			System.out.println(e.getMessage());
			System.out.println(resObj);
		}
		
		
	}
	
	//**********Public Methods***************
	@Override
	/**
	 * overwrites the parent update(). but calls the parent one as well
	 */
	public boolean update(float lon, float lat){
		boolean updateSuccessful;
		updateSuccessful = super.update(lon, lat);
		if(updateSuccessful){
			forecast.clear();
			weather = queryWeather(getWoeid());
			setCondition(weather);
			setForecast(weather);
		}
		return updateSuccessful;
	}

}
