package HomeWork;


import java.util.ArrayList;
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
import processing.core.PConstants;

/**My program allows you to see the main indicators of the country's development, such as
 * GDP (PPP) per person
 * Happy index
 * Corruption index
 * AverangeWage
 * Population
The program also shade countries by level Index of Happy, In of Corruption or GDP

Even with a mouse click, you can view more detailed information 
about the country, and when choosing another country, compare the indicators

GDP - Gross Domestic Product
Corruption Index- Corruption Perceptions Index, CPI

**/

public class AverangeWage extends PApplet {
	
//declaring  variables
	private static final long serialVersionUID = 1L;
	//create variable;
	private UnfoldingMap map; 
	private String cityFile = "city-data.json";
	private String countryFile = "countries.geo.json";
	private List <Marker> cityMarkers;
	private List<Marker> countryMarkers;
	private Marker lastSelected;
	private Marker lastClicked;
	private Marker lastClicked2;
	Map <String , Float> AverangeWage;
	Map <String , Float> Population;
	Map <String , Float> Salary;
	Map <String , Float> Happy;
	Map <String , Float> Corruption;
	int countClicked =0;
	
	
	public void setup () {
		//size canvas
		size(900,700, OPENGL);
		//create a map
		map = new UnfoldingMap (this, 200, 50, 650, 600, new Google.GoogleMapProvider());
		MapUtils.createDefaultEventDispatcher(this, map);
		
		//load country features and markers
		List<Feature> countries = GeoJSONReader.loadData(this, countryFile);
		countryMarkers = MapUtils.createSimpleMarkers(countries);
		
		
		AverangeWage = loadAverangeWageFromCSV ("D:/AverangeWage3.csv");
		Population = loadPopulationCSV ("D:/population.csv");
		Salary = loadSalaryCSV ("D:/salary.csv");
		Happy = loadHappyCSV ("D:/happy.csv");
		Corruption = loadCorruptionCSV ("D:/corruption.csv");
		println("Loaded averangeWage" + AverangeWage.size() + " data entries");
		println("Loaded polulation " + Population.size() + " data entries");
		println("Loaded salary " + Salary.size() + " data entriess");
		println("Loaded happy " + Happy.size() + " data entriess");
		println("Loaded corruption " + Corruption.size() + " data entriess");
		

		List<Feature> cities = GeoJSONReader.loadData(this, cityFile);

		
		
		cityMarkers = new ArrayList<Marker>();
		for(Feature city : cities) {
		  cityMarkers.add(new Markers(city));
			//System.out.println(city.getProperty("salary"));
		}
		

		//add properties of salary to counryMarkes 
			for (Marker c : countryMarkers) {
			String CountyId = c.getId();
			//System.out.println(cityId);
			if (Salary.containsKey(CountyId)) {
			Float averangeSalary = Salary.get(CountyId);
			//System.out.println(averangeSalary);
			c.setProperty("salary", averangeSalary); 
			//System.out.println(c.getProperty("salary"));

				}
			else {
				c.setProperty("salary", "not information");
			}
			//System.out.println(city.getProperty("salary"));
			}
			
			//add properties of salary to cityMarkes 
			
			for (Marker c : cityMarkers) {
				c.setProperty("salary", "not information");
			}
			
			for (Marker m : countryMarkers) {
				m.setProperty("lastClicked", null);
			}
			
			
		for (Marker m : countryMarkers) {
			for (Marker c : cityMarkers) {
				if(c.getStringProperty("country").equals(m.getStringProperty("name"))) {
					//System.out.println(c.getStringProperty("country"));
					//System.out.println(m.getStringProperty("name"));
					String salary = m.getProperty("salary").toString();
					//System.out.println(salary);
					c.setProperty("salary", salary);
					//System.out.println(c.getStringProperty("salary"));
					
					}
							
			}
		
		}

		
		map.addMarkers(countryMarkers);
	
	    map.addMarkers(cityMarkers);
	    
	}
	


	public void draw () {
		background (150,150,150);
		map.draw();
		showTitle—ity(mouseX, mouseY);
		showTitle(mouseX, mouseY);
		keys();
		showShade ();
		showKey ();
		if (lastClicked != null) {
			showInfo ();
		}
	}
	
	//we implement the method of clicking the mouse button
	
