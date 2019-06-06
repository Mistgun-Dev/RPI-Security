package com.rpi_security;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import java.util.Calendar;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;

/**
 * Classe CameraFragment : Cette classe permet l'accès à une caméra, sa visualisation en temp réel, et l'interraction avec le serveur
 * Raspberry. Cette classe instencie un objet de type "SocketConnection" permettant l'envoi de commandes sur la Raspberry via proto° TCP
 * Fonctionalités :
 *
 *    - Visualisation du flux vidéo de la caméra dans une WebView
 *    - Contrôle des servomoteurs de la Raspberry via les flèches directionnelle
 *    - Prises de captures d'écran, enregistré dans le dossier "sdcard/RPISecurity/ScreenShots"
 *    - Activation et désactivation de l'alarme Buzzer de la Raspberry
 */

public class CameraFragment extends Fragment
{

    WebView cameraWebView;    //WebView affichant le flux de la caméra
    String textIP, textPort;
    SocketConnection socketConnection;
    Button btnLeft, btnRight, btnUp, btnDown, btnScreenShot, btnAlarm;
    boolean isAlarmActivated = false;
    Command cmd;
    ProgressBar progressBarWebView;
    View RootView;
    boolean webViewSuccess = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RootView = inflater.inflate(R.layout.fragment_camera, container, false);
        progressBarWebView = (ProgressBar) RootView.findViewById(R.id.progressBar);

        //Récupération des informations de connexion, et connexion au serveur TCP de la RPI
        GetInfoConnexion();
        viewCameraStream();
        socketConnection = new SocketConnection(textIP, textPort);

        //Gestion d'évènements des bouttons
        eventArrowDirection();
        eventButtonScreenShot();
        eventButtonAlarm();

        return RootView;
    }

    /**
     * Fonction gérant les évènements du bouton d'activation/désactivation de l'alarme
     */
    void eventButtonAlarm()
    {
        this.btnAlarm = (Button) RootView.findViewById(R.id.btnAlarm);

        //Boutton d'activation désactivation du buzzer
        this.btnAlarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isAlarmActivated == false) {
                    btnAlarm.setBackgroundResource(R.mipmap.outline_volume_off_black_24);
                    socketConnection.sendCommandToRPI(cmd.ACIVATE_ALARM);
                }
                else {
                    btnAlarm.setBackgroundResource(R.mipmap.outline_volume_up_black_24);
                    socketConnection.sendCommandToRPI(cmd.DESACTIVATE_ALARM);
                }

                isAlarmActivated = !isAlarmActivated;
            }
        });
    }

    /**
     * Fonction gérant les évènements du bouton de prise de captures d'images
     * Captures d'images enregistrés dans le dossier "sdcard/RPISecurity/ScreenShots"
     * Le nom de chaque images correspond à la date et l'heure de capture
     */
    void eventButtonScreenShot()
    {
        this.btnScreenShot = (Button) RootView.findViewById(R.id.btnScreenShot);

        this.btnScreenShot.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cameraWebView.setDrawingCacheEnabled(true);
                cameraWebView.buildDrawingCache();
                Bitmap bm = Bitmap.createBitmap(cameraWebView.getMeasuredWidth(),
                        cameraWebView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

                Canvas bigcanvas = new Canvas(bm);
                Paint paint = new Paint();
                int iHeight = bm.getHeight();
                bigcanvas.drawBitmap(bm, 0, iHeight, paint);
                cameraWebView.draw(bigcanvas);

                if (bm != null) {
                    try {
                        String path = Environment.getExternalStorageDirectory()
                                .toString();
                        OutputStream fOut = null;

                        Date currentTime = Calendar.getInstance().getTime();
                        File file = new File(path, "/RPISecurity/ScreenShots/"+String.valueOf(currentTime)+".png");
                        fOut = new FileOutputStream(file);
                        Toast.makeText(getActivity(), "Images enregistré dans le dossier RPISecurity/Screenshots", Toast.LENGTH_LONG).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 50, fOut);
                        fOut.flush();
                        fOut.close();
                        bm.recycle();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * Fonction gérant les evènements des touches directionnelles permettant la rotation de la caméra horizontalement
     * et verticalement
     */
    void eventArrowDirection()
    {
        this.btnUp = (Button) RootView.findViewById(R.id.btnUp);
        this.btnDown = (Button) RootView.findViewById(R.id.btnDown);
        this.btnRight = (Button) RootView.findViewById(R.id.btnRight);
        this.btnLeft = (Button) RootView.findViewById(R.id.btnLeft);

        //HAUT
        this.btnUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                socketConnection.sendCommandToRPI(cmd.ROT_CAM_UP);
            }
        });
        //BAS
        this.btnDown.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                socketConnection.sendCommandToRPI(cmd.ROT_CAM_DOWN);
            }
        });
        //DROITE
        this.btnRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                socketConnection.sendCommandToRPI(cmd.ROT_CAM_RIGHT);
            }
        });
        //GAUCHE
        this.btnLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                socketConnection.sendCommandToRPI(cmd.ROT_CAM_LEFT);
            }
        });
    }

    /**
     * Fonction récupérant l'adresse IP et le Port de connexion
     */
    void GetInfoConnexion()
    {
        this.textIP = getArguments().getString("IP");
        this.textPort = getArguments().getString("PORT");
    }

    /**
     * Fonction initialisant la WebView permettant la visualisation du flux vidéo de la caméra
     */
    void viewCameraStream()
    {
        cameraWebView = (WebView) RootView.findViewById(R.id.webViewCamera);

        cameraWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                progressBarWebView.setVisibility(View.GONE);
                if(webViewSuccess == true)
                    Toast.makeText(getActivity(), "Connexion Réussie", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                new AlertDialog.Builder(getActivity())
                        .setMessage("Connexion échouée, vérifiez la connexion internet ainsi que les informations de connexion")
                        .setPositiveButton(android.R.string.ok, null).create().show();

                webViewSuccess = false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                Log.e("LOG", "PAGE TERMINEE");
                progressBarWebView.setVisibility(View.GONE);
            }
        });

        cameraWebView.getSettings().setBuiltInZoomControls(true);   // Zoom webView
        cameraWebView.getSettings().setDisplayZoomControls(false);  // Zoom webview
        cameraWebView.getSettings().setUseWideViewPort(true);
        cameraWebView.getSettings().setLoadWithOverviewMode(true);
        cameraWebView .loadUrl("http://" + this.textIP + ":" + this.textPort);
    }

    /**
     * Fonction permettant la déstruction de la WebView
     */
    public void destroyWebView()
    {
        if(cameraWebView != null)
        {
            cameraWebView.removeAllViews();
            cameraWebView.clearHistory();
            cameraWebView.clearCache(true);
            cameraWebView.loadUrl("about:blank");
            cameraWebView.onPause();
            cameraWebView.removeAllViews();
            cameraWebView.destroyDrawingCache();
            cameraWebView.pauseTimers();
            cameraWebView.destroy();
            cameraWebView = null;
        }
    }
}
