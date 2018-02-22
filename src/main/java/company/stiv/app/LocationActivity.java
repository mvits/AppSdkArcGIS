package company.stiv.app;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;

public class LocationActivity extends AppCompatActivity {


    Spinner mySpinner;
    MapView mapLocationView;
    ArcGISMap myMap;
    LocationDisplay myLocationDisplay;


    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_location);

        // Traer el spinner del layout
        mySpinner = (Spinner) findViewById(R.id.spinner_map);

        //Traer el mapView del layout
        mapLocationView =(MapView) findViewById(R.id.map_location);
        myMap = new ArcGISMap(Basemap.createImagery());
        mapLocationView.setMap(myMap);


        //Traer la localización del display
        myLocationDisplay = mapLocationView.getLocationDisplay();

    }
}
