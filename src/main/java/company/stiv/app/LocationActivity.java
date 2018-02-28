package company.stiv.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;


import java.util.ArrayList;

public class LocationActivity extends AppCompatActivity {


    Spinner mySpinner;
    MapView mapLocationView;
    ArcGISMap myMap;
    LocationDisplay myLocationDisplay;

    int requestCode=2;
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


        myLocationDisplay.addDataSourceStatusChangedListener(new LocationDisplay.DataSourceStatusChangedListener() {
            @Override
            public void onStatusChanged(LocationDisplay.DataSourceStatusChangedEvent dataSourceStatusChangedEvent) {
                //Ubicacion Iniciada (ok)
                if(dataSourceStatusChangedEvent.isStarted())
                    return;

                //Error reportado
                if (dataSourceStatusChangedEvent.getError()== null)
                    return;


                //Chequear los permisos correspondientes

                boolean permissionCheck1 = ContextCompat.checkSelfPermission(LocationActivity.this,reqPermissions[0])== PackageManager.PERMISSION_GRANTED;
                boolean permissionCheck2 = ContextCompat.checkSelfPermission(LocationActivity.this,reqPermissions[1])== PackageManager.PERMISSION_GRANTED;

                if (!(permissionCheck1&&permissionCheck2)){
                    //Los permisos no estan, se solicita permisos al usuario
                    ActivityCompat.requestPermissions(LocationActivity.this,reqPermissions,requestCode);
                }else{
                    //Reporta al usuario que no tiene habilitado los servicios  en el dispositivo

                    String message = String.format("Error in DataSourceStatusChangedListener: %s",dataSourceStatusChangedEvent.getSource().getLocationDataSource().getError().getMessage());
                    Toast.makeText(LocationActivity.this,message,Toast.LENGTH_LONG).show();

                    //Actualiza la inteface y refleja la ubicacion en pantalla
                    mySpinner.setSelection(0,true);
                }
            }
        });

        //Lista de Opciones de ubicacbion para el spinner

        ArrayList<ItemData> list=new ArrayList<>();
        list.add(new ItemData("Stop",R.mipmap.locationdisplaydisabled));
        list.add(new ItemData("On",R.mipmap.locationdisplayon));
        list.add(new ItemData("Re-Center",R.mipmap.locationdisplayrecenter));
        list.add(new ItemData("Navigation",R.mipmap.locationdisplaynavigation));
        list.add(new ItemData("Compass",R.mipmap.locationdisplayheading));


        SpinnerAdapter adapter = new SpinnerAdapter(this,R.layout.spinner_location,R.id.txt,list);
         mySpinner.setAdapter(adapter);

         mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 switch (position)
                 {
                     case 0:
                         // Detener la ubicacion
                         if (myLocationDisplay.isStarted())
                             myLocationDisplay.stop();
                         break;

                     case 1:
                         // Iniciar la localizacion
                         if (!myLocationDisplay.isStarted())
                             myLocationDisplay.startAsync();
                         break;

                     case 2:
                         // Re-Centrar la vista de ubicacion
                         // AutoPanMode - Default: In this mode, the MapView attempts to keep the location symbol on-screen by
                         // re-centering the location symbol when the symbol moves outside a "wander extent". The location symbol
                         // may move freely within the wander extent, but as soon as the symbol exits the wander extent, the MapView
                         // re-centers the map on the symbol.
                         myLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);
                         if(!myLocationDisplay.isStarted())
                             myLocationDisplay.startAsync();
                         break;

                     case 3:
                         //Iniciar el modo navegacion
                         myLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.NAVIGATION);
                         if (!myLocationDisplay.isStarted())
                             myLocationDisplay.startAsync();
                         break;

                     case 4:
                         myLocationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.COMPASS_NAVIGATION);
                         if(!myLocationDisplay.isStarted())
                             myLocationDisplay.startAsync();


                         break;
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[]grantResults){
        // Cuando la peticion de permiso es cancelada

        if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            // Cuando es concebido el permiso, inicia la ubicacion
            myLocationDisplay.startAsync();
        }else {
            //Cuando el permiso es denegado muestra un mensaje Toasr

            Toast.makeText(LocationActivity.this,getResources().getString(R.string.location_permission_denied),Toast.LENGTH_SHORT).show();

            mySpinner.setSelection(0,true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapLocationView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapLocationView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapLocationView.dispose();
    }
}
