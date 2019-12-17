package com.example.projet_android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.camera2.TotalCaptureResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.zip.Inflater;

import static com.example.projet_android.Base_de_donnee.TAG;
import static com.example.projet_android.Fragment_reponse.mtts;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_question.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_question#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_question extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1; // on va se servir de ce paramettre pour stocker la question
    private String mParam2;

    MyContentProvider moncontentprovider ;

    CursorLoader mycursorLoad;
    private ListView liste_view_de_la_liste_de_mot;

    public TextView getTexview_question() {
        return texview_question;
    }

    public void setTexview_question(TextView texview_question) {
        this.texview_question = texview_question;
    }

    public TextView getText_view_de_la_question() {
        return text_view_de_la_question;
    }

    public void setText_view_de_la_question(TextView text_view_de_la_question) {
        this.text_view_de_la_question = text_view_de_la_question;
    }

    public Button getButton_passer_la_question() {
        return button_passer_la_question;
    }

    public void setButton_passer_la_question(Button button_passer_la_question) {
        this.button_passer_la_question = button_passer_la_question;
    }

    public Button getValider() {
        return valider;
    }

    public void setValider(Button valider) {
        this.valider = valider;
    }

    static Cursor cursor ;
    static Cursor cursor2 ;

    TextView texview_question ;




    private OnFragmentInteractionListener mListener;

    TextView text_view_de_la_question ;
    Button button_passer_la_question ;
    Button valider ;
    Spinner spinner_de_choix_categorie;
    GridView grid_view_de_la_liste_de_mot ;
    static View vue_du_frag;
    SimpleCursorAdapter sca ;
    public static ListView mon_recycler_view ;
    static ArrayList listtmp ;
    static String nom_langue ;
    static String mot_en_cours ;
    public Fragment_question() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_question.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_question newInstance(String param1, String param2) {
        Fragment_question fragment = new Fragment_question();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        moncontentprovider = new MyContentProvider();
    }
    static int en_cours ;

    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /**
         * Attribut visuel de la classe :
         */
        vue_du_frag = inflater.inflate(R.layout.fragment_question, container, false);
        text_view_de_la_question = vue_du_frag.findViewById(R.id.Textview_question) ;
        text_view_de_la_question.setText(mParam1);
        valider = vue_du_frag.findViewById(R.id.button_valider) ;

        Log.d(TAG, "Fragment Question : OnCreateView .");


        /**
         * On affiche un mot au hasard parmis la liste de dispo pour la categorie  .
         */
        if ( mParam1 != null && mParam1 == "affiche" && mParam2 == Base_de_donnee.TABLE_MOT){

            String catgeorie_en_cours = MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_CATEGORIE) ;
            String langue_en_cours = MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1) ;

            MainActivity.layout_reponse.setVisibility(View.VISIBLE);

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(langue_en_cours).appendPath(catgeorie_en_cours);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);
            texview_question = vue_du_frag.findViewById(R.id.Textview_question);
            //texview_question.setText("Traduire ce mot : \n");
            //Log.d(TAG,"Dans Fragment Question : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) + " | " + cursor.getColumnName(2) + " | " + cursor.getColumnName(3)) ;

            final ArrayList<Integer> liste_de_mot_deja_etudie = new ArrayList<>();
            en_cours = 0 ;
            Random rd = new Random(cursor.getCount());
            en_cours = rd.nextInt(cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                while ( liste_de_mot_deja_etudie.contains(en_cours)){
                    en_cours = rd.nextInt(cursor.getCount());
                }
                liste_de_mot_deja_etudie.add(en_cours);
                en_cours = rd.nextInt(cursor.getCount());
            }
            Log.d(TAG, "onCreateView: liste == " + liste_de_mot_deja_etudie.toString());


            // todo , regarder pourquoi y a tous le smots qui sortes
            cursor.moveToFirst();
            cursor.move(liste_de_mot_deja_etudie.get(0));
            liste_de_mot_deja_etudie.remove(0);
            MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_MOT_QUESTION, cursor.getString(2));
            texview_question.setTextSize(24);
            texview_question.setTextAlignment(0);
            texview_question.setText("Traduire ce mot : \n" + cursor.getString(2));
            mot_en_cours = cursor.getString(2);
            Fragment_bas.trad.setBackgroundColor(Color.rgb(255 , 235 , 59));

            button_passer_la_question = vue_du_frag.findViewById(R.id.button_passer_question);
            button_passer_la_question.setVisibility(View.VISIBLE);
            button_passer_la_question.setText("Passer cette Question");



            button_passer_la_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( cursor.moveToFirst() && liste_de_mot_deja_etudie.size() > 0){
                        cursor.move(liste_de_mot_deja_etudie.get(0));
                        liste_de_mot_deja_etudie.remove(0);
                        MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_MOT_QUESTION, cursor.getString(2));
                        texview_question.setText("Traduire ce mot : \n" + cursor.getString(2));
                        mot_en_cours = cursor.getString(2);
                        Fragment_bas.trad.setBackgroundColor(Color.rgb(255 , 235 , 59));
                        // 255 , 235 , 59


                    }
                    else {
                        Toast.makeText(getContext(),"Il ne reste plus de mot de disponible. Recommencer si vous voulez , ou ajouter des traductions .", Toast.LENGTH_LONG);
                    }
                }
            });

            MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("valide","traduction"),"valider choix");
            MainActivity.ChargeFragmentDansEmplacement_Reponse(Fragment_reponse.newInstance("affiche","reponse"),"traduction");



            Log.d(TAG, "Fragment Question: OnCreateView terminé , Question en place .");
        }
        if ( mParam1 != null && mParam1.equals("apprentissage") && mParam2.equals("mot a mot") ){
        //if ( mParam1 != null && mParam1.equals("apprentissage") && mParam2 != null ){
            /**
             * Dans le cas d'un apprentissage par categories et d'apres la langue choisie precedement dans le menu de demarrage
             */
            spinner_de_choix_categorie = vue_du_frag.findViewById(R.id.spinner_liste_categorie) ;

            // TODO separer l'apprentissage mot à mot et par categorie
            grid_view_de_la_liste_de_mot = vue_du_frag.findViewById(R.id.gridview_liste_mot);

                //grid_view_de_la_liste_de_mot = vue_du_frag.findViewById(R.id.gridview_liste_mot);

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_CATGEORIE);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);

            Log.d(TAG,"Dans Fragment question apprentissage : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;

            Log.d(TAG,"cursor count  !" + cursor.getCount());
            cursor.moveToFirst();
            String[] fromColumns = new String[] {Base_de_donnee.NOM_CATGEORIE };
            int[] toControlIDs = new int[] {android.R.id.text1};
            sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1 , cursor,
                    fromColumns,
                    toControlIDs);
            spinner_de_choix_categorie.setAdapter(sca);

            spinner_de_choix_categorie.setVisibility(View.VISIBLE);
            spinner_de_choix_categorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG,"Reussis !");
                    MainActivity.layout_reponse.setVisibility(View.VISIBLE);

                    Uri.Builder builder = new Uri.Builder();
                    cursor.moveToFirst();
                    cursor.move(position);
                    String categorie = cursor.getString(1);
                    //view.toString()

                    final int l = (Integer.parseInt(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1)));
                    final int l2 = (Integer.parseInt(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2)));
                    builder.scheme("content").authority(Base_de_donnee.authority)
                            .appendPath(Base_de_donnee.TABLE_TRAD)
                            .appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1))
                            .appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2))
                            .appendPath(categorie);
                    Uri uri = builder.build();
                    cursor2 = moncontentprovider.query(uri,
                            null
                            ,null,
                            null,
                            null);

                    //Log.d(TAG,"Dans Fragment question apprentissage : " + cursor2.getColumnName(0 ) + " | " + cursor2.getColumnName(1) ) ;

                    cursor2.moveToFirst();
                    String[] fromColumns ;
                    if ( l < l2 ) {
                        fromColumns = new String[]{"mot_question"};
                    }
                    else {
                        fromColumns = new String[]{"mot_reponse"};
                    }
                    int[] toControlIDs = new int[] {android.R.id.text2};
                    sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2 , cursor2,
                            fromColumns,
                            toControlIDs);
                    grid_view_de_la_liste_de_mot.setAdapter(sca);
                    Log.d("d","\n\n\n On colorie le mot : " + grid_view_de_la_liste_de_mot.getCount()) ;

                    grid_view_de_la_liste_de_mot.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            /**
                             * On restant appuyé , on va ajouté les mots a notre liste perso .
                             */
                            String langue ;
                            cursor2.moveToFirst();
                            cursor2.move(position);
                            String mot_selectionne_long = cursor2.getString(2) ;
                            Log.d("d","Vous etes resté appuyé lg sur " + mot_selectionne_long +" position = " + position) ;


                            /**
                             * Ajouter a la liste lors d'un clique long , et lors de l'initialisation , verifier les enfants de la liste et colorier ceux present .
                             * voir pour chaque chlds .
                             */
                            Uri.Builder builder = new Uri.Builder();
                            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.ID_LANGUE).appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2));
                            Uri uri = builder.build();
                            Cursor cursor_tmp = moncontentprovider.query(uri,
                                    null
                                    ,null,
                                    null,
                                    null);
                            cursor_tmp.moveToFirst();
                            Log.d(TAG,"nom_langue d'apres id : " + cursor_tmp.getString(0 ) ) ;
                            nom_langue = cursor_tmp.getString(0 ) ;

                            String id_langue = cursor_tmp.getString(0);


                                /*
                                Recuperer la traduction
                                 */
                            /***/
                            Uri.Builder builder2 = new Uri.Builder();
                            Log.d("d" ,"Fragment Reponse : mot a traduire :" + mot_selectionne_long );
                            builder2.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(mot_selectionne_long);
                            Uri uri2 = builder2.build();
                            Log.d("d" ,"Fragment Reponse : URI " + uri2 );
                            cursor_tmp = moncontentprovider.query(uri2,
                                    null
                                    ,null,
                                    null,
                                    null);
                            cursor_tmp.moveToFirst();

                            listtmp = new ArrayList<String>() ;
                            listtmp.add(id_langue); // on ajoute la langue
                            listtmp.add(mot_selectionne_long); // on ajoute le mot

                            if ( cursor_tmp != null ){
                                if ( cursor_tmp.getString(1).equals(mot_selectionne_long)){
                                    // cas ou la traduction colle avec le mot choisi
                                    listtmp.add(cursor_tmp.getString(2));
                                }
                                else {
                                    // le cas ou c'est la reponse de la trad qui match avec le mot selectionne
                                    listtmp.add(cursor_tmp.getString(1));
                                }

                            }

                            Uri.Builder builder_req = new Uri.Builder();
                            builder_req.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.ID_LANGUE).appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2));
                            Uri uri_req = builder_req.build();
                            Cursor cursor_req = moncontentprovider.query(uri_req,
                                    null
                                    ,null,
                                    null,
                                    null);
                            cursor_req.moveToFirst();
                            Log.d(TAG,"nom_langue d'apres id : " + cursor_req.getString(0 ) ) ;
                            nom_langue = cursor_req.getString(0 ) ;

                            Cursor curso_delete ;
                            MyContentProvider myContentProvider_delete = new MyContentProvider();
                            builder_req = new Uri.Builder();
                            builder_req.scheme("content").authority(Base_de_donnee.authority).appendPath("liste").appendPath(nom_langue).appendPath(mot_selectionne_long);
                            uri_req = builder_req.build();
                            curso_delete = myContentProvider_delete.query(uri_req,
                                    null
                                    ,null ,null,null);

                            if ( curso_delete.getCount() != 0 ){
                                grid_view_de_la_liste_de_mot.getChildAt(position).setBackgroundColor(Color.WHITE);
                                Boolean remove = MainActivity.liste_de_mot.remove(listtmp );

                                Log.d(Base_de_donnee.TAG, "onItemLongClick: " + listtmp.toString());

                                //MyContentProvider myContentProvider_delete = new MyContentProvider();
                                Uri.Builder builder_liste_delete = new Uri.Builder();
                                builder_liste_delete.scheme("content").authority(Base_de_donnee.authority).appendPath("liste").appendPath(listtmp.get(0).toString()).appendPath(listtmp.get(1).toString());
                                Uri uri_liste_delete = builder_liste_delete.build();
                                myContentProvider_delete.delete(uri_liste_delete,
                                        null
                                        ,null );
                                Log.d(TAG, "onItemLongClick: remove = " + remove);
                                //grid_view_de_la_liste_de_mot.getChildAt(position).setBackgroundColor(Color.WHITE);
                            }
                            else {
                                Boolean add = MainActivity.liste_de_mot.add(listtmp) ;
                                MyContentProvider myContentProvider = new MyContentProvider();
                                Uri.Builder builder_liste = new Uri.Builder();
                                builder_liste.scheme("content").authority(Base_de_donnee.authority).appendPath("liste");
                                Uri uri_liste = builder_liste.build();
                                ContentValues cv = new ContentValues();
                                cv.put("langue_nom",id_langue);
                                cv.put("mot",mot_selectionne_long);
                                cv.put("trad",listtmp.get(2).toString());
                                myContentProvider.insert(uri_liste,cv);

                                Log.d(TAG, "onItemLongClick: add = " + add);
                                grid_view_de_la_liste_de_mot.getChildAt(position).setBackgroundColor(Color.GREEN);
                            }




                            /**
                             * Implementer l'ajout dans notre liste perso , faire une page pour l'afficher aussi .
                             */

                            Log.d(TAG, "onItemLongClick: liste : " + MainActivity.liste_de_mot.toString());
                            listtmp.clear();

                            return true;
                        }
                    });
                    grid_view_de_la_liste_de_mot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            /**
                             * On ne peut recreer de fragment a chaque click .
                             */
                            cursor2.moveToFirst();
                            cursor2.move(position);
                            Log.d(TAG, "onItemClick: " + cursor2.getString(0) + " / "+ cursor2.getString(1) + " / "+ cursor2.getString(2) + " / "+ cursor2.getString(3) + " / "+ cursor2.getString(4) + " / ");
                            String mot_selectionne ;
                            if ( l < l2 ) {
                                mot_selectionne = cursor2.getString(1);
                            }
                            else{
                                mot_selectionne = cursor2.getString(2);
                            }
                            MainActivity.ChargeFragmentDansEmplacement_Reponse(Fragment_reponse.newInstance("trad",mot_selectionne),"trad");
                        }
                    });

                    grid_view_de_la_liste_de_mot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            grid_view_de_la_liste_de_mot.removeOnLayoutChangeListener(this);
                            Log.d("d","Liste_completée , getcount " + grid_view_de_la_liste_de_mot.getCount() + " getchildcount() = " + grid_view_de_la_liste_de_mot.getChildCount()) ;
                            for (int i = 0; i < grid_view_de_la_liste_de_mot.getCount(); i++) {
                                cursor2.moveToFirst();
                                cursor2.move(i);
                                String mot_a_remplir = cursor2.getString(2) ;
                                /**
                                 * faire une requete avec la langue et le mot
                                 */
                                /*
                                if ( listtmp != null && listtmp.contains(mot_a_remplir)) {
                                    grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.GREEN);
                                }

                                 */
                                /** On va recuperer le nom_de_la_langue en cours d'apprentisasge */
                                Uri.Builder builder = new Uri.Builder();
                                builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.ID_LANGUE).appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2));
                                Uri uri = builder.build();
                                Cursor cursor_tmp = moncontentprovider.query(uri,
                                        null
                                        ,null,
                                        null,
                                        null);
                                cursor_tmp.moveToFirst();
                                Log.d(TAG,"nom_langue d'apres id : " + cursor_tmp.getString(0 ) ) ;
                                nom_langue = cursor_tmp.getString(0 ) ;

                                Cursor curso_delete ;
                                MyContentProvider myContentProvider_delete = new MyContentProvider();
                                Uri.Builder builder_liste_delete = new Uri.Builder();
                                builder_liste_delete.scheme("content").authority(Base_de_donnee.authority).appendPath("liste").appendPath(nom_langue).appendPath(mot_a_remplir);
                                Uri uri_liste_delete = builder_liste_delete.build();
                                curso_delete = myContentProvider_delete.query(uri_liste_delete,
                                        null
                                        ,null ,null,null);

                                if ( curso_delete.getCount() != 0 ){
                                    grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.GREEN);
                                }
                                else {
                                    grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.WHITE);
                                }
                                    //grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.GREEN);
                                    //grid_view_de_la_liste_de_mot.getChildAt(i).setElevation(1);

                                }


                            }

                    });

                    grid_view_de_la_liste_de_mot.setVisibility(View.VISIBLE);


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

            Maj_liste_user_avec_liste_affiche();







        }
        if ( mParam1 != null && mParam1.equals("apprentissage") && mParam2.equals("categorie") ){
            Log.d(TAG, "onCreateView: Apprentissage par categorie");
            spinner_de_choix_categorie = vue_du_frag.findViewById(R.id.spinner_liste_categorie) ;
            liste_view_de_la_liste_de_mot = new ListView(getContext());
            liste_view_de_la_liste_de_mot = vue_du_frag.findViewById(R.id.liste_view_liste_mot);

            text_view_de_la_question.setText("Choisir une Catégorie qui vous interresse :");

            // todo debut

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_CATGEORIE);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);

            Log.d(TAG,"Dans Fragment question apprentissage : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;

            Log.d(TAG,"cursor count  !" + cursor.getCount());
            cursor.moveToFirst();
            String[] fromColumns = new String[] {Base_de_donnee.NOM_CATGEORIE };
            int[] toControlIDs = new int[] {android.R.id.text1};
            sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1 , cursor,
                    fromColumns,
                    toControlIDs);
            spinner_de_choix_categorie.setAdapter(sca);

            spinner_de_choix_categorie.setVisibility(View.VISIBLE);
            spinner_de_choix_categorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, final View view, final int position, long id) {
                    Log.d(TAG, "Reussis liste!");
                    MainActivity.layout_reponse.setVisibility(View.VISIBLE);

                    Uri.Builder builder = new Uri.Builder();
                    cursor.moveToFirst();
                    cursor.move(position);
                    String categorie = cursor.getString(1);
                    //view.toString()

                    /**
                     * public static final String liste_trad_langueBase_categorie = Base_de_donnee.TABLE_TRAD + "/*//*/*";
                     *     // langue1/langue2/catgeorie
                     *
                     */

                    String l = String.valueOf((Integer.parseInt(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1))));
                    builder.scheme("content").authority(Base_de_donnee.authority)
                            .appendPath(Base_de_donnee.TABLE_TRAD)
                            .appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1))
                            .appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2))
                            .appendPath(categorie);
                    Uri uri = builder.build();
                    final Cursor cursor2 = moncontentprovider.query(uri,
                            null
                            , null,
                            null,
                            null);

                    cursor2.moveToFirst();


                    Uri.Builder builder_req = new Uri.Builder();
                    builder_req.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.ID_LANGUE).appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1));
                    Uri uri_req = builder_req.build();
                    Cursor cursor_req = moncontentprovider.query(uri_req,
                            null
                            , null,
                            null,
                            null);
                    cursor_req.moveToFirst();
                    if (cursor2.getCount() != 0) {

                        liste_view_de_la_liste_de_mot.setVisibility(View.VISIBLE);

                        Log.d(TAG, "onItemSelected: curosr2" + cursor2.getString(3) + "   " + cursor2.getString(4));


                        if (cursor_req.getString(0).equals(cursor2.getString(4))) {

                            Log.d(TAG, "onItemSelected: cursor2 " + cursor2.getCount());
                            String[] fromColumns = new String[]{"mot_question", "mot_reponse"};
                            int[] toControlIDs = new int[]{R.id.mot_reponse, R.id.mot_question};
                            sca = new SimpleCursorAdapter(getContext(), R.layout.layout_liste_mot_apprentissage_categorie, cursor2,
                                    fromColumns,
                                    toControlIDs);
                            liste_view_de_la_liste_de_mot.setAdapter(sca);
                        } else {
                            Log.d(TAG, "onItemSelected: cursor2 " + cursor2.getCount());
                            String[] fromColumns = new String[]{"mot_question", "mot_reponse"};
                            int[] toControlIDs = new int[]{R.id.mot_question, R.id.mot_reponse};
                            sca = new SimpleCursorAdapter(getContext(), R.layout.layout_liste_mot_apprentissage_categorie, cursor2,
                                    fromColumns,
                                    toControlIDs);
                            liste_view_de_la_liste_de_mot.setAdapter(sca);

                        }

                        liste_view_de_la_liste_de_mot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                                final ImageView image = new ImageView(getContext());
                                //FileInputStream f =  new FileInputStream(String.format("%s/downloadfile-6.jpg", getContext().getFilesDir().toString())) ;
                                BufferedInputStream buf = null;
                                Log.d(TAG, "coucou");
                                try {
                                    TextView tv = view.findViewById(R.id.mot_question);
                                    String mot = tv.getText().toString();

                                    Uri.Builder builder = new Uri.Builder();
                                    Log.d("d", "Fragment ma liste perso : mot == " + mot);
                                    // on recherche le lien de l'image interne
                                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath("img_int").appendPath(mot);
                                    Uri uri = builder.build();
                                    Log.d("d", "Fragment ma liste perso  : URI " + uri);
                                    MyContentProvider moncontentprovider = new MyContentProvider();
                                    cursor = moncontentprovider.query(uri,
                                            null
                                            , null,
                                            null,
                                            null);
                                    cursor.moveToFirst();


                                    if (cursor != null) {
                                        Log.d(TAG, "onItemClick: ma liste perso , lien == " + cursor.getString(5));
                                        if (cursor.getString(5) != null && !cursor.getString(5).equals("vide")) {
                                            FileInputStream in = new FileInputStream(cursor.getString(5));
                                            buf = new BufferedInputStream(in);
                                            Bitmap bitmap = BitmapFactory.decodeStream(buf);
                                            image.setImageBitmap(bitmap);
                                            new AlertDialog.Builder(getContext())
                                                    .setTitle("Image associée ( interne ) :")
                                                    .setPositiveButton("Lecture Son", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            mtts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                                                                @Override
                                                                public void onInit(int status) {
                                                                    if (status == TextToSpeech.SUCCESS) {
                                                                        int res = mtts.setLanguage(Locale.FRANCE);
                                                                        if (res == TextToSpeech.LANG_MISSING_DATA) {
                                                                            Log.d(TAG, "onInit: OUps , langue non prise en compte...");
                                                                        }
                                                                        TextView tv = view.findViewById(R.id.mot_reponse);
                                                                        String mot = tv.getText().toString();
                                                                        mtts.speak(mot, TextToSpeech.QUEUE_FLUSH, null);
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }).setNegativeButton("Supprimer Image interne ?", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    File file = new File(cursor.getString(5));
                                                    //on regarde si il existe (test qui est amené a disparaitre, c’était pour être sur)
                                                    if (file.exists()) {
                                                        Log.d(TAG, "onClick: Fichier trouvé");
                                                        // on va supprimer la reference de la BDD en mettant à jour le champs img_interne à ""

                                                        TextView tv = view.findViewById(R.id.mot_question);
                                                        String mot = tv.getText().toString();
                                                        Uri.Builder uri_tmp = new Uri.Builder();
                                                        uri_tmp.scheme("content").authority(Base_de_donnee.authority).appendPath("img_int").appendPath(mot);
                                                        Uri urii = uri_tmp.build();
                                                        MyContentProvider myContentProvider = new MyContentProvider();
                                                        ContentValues cv = new ContentValues();

                                                        cv.put("mot", mot);
                                                        cv.put("img_interne", "vide");
                                                        int tmp = myContentProvider.update(urii, cv, null, null);

                                                    } else {
                                                        Log.d(TAG, "onClick: Fichier non trouvé");
                                                    }
                                                    //Suppression du fichier et exception si ce n'est pas le cas
                                                    if (file.delete() == false) {
                                                        try {
                                                            throw new IOException();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }


                                                }
                                            }).setView(image).show();
                                            cursor.moveToFirst();
                                        } else {
                                            Log.d(TAG, "onItemClick: Image interne non trouvé , on cherche le lien externe ");
                                            if (cursor.getString(6) != null) {
                                                Log.d(TAG, "onItemClick: on tente l'ouverture du lien ... " + cursor.getString(6));

                                                WebView wv = new WebView(getContext());
                                                wv.loadUrl(cursor.getString(6));
                                                wv.setWebViewClient(new WebViewClient() {
                                                    @Override
                                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                        view.loadUrl(url);
                                                        return true;
                                                    }
                                                });
                                                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                                                alert.setTitle("Image externe :");
                                                alert.setView(wv);
                                                alert.setNegativeButton("Fermer", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                                alert.setPositiveButton("Lecture Son", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mtts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                                                            @Override
                                                            public void onInit(int status) {
                                                                if (status == TextToSpeech.SUCCESS) {
                                                                    int res = mtts.setLanguage(Locale.FRANCE);
                                                                    if (res == TextToSpeech.LANG_MISSING_DATA) {
                                                                        Log.d(TAG, "onInit: OUps , langue non prise en compte...");
                                                                    }
                                                                    TextView tv = view.findViewById(R.id.mot_reponse);
                                                                    String mot = tv.getText().toString();
                                                                    mtts.speak(mot, TextToSpeech.QUEUE_FLUSH, null);
                                                                }
                                                            }
                                                        });
                                                    }
                                                });
                                                alert.show();


                                            } else {
                                                new AlertDialog.Builder(getContext())
                                                        .setTitle("Image associée : Aucune image trouvée ...")
                                                        .setNegativeButton("Aller dans la gestion de BDD pour ajouter une image ( else )", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        }).setPositiveButton("Lecture Son", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        mtts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                                                            @Override
                                                            public void onInit(int status) {
                                                                if (status == TextToSpeech.SUCCESS) {
                                                                    int res = mtts.setLanguage(Locale.FRANCE);
                                                                    if (res == TextToSpeech.LANG_MISSING_DATA) {
                                                                        Log.d(TAG, "onInit: OUps , langue non prise en compte...");
                                                                    }
                                                                    TextView tv = view.findViewById(R.id.mot_reponse);
                                                                    String mot = tv.getText().toString();
                                                                    mtts.speak(mot, TextToSpeech.QUEUE_FLUSH, null);
                                                                }
                                                            }
                                                        });
                                                    }
                                                }).show();
                                            }
                                        }

                                    }


                                } catch (FileNotFoundException e) {
                                    Log.d(TAG, "fichier image non trouvé ...");
                                    e.printStackTrace();
                                    new AlertDialog.Builder(getContext())
                                            .setTitle("Image associée : Aucune image trouvée ...")
                                            .setNegativeButton("Aller dans la gestion de BDD pour ajouter une image", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                }
                                            }).setPositiveButton("Lecture Son", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mtts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                                                @Override
                                                public void onInit(int status) {
                                                    if (status == TextToSpeech.SUCCESS) {
                                                        int res = mtts.setLanguage(Locale.FRANCE);
                                                        if (res == TextToSpeech.LANG_MISSING_DATA) {
                                                            Log.d(TAG, "onInit: OUps , langue non prise en compte...");
                                                        }
                                                        TextView tv = view.findViewById(R.id.mot_reponse);
                                                        String mot = tv.getText().toString();
                                                        mtts.speak(mot, TextToSpeech.QUEUE_FLUSH, null);
                                                    }
                                                }
                                            });
                                        }
                                    }).show();

                                }


                            }


                        });
                        liste_view_de_la_liste_de_mot.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                /**
                                 * On restant appuyé , on va ajouté les mots a notre liste perso .
                                 */

                                cursor2.moveToFirst();
                                cursor2.move(position);
                                String mot_selectionne_long = cursor2.getString(2);
                                Log.d("d", "Vous etes resté appuyé lg sur " + mot_selectionne_long + " position = " + position);


                                /**
                                 * Ajouter a la liste lors d'un clique long , et lors de l'initialisation , verifier les enfants de la liste et colorier ceux present .
                                 * voir pour chaque chlds .
                                 */
                                Uri.Builder builder = new Uri.Builder();
                                builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.ID_LANGUE).appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2));
                                Uri uri = builder.build();
                                Cursor cursor_tmp = moncontentprovider.query(uri,
                                        null
                                        , null,
                                        null,
                                        null);
                                cursor_tmp.moveToFirst();
                                Log.d(TAG, "nom_langue d'apres id : " + cursor_tmp.getString(0));
                                String nom_languebis = cursor_tmp.getString(0);

                                String id_langue = cursor_tmp.getString(0);

                                Uri.Builder builder2 = new Uri.Builder();
                                Log.d("d", "Fragment Reponse : mot a traduire :" + mot_selectionne_long);
                                builder2.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(mot_selectionne_long);
                                Uri uri2 = builder2.build();
                                Log.d("d", "Fragment Reponse : URI " + uri2);
                                cursor_tmp = moncontentprovider.query(uri2,
                                        null
                                        , null,
                                        null,
                                        null);
                                cursor_tmp.moveToFirst();

                                listtmp = new ArrayList<String>();
                                Log.d(TAG, "onItemLongClick: idlangue " + id_langue + " / " + nom_languebis);
                                Log.d(TAG, "onItemLongClick: idlangue " + mot_selectionne_long);
                                Log.d(TAG, "onItemLongClick: idlangue " + cursor_tmp.getString(1));

                                if (id_langue.equals(nom_languebis)) {
                                    listtmp.add(id_langue); // on ajoute la langue
                                    listtmp.add(mot_selectionne_long); // on ajoute le mot

                                    if (cursor_tmp != null) {
                                        if (cursor_tmp.getString(1).equals(mot_selectionne_long)) {
                                            // cas ou la traduction colle avec le mot choisi
                                            listtmp.add(cursor_tmp.getString(2));
                                        } else {
                                            // le cas ou c'est la reponse de la trad qui match avec le mot selectionne
                                            listtmp.add(cursor_tmp.getString(1));
                                        }

                                    }

                                } else {
                                    listtmp.add(id_langue); // on ajoute la langue

                                    if (cursor_tmp != null) {
                                        if (cursor_tmp.getString(1).equals(mot_selectionne_long)) {
                                            // cas ou la traduction colle avec le mot choisi
                                            listtmp.add(cursor_tmp.getString(2));
                                        } else {
                                            // le cas ou c'est la reponse de la trad qui match avec le mot selectionne
                                            listtmp.add(cursor_tmp.getString(1));
                                        }

                                    }
                                    listtmp.add(mot_selectionne_long); // on ajoute le mot

                                }
                                // todo , ona  recuperer la liste
                                MyContentProvider myContentProvider = new MyContentProvider();
                                Uri.Builder builder_liste = new Uri.Builder();
                                builder_liste.scheme("content").authority(Base_de_donnee.authority).appendPath("liste");
                                Uri uri_liste = builder_liste.build();
                                Cursor c_liste = myContentProvider.query(uri_liste,
                                        null
                                        , null,
                                        null,
                                        null);

                                c_liste.moveToFirst();
                                while (c_liste.moveToNext()) {
                                    //Log.d(TAG, "onLayoutChange: coucou");
                                    TextView tv = view.findViewById(R.id.mot_question);
                                    String deux = tv.getText().toString();

                                    TextView tv2 = view.findViewById(R.id.mot_reponse);
                                    String trois = tv2.getText().toString();
                                    /*Log.d("d", "onLayoutChange: deux:" + deux);
                                    Log.d("d", "onLayoutChange: trois: " + trois);
                                    Log.d("d", "onLayoutChange: 2 " + c_liste.getString(2));
                                    Log.d("d", "onLayoutChange: 3 " + c_liste.getString(3));*/
                                    Log.d(TAG, "onItemLongClick: deux = " + deux + " , trois = " + trois);
                                    Log.d(TAG, "onItemLongClick: (2) = " + c_liste.getString(2) + " , (3) = " + c_liste.getString(3));
                                    Log.d(TAG, "onItemLongClick: 0 = " + listtmp.get(0).toString() + " , 2 = " + listtmp.get(1).toString());
                                    if (deux.equals(c_liste.getString(2)) && trois.equals(c_liste.getString(3))) {
                                        MyContentProvider myContentProvider_delete = new MyContentProvider();
                                        Uri.Builder builder_liste_delete = new Uri.Builder();
                                        builder_liste_delete.scheme("content").authority(Base_de_donnee.authority).appendPath("liste_del_mot").appendPath(c_liste.getString(2)).appendPath(c_liste.getString(3));
                                        Uri uri_liste_delete = builder_liste_delete.build();
                                        Log.d(TAG, "onItemLongClick: URI " + uri_liste_delete);
                                        int remove = myContentProvider_delete.delete(uri_liste_delete,
                                                null
                                                , null);

                                        Log.d(TAG, "onItemLongClick: remove = " + remove);
                                        liste_view_de_la_liste_de_mot.getChildAt(position).setBackgroundColor(Color.WHITE);
                                        return true;
                                    }
                                }


                                // todo : check langue

                                Boolean add = MainActivity.liste_de_mot.add(listtmp);
                                myContentProvider = new MyContentProvider();
                                builder_liste = new Uri.Builder();
                                builder_liste.scheme("content").authority(Base_de_donnee.authority).appendPath("liste");
                                uri_liste = builder_liste.build();
                                ContentValues cv = new ContentValues();
