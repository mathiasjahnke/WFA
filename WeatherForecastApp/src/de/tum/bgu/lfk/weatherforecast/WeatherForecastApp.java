package de.tum.bgu.lfk.weatherforecast;

import org.gicentre.geomap.GeoMap;

import de.tum.bgu.lfk.loadicons.FileExtensions;
import de.tum.bgu.lfk.loadicons.Icons;
import de.tum.bgu.lfk.weatherforecast.util.YahooWeather;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.core.PVector;

/**
 * TODO include weekdays into Trend graphic make trend graphic a widget <br>
 * TODO 5 day forecast as widgets including text (free positioning on screen)<br>
 * TODO include where we are during sunrise and sunset in Sun and Moon graphics make this a widget<br>
 * TODO heading for current condition<br>
 * TODO if no code (3200) for current condition use the one from the forecast<br>
 * TODO YahooLocation YahooWeather should be based on a YahooQuery class which takes a yql string for what ever query.<br>
 * TODO more credits than only the powered by yahoo image<br>
 * 
 * @author Mathias Jahnke, Technische Universit&auml;t M&uuml;nchen, <a href="http://www.lfk.bgu.tum.de">Chair of Cartography</a>
 * @version 0.0.1
 * @since 26.06.2015
 *
 */

@SuppressWarnings("serial")
public class WeatherForecastApp extends PApplet{
	
	private GeoMap geoMapCountries;
	//private GeoMap geoMapCities;
	
	private PImage yahooImage; 
	private PImage weatherIcon;
	
	//different fonts
	private PFont title;
	//private PFont credits;
	private PFont curTempAtLoc; //current temperature at location
	private PFont locationName;
	private PFont conditionText;
	private PFont weekday;
	private PFont forecastTemp;
	private PFont axisLabel;
	
	private YahooWeather yahooWeather;
	
	private PVector clickedLocation;
	
	private Icons weatherIcons; 
	
	public void setup(){
		size(1200,700);
		smooth();
		
		int offSetX = 10; //10
		int offSetY = 60; //60
		
		//load countries
		//geoMapCountries = new GeoMap(offSetX + 0, offSetY + 0, 726, 350, this);
		geoMapCountries = new GeoMap(offSetX + 0, offSetY + 0, 1000, 482, this);
		
		//Path notebook
		//String path = "C:/Users/mjahnke/Dropbox/UniA/Daten/countries/cntry00";
		//path uni pc
		String path = "C:/Users/Mathias/workspace/data/countries/cntry00";
		geoMapCountries.readFile(path);
		
		//load cities
		//geoMapCities = new GeoMap(offSetX + 30, offSetY + 11, 690, 265, this);
		//geoMapCities.readFile("C:/Users/Mathias/workspace/data/cities/cities");
		
		//load icon files
		weatherIcons= new Icons(this);
		weatherIcons.loadIcons("C:/Users/Mathias/workspace/data/plain_weather_icons/flat_colorful/png/", FileExtensions.PNG);
		
		//create the different fonts
		String fontName = "UniversLTStd-Light";
		title = createFont(fontName, 30, true);
		curTempAtLoc = createFont(fontName, 74, true);
		locationName = createFont(fontName, 14, true);
		conditionText = createFont(fontName, 18, true);
		weekday = createFont(fontName, 18, true);
		forecastTemp = createFont(fontName, 14, true);
		axisLabel = createFont(fontName, 10, true);
		
		//start location munich
		yahooWeather = new YahooWeather(this);
		yahooWeather.update(11.581981f, 48.135125f);
		clickedLocation = geoMapCountries.geoToScreen(11.581981f, 48.135125f);
		
		//web service image
		yahooImage = loadImage("https://poweredby.yahoo.com/purple.png", "png");
		
		weatherIcon = weatherIcons.getIcon(yahooWeather.getCode());
	}
	
	
	public void draw(){
		background(255);
		
		//draw title
		fill(0,51,89);
		textFont(title);
		textAlign(CENTER, CENTER);
		text("Weather Forecast", width/2, 23);
		
		//draw icon
		imageMode(CORNER);
		image(weatherIcon, 1050, 60);
		
		//condition text
		textFont(conditionText);
		textAlign(LEFT, CENTER);
		text(yahooWeather.getText(), 1050, 65);
		
		//draw current temperature and location
		textFont(curTempAtLoc);
		textAlign(LEFT, CENTER);
		text(yahooWeather.getTemp() + "°", 1040, 180);
		
		textFont(locationName);
		textAlign(LEFT, TOP);
		//if city name is shorter than 140 px
		if(textWidth(yahooWeather.getCity()) < 140){
			text(yahooWeather.getCity(), 1040, 225);
			//checks whether the country name is longer than 140 px 
			if(textWidth(yahooWeather.getCountry()) < 140){
				text(yahooWeather.getCountry(), 1040, 243);
			}else{
				text(yahooWeather.getCountry(), 1040, 243, 140, 40);
			}
		}else{ //if city name is longer than 140 px
			text(yahooWeather.getCity(), 1040, 225, 140, 40);
			//checks whether the country name is longer than 140 px
			if(textWidth(yahooWeather.getCountry()) < 140){
				text(yahooWeather.getCountry(), 1040, 263);
			}else{
				text(yahooWeather.getCountry(), 1040, 263, 140, 40);
			}
		}
		
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
		
		//draw separator between map and forecast
		fill(0, 51, 89);
		stroke(0, 51, 89);
		rect(10, 542, 1180, 21);
		textFont(weekday);
		textAlign(LEFT, CENTER);
		fill(255);
		text("5 Day Forecast", 13, 552);
		
		drawForecast();
		drawSunMoon();
		drawForecastChart();
		
		//draw powered by yahoo image 
		imageMode(CORNER);
		image(yahooImage, width - yahooImage.width, height - yahooImage.height);
		
	}
	
