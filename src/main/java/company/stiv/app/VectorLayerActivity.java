package company.stiv.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;

public class VectorLayerActivity extends AppCompatActivity {

    MapView MapVectorLayer;
    ArcGISTiledLayer TiledLayerMap;
    DrawerLayout ViewDrawerLayout;
    ListView ListDrawer;
    ActionBarDrawerToggle Toggle;

    String[] NavigationItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vector_layer);

        //Agregar MapView a la Vista (Layout)
        MapVectorLayer = findViewById(R.id.map_vector_layer);


        // Creacion Mapa Base
        TiledLayerMap = new ArcGISTiledLayer(getString(R.string.usgs_topo_url));


        Basemap basemap= new Basemap(TiledLayerMap);
        ArcGISMap map= new ArcGISMap(basemap);
        Viewpoint viewpoint = new Viewpoint(47.606726, -122.335564, 72223.819286);
        map.setInitialViewpoint(viewpoint);

        MapVectorLayer.setMap(map);

        // Agregar Navegacion

        NavigationItems = getResources().getStringArray(R.array.vector_tiled_types);

        ViewDrawerLayout = findViewById(R.id.drawer_layout_map);

        ListDrawer = findViewById(R.id.left_drawer);

        ListDrawer.setAdapter(new ArrayAdapter<>(this,R.layout.drawer_list_item,NavigationItems));


        ListDrawer.setOnItemClickListener(new ItemClickListener());

        ListDrawer.setItemChecked(0,true);

        setupDrawer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MapVectorLayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MapVectorLayer.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapVectorLayer.dispose();
    }

    private void setupDrawer(){

        Toggle =new ActionBarDrawerToggle(this,ViewDrawerLayout,R.string.drawer_open,R.string.drawer_close){

            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        Toggle.setDrawerIndicatorEnabled(true);
        ViewDrawerLayout.addDrawerListener(Toggle);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

        }
        setTitle(getString(R.string.vector_tiled_layer,NavigationItems[0]));
    }

    private class ItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick (AdapterView<?> parent, View view, int position, long id ){
            selectioItem(position);
        }
    }


    private void selectioItem(int position){
        ListDrawer.setItemChecked(position,true);
        setTitle(getString(R.string.vector_tiled_layer,NavigationItems[position]));
        ViewDrawerLayout.closeDrawer(ListDrawer);

        String vectorLayerTiled = null;

        switch (position){
            case 0:
                vectorLayerTiled = getString(R.string.usgs_topo_url);
                break;

            case 1:
                vectorLayerTiled = getString(R.string.usgs_hydro_url);
                break;

            case 2:
                vectorLayerTiled = getString(R.string.usgs_shade_url);
                break;

            case 3:
                vectorLayerTiled = getString(R.string.usgs_imagery_url);
                break;
        }

        TiledLayerMap = new ArcGISTiledLayer(vectorLayerTiled);


        MapVectorLayer.getMap().setBasemap(new Basemap(TiledLayerMap));


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        Toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vector_layer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Activate the navigation drawer toggle
        return (Toggle.onOptionsItemSelected(item)) || super.onOptionsItemSelected(item);
    }
}
