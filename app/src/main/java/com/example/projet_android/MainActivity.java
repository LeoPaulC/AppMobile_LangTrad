package com.example.projet_android;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements Fragment_menu.OnFragmentInteractionListener , Fragment_question.OnFragmentInteractionListener , Fragment_reponse.OnFragmentInteractionListener, Fragment_bas.OnFragmentInteractionListener , AccesDonneesBDD.OnFragmentInteractionListener , Page_demarrage.OnFragmentInteractionListener , fragment_ma_liste_perso.OnFragmentInteractionListener , fragment_acces_memoire_tel.OnFragmentInteractionListener , ajout_BDD_trad.OnFragmentInteractionListener , Fragment_apprentissage_liste.OnFragmentInteractionListener{

    /**
     * Attributs :
     */
    // Liste Fragments graphique :
    static Fragment_menu fragment_menu ;
    static Fragment_question fragment_question ;
    static Fragment_reponse fragment_reponse ;
    static Fragment_bas fragment_bas ;
    static AccesDonneesBDD frag_accesBDD ;
    static Page_demarrage frag_init ;

    static int emplacement_haut ;
    static int emplacement_question ;
    static int emplacement_reponse ;
    static int emplacement_bas ;

    static LinearLayout layout_haut ;
    static LinearLayout layout_question ;
    static LinearLayout layout_reponse ;
    static LinearLayout layout_bas ;
    static FrameLayout layout_demarrage ;

    static FloatingActionButton fab ;


    // attribut static , a voir
    static ListView ma_liste_view ;

    // liste fragment "operationnel"


    // liste attribut :
    static Base_de_donnee bdd ;
    static AccesDonneesBDD accesDonnees;

    static FragmentManager fm ;
    static FragmentTransaction transaction ;

    static final String BUNDLE_CATEGORIE ="categorie" ;
    static final String BUNDLE_MOT_QUESTION ="mot" ;
    static final String BUNDLE_LANGUE1 ="langue1" ;
    static final String BUNDLE_LANGUE2 ="langue2" ;
    static final String BUNDLE_SCORE ="score" ;
    static final String BUNDLE_NIVEAU ="niveau" ;
    static final String BUNDLE_NOM ="nom" ;

    static List<Map<String , List<String> >> mes_listes_de_mot_a_apprendre_pour_plus_tard ;
    static ArrayList<ArrayList<String>> liste_de_mot ;
    /**
     * En gros ,plsueiurs liste de hashmap < langue , liste_de_mot<String> pour avoir une liste trié par langue , voir ventuellement pour ajouter les catgeroies en compte
     *
     * */

    static Bundle bundle_de_la_session_en_cours ;

    // pour la nav bar de gauche
    private AppBarConfiguration mAppBarConfiguration;


    /**
     * Fin attributs
     */

    // methode pour faciliter l'affichage de nos tests.
    public void faireToast(String message) {
        Toast annonce = Toast.makeText(this, message, Toast.LENGTH_LONG);
        annonce.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_view);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }




        // init BDD
        bdd = new Base_de_donnee(this);
        // init bundle de session .
        bundle_de_la_session_en_cours = new Bundle();
        //bundle_de_la_session_en_cours.putString(BUNDLE_LANGUE1,"1");
        /**
         * On met le score a Zero a chaque demarrage , pour l'instant
         */
        // Initialisation des emplacement ( pour modifier leurs tailles et donc etre modulable )
        emplacement_question = R.id.emplacement_fragment_question;
        emplacement_bas = R.id.emplacement_fragment_bas;
        emplacement_haut = R.id.emplacement_fragment_bar_du_haut;
        emplacement_reponse = R.id.emplacement_fragment_reponse;


        layout_haut = new LinearLayout(this) ;
        layout_haut = new LinearLayout(this) ;
        layout_haut = new LinearLayout(this) ;
        layout_haut = new LinearLayout(this) ;
        layout_demarrage = new FrameLayout(this);

        layout_haut = findViewById(R.id.layout_bar_du_haut);
        layout_question = findViewById(R.id.layout_question );
        layout_reponse = findViewById(R.id.layout_reponse );
        layout_bas = findViewById(R.id.layout_bas) ;


        Log.d(Base_de_donnee.TAG, "onCreate MainActivity : check .. " + layout_demarrage);

        /**
         * mes listes de mots a apprendre
         */
        mes_listes_de_mot_a_apprendre_pour_plus_tard= new ArrayList<Map<String,List<String>>>();
        liste_de_mot = new ArrayList<ArrayList<String>>();
        /**
         * donc on aura : [ .......] -> langue
         *                [........]  -> mot
         *                [........]  -> trad
         *
         *Faire une requete au demarrage pour recuperer la liste de la bdd
         *
         */

        HashMap hashmapliste = new HashMap();

        MainActivity.bundle_de_la_session_en_cours.putInt(MainActivity.BUNDLE_SCORE,0);


        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();

        ma_liste_view = findViewById(R.id.ma_liste_view);

        /**
         * Nous allons decomposer les fragments en differents constructers , de maniere a les rendre plus modulable et plus efficace.
         * Les fragments qui suivent correspondent a l'initialisation de notre Application .
         */

        // Gestionnaire de Fragment

        /**
         * premiere page d'initialisation de langue et de nom du pseudo .
         */
        /*
        frag_init = Page_demarrage.newInstance("init","user");
        transaction.add(R.id.emplacement_fragment_question,frag_init,"Init user").addToBackStack("Init user") ;
         */

        Log.d("d","Init user passer :)");



        // fragment Menu Graphique
        /*fragment_menu = Fragment_menu.newInstance("",""); // pour l'instant paramettre vide à a voir pour permettre de creer le menu avec .

        transaction
                .add(R.id.emplacement_fragment_bar_du_haut, fragment_menu, "menu_du_haut")
                .addToBackStack("menu_du_haut");

         */

        // Fragment Question Graphique

        /**
         * pour le demarage on choisie la Langues ...................
         */

        /**
         *
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * a verifier plus tard
         */

        /**
         * On a deokacé ça dans le menu de navigation a gauche
         */
        /*
        fragment_bas = Fragment_bas.newInstance("init" , "bdd"); // permet de passer des paramettre et donc effecturer des actions specifiques .
        transaction.add(R.id.emplacement_fragment_bas,fragment_bas,"Button_du_bas")
                .addToBackStack("Button_du_bas") ;

        // finalise les transaction de base .

         */
        transaction.commit();

        //faireToast("Fragments de base ajoutés.");

        // pour creer la NavBar
        /**
         *
         */
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools , R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);




        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Snackbar.make(v, "Votre Score précedent : " + Fragment_bas.bonne_reps, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        bdd.close();
        Log.d("d","STOOOOOOOOOOOOP , l'activité a quitté et la bdd a été fermé  : bdd - " + bdd.toString());

    }

    public static void ChargeFragmentDansEmplacement_Question(Fragment le_nouveau_frag ){

        transaction = fm.beginTransaction();

        Log.d("d","ChargeFragmentDasnEmplacementQuestion ... " + le_nouveau_frag.toString());
        transaction.replace(R.id.emplacement_fragment_question,le_nouveau_frag).
                addToBackStack(le_nouveau_frag.getTag()) ;
        transaction.commit();
    }
    public static void ChargeFragmentDansEmplacement_Bas(Fragment le_nouveau_frag , String tag ){

        transaction = fm.beginTransaction();
        Log.d("d","ChargeFragmentDansEmplacementBas ... " + le_nouveau_frag.toString());
        transaction.replace(R.id.emplacement_fragment_bas,le_nouveau_frag).
                addToBackStack(tag) ;

        transaction.commit();
    }
    public static void ChargeFragmentDansEmplacement_Reponse(Fragment le_nouveau_frag , String tag ){

        transaction = fm.beginTransaction();
        Log.d("d","ChargeFragmentDansEmplacementBas ... " + le_nouveau_frag.toString());
        transaction.replace(R.id.emplacement_fragment_reponse,le_nouveau_frag).
                addToBackStack(tag) ;

        transaction.commit();
    }

    protected int getOrientation(){
        return getResources().getConfiguration().orientation;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
