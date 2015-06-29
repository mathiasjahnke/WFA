package de.tum.bgu.lfk.weatherforecast;

import org.gicentre.geomap.GeoMap;

import de.tum.bgu.lfk.weatherforecast.util.YahooWeather;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.*;

/**
 * how to extract coordinates from the point and polygon shapefile ???
 * @author Mathias Jahnke, Technische Universit&auml;t M&uuml;nchen, <a href="http://www.lfk.bgu.tum.de">Chair of Cartography</a>
 * @version 0.0.1
 * @since 26.06.2015
 *
 */

@SuppressWarnings("serial")
public class WeatherForecastApp extends PApplet{
	
	private GeoMap geoMapCountries;
	private GeoMap geoMapCities;
	
	private JSONObject condition; //the condition extracted from weather
	private PImage yahooImage; 
	
	//different fonts
	private PFont title;
	private PFont credits;
	private PFont curTempAtLoc; //current temperature at location
	private PFont locationName;
	private String curTemp;
	private String curLocation;
	
	private YahooWeather yahooWeather;
	
	
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
		locationName = createFont(fontName, 16, true);
		
		
		//start location munich
		//yahooLoc = new YahooLocation(48.135125f, 11.581981f, this);
		yahooWeather = new YahooWeather(this);
		yahooWeather.update(48.135125f, 11.581981f);
		curLocation = yahooWeather.getCity();
		
		println("newLocation: " + yahooWeather.getCountry() + " : " + yahooWeather.getState() + " : " + yahooWeather.getCity() + " : " + yahooWeather.getWoeid());
		
		
		//location = getLocation(48.135125f,11.581981f);
		//println(location);
		//String woeid = getWoeid(location);
		//weather = getWeather(yahooLoc.getWoeid());
		//condition = getCondition(yahooWeather.getWeather());
		
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
		
		
		//curTemp = condition.getString("temp") + "°";
		curTemp = yahooWeather.getTemp() + "°";
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
		yahooWeather.update(pv.x, pv.y);
		//location = getLocation(pv.x, pv.y);
		//String woeid = getWoeid(location);
		
		//weather = getWeather(yahooLoc.getWoeid());
		//condition = getCondition(yahooWeather.getWeather());
		
	
	}
}
