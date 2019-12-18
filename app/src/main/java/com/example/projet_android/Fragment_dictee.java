package com.example.projet_android;

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
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    Button imageb ;
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

            MainActivity.layout_reponse.setVisibility(View.VISIBLE);
            suivant = vue_du_frag.findViewById(R.id.button_mot_suivant_dictee);
            son = vue_du_frag.findViewById(R.id.button_lire_son);
            imageb = vue_du_frag.findViewById(R.id.button_image_dictee);
            rep = vue_du_frag.findViewById(R.id.editText_reponse_dictee);


            MainActivity.layout_reponse.setVisibility(View.VISIBLE);
            String categorie = MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_CATEGORIE);

            Uri.Builder builder = new Uri.Builder();builder.scheme("content").authority(Base_de_donnee.authority)
                    .appendPath(Base_de_donnee.TABLE_TRAD)
                    .appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE1))
                    .appendPath(MainActivity.bundle_de_la_session_en_cours.getString(MainActivity.BUNDLE_LANGUE2))
                    .appendPath(categorie);

            Uri uri = builder.build();
            cursor = moncontentprovider.query(uri,
                    null
                    ,null,
                    null,
                    null);

            final Cursor tmp = cursor ;

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
                    if ( cursor.moveToFirst() && liste_de_mot_deja_etudie.size() > 1){
                        tmp.move(liste_de_mot_deja_etudie.get(0));
                        liste_de_mot_deja_etudie.remove(0);
                        MainActivity.bundle_de_la_session_en_cours.putString(MainActivity.BUNDLE_MOT_QUESTION, tmp.getString(2));
                        //texview_question.setText("Traduire ce mot : \n" + cursor.getString(2));
                        mot_en_cours = tmp.getString(2);
                        //Fragment_bas.trad.setBackgroundColor(Color.rgb(255 , 235 , 59));
                        //rep.setBackgroundColor(Color.WHITE);

                        Uri.Builder builder2 = new Uri.Builder();
                        builder2.scheme("content").authority(Base_de_donnee.authority).appendPath(Base_de_donnee.TABLE_MOT).appendPath(tmp.getString(2));
                        Uri uri2 = builder2.build();

                        rep.setText("");
                        rep.setHint("Bonne réponse ! ");
                        rep.setBackgroundColor(Color.WHITE);
                        Cursor cursor2 = moncontentprovider.query(uri2,
                                null
                                ,null,
                                null,
                                null);

                        if ( cursor2 != null ){
                            cursor2.moveToFirst();
                            if ( tmp.getString(2).equals(cursor2.getString(2))){
                                MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("dictee",cursor2.getString(1).toString()),"valider dictee");
                            }
                            else {
                                MainActivity.ChargeFragmentDansEmplacement_Bas(Fragment_bas.newInstance("dictee",cursor2.getString(2).toString()),"valider dictee");
                            }
                        }

                    }
                    else {
                        MainActivity.fm.popBackStack();
                        MainActivity.fm.popBackStack();

                    }

                }
            });

            imageb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ImageView image = new ImageView(getContext());
                    //FileInputStream f =  new FileInputStream(String.format("%s/downloadfile-6.jpg", getContext().getFilesDir().toString())) ;
                    BufferedInputStream buf = null;
                    Log.d(TAG, "coucou");
                    try {
                        String mot = mot_en_cours;

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
                            if (cursor.getString(5) != null && !cursor.getString(5).equals("vide") ) {
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

                                                            String mot = mot_en_cours;
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
                                        if(file.exists()){
                                            Log.d(TAG, "onClick: Fichier trouvé");
                                            // on va supprimer la reference de la BDD en mettant à jour le champs img_interne à ""


                                            String mot = mot_en_cours;
                                            Uri.Builder uri_tmp = new Uri.Builder();
                                            uri_tmp.scheme("content").authority(Base_de_donnee.authority).appendPath("img_int").appendPath(mot) ;
                                            Uri urii = uri_tmp.build() ;
                                            MyContentProvider myContentProvider = new MyContentProvider();
                                            ContentValues cv = new ContentValues();

                                            cv.put("mot",mot);
                                            cv.put("img_interne" , "vide" );
                                            int tmp = myContentProvider.update(urii,cv,null,null) ;

                                        }else{
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
                                if (cursor.getString(6) != null ) {
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

                                                        String mot = mot_en_cours;
                                                        mtts.speak(mot, TextToSpeech.QUEUE_FLUSH, null);
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    alert.show();


                                }
                                else {
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

                                                        String mot = mot_en_cours;
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

                                            String mot = mot_en_cours;
                                            mtts.speak(mot, TextToSpeech.QUEUE_FLUSH, null);
                                        }
                                    }
                                });
                            }
                        }).show();

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
        MainActivity.layout_question.setVisibility(View.GONE);
        MainActivity.layout_demarrage.setVisibility(View.VISIBLE);
        MainActivity.layout_bas.setVisibility(View.GONE);

        MainActivity.layout_reponse.setVisibility(View.GONE);
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
