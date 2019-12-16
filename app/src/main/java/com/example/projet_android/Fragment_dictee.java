package com.example.projet_android;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import static com.example.projet_android.Base_de_donnee.TAG;
import static com.example.projet_android.Fragment_reponse.mtts;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_dictee.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_dictee#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_dictee extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View vue_du_frag;

    public Fragment_dictee() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_dictee.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_dictee newInstance(String param1, String param2) {
        Fragment_dictee fragment = new Fragment_dictee();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    MyContentProvider moncontentprovider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        moncontentprovider = new MyContentProvider();
    }

    static Button suivant ;
    Button son ;
    Button image ;
    static EditText rep ;
    static Cursor cursor ;
    int en_cours;
    static String mot_en_cours ;
    static String trad_en_cours ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vue_du_frag = inflater.inflate(R.layout.fragment_fragment_dictee, container, false);

        if ( mParam1 != null && mParam1.equals("dictee")){
            suivant = vue_du_frag.findViewById(R.id.button_mot_suivant_dictee);
            son = vue_du_frag.findViewById(R.id.button_lire_son);
            image = vue_du_frag.findViewById(R.id.button_image_dictee);
            rep = vue_du_frag.findViewById(R.id.editText_reponse_dictee);


            MainActivity.layout_reponse.setVisibility(View.VISIBLE);

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT);
            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);

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

            cursor.moveToFirst();
            cursor.move(liste_de_mot_deja_etudie.get(0));
            liste_de_mot_deja_etudie.remove(0);
            MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_MOT_QUESTION, cursor.getString(2));

            mot_en_cours = cursor.getString(2);


            mtts = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        int res = mtts.setLanguage(Locale.FRANCE);
                        if (res == TextToSpeech.LANG_MISSING_DATA) {
                            Log.d(TAG, "onInit: OUps , langue non prise en compte...");
                        }
                    }

                }
            });

            son.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = mot_en_cours;
                    mtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            });
            Uri.Builder builder2 = new Uri.Builder();
            builder2.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(cursor.getString(2));
            Uri uri2 = builder2.build();
            Cursor cursor2 = moncontentprovider.query(uri2,
                    null
                    ,null,
                    null,
                    null);

            if ( cursor2 != null ){
                cursor2.moveToFirst();
                if ( cursor.getString(2).equals(cursor2.getString(2))){
                    MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("dictee",cursor2.getString(1).toString()),"valider dictee");
                }
                else {
                    MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("dictee",cursor2.getString(2).toString()),"valider dictee");
                }
            }


            suivant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( cursor.moveToFirst() && liste_de_mot_deja_etudie.size() >= 1){
                        cursor.move(liste_de_mot_deja_etudie.get(0));
                        liste_de_mot_deja_etudie.remove(0);
                        MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_MOT_QUESTION, cursor.getString(2));
                        //texview_question.setText("Traduire ce mot : \n" + cursor.getString(2));
                        mot_en_cours = cursor.getString(2);
                        //Fragment_bas.trad.setBackgroundColor(Color.rgb(255 , 235 , 59));
                        //rep.setBackgroundColor(Color.WHITE);

                        Uri.Builder builder2 = new Uri.Builder();
                        builder2.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(cursor.getString(2));
                        Uri uri2 = builder2.build();
                        Cursor cursor2 = moncontentprovider.query(uri2,
                                null
                                ,null,
                                null,
                                null);

                        if ( cursor2 != null ){
                            cursor2.moveToFirst();
                            if ( cursor.getString(2).equals(cursor2.getString(2))){
                                MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("dictee",cursor2.getString(1).toString()),"valider dictee");
                            }
                            else {
                                MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("dictee",cursor2.getString(2).toString()),"valider dictee");
                            }
                        }

                    }

                }
            });





            MainActivity.layout_bas.setVisibility(View.VISIBLE);






        }

        return vue_du_frag ;
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
