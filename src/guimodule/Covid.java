package guimodule;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import de.fhpotsdam.unfolding.providers.OpenMapSurferProvider;

public class Covid extends PApplet {
	
	UnfoldingMap map;
	Map <String , Float> CovidCount;
	List <Feature> countries;
	List <Marker> countryMarkers;
	
	//set the date so that the date is automatically taken from the computer and there is always up-to-date data
	int d = day() -1;    // Values from 1 - 31
	int m = month();  // Values from 1 - 12
	int y = year();   // 2003, 2004, 2005, etc.

	String date = y +"-0"+m+"-"+d;

	
	public void setup () {
		
		size (800, 600, OPENGL);
		map = new UnfoldingMap (this, 50, 50, 700, 500);
		MapUtils.createDefaultEventDispatcher(this, map);
		CovidCount = loadLifeExpectancyFromCSV ("https://covid.ourworldindata.org/data/owid-covid-data.csv");
		println("Loaded " + CovidCount.size() + " data entries");
		System.out.print(date);
		countries = GeoJSONReader.loadData(this, "countries.geo.json");
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		map.addMarkers(countryMarkers);
		shadeCountries();
	}


	public void draw () {
		map.draw();
		
	}
	
	private Map<String, Float> loadLifeExpectancyFromCSV(String fileName) {
		Map <String, Float> lifeExpMap = new HashMap <String, Float> ();
		String [] rows = loadStrings (fileName); 
		for (String row : rows) {
			String[] columns = row.split(",");
			if (columns[3].equals("2020-08-13")) {
				lifeExpMap.put(columns[0], Float.parseFloat(columns[4]));
			
		}
		}
		return lifeExpMap;
	}
	
	private void shadeCountries () {
		for (Marker marker : countryMarkers) {
			String countryID = marker.getId();
			
			if (CovidCount.containsKey(countryID)) {
				float Covid = CovidCount.get(countryID);
				
				int colorLevel = (int) map (Covid, 1, 5000000, 10, 255);
				marker.setColor(color(colorLevel,100,255-colorLevel));
			}
			
			else {
				marker.setColor(color(20,20,20));
			}
		}
	}
}
