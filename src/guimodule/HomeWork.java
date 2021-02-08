package guimodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PGraphics;

public class HomeWork extends PApplet {
	
	private UnfoldingMap map; 
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	private List <Marker> cityMarkers;
	private List<Marker> countryMarkers;
	private Marker lastSelected;

	Map <String , Float> AverangeWage;
	
	public void setup () {
		//size canvas
		size(900,700, OPENGL);
		//create a map
		map = new UnfoldingMap (this, 200, 50, 650, 600);
		MapUtils.createDefaultEventDispatcher(this, map);
		
		//load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);
		AverangeWage = loadAverangeWageFromCSV ("D:/AverangeWage3.csv");
		println("Loaded " + AverangeWage.size() + " data entries");
		cityMarkers = new ArrayList<Marker>();

		map.addMarkers(countryMarkers);
		shadeCountries();
		
		
	}

	public void draw () {
		map.draw();
		
	
		
	}
	

	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;
		
		}
		
		selectMarkerIfHover(countryMarkers);
		
	}
	
	
	private void selectMarkerIfHover(List<Marker> markers)
	{

		if (lastSelected != null) {
			return;
		}
		
		for (Marker m : markers) {
		
			if (m.isInside(map, mouseX, mouseY)) {
				lastSelected =  m;
				m.setSelected(true);
				return;
			}
		}
		
	}
	

	

	private Map<String, Float> loadAverangeWageFromCSV(String fileName) {
		Map <String, Float> AverangeWage = new HashMap <String, Float> ();
		String [] rows = loadStrings (fileName); 
		for (String row : rows) {
			String[] columns = row.split(",");
			if (columns.length == 4 && !columns[2].equals("n/a") && !columns[2].equals("") ) {
			AverangeWage.put(columns[0], Float.parseFloat(columns[2]));
			}
			
		
		}
		return AverangeWage;
	}
	
	
	private void shadeCountries () {
		for (Marker marker : countryMarkers) {
			String countryID = marker.getId();
			
			if (AverangeWage.containsKey(countryID)) {
				float AverangeAge = AverangeWage.get(countryID);
				
				int colorLevel = (int) map (AverangeAge, 1000, 140000, 10, 255);
				marker.setColor(color(0,0,100+colorLevel));
			}
			
			else {
				marker.setColor(color(20,20,20));
			}
		}
	}
	
	

}
