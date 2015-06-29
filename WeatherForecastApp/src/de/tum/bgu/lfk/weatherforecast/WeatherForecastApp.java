package de.tum.bgu.lfk.weatherforecast;

import org.gicentre.geomap.GeoMap;

import de.tum.bgu.lfk.weatherforecast.util.YahooWeather;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

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
	
	private PImage yahooImage; 
	
	//different fonts
	private PFont title;
	private PFont credits;
	private PFont curTempAtLoc; //current temperature at location
	private PFont locationName;
	
	private String curTemp;
	private String curLocation;
	
	private YahooWeather yahooWeather;
	
	private PVector clickedLocation;
	
	public void setup(){
		size(1200,700);
		smooth();
		
		int offSetX = 10; //10
		int offSetY = 60; //60
		
		//load countries
		//geoMapCountries = new GeoMap(offSetX + 0, offSetY + 0, 726, 350, this);
		geoMapCountries = new GeoMap(offSetX + 0, offSetY + 0, 1000, 482, this);
		geoMapCountries.readFile("C:/Users/Mathias/workspace/data/countries/cntry00");
		
		//load cities
		//geoMapCities = new GeoMap(offSetX + 30, offSetY + 11, 690, 265, this);
		//geoMapCities.readFile("C:/Users/Mathias/workspace/data/cities/cities");
		
		//create the different fonts
		String fontName = "UniversLTStd-Light";
		title = createFont(fontName, 30, true);
		curTempAtLoc = createFont(fontName, 74, true);
		locationName = createFont(fontName, 14, true);
		
		
		//start location munich
		yahooWeather = new YahooWeather(this);
		yahooWeather.update(11.581981f, 48.135125f);
		curLocation = yahooWeather.getCity();
		//clickedLocation = geoMapCountries.geoToScreen(48.135125f, 11.581981f);
		clickedLocation = geoMapCountries.geoToScreen(11.581981f, 48.135125f);
		
		
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
		
		//draw current temperature and location
		curTemp = yahooWeather.getTemp() + "°";
		textFont(curTempAtLoc);
		textAlign(LEFT, CENTER);
		text(curTemp, 1030, 100);
		
		textFont(locationName);
		textAlign(LEFT, CENTER);
		text(yahooWeather.getCountry(), 1030, 145);
		
		//draw countries
		strokeWeight(1);
		fill(228, 225, 213);
		stroke(152,198,234);
		geoMapCountries.draw();
		
		//draw cities
		//strokeWeight(2);
		//fill(85);
		//geoMapCities.draw();
		
		//draw clicked location
		ellipseMode(CENTER);
		stroke(156, 13, 22);
		fill(156, 13, 22);
		ellipse(clickedLocation.x, clickedLocation.y, 6, 6);
		
		//draw powered by yahoo image 
		image(yahooImage, width - yahooImage.width, height - yahooImage.height);
		
	}
	
	public void mouseReleased(){
		PVector pv = geoMapCountries.screenToGeo(mouseX, mouseY);
		println(pv);
		yahooWeather.update(pv.x, pv.y);
		clickedLocation.x = mouseX;
		clickedLocation.y = mouseY;
		println(clickedLocation);
		println("newLocation: " + yahooWeather.getCountry() + " : " + yahooWeather.getState() + " : " + yahooWeather.getCity() + " : " + yahooWeather.getWoeid());
		
		
		
	}
}
