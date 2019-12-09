package com.example.projet_android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.example.projet_android.Base_de_donnee.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link fragment_acces_memoire_tel.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link fragment_acces_memoire_tel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_acces_memoire_tel extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View vue_du_fragment;

    static AutoCompleteTextView chemin ;
    Button valider_choix_mot_image ;
    private WebView webview_image;

    public fragment_acces_memoire_tel() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_acces_memoire_tel.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_acces_memoire_tel newInstance(String param1, String param2) {
        fragment_acces_memoire_tel fragment = new fragment_acces_memoire_tel();
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
        chemin = new AutoCompleteTextView(getContext()) ;
        valider_choix_mot_image = new Button(getContext());
        webview_image = new WebView(getContext());
    }


    static String url ;
    static String DownloadImageURL ;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vue_du_fragment  = inflater.inflate(R.layout.fragment_fragment_acces_memoire_tel, container, false);

        if (mParam1 !=null ){
            MainActivity.layout_reponse.setVisibility(View.INVISIBLE);
            webview_image = vue_du_fragment.findViewById(R.id.webview_image) ;
            valider_choix_mot_image = vue_du_fragment.findViewById(R.id.valider_choix_mot_image) ;
            chemin = (AutoCompleteTextView) vue_du_fragment.findViewById(R.id.chemin_rep) ;

            // pour nos test
            chemin.setText("Voiture");


            String[] tableauString = getResources().getStringArray(R.array.tableau_mot);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, tableauString);

            chemin.setAdapter(adapter);
            valider_choix_mot_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    webview_image.getSettings().setLoadsImagesAutomatically(true);
                    webview_image.getSettings().setAllowFileAccess(true);
                    webview_image.getSettings().setDomStorageEnabled(true);
                    webview_image.getSettings().setJavaScriptEnabled(true);
                    webview_image.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

                    webview_image.setWebViewClient(new WebViewClient());

                    //Register the WebView to be able to take display a menu...
                    //You'll need this menu to choose an action on long press
                    registerForContextMenu(webview_image);
                    //webview_image.loadUrl(HTTP_URL);
                    String url = "https://www.google.com/search?hl=fr&biw=1870&bih=919&tbm=isch&sxsrf=ACYBGNQevJnnHnyN_4qXYAPXdH1p4cqxIg%3A1574949813744&sa=1&ei=tdPfXbGLLYTnxgOQh6vwDw&q="+chemin.getText()+"&oq="+chemin.getText()+"&gs_l=img.3..0i67l6j0j0i67j0l2.1461.2033..2141...2.0..0.68.257.5......0....1..gws-wiz-img.....10..35i362i39j35i39.Q2ovIvlBHrk&ved=0ahUKEwjxub-hiY3mAhWEs3EKHZDDCv4Q4dUDCAY&uact=5#imgrc=gzJYbbw5cVkXPM:" ;
                    webview_image.loadUrl(url);
                    // url google image recherche de fraise
                    //webview_image.setVisibility(View.VISIBLE);
                    webview_image.setVisibility(View.VISIBLE);

                }
            });




        }
        return vue_du_fragment ;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                    ContextMenu.ContextMenuInfo contextMenuInfo){
        super.onCreateContextMenu(contextMenu, view, contextMenuInfo);

        final WebView.HitTestResult webViewHitTestResult = webview_image.getHitTestResult();

        if (webViewHitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE  ) {

            contextMenu.setHeaderTitle("Telechargement");

            contextMenu.add(0, 1, 0, "Telecharger l'image ?")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {

                            DownloadImageURL = webViewHitTestResult.getExtra();

                            if(URLUtil.isValidUrl(url) || ( DownloadImageURL.startsWith("https:") || DownloadImageURL.startsWith("http:") ) ){ // voir pour modider ça car fixe pour l'instant
                                askForPermission();
                                //lance_requette_DL(url , DownloadImageURL);
                                return true ;
                            }
                            else {
                                Toast.makeText(getActivity(),"Oups , ce type d'image n'est pas pris en compte ", Toast.LENGTH_LONG).show();
                                return false;
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == 2)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //on appelle
                lance_requette_DL(url , DownloadImageURL);
            }
            else if (shouldShowRequestPermissionRationale(permissions[0]) == false)
            {
                displayOptions();
            }
            else
            {
                explain();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void lance_requette_DL(String url , String DownloadImageURL){

        Log.d(TAG, "lance_requette_DL: " + DownloadImageURL.toString());

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadImageURL));

        request.allowScanningByMediaScanner();
        //request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fraise.jpg");
        //request.setDestinationUri(Uri.parse(getContext().getFilesDir().toString()+"/fraise.jpg")) ;
        String destinationDirectory = Environment.getExternalStorageDirectory() + "/Images/" ;
        request.setDestinationUri(Uri.fromFile(new File(destinationDirectory,chemin.getText()+".jpg")));

        request.setDescription("Votre image");
        request.setTitle(chemin.getText()+".jpg");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        Toast.makeText(getActivity(),"Image Downloaded Successfully.",Toast.LENGTH_LONG).show();

    }

    private void displayOptions()
    {
        Log.d(TAG, "displayOptions: 1");
    }

    private void askForPermission()
    {
        requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 2);
    }

    private void explain()
    {
        Log.d(TAG, "explain: 2 ");
        askForPermission();
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
