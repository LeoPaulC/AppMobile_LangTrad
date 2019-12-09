package com.example.projet_android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.projet_android.Base_de_donnee.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_reponse.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_reponse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_reponse extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Cursor cursor ;
    MyContentProvider moncontentprovider ;
    static ProgressBar pb_exit ;
    static int progress = 0 ;
    private static int compteur = 0 ;

    private OnFragmentInteractionListener mListener;
    private View vue_du_fragment;

    public static EditText getEditText_reponse() {
        return editText_reponse;
    }

    public static void setEditText_reponse(EditText editText_reponse) {
        Fragment_reponse.editText_reponse = editText_reponse;
    }

    static EditText editText_reponse ;
    ImageButton affiche_image ;

    public Fragment_reponse() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_reponse.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_reponse newInstance(String param1, String param2) {
        Fragment_reponse fragment = new Fragment_reponse();
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

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vue_du_fragment = inflater.inflate(R.layout.fragment_reponse, container, false);


        if ( mParam1 != null && mParam1 == "affiche" && mParam2 == "reponse") {

            editText_reponse = ((EditText)vue_du_fragment.findViewById(R.id.textView_reponse));
            affiche_image = vue_du_fragment.findViewById(R.id.button_affiche_image) ;
            
            editText_reponse.setVisibility(View.VISIBLE);
            affiche_image.setVisibility(View.VISIBLE);
            
            affiche_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: imageView affiche");
                }
            });

        }
        if ( mParam1 != null && mParam1.equals("trad") && mParam2 != null){ // affichier al traduction d'apres un mot slectionné
            editText_reponse = ((EditText)vue_du_fragment.findViewById(R.id.textView_reponse));
            editText_reponse.setEnabled(false);
            editText_reponse.setVisibility(View.VISIBLE);
            Uri.Builder builder = new Uri.Builder();
            Log.d("d" ,"Fragment Reponse : mot a traduire :" + mParam2 );
            builder.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(mParam2);
            Uri uri = builder.build();
            Log.d("d" ,"Fragment Reponse : URI " + uri );
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);
            cursor.moveToFirst();
            if ( cursor != null ){
               if ( cursor.getString(1).equals(mParam2)){
                   // cas ou la traduction colle avec le mot choisi
                   editText_reponse.setText(cursor.getString(2));

               }
               else {
                   // le cas ou c'est la reponse de la trad qui match avec le mot selectionne
                   editText_reponse.setText(cursor.getString(1));
               }

            }


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
        if ( mParam1 != null && mParam1 == "affiche" && mParam2 == "reponse") {
            MainActivity.fm.popBackStack();
        }
        if ( mParam1 != null && mParam1 == "trad" && mParam2 != null ) {
            for (Fragment a : MainActivity.fm.getFragments() ) {
                // on retire de la liste tous les fragment créé pour la réponse
                if ( a instanceof Fragment_reponse)
                    MainActivity.fm.popBackStack();
            }
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
