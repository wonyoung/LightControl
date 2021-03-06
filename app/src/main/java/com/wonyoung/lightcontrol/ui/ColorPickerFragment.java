package com.wonyoung.lightcontrol.ui;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;
import com.wonyoung.lightcontrol.R;
import com.wonyoung.lightcontrol.control.LightController;

/**
 * Created by wonyoung.jang on 15. 7. 22.
 */
public class ColorPickerFragment extends Fragment {
    private static final int COLOR_INIT = Color.rgb(160, 160, 255);

    private LightController mLightController;

    public static Fragment newInstance(int sectionNumber) {
        ColorPickerFragment fragment = new ColorPickerFragment();
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_colorpicker, container, false);
        
        ColorPicker picker = (ColorPicker) rootView.findViewById(R.id.picker);
        SVBar svbar = (SVBar) rootView.findViewById(R.id.svbar);

        picker.addSVBar(svbar);

        picker.setColor(COLOR_INIT);
        rootView.setBackgroundColor(COLOR_INIT);

        picker.setShowOldCenterColor(false);
        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(final int i) {
                rootView.setBackgroundColor(i);
                mLightController.color(i);
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
        mLightController = mainActivity;
    }
}
