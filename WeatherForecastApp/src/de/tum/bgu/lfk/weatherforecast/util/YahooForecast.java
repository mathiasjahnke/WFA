package de.tum.bgu.lfk.weatherforecast.util;

/**
 * to store the five day forecast of the yahoo weather.
 * @author Mathias Jahnke, Technische Universit&auml;t M&uuml;nchen, <a href="http://www.lfk.bgu.tum.de">Chair of Cartography</a>
 * @version 0.0.1
 * @since 26.06.2015
 *
 */
public class YahooForecast {

	private String date;
	private String high;
	private String code;
	private String low;
	private String text;
	private String day;
	private String unit;
	
	//Constructor
	/**
	 * constructor.
	 * constructor is using public methods setDate(), setDay(), setUnit() 
	 * @param date String
	 * @param high String
	 * @param low String
	 * @param code String
	 * @param text String
	 * @param day String
	 */
	public YahooForecast(String date, String high, String low, String code,
			String text, String day, String unit) {
		setDate(date);
		this.high = high;
		this.code = code;
		this.low = low;
		this.text = text;
		setDay(day);
		setUnit(unit);
		
	}

	//GETTER SETTER
	public String getDate() {
		return date;
	}

	/**
	 * set the date and replaces the abbreviation of the month with the full name.
	 * @param date
	 */
	public final void setDate(String date) {
		if(date.matches(".*Jun.*")){
			this.date = date.replaceAll("Jun", "June");
		}else if(date.matches(".*Jul.*")){
			this.date = date.replaceAll("Jul", "July");
		}
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDay() {
		return day;
	}

	/**
	 * to set the day. replaces the abbreviation of the weekday with the full name (Sun = Sunday etc.). 
	 * If no valid abbreviation day is set to noDay (Sun, Mon, Tue etc. noDay is set)  
	 * @param day String
	 */
	public final void setDay(String day) {
		if(day.equals("Sun")){
			this.day = "Sunday";
		}else if (day.equals("Mon")){
			this.day = "Monday";
		}else if (day.equals("Tue")){
			this.day = "Tuesday";
		}else if (day.equals("Wed")){
			this.day = "Wednesday";
		}else if (day.equals("Thu")){
			this.day = "Thursday";
		}else if (day.equals("Fri")){
			this.day = "Friday";
		}else if (day.equals("Sat")){
			this.day = "Saturday";
		}
	} 
	
	/**
	 * to set the unit.
	 * "c" or "C" for Celsius and "f" of "F" for Fahrenheit.
	 * @param unit String
	 */
	public final void setUnit(String unit) {
		if(unit == "f" || unit == "F"){
			this.unit = "F";
		}else if (unit == "c" || unit == "C"){
			this.unit = "C";
		}else {
			this.unit = "noUnit";
		}
	}

	/**
	 * "c" or "C" for Celsius and "f" of "F" for Fahrenheit.
	 * @return String
	 */
	public String getUnit() {
		return unit;
	}
}