	public void mouseClicked()
	{
		

		for (Marker country : countryMarkers) {
			if (country.isInside(map, mouseX, mouseY) && countClicked ==0) {
				lastClicked = country;
				showInfo();
				countClicked ++;
				country.setProperty("lastClicked", 1);
				checkCountClicked();
				System.out.println(countClicked);
			}
	
			else if (country.isInside(map, mouseX, mouseY) && countClicked ==1) {
				lastClicked2 = country;
				showInfo();
				countClicked ++;
				country.setProperty("lastClicked", 1);
				checkCountClicked();
				//System.out.println(countClicked);
			}	
			
			else if (country.isInside(map, mouseX, mouseY) && countClicked ==2) {
				countClicked ++;
				checkCountClicked();
				//System.out.println(countClicked);
			}	
				
			}

		
	}
	
	//This method needed for clear countClicked
	
	public void checkCountClicked() {
		if (countClicked > 2) {
			countClicked =0;
			lastClicked = null;
			for (Marker country : countryMarkers) {
				country.setProperty("lastClicked", null);	
			}

		
		}
	}
	
	//implement the method of show information for country when we clicked by contry
	public void showInfo () {
		if (countClicked ==1) {
			fill(255,255,255);
		rect(350,450,300,130);
		
		
		
		for (Marker m : countryMarkers) {
			if (m.getIntegerProperty("lastClicked") != null) {
				
			//m.getIntegerProperty("lastClicked");
				
				String countryID = m.getId();
				String countryName = m.getStringProperty("name");
				if (AverangeWage.containsKey(countryID)) {
				float wage = AverangeWage.get(countryID); 
				String wageOut = "GDP (PPP) per person:" + wage + "$ per year";				
				float population = Population.get(countryID);	

				String name = m.getStringProperty("name") + "";

				String pop = "Population: "+ population+ " Million";

				pushStyle();
				fill(0, 0, 0);
				textSize(16);
				text(name, 355, 460);
				textSize(12);
				textAlign(PConstants.LEFT, PConstants.TOP);
				text(pop, 355, 480);
				text(wageOut, 355, 495);
				if (Salary.containsKey(countryID)) {
				float salary = Salary.get(countryID);
				String salaryOut = "AverangeWage: " + salary + "$ per year" ;
				fill(0, 0, 0);
				text(salaryOut, 355, 510);		
				 }
				if (Happy.containsKey(countryName)) {
					float happy = Happy.get(countryName);
					String happyOut = "Happy index: " + happy + "" ;
					fill(0, 0, 0);
					text(happyOut, 355, 525);		
					 }
				if (Corruption.containsKey(countryName)) {
					float corruption = Corruption.get(countryName);
					String corruptionOut = "Corruption index: " + corruption + "" ;
					fill(0, 0, 0);
					text(corruptionOut, 355, 540);		
					 }
								
				
				popStyle();
				return;

				}
			}
		}
		//String name = getStringProperty("name");
		}
		if (countClicked ==2) {
			fill(255,255,255);
		rect(210,450,300,130);
		String countryID = lastClicked.getId();
		String countryName = lastClicked.getStringProperty("name");
		String name1 = lastClicked.getStringProperty("name") + "";
		fill(0, 0, 0);
		textSize(16);
		text(name1, 215, 460);
		textSize(12);
		if (Population.containsKey(countryID)) {
			float pop = Population.get(countryID); 
			String popOut = "Population:" + pop + " million";	
			text(popOut, 215, 480);
		}
		if (AverangeWage.containsKey(countryID)) {
			float wage = AverangeWage.get(countryID); 
			String wageOut = "GDP (PPP) per person:" + wage + "$ per year";	
			text(wageOut, 215, 495);
		}
		
		if (Salary.containsKey(countryID)) {
			float salary = Salary.get(countryID);
			String salaryOut = "AverangeWage: " + salary + "$ per year" ;
			fill(0, 0, 0);
			text(salaryOut, 215, 510);		
			 }
			if (Happy.containsKey(countryName)) {
				float happy = Happy.get(countryName);
				String happyOut = "Happy index: " + happy + "" ;
				fill(0, 0, 0);
				text(happyOut, 215, 525);		
				 }
			if (Corruption.containsKey(countryName)) {
				float corruption = Corruption.get(countryName);
				String corruptionOut = "Corruption index: " + corruption + "" ;
				fill(0, 0, 0);
				text(corruptionOut, 215, 540);		
				 }
							
			

		
		fill(255,255,255);
		rect(540,450,300,130);
		String countryID2 = lastClicked2.getId();
		String countryName2 = lastClicked2.getStringProperty("name");
		fill(0, 0, 0);
		textSize(16);
		text(countryName2, 545, 460);
		textSize(12);
		if (Population.containsKey(countryID2)) {
			float pop = Population.get(countryID2); 
			String popOut = "Population:" + pop + " million";	
			text(popOut, 545, 480);
		}
		if (AverangeWage.containsKey(countryID2)) {
			float wage = AverangeWage.get(countryID2); 
			String wageOut = "GDP (PPP) per person:" + wage + "$ per year";	
			text(wageOut, 545, 495);
		}
		
		if (Salary.containsKey(countryID2)) {
			float salary = Salary.get(countryID2);
			String salaryOut = "AverangeWage: " + salary + "$ per year" ;
			fill(0, 0, 0);
			text(salaryOut, 545, 510);		
			 }
			if (Happy.containsKey(countryName2)) {
				float happy = Happy.get(countryName2);
				String happyOut = "Happy index: " + happy + "" ;
				fill(0, 0, 0);
				text(happyOut, 545, 525);		
				 }
			if (Corruption.containsKey(countryName2)) {
				float corruption = Corruption.get(countryName2);
				String corruptionOut = "Corruption index: " + corruption + "" ;
				fill(0, 0, 0);
				text(corruptionOut, 545, 540);		
				 }
		}
	}
	