	public void mouseReleased(){
		
		PVector pv = geoMapCountries.screenToGeo(mouseX, mouseY);
		//println(pv);
		boolean updateSuccessful;
		updateSuccessful = yahooWeather.update(pv.x, pv.y);
		if(updateSuccessful){
			clickedLocation.x = mouseX;
			clickedLocation.y = mouseY;
			if(yahooWeather.getCode().equals("3200")){
				weatherIcon = weatherIcons.getIcon("na");
			}else{
				weatherIcon = weatherIcons.getIcon(yahooWeather.getCode());
			}
		}
		//println(yahooWeather.getDate());
		//println(yahooWeather.getCode());
		//println("newLocation: " + yahooWeather.getCountry() + " : " + yahooWeather.getState() + " : " + yahooWeather.getCity() + " : " + yahooWeather.getWoeid());
	}
	
	private void drawSunMoon(){
		stroke(0,51,89);
		fill(240, 240, 240);
		strokeWeight(1);
		
		//draw base line
		line(1058, 500 , 1176, 500);
		
		//draw arc
		arc(1118, 500, 100, 100, PI, 2 * PI);
		
		//draw labels
		textFont(weekday);
		textAlign(LEFT, CENTER);
		fill(0, 51, 89);
		text("Sun & Moon", 1040, 420);
		
		textFont(axisLabel);
		textAlign(CENTER, CENTER);
		fill(0, 51, 89);
		text(yahooWeather.getSunrise(), 1068, 515);
		text(yahooWeather.getSunset(), 1168, 515);
		
		//draw sun and moon icons
		image(weatherIcons.getIcon("32"), 1058, 500, 25, 25);
		image(weatherIcons.getIcon("31"), 1178, 500, 25, 25);
	}
	
	private void drawForecast(){
		fill(0,51,89);
		textAlign(CENTER, BASELINE);
		for(int i = 0; i < 5; i++){
			textFont(weekday);
			text(yahooWeather.getForecast().get(i).getDay(), 100 + (i * 200), 585);
			textFont(forecastTemp);
			text(yahooWeather.getForecast().get(i).getHigh() + "°", (100 + (i * 200)) - 20, 608);
			drawArrowHigh((100 + (i * 200)) - 37, 608);
			text(yahooWeather.getForecast().get(i).getLow() + "°", (100 + (i * 200)) + 20, 608);
			drawArrowLow((100 + (i * 200)) + 3, 608);
			imageMode(CENTER);
			image(weatherIcons.getIcon(yahooWeather.getForecast().get(i).getCode()), 100 + (i * 200), 635, 50, 50);
		}
	}
	
