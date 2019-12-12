package com.example.projet_android;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;

import static com.example.projet_android.Base_de_donnee.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ajout_BDD_trad.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ajout_BDD_trad#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ajout_BDD_trad extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View vue_du_fragment;
    private SimpleCursorAdapter sca;

    public ajout_BDD_trad() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ajout_BDD_trad.
     */
    // TODO: Rename and change types and number of parameters
    public static ajout_BDD_trad newInstance(String param1, String param2) {
        ajout_BDD_trad fragment = new ajout_BDD_trad();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    MyContentProvider moncontentprovider ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        moncontentprovider = new MyContentProvider();
    }

    TextView tv_choix_cat ;
    TextView tv_langue1 ;
    TextView tv_langue2 ;

    EditText mot1 ;
    EditText mot2 ;

    Spinner sp_cat ;
    Spinner sp_l1 ;
    Spinner sp_l2 ;

    Button add_valider ;

    Cursor cursor ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vue_du_fragment = inflater.inflate(R.layout.fragment_ajout__bdd_trad, container, false);
        Log.d(TAG, "onCreateView: Ajout BDD trad " + mParam1 + mParam2);

        if ( mParam1 != null || mParam2 != null ){

            tv_choix_cat = vue_du_fragment.findViewById(R.id.add_tv_choix_categorie) ;
            tv_langue1 = vue_du_fragment.findViewById(R.id.tv_langue1);
            tv_langue2 = vue_du_fragment.findViewById(R.id.tv_langue2);

            sp_cat= vue_du_fragment.findViewById(R.id.add_categorie);
            sp_l1= vue_du_fragment.findViewById(R.id.add_langue1);
            sp_l2= vue_du_fragment.findViewById(R.id.add_langue2);

            mot1 = vue_du_fragment.findViewById(R.id.add_mot);
            mot2 = vue_du_fragment.findViewById(R.id.add_trad);

            add_valider= vue_du_fragment.findViewById(R.id.add_valider) ;


            // TODO : GERE le spinner du choix de catgeorie

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_CATGEORIE);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);
            Log.d(TAG,"Dans L'acces au données : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) ) ;
            cursor.moveToFirst();
            String[] fromColumns = new String[] {Base_de_donnee.NOM_CATGEORIE , Base_de_donnee.ID_CATEGORIE};
            int[] toControlIDs = new int[] {android.R.id.text2};
            sca = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_2 , cursor,
                    fromColumns,
                    toControlIDs);
            sca.setDropDownViewResource(android.R.layout.simple_list_item_2);
            sp_cat.setAdapter(sca);

            // TODO gere l'affichage des langue 1 et 2

            builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_LANGUE);
            uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);
            cursor.moveToFirst();
            fromColumns = new String[] {Base_de_donnee.LANGUE_NOM , Base_de_donnee.ID_LANGUE};
            toControlIDs = new int[] {android.R.id.text1};
            Log.d(TAG, "onCreateView AJout BDD trad: " + cursor.getCount());
            sca = new SimpleCursorAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_2 , cursor,
                    fromColumns,
                    toControlIDs);
            sca.setDropDownViewResource(android.R.layout.simple_list_item_2);
            sp_l1.setAdapter(sca);
            sp_l2.setAdapter(sca);

            // TODO ajoute a la BDD :
            /**
             *  mot1 - langue 1
             *  mot 2 - langue 2
             *  trad : mot 1 -> mot 2 ; langue 1 -> langue 2
             */
            sp_l1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if ( position == sp_l2.getSelectedItemPosition()){
                        add_valider.setEnabled(false);
                    }
                    else {
                        add_valider.setEnabled(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            sp_l2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if ( position == sp_l1.getSelectedItemPosition() ){
                        add_valider.setEnabled(false);
                    }
                    else {
                        add_valider.setEnabled(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            add_valider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // recueprer les valeurs des champs remplie
                    int cat = sp_cat.getSelectedItemPosition() ;
                    int l1 = sp_l1.getSelectedItemPosition() ;
                    int l2 = sp_l2.getSelectedItemPosition() ;

                    Log.d(TAG, "onClick: langue de Src :  pos : "+ l1 + " " + ((Cursor) sp_l1.getItemAtPosition(l1)).getString(1) );
                    Log.d(TAG, "onClick: langue de Dst :  pos : "+ l2 + " " + ((Cursor) sp_l2.getItemAtPosition(l2)).getString(1) );
                    Log.d(TAG, "onClick: Categorie :  " + ((Cursor) sp_cat.getItemAtPosition(cat)).getString(1) );

                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("content").authority(Base_de_donnee.authority).appendPath("ajout").appendPath(mot1.getText().toString())
                            .appendPath(String.valueOf(l1)).appendPath(mot2.getText().toString()).appendPath(String.valueOf(l2)).appendPath(((Cursor) sp_cat.getItemAtPosition(cat)).getString(1).toString())
                    .appendPath(((Cursor) sp_l1.getItemAtPosition(l1)).getString(1)).appendPath(((Cursor) sp_l2.getItemAtPosition(l2)).getString(1));

                    Uri uri = builder.build();
                    Log.d(TAG, "onClick: Uri == " + uri);
                    Uri res = moncontentprovider.insert(uri,
                            null
                            );
                    Log.d(TAG, "onClick: ajout trad :: " + res);


                }
            });


            // TODO creation de categorie





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
