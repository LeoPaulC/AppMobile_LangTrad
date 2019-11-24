package com.example.projet_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Fragment_menu.OnFragmentInteractionListener , Fragment_question.OnFragmentInteractionListener , Fragment_reponse.OnFragmentInteractionListener, Fragment_bas.OnFragmentInteractionListener , AccesDonneesBDD.OnFragmentInteractionListener , Page_demarrage.OnFragmentInteractionListener{

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

    static HashMap<String , ArrayList<String> > ma_liste_de_mot_a_apprendre_pour_plus_tard ;
    /**
     * En gros , hashmap < langue , liste_de_mot<String> pour avoir une liste trié par langue , voir ventuellement pour ajouter les catgeroies en compte
     *
     * */

    static Bundle bundle_de_la_session_en_cours ;




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
        setContentView(R.layout.activity_main);
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
        frag_init = Page_demarrage.newInstance("init","user");
        transaction.add(R.id.emplacement_fragment_question,frag_init,"Init user").addToBackStack("Init user") ;


        Log.d("d","Init user passer :)");

        // fragment Menu Graphique
        fragment_menu = Fragment_menu.newInstance("",""); // pour l'instant paramettre vide à a voir pour permettre de creer le menu avec .

        transaction
                .add(R.id.emplacement_fragment_bar_du_haut, fragment_menu, "menu_du_haut")
                .addToBackStack("menu_du_haut");

        // Fragment Question Graphique

        /**
         * pour le demarage on choisie la Langues ...................
         */

        /**
         *
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * a verifier plus tard
         */


        fragment_bas = Fragment_bas.newInstance("init" , "bdd"); // permet de passer des paramettre et donc effecturer des actions specifiques .
        transaction.add(R.id.emplacement_fragment_bas,fragment_bas,"Button_du_bas")
                .addToBackStack("Button_du_bas") ;

        // finalise les transaction de base .
        transaction.commit();

        //faireToast("Fragments de base ajoutés.");



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