	//implement method when we moved mouse
	public void mouseMoved()
	{
		// clear the last selection
		if (lastSelected != null) {
			lastSelected.setSelected(false);
			lastSelected = null;

		
		}
		selectMarkerIfHover(cityMarkers);
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
	
	//implement method when cursor cursor on City marker and show information
	private void showTitle—ity(float x, float y) {
		
		for (Marker m : cityMarkers) {
			if (m.isSelected()) {
				String name = "City: "  +m.getStringProperty("name") + " ";
				String pop = "Pop: " + Float.parseFloat(m.getStringProperty("population")) + " Million";
				String salary = "«‡ÔÎ‡Ú‡ ‚ „Ó‰: " +m.getProperty("salary").toString();
				//System.out.println(salary);
				pushStyle();
				
				fill(255, 255, 255);
				textSize(12);
				rectMode(PConstants.CORNER);
				rect(x, y-10-39, Math.max(textWidth(name), textWidth(salary)) + 6, 60);
				fill(0, 0, 0);
				textAlign(PConstants.LEFT, PConstants.TOP);
				text(name, x+3, y-43);
				text(pop, x+3, y - 28);
				text(salary, x+3, y - 15);
				popStyle();
				
					
					
				}
			}
		}
		
		
	
	//implement method when cursor cursor on country Marker and show information
	
	private void showTitle(float x, float y) {
		
		for (Marker m : countryMarkers) {
			if (m.isSelected()) {
				String countryID = m.getId();
				if (AverangeWage.containsKey(countryID)) {
				float wage = AverangeWage.get(countryID);
				String wageOut = "Wage:" + wage + "$ per year";	
				float population = Population.get(countryID);			
				
			
			
			
				fill(255,255,255);
				
				
				String name = m.getStringProperty("name") + "";
				
			
				String pop = "Pop: "+ population+ " Million";

				
				pushStyle();
				
				fill(255, 255, 255);
				textSize(12);
				rectMode(PConstants.CORNER);
				rect(x+10, y+10, Math.max(textWidth(name), textWidth(wageOut)) + 6, 54);
				fill(0, 0, 0);
				textAlign(PConstants.LEFT, PConstants.TOP);
				text(name, x+12, y+18);
				text(pop, x+12, y+33);
				text(wageOut, x+12, y+48);
				
					
					
				}
			}
		}
		
		
	}

	//load data to HashMap
	
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
	

	//load data to HashMap
	
	private Map<String, Float> loadPopulationCSV(String fileName) {
		Map <String, Float> Population = new HashMap <String, Float> ();
		String [] rows = loadStrings (fileName); 
		for (String row : rows) {
			String[] columns = row.split(";");
			if (columns.length == 4 && !columns[2].equals("") ) {
			Population.put(columns[1], Float.parseFloat(columns[2]));
			}
			
		
		}
		return Population;
	}
	
	
	//load data to HashMap
	private Map<String, Float> loadHappyCSV(String fileName) {
		Map <String, Float> Happy = new HashMap <String, Float> ();
		String [] rows = loadStrings (fileName); 

		for (String row : rows) {
			String[] columns = row.split(",");
			//System.out.println(columns.length);
			if (columns.length == 3 && !columns[2].equals("") ) {
			Happy.put(columns[1], Float.parseFloat(columns[2]));
			}
			
		
		}
		return Happy;
	}
	
	//load data to HashMap
	private Map<String, Float> loadCorruptionCSV(String fileName) {
		Map <String, Float> Corruption = new HashMap <String, Float> ();
		String [] rows = loadStrings (fileName); 

		for (String row : rows) {
			String[] columns = row.split(",");
			//System.out.println(columns.length);
			if (columns.length == 3) {
			Corruption.put(columns[1], Float.parseFloat(columns[0]));
			}
			
		
		}
		return Corruption;
	}
	
	//load data to HashMap
	private Map<String, Float> loadSalaryCSV(String fileName) {
		Map <String, Float> Salary = new HashMap <String, Float> ();
		String [] rows = loadStrings (fileName); 
		for (int i=0; i <rows.length; i++) {
			//System.out.println(rows[i]);
		}
		
		for (String row : rows) {
			String[] columns = row.split(",");
			//System.out.println(columns.length);
			String year= columns[4].substring(2,6);
			//System.out.println(year);
			if (year.equals("2019")) {
			Salary.put(columns[0].substring(1), Float.parseFloat(columns[12]));
			}
			
		}
	

		return Salary;
	}
	
	
	
	//implement method of shade country by Level GDP
	private void shadeGDP() {
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
	
	//implement method of shade country by Index of Happy
	private void shadeCountriesHappy () {
		for (Marker marker : countryMarkers) {
			String countryName = marker.getStringProperty("name");
			//System.out.println(countryName);
			
			if (Happy.containsKey(countryName)) {
				float happy = Happy.get(countryName);
				
				int colorLevel = (int) map (happy, 2, 10, 0, 255);
				marker.setColor(color(0,colorLevel,0));
			
			}
			
			else {
				marker.setColor(color(20,20,20));
			}
		}
	}
	
	//implement method of shade country by index of Corruption
	private void shadeCountriesCorruption () {
		for (Marker marker : countryMarkers) {
			String countryName = marker.getStringProperty("name");
			//System.out.println(countryName);
			
			if (Corruption.containsKey(countryName)) {
				float corruption = Corruption.get(countryName);
				
				int colorLevel = (int) map (corruption, 8, 88, 0, 210);
				marker.setColor(color(colorLevel,0,0));
			
			}
			
			else {
				marker.setColor(color(20,20,20));
			}
		}

	}
	
	
	private void keys () {
		fill(255,255,255);
		rect(30,50,140,600);
	}
	
	
	//implement method of shade country by level GDP, Index of Happy,
	//depending on the mouse click
	public void mouseReleased() {
		if (mouseX > 50 && mouseX < 150
				&& mouseY > 70 && mouseY < 120) {
			shadeGDP();
			fill(255,255,255);
		
			}
		else if (mouseX > 50 && mouseX < 150
				&& mouseY > 130 && mouseY < 180) {
			shadeCountriesHappy(); }
		else if (mouseX > 50 && mouseX < 150
				&& mouseY > 190 && mouseY < 250) {
			shadeCountriesCorruption ();
			}
	
	}
	
	//add buttons to keyboard
	private void showShade () {
		fill(0,0,255);
		rect(50,70, 100, 50);
		fill(0,255,0);
		rect(50,130, 100, 50);
		fill(255,0,0);
		rect(50,190, 100, 50);
		fill(0, 255, 255);
		ellipse(100,280,20,20);
		fill(0, 0, 0);
		text("Show GDP \n per capita", 55,80);
		text("Show \n Happy", 55,140);
		text("Show \n Corruption", 55,200);
		
		text("Size ~ AverangeWage", 40, 300);
	}
	
	//Implement method for show indicator level on map
	private void showKey () {
		fill(255,255,255);
		rect(210,590,630,50);
		
		int [] colorLevel = colorLevel ();
		if (mouseX > 50 && mouseX < 150
				&& mouseY > 70 && mouseY < 120) {

		fill(0,0,colorLevel[0]);
		rect(225,600, 120,30 );
		fill(0,0,colorLevel[1]);
		rect(345,600, 120,30 );
		fill(0,0,colorLevel[2]);
		rect(465,600, 120,30 );
		fill(0,0,colorLevel[3]);
		rect(585,600, 120,30 );
		fill(0,0,colorLevel[4]);
		rect(705,600, 120,30 );
		}

		else if (mouseX > 50 && mouseX < 150
				&& mouseY > 130 && mouseY < 180) {
			fill(0,colorLevel[0],0);
			rect(225,600, 120,30 );
			fill(0,colorLevel[1],0);
			rect(345,600, 120,30 );
			fill(0,colorLevel[2],0);
			rect(465,600, 120,30 );
			fill(0,colorLevel[3],0);
			rect(585,600, 120,30 );
			fill(0,colorLevel[4],0);
			rect(705,600, 120,30 );
			}
		else if (mouseX > 50 && mouseX < 150
				&& mouseY > 190 && mouseY < 250) {
			fill(colorLevel[0],0,0);
			rect(225,600, 120,30 );
			fill(colorLevel[1],0,0);
			rect(345,600, 120,30 );
			fill(colorLevel[2],0,0);
			rect(465,600, 120,30 );
			fill(colorLevel[3],0,0);
			rect(585,600, 120,30 );
			fill(colorLevel[4],0,0);
			rect(705,600, 120,30 );
				}

	}
	

	//helper method for choosing a color for a method showKeys
	private int [] colorLevel () {
		if (mouseX > 50 && mouseX < 150
				&& mouseY > 70 && mouseY < 120) {
			int [] colorLevelGDP = new int [5];

			int colorLevel1 = (int) map (1001, 1000, 140000, 10, 255);
			int colorLevel2 = (int) map (30000, 2, 10, 10, 255);
			int colorLevel3 = (int) map (60000, 2, 10, 10, 255);
			int colorLevel4 = (int) map (90000, 2, 10, 10, 255);
			int colorLevel5 = (int) map (139000, 2, 10, 10, 255);
			colorLevelGDP[0] = colorLevel1;
			colorLevelGDP[1] = colorLevel2;
			colorLevelGDP[2] = colorLevel3;
			colorLevelGDP[3] = colorLevel4;
			colorLevelGDP[4] = colorLevel5;
			return colorLevelGDP; }
			
			else if (mouseX > 50 && mouseX < 150
					&& mouseY > 130 && mouseY < 180) {
				int [] colorLevelHappy = new int [5];
				int colorLevel1 = (int) map (2, 2, 10, 10, 255);
				int colorLevel2 = (int) map (4, 2, 10, 10, 255);
				int colorLevel3 = (int) map (6, 2, 10, 10, 255);
				int colorLevel4 = (int) map (8, 2, 10, 10, 255);
				int colorLevel5 = (int) map (10, 2, 10, 10, 255);
				colorLevelHappy[0] = colorLevel1;
				colorLevelHappy[1] = colorLevel2;
				colorLevelHappy[2] = colorLevel3;
				colorLevelHappy[3] = colorLevel4;
				colorLevelHappy[4] = colorLevel5;
				return colorLevelHappy;
				
			}	
			else if (mouseX > 50 && mouseX < 150
					&& mouseY > 190 && mouseY < 250) {
				int [] colorLevelCorruption = new int [5];
				int colorLevel1 = (int) map (9, 8, 88, 10, 210);
				int colorLevel2 = (int) map (20, 2, 10, 10, 210);
				int colorLevel3 = (int) map (40, 2, 10, 10, 210);
				int colorLevel4 = (int) map (60, 2, 10, 10, 210);
				int colorLevel5 = (int) map (87, 8, 88, 10, 210);
				colorLevelCorruption[0] = colorLevel1;
				colorLevelCorruption[1] = colorLevel2;
				colorLevelCorruption[2] = colorLevel3;
				colorLevelCorruption[3] = colorLevel4;
				colorLevelCorruption[4] = colorLevel5;
				return colorLevelCorruption;
				
			}	
			
			else {
				int [] colorLevelNull = new int [5];
				return colorLevelNull;
			}
		
					
	
	}

}
