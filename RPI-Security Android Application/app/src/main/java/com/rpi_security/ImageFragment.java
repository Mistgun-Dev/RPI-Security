package com.rpi_security;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rpi_security.R;

import java.io.File;

/**
 * Created by Mohamed on 26/03/2019.
 *
 * Classe ImageFragment : Classe permettant l'affichage d'une image séléctionnée par l'utilisateur dans la GridView de capture d'images
 */

public class ImageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_image, container, false);

        String imagePath = getArguments().getString("imagePath");
        File imgFile = new  File(imagePath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView myImage = (ImageView) RootView.findViewById(R.id.image);
            myImage.setImageBitmap(myBitmap);
        }

        return RootView;
    }
}
