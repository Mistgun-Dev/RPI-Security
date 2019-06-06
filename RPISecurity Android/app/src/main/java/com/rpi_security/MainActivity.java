package com.rpi_security;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.File;

/**
 * Classe MainActivity : Cette classe est la seule activité du programme, elle permet l'initialisation du programme, et
 * l'initialisation de la barre de navigation du Menu.
 */


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //Variables du Menu de Navigation
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSideBar();  //Initialisation de la barre de navigation du Menu
        createRepositoriesApp();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageCameraFragment(), "FRAGMENT_MANEGE_CAMERA").commit();
    }

    /**
     * Fonction initialisant la barre de navigation du menu
     */
    void initSideBar()
    {
        mToolBar = (Toolbar)findViewById(R.id.navigation_toolbar);
        setSupportActionBar(mToolBar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToogle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Fonction créant les répertoires au démarrage de l'application si il n'éxistent pas
     * Répertoire principale : "sdcard/RPISecurity"
     * Répertoire ScreenShots : "sdcard/RPISecurity/ScreenShots"
     */
    void createRepositoriesApp()
    {
        File mainDirRPI = new File("/sdcard/RPISecurity");
        if (!mainDirRPI.exists()) {
            mainDirRPI.mkdir();
        }

        File screenShotsDir = new File("/sdcard/RPISecurity/ScreenShots");
        if (!screenShotsDir.exists()) {
            screenShotsDir.mkdir();
        }
    }

    /**
     * Fonction d'évenements permettant de gérer les actions liés au différents choix du Menu
     * Chaque choix permet l'affichage de son Fragment correspondant
     * @param menuItem Item pointé lors du click
     * @return true si un choix a été séléctionné par l'utilisateur
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.nav_camera:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageCameraFragment(), "FRAGMENT_MANEGE_CAMERA").commit();
                break;
            case R.id.nav_historique:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IntrusionHistoryFragment(), "FRAGMENT_HISTORY").commit();
                break;
            case R.id.nav_images:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScreenShotsFragment(), "FRAGMENT_PICTURES").commit();
                break;
            case R.id.nav_info:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InfoFragment(), "FRAGMENT_INFO").commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment(), "FRAGMENT_SETTINGS").commit();
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Fonction permettant la gestion des évènements lors d'un appui retour
     * Gestion d'affichage des fragments lors d'un appui retour, les fragments ne possédent pas de fonction onBackPressed()
     * la gestion des retours est éfféctués dans la classe parent : MainActivity
     */
    @Override
    public void onBackPressed()
    {
        //Si Fragment courant = fragment enfant, on remonte au fragment parent
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 ){
            getSupportFragmentManager().popBackStack();
            return;
        }
        //Si fragment courant fait partie des fragment parent, on retourne au fragment principal
        else
        {
            Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            //Si fragment parent == premier fragment, on quitte normalement l'application
            if(f instanceof ManageCameraFragment) {
                //this.finish;
            }
            //Sinon on retourne au premier fragment
            else
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ManageCameraFragment(), "FRAGMENT_MANAGE_CAM").commit();
                Log.e("EE", "ON REPART AU FRAGMENT PRINCIPAL");
                return;
            }
        }

        //Ranger la barre de navigation du Menu si elle est ouverte et qu'on appui sur retour
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToogle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}