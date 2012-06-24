package com.android.rgapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class MapsActivity extends MapActivity implements LocationListener
{
	private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;
	private GeoPoint currentPoint;
	private Location currentLocation;
	private List<Overlay> mapOverlays;
	private Projection projection;  
	private GeoPoint gp1;
	private GeoPoint gp2;
	int lat;
	int lon;
	Double d1;
	Double d2;
	
	//Reading the xml file for weather data
    ReadXMLFile rxf = new ReadXMLFile();
    
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public class RouteOverlay extends Overlay{ 

		private GeoPoint mgp1;
		private GeoPoint mgp2;
		//private int mRadius=6;
		//private int defaultColor;
		//private String text="";
		//private Bitmap img = null;



		public RouteOverlay() { // GeoPoint is a int. (6E)
		    
		}

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			super.draw(canvas, mapView, shadow);
		    Projection projection = mapView.getProjection();
		    
		        Paint paint = new Paint();
		        paint.setDither(true);
		        paint.setAntiAlias(true);
		        paint.setStyle(Paint.Style.FILL_AND_STROKE);
		        paint.setStrokeJoin(Paint.Join.ROUND);
		        paint.setStrokeCap(Paint.Cap.ROUND);
		        paint.setStrokeWidth(2);
		        
		               
		        Point p1 = new Point();
		        Point p2 = new Point();
		        
		        Path path = new Path();
		        try{

			    	
					InputStreamReader inputreader = new InputStreamReader(getAssets().open("map.txt"));
					BufferedReader br = new BufferedReader(inputreader);
					String strLine;
					int i = 0;
					int g = 0;
					
					GeoPoint g1[] = null;
					String array[][] = null;
						
					
					 while ((strLine = br.readLine()) != null)   {
					 // Print the content on the console
					 System.out.println (strLine);
					 StringTokenizer st1 = new StringTokenizer(strLine, ";");
					 int elementNumber = st1.countTokens();
					 
					 while (elementNumber > 0)
					 {
						 		 String strco = st1.nextToken();
						 		 StringTokenizer st2 = new StringTokenizer(strco, ",");
					     		 while (st2.hasMoreTokens())
					     		 {
						         array[i][0] =  st2.nextToken();
						         array[i][1] = st2.nextToken();
						         try
						         {
						         d1 = (Double.parseDouble(array[i][0]));
						         //lat = (Integer.parseInt(d1.toString()));
						         lat = (int)(d1 * 1E6);
						         d2 = (Double.parseDouble(array[i][1]));
						        // lon = Integer.parseInt(d2.toString());
						         lon = (int)(d2 * 1E6);
						         g1[g] = new GeoPoint(lat, lon);
						      
						         System.out.println(lat);
					        	 System.out.println(lon);
						         }
						         catch(Exception e)
						         {
						        	 System.out.println("ERROR!!");
						        	 System.out.println(lat);
						        	 System.out.println(lon);
						        	 System.out.println(array[i][0]);
						        	 System.out.println(d1);
						        	 System.out.println(elementNumber);
						         }
						         
						         }
					     		 if(g>0)
					     		 {
					     		 gp1 = g1[g--];
					     		 gp2 = g1[g++];
					     		projection.toPixels(gp1, p1);
								projection.toPixels(gp2, p2);
						        
								path.moveTo(p2.x, p2.y);
								path.lineTo(p1.x,p1.y);

								canvas.drawPath(path, paint);
								
					     		}
					     		 g++;
					     		 elementNumber--;
					     		 i++;
					 }
					 }
					 br.close();
					 inputreader.close();
					 mapView.invalidate();
		        }
		        catch (Exception e){//Catch exception if any
		    		System.err.println("Error: " + e.getMessage());
		    	}
		        
		        
				return true;
					 }
			      
		   
		    }
	
	public static Context getContext()
	{
		return getContext();
	}
	
