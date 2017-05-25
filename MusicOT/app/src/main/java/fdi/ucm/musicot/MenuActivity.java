package fdi.ucm.musicot;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import com.example.usuario_local.music_ot.R;

import fdi.ucm.musicot.Misc.Utils;
import fdi.ucm.musicot.Modelo.DAO;
import fdi.ucm.musicot.Modelo.Reproductor;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static DAO dao;

    private ArtistasFragment fragmentArtistas;
    private CancionesFragment fragmentCanciones;
    private AlbumesFragment fragmentAlbumes;
    public static ReproductorFragment fragmentReproductor;
    public static ReproductorFragmentMini fragmentMini;
    private TableLayout fragmentArtistasContenedor;

    public static MenuActivity menuActivity;

    public static Reproductor reproductor = null;
    private LinearLayout miniReproductorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(dao == null) {
            dao = new DAO();
        }

        if(reproductor == null) {
            reproductor = new Reproductor();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inicializamos los fragmentos de la actividad MAIN
        fragmentArtistas = new ArtistasFragment();
        fragmentArtistas.setRetainInstance(true);
        fragmentArtistas.setMenuActivity(this);

        fragmentCanciones = new CancionesFragment();
        fragmentCanciones.setRetainInstance(true);
        fragmentCanciones.setMenuActivity(this);

        fragmentAlbumes = new AlbumesFragment();
        fragmentAlbumes.setRetainInstance(true);
        fragmentAlbumes.setMenuActivity(this);

        fragmentReproductor = new ReproductorFragment();
        fragmentReproductor.setRetainInstance(true);

        fragmentMini = new ReproductorFragmentMini();
        fragmentMini.setRetainInstance(true);

        miniReproductorFragment = (LinearLayout) findViewById(R.id.mini_bot_reproductor);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(Utils.currentFragment == null) {
            Utils.currentFragment = fragmentReproductor;
            Utils.currentMiniFragment = fragmentMini;
        }
        transicionarMenuFragmento(R.id.mini_bot_reproductor, Utils.currentMiniFragment);
        cambiaFragment(R.id.fragment_contentmenu1, Utils.currentFragment);

        menuActivity = this;
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
        getMenuInflater().inflate(R.menu.menu, menu);
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_albumes) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Va a la ventana Albumes ( onClick )
     * @param menuItem
     */
    public void menuAlbumesOnClick(MenuItem menuItem){

        cambiaFragment(R.id.fragment_contentmenu1, fragmentAlbumes);
    }

    public void menuInicioOnClick(MenuItem menuItem){

        cambiaFragment(R.id.fragment_contentmenu1, fragmentReproductor);
    }

    /**
     * Va a la ventana Canciones ( onClick )
     * @param menuItem
     */
    public void menuTemasOnClick(MenuItem menuItem){

        cambiaFragment(R.id.fragment_contentmenu1, fragmentCanciones);

    }

    public void cambiaFragment(int idNewFragment, Fragment newFragment){

        if(Reproductor.isDeployed
                && idNewFragment==R.id.fragment_contentmenu1
                && newFragment != fragmentReproductor){

            findViewById(R.id.mini_bot_reproductor).setVisibility(View.VISIBLE);
        }else if(idNewFragment == R.id.fragment_contentmenu1
                && newFragment == fragmentReproductor){

            findViewById(R.id.mini_bot_reproductor).setVisibility(View.INVISIBLE);
        }

        transicionarMenuFragmento(idNewFragment, newFragment);
        Utils.currentFragment = newFragment;
    }

    /**
     * Va a la ventana Canciones ( onClick )
     * @param menuItem
     */
    public void menuArtistasOnClick(MenuItem menuItem){

        transicionarMenuFragmento(R.id.fragment_contentmenu1, fragmentArtistas);
    }

    /**
     * Sustituye el fragmento del contenedor con la ID dada por el nuevo fragmento dado, también
     * encoge el menú lateral si está dsplegado
     * @param id
     * @param newFragment
     */
    public void transicionarMenuFragmento(int id, Fragment newFragment){

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        transaction.replace(id, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public DAO getDAO(){

        return this.dao;
    }
}
