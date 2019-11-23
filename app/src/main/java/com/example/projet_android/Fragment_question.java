package com.example.projet_android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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

    Cursor cursor ;

    TextView texview_question ;




    private OnFragmentInteractionListener mListener;

    TextView text_view_de_la_question ;
    Button button_passer_la_question ;
    Button valider ;
    static View vue_du_frag;
    public static ListView mon_recycler_view ;

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

        Log.d(Base_de_donnee.TAG, "Fragment Question : OnCreateView .");


        /**
         * On affiche un mot au hasard parmis la liste de dispo pour la categorie  .
         */
        if ( mParam1 != null && mParam1 == "affiche" && mParam2 == Base_de_donnee.TABLE_MOT){

            String catgeorie_en_cours = MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_CATEGORIE) ;
            String langue_en_cours = MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2) ;

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);
            texview_question = vue_du_frag.findViewById(R.id.Textview_question);
            texview_question.setText("Traduire ce mot : ");
            Log.d(Base_de_donnee.TAG,"Dans Fragment Question : " + cursor.getColumnName(0 ) + " | " + cursor.getColumnName(1) + " | " + cursor.getColumnName(2) + " | " + cursor.getColumnName(3)) ;

            cursor.moveToFirst();
            MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_MOT_QUESTION, cursor.getString(2));
            texview_question.setText("Traduire ce mot : " + cursor.getString(2));

            button_passer_la_question = vue_du_frag.findViewById(R.id.button_passer_question);
            button_passer_la_question.setVisibility(View.VISIBLE);
            button_passer_la_question.setText("Passer cette Question");
            button_passer_la_question.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( cursor.moveToNext() ){
                        MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_MOT_QUESTION, cursor.getString(2));
                        texview_question.setText("Traduire ce mot : " + cursor.getString(2));
                        Fragment_bas.trad.setBackgroundColor(Color.rgb(255 , 235 , 59));
                        // 255 , 235 , 59

                    }
                }
            });

            MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("valide","traduction"),"valider choix");
            MainActivity.ChargeFragmentDansEmplacement_Reponse(Fragment_reponse.newInstance("affiche","reponse"),"traduction");



            Log.d(Base_de_donnee.TAG, "Fragment Question: OnCreateView terminé , Question en place .");
        }

        return vue_du_frag;

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
