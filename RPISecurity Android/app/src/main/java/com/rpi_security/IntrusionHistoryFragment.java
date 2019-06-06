package com.rpi_security;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rpi_security.R;

/**
 * Classe IntrusionHistoryFragment : Classe permettant l'affichage de l'historique des intrusions détéctés par la caméra, et des images
 * prises lors des détéction
 */

public class IntrusionHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_history, container, false);
    }

}
