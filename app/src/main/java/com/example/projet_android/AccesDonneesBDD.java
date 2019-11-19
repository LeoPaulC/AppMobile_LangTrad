package com.example.projet_android;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.projet_android.MainActivity.frag_accesBDD;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccesDonneesBDD.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccesDonneesBDD#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccesDonneesBDD extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MyContentProvider moncontentprovider ;

    CursorLoader mycursorLoad;
    SimpleCursorAdapter sca ;
    private OnFragmentInteractionListener mListener;
    private View vue_du_fragment;
    ListView lv ;
    Cursor cursor ;
    TextView en_tete_list ;

    public AccesDonneesBDD() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccesDonnees.
     */
    // TODO: Rename and change types and number of parameters
    public static AccesDonneesBDD newInstance(String param1, String param2) {
        AccesDonneesBDD fragment = new AccesDonneesBDD();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        Log.d(Base_de_donnee.TAG, "Fragment Acces Donnees : new Instance .");
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
        Log.d(Base_de_donnee.TAG, "Fragment Acces Donnees : OnCreate .");


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        vue_du_fragment = inflater.inflate(R.layout.fragment_acces_donnees_bdd, container, false);
        Log.d(Base_de_donnee.TAG, "Fragment Acces Donnees : OnCreateView .");

        if ( mParam1 != null && mParam1 == "affiche" && mParam2 == Base_de_donnee.CATEGORIE){
            /**
             * on va construire l'URI pour recuperer les categories .
             */
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_CATGEORIE);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);
            en_tete_list = vue_du_fragment.findViewById(R.id.textView_en_tete_list);
            en_tete_list.setText("Choisir Votre Categorie : ");
            Log.d(Base_de_donnee.TAG,"Dans L'acces au données : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;
            lv = vue_du_fragment.findViewById(R.id.ma_liste_view);
            cursor.moveToFirst();
            String[] fromColumns = new String[] {Base_de_donnee.NOM_CATGEORIE , Base_de_donnee.ID_CATEGORIE};
            int[] toControlIDs = new int[] {android.R.id.text1};
            sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2 , cursor,
                    fromColumns,
                    toControlIDs);
            sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lv.setAdapter(sca);
            Log.d(Base_de_donnee.TAG, "Fragment Acces Donnees : OnCreateView terminé , liste en place .");

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(parent.getContext(),
                            ">> : " + ((Cursor) parent.getItemAtPosition(position)).getString(1),
                            Toast.LENGTH_SHORT).show();
                    // on met la categorie choisie dans le bundle de session
                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_CATEGORIE,((Cursor) parent.getItemAtPosition(position)).getString(1));

                    //MainActivity.frag_accesBDD = AccesDonneesBDD.newInstance("affiche",Base_de_donnee.TABLE_LANGUE); // pour l'instant paramettre vide à a voir pour permettre de creer le menu avec .
                    Log.d(Base_de_donnee.TAG, "Fragment Acces Donnees : OnClickListener Categorie.");
                    MainActivity.ChargeFragmentDansEmplacement_Question(AccesDonneesBDD.newInstance("affiche",Base_de_donnee.TABLE_LANGUE));



                }
            });


        }
        if ( mParam1 != null && mParam1 == "affiche" && mParam2 == Base_de_donnee.TABLE_LANGUE){
            /**
             * on va construire l'URI pour recuperer les Langues .
             */

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_LANGUE);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);
            en_tete_list = vue_du_fragment.findViewById(R.id.textView_en_tete_list);
            en_tete_list.setText("Choisir Votre Langue : ");
            Log.d(Base_de_donnee.TAG,"Dans L'acces au données : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;
            lv = vue_du_fragment.findViewById(R.id.ma_liste_view);
            cursor.moveToFirst();
            String[] fromColumns = new String[] {Base_de_donnee.LANGUE_NOM};
            int[] toControlIDs = new int[] {android.R.id.text1};
            sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2 , cursor,
                    fromColumns,
                    toControlIDs);
            sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            lv.setAdapter(sca);
            Log.d(Base_de_donnee.TAG, "Fragment Acces Donnees : OnCreateView terminé , liste en place .");

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(parent.getContext(),
                            ">> : " + ((Cursor) parent.getItemAtPosition(position)).getString(1),
                            Toast.LENGTH_SHORT).show();
                    // on met la categorie choisie dans le bundle de session
                    String pos = String.valueOf(position+1);
                    Log.d(Base_de_donnee.TAG, "POSITION de la langue :::::::::::: " + pos);

                    MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_LANGUE2,pos);

                    Log.d(Base_de_donnee.TAG, "Fragment Acces Donnees : OnClickListener Langue .");
                    MainActivity.ChargeFragmentDansEmplacement_Question(Fragment_question.newInstance("affiche",Base_de_donnee.TABLE_MOT));


                }
            });

        }



        return vue_du_fragment;
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