/*
                                Log.d(TAG, "onItemLongClick: languebis " +  nom_languebis);
                                Log.d(TAG, "onItemLongClick: mot " + mot_selectionne_long);
                                Log.d(TAG, "onItemLongClick: listtmpget(2) " + listtmp.get(2).toString() );
                                Log.d(TAG, "onItemLongClick: listtmpget(0) :: " + listtmp.get(0).toString() );



 */
                                Uri.Builder b = new Uri.Builder();
                                b.scheme("content").authority(Base_de_donnee.authority).appendPath("langue_mot").appendPath(mot_selectionne_long);
                                Uri u = b.build();
                                Cursor cu = myContentProvider.query(u, null, null, null, null);
                                cu.moveToFirst();
                                String langueMot = cu.getString(0);
                                Log.d(TAG, "onItemLongClick: LangueMot sleect : " + langueMot + " / " + MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2));

                                if (MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2).equals(langueMot)) {
                                    // cursor2
                                    Log.d(TAG, "onItemLongClick: CAS 1:::::");
                                    cv.put("langue_nom", id_langue);
                                    cv.put("mot", listtmp.get(2).toString());
                                    cv.put("trad", mot_selectionne_long);
                                    myContentProvider.insert(uri_liste, cv);
                                } else {
                                    Log.d(TAG, "onItemLongClick: CAS 2:::::");
                                    cv.put("langue_nom", id_langue);
                                    cv.put("mot", mot_selectionne_long);
                                    cv.put("trad", listtmp.get(2).toString());
                                    myContentProvider.insert(uri_liste, cv);
                                }


                                Log.d(TAG, "onItemLongClick: add = " + add);
                                liste_view_de_la_liste_de_mot.getChildAt(position).setBackgroundColor(Color.GREEN);


                                /**
                                 * Implementer l'ajout dans notre liste perso , faire une page pour l'afficher aussi .
                                 */

                                Log.d(TAG, "onItemLongClick: liste : " + MainActivity.liste_de_mot.toString());
                                listtmp.clear();

                                return true;
                            }
                        });

                        liste_view_de_la_liste_de_mot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                            @Override
                            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                liste_view_de_la_liste_de_mot.removeOnLayoutChangeListener(this);
                                Log.d("d", "Liste_completée , getcount " + liste_view_de_la_liste_de_mot.getCount() + " getchildcount() = " + liste_view_de_la_liste_de_mot.getChildCount());
                                MyContentProvider myContentProvider = new MyContentProvider();
                                Uri.Builder builder_liste = new Uri.Builder();
                                builder_liste.scheme("content").authority(Base_de_donnee.authority).appendPath("liste");
                                Uri uri_liste = builder_liste.build();
                                Cursor c_liste = myContentProvider.query(uri_liste,
                                        null
                                        , null,
                                        null,
                                        null);

                                Log.d("d", "onLayoutChange: taille_LISTE :" + c_liste.getCount());

                                for (int i = 0; i < liste_view_de_la_liste_de_mot.getCount(); i++) {
                                    c_liste.moveToFirst();
                                    if (c_liste.getCount() < 1) break;
                                    do {
                                        //Log.d(TAG, "onLayoutChange: coucou");
                                        TextView tv = liste_view_de_la_liste_de_mot.getChildAt(i).findViewById(R.id.mot_question);
                                        String deux = tv.getText().toString();

                                        TextView tv2 = liste_view_de_la_liste_de_mot.getChildAt(i).findViewById(R.id.mot_reponse);
                                        String trois = tv2.getText().toString();
                                        //Log.d(TAG, "onLayoutChange: i :: " + i );
                                    /*Log.d("d", "onLayoutChange: deux:" + deux);
                                    Log.d("d", "onLayoutChange: trois: " + trois);
                                    Log.d("d", "onLayoutChange: 2 " + c_liste.getString(2));
                                    Log.d("d", "onLayoutChange: 3 " + c_liste.getString(3));*/
                                        if (deux.equals(c_liste.getString(2)) && trois.equals(c_liste.getString(3))) {
                                            liste_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.GREEN);
                                        }
                                    } while (c_liste.moveToNext());

                                }


                            }

                        });


                    }
                    else{
                        liste_view_de_la_liste_de_mot.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

            liste_view_de_la_liste_de_mot.setVisibility(View.VISIBLE);


        }

        return vue_du_frag;

    }

    /**
     * Voir ça plus tard
     */

    public void Maj_liste_user_avec_liste_affiche(){

        Log.d("d","\n\n\n nb child: " + grid_view_de_la_liste_de_mot.getChildCount() + " , count()  =  " + grid_view_de_la_liste_de_mot.getCount()) ;
        for (int i = 0; i < grid_view_de_la_liste_de_mot.getCount(); i++) {
            cursor2.moveToFirst();
            cursor2.move(i);
            String mot_a_remplir = cursor2.getString(2) ;
            if ( MainActivity.liste_de_mot.contains(mot_a_remplir)) {
                Log.d("d","\n\n\n On colorie le mot : " + mot_a_remplir + " i =  " + i) ;
                //grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.GREEN);
                //grid_view_de_la_liste_de_mot.getChildAt(i).setElevation(1);
                grid_view_de_la_liste_de_mot.getChildAt(i).setBackgroundColor(Color.RED);
                /**
                 * Voir ça demain !
                 */
            }


        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if ( mParam1 != null && mParam1.equals("apprentissage") && mParam2.equals("mot a mot") ){
            MainActivity.layout_question.setVisibility(View.GONE);
            MainActivity.layout_demarrage.setVisibility(View.VISIBLE);
            MainActivity.layout_reponse.setVisibility(View.GONE);
        }
        if ( mParam1 != null && mParam1.equals("apprentissage") && mParam2.equals("categorie") ){
            MainActivity.layout_question.setVisibility(View.GONE);
            MainActivity.layout_demarrage.setVisibility(View.VISIBLE);
            MainActivity.layout_reponse.setVisibility(View.GONE);
        }
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
