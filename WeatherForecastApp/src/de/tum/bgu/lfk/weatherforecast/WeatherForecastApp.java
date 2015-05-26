package de.tum.bgu.lfk.weatherforecast;

import org.gicentre.geomap.GeoMap;

import processing.core.PApplet;
import processing.core.PVector;
import processing.data.*;

@SuppressWarnings("serial")
public class WeatherForecastApp extends PApplet{
	
	private GeoMap geoMapCountries;
	private GeoMap geoMapCities;
	private JSONObject obj;
	
	public void setup(){
		size(700,700);
		
		geoMapCountries = new GeoMap(0, 0, 500, 300, this);
		geoMapCountries.readFile("C:/Users/Mathias/workspace/data/countries/cntry00");
		
		geoMapCities = new GeoMap(0, 0, 500, 300, this);
		geoMapCities.readFile("C:/Users/Mathias/workspace/data/countries/cntry00");
		
	}
	
	
	public void draw(){
		background(55);
		
		fill(210, 210, 210);
		stroke(235);
		geoMapCountries.draw();
		
	}
	
	public void mouseClicked(){
		PVector pv = geoMapCountries.screenToGeo(mouseX, mouseY);
		println(pv);
	}
	
	private JSONObject getJSONObject(float lat, float lon){
		JSONObject obj = null;
		
		return obj;
	}

}
