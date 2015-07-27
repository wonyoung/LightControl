package com.wonyoung.lightcontrol.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.wonyoung.lightcontrol.R;
import com.wonyoung.lightcontrol.control.LightController;

/**
 * Created by wonyoung.jang on 15. 7. 24.
 */
public class PresetColorFragment extends Fragment {
    private static final int MAX_PERIOD = 9;
    private static final int MAX_BRIGHTNESS = 255;
    private static final int DEFAULT_PERIOD = 4;
    private static final int DEFAULT_BRIGHTNESS = 255;

    private LightController mLightController;

    public static PresetColorFragment newInstance(int sectionNumber) {
        PresetColorFragment fragment = new PresetColorFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_presetcolor, container, false);
        ImageButton blackButton = (ImageButton) rootView.findViewById(R.id.button_black);
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLightController.black();
            }
        });
        ImageButton whiteButton = (ImageButton) rootView.findViewById(R.id.button_white);
        whiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLightController.white();
            }
        });
        ImageButton rainbowButton = (ImageButton) rootView.findViewById(R.id.button_rainbow);
        rainbowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLightController.rainbow();
            }
        });
        ImageButton gradientButton = (ImageButton) rootView.findViewById(R.id.button_gradient);
        gradientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLightController.gradient();
            }
        });
        Button stopButton = (Button) rootView.findViewById(R.id.button_stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLightController.pause();
            }
        });
        Button blinkButton = (Button) rootView.findViewById(R.id.button_blink);
        blinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLightController.blink();
            }
        });

        SeekBar periodSeekBar = (SeekBar) rootView.findViewById(R.id.seekbar_period);
        periodSeekBar.setMax(MAX_PERIOD - 1);
        periodSeekBar.setProgress(DEFAULT_PERIOD);
        periodSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLightController.period(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar brightnessSeekBar = (SeekBar) rootView.findViewById(R.id.seekbar_brightness);
        brightnessSeekBar.setMax(MAX_BRIGHTNESS);
        brightnessSeekBar.setProgress(DEFAULT_BRIGHTNESS);
        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLightController.brightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity mainActivity = (MainActivity) activity;
        mainActivity.onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
        mLightController = mainActivity.getLightController();
    }

}
