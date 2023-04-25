package com.example.asteroiddodger;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class gameFragment extends Fragment {

    View rootView;


    public gameFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.game_layout, container, false);
        FloatingActionButton pauseButton = rootView.findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(pause);

        return rootView;
    }

    public View.OnClickListener pause = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gameView game = (gameView) rootView.findViewById(R.id.gameView);
            Log.i("Paused", "paused");
            game.togglePause();
        }
    };


}
