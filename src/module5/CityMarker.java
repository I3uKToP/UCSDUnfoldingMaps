package module5;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** Implements a visual marker for cities on an earthquake map
 * 
 * @author UC San Diego Intermediate Software Development MOOC team
 * @author Your name here
 *
 */
// TODO: Change SimplePointMarker to CommonMarker as the very first thing you do 
// in module 5 (i.e. CityMarker extends CommonMarker).  It will cause an error.
// That's what's expected.
public class CityMarker extends CommonMarker {
	
	public static int TRI_SIZE = 10;  // The size of the triangle marker
	
	public CityMarker(Location location) {
		super(location);
	}
	
	
	public CityMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
		// Cities have properties: "name" (city name), "country" (country name)
		// and "population" (population, in millions)
	}

	
	/**
	 * Implementation of method to draw marker on the map.
	 */

	
	/** Show the title of the city if this marker is selected */
	public void showTitle(PGraphics pg, float x, float y)
	{
		
		String name = getCity();
		float pop = getPopulation();
		
		String info = name + "\nНаселение:" +pop + " млн человек";
		
		pg.pushStyle();
		
		if (x < 450 ) {
		pg.fill(255,255,255);
		pg.rect(x+5, y-5, 150 ,37);
		
		pg.fill(0,0,0);
		pg.textSize(10);
		pg.text(info, x+10,y+10);
	
		}
		
		else {
			pg.fill(255,255,255);
			pg.rect(x-145, y-32, 150 ,37);
			
			pg.fill(0,0,0);
			pg.textSize(10);
			pg.text(info, x-140,y-17);
		}
		
		
		pg.popStyle();
		// TODO: Implement this method
	}
	
	
	
	/* Local getters for some city properties.  
	 */
	public String getCity()
	{
		return getStringProperty("name");
	}
	
	public String getCountry()
	{
		return getStringProperty("country");
	}
	
	public float getPopulation()
	{
		return Float.parseFloat(getStringProperty("population"));
	}


	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		// TODO Auto-generated method stub
		
		pg.pushStyle();
		float R =  (float) (TRI_SIZE * Math.sqrt(3)/3);
		float r = (float) (TRI_SIZE * Math.sqrt(3)/6);
		
		pg.fill(255,0,0);
		pg.triangle(x-TRI_SIZE/2, y+r, x+TRI_SIZE/2, y+r, x, y-R);
		
		
	}
}
