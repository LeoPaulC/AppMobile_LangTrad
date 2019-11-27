package com.example.projet_android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import java.util.zip.Inflater;

import static com.example.projet_android.Base_de_donnee.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Page_demarrage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Page_demarrage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Page_demarrage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText editText_nom ;
    Spinner liste_langue ;
    Spinner liste_langue2 ;
    Cursor cursor ;
    SimpleCursorAdapter sca ;
    private OnFragmentInteractionListener mListener;
    private MyContentProvider moncontentprovider;
    ImageButton valider_choix ;
    View vue_du_fragment;

    RadioButton mot_a_mot ;
    RadioButton plusieurs_mot_par_catgeorie ;
    RadioButton quizz ;

    public Page_demarrage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Page_demarrage.
     */
    // TODO: Rename and change types and number of parameters
    public static Page_demarrage newInstance(String param1, String param2) {
        Page_demarrage fragment = new Page_demarrage();
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
        liste_langue = new Spinner(getContext());
        liste_langue2  = new Spinner(getContext()) ;
        moncontentprovider = new MyContentProvider();
        valider_choix = new ImageButton(getContext());
        mot_a_mot = new RadioButton(getContext());
        plusieurs_mot_par_catgeorie = new RadioButton(getContext());
        quizz = new RadioButton(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vue_du_fragment = inflater.inflate(R.layout.fragment_page_demarrage, container, false);

        Log.d(TAG,"Page_Demarrage : OnCreateView " + mParam1 + " " + mParam2) ;

        if ( mParam1 != null && mParam1.equals("init") && mParam2.equals("user")){
            /**
             * On accede a cette page qui sert de Menu en quelque sorte
             * Choix de la langue de base ,
             * Choix de l'activité souhaité
             * Choix de notre Nom
             * Initialisation du score
             * Passerelle vers les autres "Activitées"
             */

            // trouver tous les Layout associé au paramettre de ce fragment .
            editText_nom = vue_du_fragment.findViewById(R.id.editText_choix_nom) ;
            liste_langue = vue_du_fragment.findViewById(R.id.spinner_choix_langue_de_base);
            liste_langue2 = vue_du_fragment.findViewById(R.id.spinner_choix_langue_de_base2) ;
            valider_choix = vue_du_fragment.findViewById(R.id.imageButton2) ;
            mot_a_mot = vue_du_fragment.findViewById(R.id.radioButton_apprentissage_mot_a_mot);
            plusieurs_mot_par_catgeorie = vue_du_fragment.findViewById(R.id.radioButton_apprentissage_plusieurs_mot);
            quizz = vue_du_fragment.findViewById(R.id.radioButton_quizz) ;


            if ( MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_NOM) != null )
                editText_nom.setText(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_NOM));


            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_LANGUE);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);
            cursor.moveToFirst();
            String[] fromColumns = new String[] {Base_de_donnee.LANGUE_NOM};
            int[] toControlIDs = new int[] {android.R.id.text1};
            Log.d(TAG, "onCreateView Page_demarrage: " + cursor.getCount());
            sca = new SimpleCursorAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_2 , cursor,
                    fromColumns,
                    toControlIDs);
            sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            liste_langue.setAdapter(sca);
            liste_langue2.setAdapter(sca);






            valider_choix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // recuperation des langues choisies :
                    int langue_de_base = liste_langue.getSelectedItemPosition()+1 ;
                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_LANGUE1 , String.valueOf(langue_de_base));

                    int langue_a_apprendre = liste_langue2.getSelectedItemPosition()+1 ;
                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_LANGUE2 , String.valueOf(langue_a_apprendre));

                    if ( !editText_nom.getText().toString().equals("") )
                        MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_NOM , editText_nom.getText().toString());
                    else MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_NOM , "User_Guest");

                    //Fragment_menu.nom.setText(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_NOM));

                    MainActivity.layout_demarrage = vue_du_fragment.findViewById(R.id.layout_demarrage);







                    if ( quizz.isChecked() ){
                        /**
                         * Si aucun des 2 radio boutton d'apprentissage n'est selectionné , on lance le quizz par catgeroie
                         */
                        MainActivity.layout_demarrage.setVisibility(View.INVISIBLE);
                        MainActivity.layout_question.setVisibility(View.VISIBLE);
                        MainActivity.layout_haut.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(),"Spinner res : " + langue_de_base + "/ nom : " + MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_NOM), Toast.LENGTH_SHORT).show();
                        //MainActivity.ChargeFragmentDansEmplacement_Question(AccesDonneesBDD.newInstance("affiche",Base_de_donnee.TABLE_LANGUE)); // pour l'instant paramettre vide à a voir pour permettre de creer le menu avec .
                        MainActivity.ChargeFragmentDansEmplacement_Question(AccesDonneesBDD.newInstance("affiche",Base_de_donnee.TABLE_CATGEORIE));
                    }
                    else if ( mot_a_mot.isChecked() ){
                        /**
                         * On va etudier les mot un par un dans la langue de base vers d'autre langue .
                         */
                        Log.d(TAG, "onClick: getfragmetncount :" + MainActivity.fm.getFragments().size());

                        for(Fragment f : MainActivity.fm.getFragments())
                            Log.d(TAG, "onClick: class : " + f.getArguments());



//                        MainActivity.layout_demarrage = vue_du_fragment.findViewById(R.id.layout_demarrage);
                        MainActivity.layout_demarrage.setVisibility(View.INVISIBLE);

                        MainActivity.layout_question.setVisibility(View.VISIBLE);
                        MainActivity.layout_haut.setVisibility(View.VISIBLE);

                        Fragment_question apprentissage_par_categorie = Fragment_question.newInstance("apprentissage" , "mot a mot") ;
                        MainActivity.ChargeFragmentDansEmplacement_Question(apprentissage_par_categorie);
                        Log.d(TAG, "onClick2: getfragmetncount :" + MainActivity.fm.getFragments().size());

                        for(Fragment f : MainActivity.fm.getFragments())
                            Log.d(TAG, "onClick: class : " + f.getTag());

                    }
                    else if( plusieurs_mot_par_catgeorie.isChecked()){
                        /**
                         * etudier les mots par categories
                         */
                        /**
                         * Faire les 2 cas au dessus
                         */


                    }

                }
            });

        }
        return vue_du_fragment ;

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
