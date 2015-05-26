package de.tum.bgu.lfk.weatherforecast;

import org.gicentre.geomap.GeoMap;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class WeatherForecastApp extends PApplet{
	
	private GeoMap geoMap;
	
	public void setup(){
		size(700,700);
		
		geoMap = new GeoMap(0, 0, 500, 300, this);
		geoMap.readFile("C:/Users/Mathias/workspace/data/countries/cntry00");
		
	}
	
	
	public void draw(){
		background(55);
		
		fill(210, 210, 210);
		stroke(235);
		geoMap.draw();
		
	}

}
