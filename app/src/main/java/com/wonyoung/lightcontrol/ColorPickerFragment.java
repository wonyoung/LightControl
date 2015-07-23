package com.wonyoung.lightcontrol;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wonyoung.jang on 15. 7. 22.
 */
public class ColorPickerFragment extends Fragment {
    private LightController mController;
    private Delayer delayer = new Delayer();

    private void init(LightController controller) {
        mController = controller;
    }

    public static Fragment newInstance(LightController controller) {
        ColorPickerFragment fragment = new ColorPickerFragment();
        fragment.init(controller);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_colorpicker, container, false);
        
        ColorPicker picker = (ColorPicker) rootView.findViewById(R.id.picker);
        SVBar svbar = (SVBar) rootView.findViewById(R.id.svbar);

        picker.addSVBar(svbar);

        picker.setShowOldCenterColor(false);
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(final int i) {
                rootView.setBackgroundColor(i);
                delayer.run(new Runnable() {
                    @Override
                    public void run() {
                        mController.color(i);
                    }
                });
            }
        });

        mController.color(picker.getColor());

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private class Delayer {
        private Handler handler = new Handler();

        public void run(Runnable runnable) {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(runnable, 400);
        }
    }
}
