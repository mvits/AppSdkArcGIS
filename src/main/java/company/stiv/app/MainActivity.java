package company.stiv.app;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.MapView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MapView myMap;
    ArcGISMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /**Referencia al objeto map de la vista **/
        myMap =(MapView)findViewById(R.id.map);

        /**Crear una instancia vacia map**/
        map= new ArcGISMap(Basemap.Type.STREETS_NIGHT_VECTOR,4.710988599999999, -74.072092,9);


        /**Agregar a la vista de mapa**/
        myMap.setMap(map);
        /**App Code**/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        /**Limpiar contenido del map**/
        map= new ArcGISMap();


        int id = item.getItemId();



        switch (id){

            case R.id.exercise_1:
                /**Cargar Servicio de Mapa Base**/
                ArcGISMapImageLayer mapImageLayer= new ArcGISMapImageLayer(getResources().getString(R.string.world_topo_service));

                /**Agregar el layer correspondiente del mapa base al map**/
                map.getOperationalLayers().add(mapImageLayer);

                /**Agregar a la vista del mapa**/

                myMap.setMap(map);
                break;
            case R.id.exercise_2:
                //ConstraintLayout viewMap= (ConstraintLayout) findViewById(R.id.content);
                // viewMap.removeView(myMap);

                /** Example Inflated
                LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View vectorLayerLayout = inflater.inflate(R.layout.vector_layer,(ViewGroup)findViewById(R.id.drawer_layout_map));
                 viewMap.addView(vectorLayerLayout);**/

                Intent intent =new Intent(this,VectorLayerActivity.class);
                startActivity(intent);

                break;



        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onPause(){
        super.onPause();
        myMap.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        myMap.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myMap.dispose();
    }

}