	private void drawArrowHigh(int x, int y){
		stroke(0,51,89);
		strokeWeight(1);
		line(x, y, x, y - 12);
		line(x, y - 12, x - 3, y - 6);
		line(x, y - 12, x + 3, y - 6);
	}
	
	private void drawArrowLow(int x, int y){
		stroke(0,51,89);
		strokeWeight(1);
		line(x, y, x, y - 12);
		line(x, y, x - 3, y - 6);
		line(x, y, x + 3, y - 6);
	}
	
	/**
	 * the following jars from the processing folder have to be
	 * on the java build path:
	 * core.jar,
	 * jogl-all.jar,
	 * gluegen-rt.jar,
	 * jogl-all-natives-windows-amd64.jar,
	 * gluegen-rt-natives-windows-amd64.jar 
	 */
	private void drawForecastChart(){
		//draw heading
		textFont(weekday);
		textAlign(LEFT, BASELINE);
		fill(0, 51, 89);
		text("Trend", 1038, 585);
		
		//minor x axis
		stroke(221, 221, 221);
		line(1058, 628, 1178, 628);
		line(1058, 608, 1178, 608);
		line(1058, 668, 1178, 668);
		
		stroke(0,51,89);
		strokeWeight(1);
		//y axis
		line(1058, 593, 1058, 673);
		//x axis
		line(1058, 648, 1178, 648);
		//x tick marks
		for(int i = 0; i <= 4; i++){
			line(1058 + (i * 30), 645, 1058 + (i * 30), 651);
		}
		
		//y axis labels
		textFont(axisLabel);
		textAlign(RIGHT, CENTER);
		text("-20°", 1055, 668);
		text("0°", 1055, 648);
		text("20°", 1055, 628);
		text("40°", 1055, 608);
				
		//curve high values
		stroke(156, 13, 22);
		fill(156, 13, 22);
		strokeWeight(2);
		noFill();
		beginShape();
		curveVertex(1058, 648 - Integer.parseInt(yahooWeather.getForecast().get(0).getHigh()));
		curveVertex(1058, 648 - Integer.parseInt(yahooWeather.getForecast().get(0).getHigh()));
		curveVertex(1088, 648 - Integer.parseInt(yahooWeather.getForecast().get(1).getHigh()));
		curveVertex(1118, 648 - Integer.parseInt(yahooWeather.getForecast().get(2).getHigh()));
		curveVertex(1148, 648 - Integer.parseInt(yahooWeather.getForecast().get(3).getHigh()));
		curveVertex(1178, 648 - Integer.parseInt(yahooWeather.getForecast().get(4).getHigh()));
		curveVertex(1178, 648 - Integer.parseInt(yahooWeather.getForecast().get(4).getHigh()));
		endShape();
		
		//curve low values
		stroke(0,51,89);
		fill(0, 51,89);
		noFill();
		beginShape();
		curveVertex(1058, 648 - Integer.parseInt(yahooWeather.getForecast().get(0).getLow()));
		curveVertex(1058, 648 - Integer.parseInt(yahooWeather.getForecast().get(0).getLow()));
		curveVertex(1088, 648 - Integer.parseInt(yahooWeather.getForecast().get(1).getLow()));
		curveVertex(1118, 648 - Integer.parseInt(yahooWeather.getForecast().get(2).getLow()));
		curveVertex(1148, 648 - Integer.parseInt(yahooWeather.getForecast().get(3).getLow()));
		curveVertex(1178, 648 - Integer.parseInt(yahooWeather.getForecast().get(4).getLow()));
		curveVertex(1178, 648 - Integer.parseInt(yahooWeather.getForecast().get(4).getLow()));
		endShape();
		
		
	}
}
