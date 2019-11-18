package com.example.projet_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Fragment_menu.OnFragmentInteractionListener , Fragment_question.OnFragmentInteractionListener , Fragment_reponse.OnFragmentInteractionListener, Fragment_bas.OnFragmentInteractionListener , AccesDonneesBDD.OnFragmentInteractionListener{

    /**
     * Attributs :
     */
    // Liste Fragments graphique :
    static Fragment_menu fragment_menu ;
    static Fragment_question fragment_question ;
    static Fragment_reponse fragment_reponse ;
    static Fragment_bas fragment_bas ;
    static AccesDonneesBDD frag_accesBDD ;

    // attribut static , a voir
    static ListView ma_liste_view ;

    // liste fragment "operationnel"


    // liste attribut :
    static Base_de_donnee bdd ;
    static AccesDonneesBDD accesDonnees;

    static FragmentManager fm ;
    static FragmentTransaction transaction ;

    static final String BUNDLE_CATEGORIE ="categorie" ;
    static final String BUNDLE_LANGUE1 ="langue1" ;
    static final String BUNDLE_LANGUE2 ="langue2" ;
    static final String BUNDLE_SCORE ="score" ;
    static final String BUNDLE_NIVEAU ="niveau" ;

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

        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();

        Log.d("d","Coucouuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");

        ma_liste_view = findViewById(R.id.ma_liste_view);

        /**
         * Nous allons decomposer les fragments en differents constructers , de maniere a les rendre plus modulable et plus efficace.
         * Les fragments qui suivent correspondent a l'initialisation de notre Application .
         */

        // Gestionnaire de Fragment

        // fragment Menu Graphique
        fragment_menu = Fragment_menu.newInstance("",""); // pour l'instant paramettre vide à a voir pour permettre de creer le menu avec .

        transaction
                .add(R.id.emplacement_fragment_bar_du_haut, fragment_menu, "menu_du_haut")
                .addToBackStack("menu_du_haut");

        // Fragment Question Graphique

        /**
         * pour le demarage on choisie la Langues ...................
         */

        frag_accesBDD = AccesDonneesBDD.newInstance("affiche",Base_de_donnee.TABLE_CATGEORIE); // pour l'instant paramettre vide à a voir pour permettre de creer le menu avec .

        transaction
                .add(R.id.emplacement_fragment_question, frag_accesBDD, "frag_question")
                .addToBackStack("frag_question");



        /**
         *
         * TEST !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         */
        /*
        frag_accesBDD = AccesDonneesBDD.newInstance("affiche",Base_de_donnee.CATEGORIE);
        transaction.replace(R.id.emplacement_fragment_question,frag_accesBDD).addToBackStack("categorie");


         */
        // Fragment Reponse graphique
        /*
        fragment_reponse = Fragment_reponse.newInstance("Reponse : ..............................................................","");

        transaction
                .add(R.id.emplacement_fragment_reponse, fragment_reponse, "frag_reponse")
                .addToBackStack("frag_reponse");



         */
                //Log.d("LOG", "premier fragment ajouté ok");

                // Fragment du bas de l'ecran graphique

                /**
                 * Gestion de la creation du Pré-Remplissage de la BDD ( partiel pour l'instant ) .
                 */

                fragment_bas = Fragment_bas.newInstance("init" , "bdd"); // permet de passer des paramettre et donc effecturer des actions specifiques .
        transaction.add(R.id.emplacement_fragment_bas,fragment_bas,"Button_du_bas")
                .addToBackStack("Button_du_bas") ;

        // finalise les transaction de base .
        transaction.commit();

        faireToast("Fragments de base ajoutés.");


    }

    @Override
    protected void onStop() {
        super.onStop();
        bdd.close();
        Log.d("d","STOOOOOOOOOOOOP , l'activité a quitté et la bdd a été fermé  : bdd - " + bdd.toString());
    }

    public static void ChargeFragmentDansEmplacement_Question(Fragment le_nouveau_frag){

        transaction = fm.beginTransaction();

        Log.d("d","ChargeFragmentDasnEmplacementQuestion ... " + le_nouveau_frag.toString());
        transaction.replace(R.id.emplacement_fragment_question,le_nouveau_frag).
                addToBackStack(le_nouveau_frag.getTag()) ;
        transaction.commit();
    }

    protected int getOrientation(){
        return getResources().getConfiguration().orientation;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