@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.main);
	    
	    
	    
	    mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    mapView.setSatellite(false);
	    mapView.setStreetView(true);
	    mapController = mapView.getController();
	    mapController.setZoom(15);
	    mapView.bringToFront();
	    
	   getLastLocation();
	    animateToCurrentLocation();
	    
	    mapOverlays = mapView.getOverlays();        
	    projection = mapView.getProjection();
	    mapOverlays.add(new RouteOverlay()); 
	    
	 /*   
	    ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
        	//find the current intent of the map
        	//Intent mapIntent = new Intent(Intent.ACTION_VIEW); 
       	    Uri uri1 = Uri.parse("geo:0,0?q=http://www.raleighnc.gov/content/PRecDesignDevelop/Documents/Maps/SystemMaps/KML/TheCapitalAreaGreenwaySystem.kmz"); 
       	    Intent mapIntent = new Intent(MapsActivity.this, mapView.getClass());
       	    //Uri uri1 = Uri.parse("geo:0,0?q=file:///C:/Users/Owner/Downloads/RGreenway/greenway.kmz");
        	//Uri uri1 = Uri.parse("geo:0,0?q=file:///RGreenWay/forecast.kml"); 
        	mapIntent.setData(uri1);
       	    startActivity(Intent.createChooser(mapIntent, "Greenway Trails"));
           }
        });
        */
	    
	    //Calendar button
	    
	    final ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        button1.setOnTouchListener(new OnTouchListener() {
        	
           public boolean onTouch(View v, MotionEvent event) {
        	 if (event.getAction() == MotionEvent.ACTION_DOWN) {
            	 
        		 //send webview to the back
        		 MapView mapView = (MapView) findViewById(R.id.mapview);
                 mapView.bringToFront();
                
               //Reading the xml file for weather data
                 ReadXMLFile rxf = new ReadXMLFile();
                 rxf.readXMLData();
                 
        		 
                 //just to display, not active code
        		 ImageButton ib = (ImageButton) findViewById(R.id.cal);
        		 ib.setImageResource(R.drawable.cal);
        		 ib.bringToFront();
           	  


                 TextView cal2 = (TextView) findViewById(R.id.calendar2);
                 cal2.setBackgroundResource(R.color.white);
                 cal2.setTextSize(58);
                 cal2.setText(" "+rxf.date + " ");
                 cal2.bringToFront();
                 TextView cal = (TextView) findViewById(R.id.calendar1);
                 cal.setBackgroundResource(R.color.white);
                 cal.setTextSize(20);
                 cal.setText("     "+rxf.month +"                  Events >");
                 
                 cal.bringToFront();
                 TextView cal3 = (TextView) findViewById(R.id.calendar3);
                 cal3.setBackgroundResource(R.color.white);
                 cal3.setTextSize(18);
                 cal3.setText(rxf.weekday);
                 cal3.bringToFront();
                 
                 
            	//load active and inactive images into the image buttons
        		  ImageButton button = (ImageButton) findViewById(R.id.button1);
                  button.setImageResource(R.drawable.ic_launcher_cal);
                  button.setBackgroundResource(R.color.white);
                  button.bringToFront();
                 
                  button = (ImageButton) findViewById(R.id.button3);
                  button.setImageResource(R.drawable.ic_launcher_workin);
                  button.setBackgroundResource(R.color.white);
                  button.bringToFront();
                  
                  button = (ImageButton) findViewById(R.id.button2);
                  button.setImageResource(R.drawable.ic_launcher_weatherin);
                  button.setBackgroundResource(R.color.white);
                  button.bringToFront();
                 
                  button = (ImageButton) findViewById(R.id.button4);
                  button.setImageResource(R.drawable.ic_launcher_socin);
                  button.setBackgroundResource(R.color.white);
                  button.bringToFront();
                  
                  button = (ImageButton) findViewById(R.id.button5);
                  button.setImageResource(R.drawable.ic_launcher_scfin);
                  button.setBackgroundResource(R.color.white);
                  button.bringToFront();
                  
                  ImageView vw = (ImageView) findViewById(R.id.banner);
                  vw.bringToFront();
                
        	 }

               return false;
           }
       });
	    
        //Weather Button
        
        final ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        button2.setOnTouchListener(new OnTouchListener() {
        	
           public boolean onTouch(View v, MotionEvent event) {
        	 if (event.getAction() == MotionEvent.ACTION_DOWN) {
        		  
        		  //send webview to the back
        		  MapView mapView = (MapView) findViewById(R.id.mapview);
                  mapView.bringToFront();
                  
                  
                  
                  //just to display not active code
                 rxf.readXMLData();
                 TextView tb = (TextView) findViewById(R.id.temp);
          		 tb.setText(rxf.tempf);
          		 tb.setTextSize(34);
         		 tb.setBackgroundResource(R.color.white);
         		 tb.bringToFront();
                 tb = (TextView) findViewById(R.id.weather);
         		// tb.setText("                                    " + rxf.weather + "\n" + "            " + rxf.tempf + "               " + rxf.humidity  + "\n" + "         " + "Raleigh, NC");
         		 tb.setText("" + rxf.weather +"\n"+ "" +rxf.humidity);
                 tb.setTextSize(18);
         		 tb.setBackgroundResource(R.color.white);
         		 tb.bringToFront();
         		 
         		 
         		 
         		 ImageView ig = (ImageView) findViewById(R.id.weathericon);
         		 
         		 try {
         		 URL newurl = new URL(rxf.icon);
         		 Bitmap iconval = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
         		 ig.setImageBitmap(iconval);
         		
         		 ig.bringToFront();
         		 }
         		 catch(Exception e)
         		 {
         			 
         		 }
         	//	 im.setImageResource(R.drawable.ic_launcher_cal);
         	//	 im.bringToFront();
            	  
                  //load active and inactive images into the image buttons
                  ImageButton button = (ImageButton) findViewById(R.id.button2);
                  button.setImageResource(R.drawable.ic_launcher_weather);
                  button.setBackgroundResource(R.color.white);
                  
                  button = (ImageButton) findViewById(R.id.button1);
                  button.setImageResource(R.drawable.ic_launcher_calin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button3);
                  button.setImageResource(R.drawable.ic_launcher_workin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button4);
                  button.setImageResource(R.drawable.ic_launcher_socin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button5);
                  button.setImageResource(R.drawable.ic_launcher_scfin);
                  button.setBackgroundResource(R.color.white);
                  
                  
                  
                              
        	 }

               return false;
           }
       });
        
        //Workout button
        final ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        button3.setOnTouchListener(new OnTouchListener() {
        	
           public boolean onTouch(View v, MotionEvent event) {
        	 if (event.getAction() == MotionEvent.ACTION_DOWN) {
        		
        		 //send webview to the back
        		 MapView mapView = (MapView) findViewById(R.id.mapview);
                 mapView.bringToFront();
                 
               //just to display not active code
                 ImageButton ib = (ImageButton) findViewById(R.id.cal);
         		 ib.setImageResource(R.drawable.workout);
         		 ib.bringToFront();;
        		 
                 //load active and inactive images into the image buttons
            	  ImageButton button = (ImageButton) findViewById(R.id.button3);
                  button.setImageResource(R.drawable.ic_launcher_work);
                  button.setBackgroundResource(R.color.white);
                  
                  button = (ImageButton) findViewById(R.id.button1);
                  button.setImageResource(R.drawable.ic_launcher_calin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button2);
                  button.setImageResource(R.drawable.ic_launcher_weatherin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button4);
                  button.setImageResource(R.drawable.ic_launcher_socin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button5);
                  button.setImageResource(R.drawable.ic_launcher_scfin);
                  button.setBackgroundResource(R.color.white);
        	 }

               return false;
           }
       });
        
        
        //social networking button
        final ImageButton button4 = (ImageButton) findViewById(R.id.button4);
        button4.setOnTouchListener(new OnTouchListener() {
        	
           public boolean onTouch(View v, MotionEvent event) {
        	 if (event.getAction() == MotionEvent.ACTION_DOWN) {
        		 
        		 //send webview to the back
        		 MapView mapView = (MapView) findViewById(R.id.mapview);
                 mapView.bringToFront();
                 
                 
                 //load active and inactive images into the image buttons
            	  ImageButton button = (ImageButton) findViewById(R.id.button4);
                  button.setImageResource(R.drawable.ic_launcher_soc);
                  button.setBackgroundResource(R.color.white);
                  
                  button = (ImageButton) findViewById(R.id.button1);
                  button.setImageResource(R.drawable.ic_launcher_calin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button3);
                  button.setImageResource(R.drawable.ic_launcher_workin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button2);
                  button.setImageResource(R.drawable.ic_launcher_weatherin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button5);
                  button.setImageResource(R.drawable.ic_launcher_scfin);
                  button.setBackgroundResource(R.color.white);
               
        	 }

               return false;
           }
       });
        
        //SeeClickFix button
        final ImageButton button5 = (ImageButton) findViewById(R.id.button5);
        button5.setOnTouchListener(new OnTouchListener() {
        	
           public boolean onTouch(View v, MotionEvent event) {
        	 if (event.getAction() == MotionEvent.ACTION_DOWN) {
        		 
        		//send webview to the back
        		 MapView mapView = (MapView) findViewById(R.id.mapview);
                 mapView.bringToFront();
                 
        		  //load active and inactive images into the image buttons
            	  ImageButton button = (ImageButton) findViewById(R.id.button5);
                  button.setImageResource(R.drawable.ic_launcher_scf);
                  button.setBackgroundResource(R.color.white);
                  
                  button = (ImageButton) findViewById(R.id.button1);
                  button.setImageResource(R.drawable.ic_launcher_calin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button3);
                  button.setImageResource(R.drawable.ic_launcher_workin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button4);
                  button.setImageResource(R.drawable.ic_launcher_socin);
                  button.setBackgroundResource(R.color.white);
                  button = (ImageButton) findViewById(R.id.button2);
                  button.setImageResource(R.drawable.ic_launcher_weatherin);
                  button.setBackgroundResource(R.color.white);
                  
                  WebView webView = (WebView) findViewById(R.id.webView1);
                  webView.bringToFront();
                  webView.getSettings().setJavaScriptEnabled(true);
                  webView.getSettings().setSupportZoom(true);
                  webView.getSettings().setBuiltInZoomControls(true);
                  webView.setWebViewClient(new WebViewClient());
                  webView.loadUrl("http://seeclickfix.com/raleigh/report");
                  
               
        	 }

               return false;
           }
       });
        
   

} 
        
	public void getLastLocation(){
	    String provider = getBestProvider();
	    currentLocation = locationManager.getLastKnownLocation(provider);
	    if(currentLocation != null){
	        setCurrentLocation(currentLocation);
	    }
	    else
	    {
	        Toast.makeText(this, "Location not yet acquired", Toast.LENGTH_LONG).show();
	    }
	}
	 
	public void animateToCurrentLocation(){
	    if(currentPoint!=null){
	        mapController.animateTo(currentPoint);
	    }
	}
	 
	public String getBestProvider(){
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    Criteria criteria = new Criteria();
	    criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
	    criteria.setAccuracy(Criteria.NO_REQUIREMENT);
	    String bestProvider = locationManager.getBestProvider(criteria, true);
	    return bestProvider;
	}
	 
	public void setCurrentLocation(Location location){
	    int currLatitude = (int) (location.getLatitude()*1E6);
	    int currLongitude = (int) (location.getLongitude()*1E6);
	    currentPoint = new GeoPoint(currLatitude,currLongitude);
	    
	    
	    currentLocation = new Location("");
	    currentLocation.setLatitude(currentPoint.getLatitudeE6() / 1e6);
	    currentLocation.setLongitude(currentPoint.getLongitudeE6() / 1e6);
	}
	
	


	@Override
	public void onLocationChanged(Location newLocation) {
		// TODO Auto-generated method stub
		setCurrentLocation(newLocation);
	}
	
	@Override
	protected void onResume() {
	    super.onResume();
	    locationManager.requestLocationUpdates(getBestProvider(), 1000, 1, this);
	}
	 
	@Override
	protected void onPause() {
	    super.onPause();
	    locationManager.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
}

