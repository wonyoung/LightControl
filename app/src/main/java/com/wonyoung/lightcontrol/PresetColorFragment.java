package com.wonyoung.lightcontrol;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wonyoung.jang on 15. 7. 24.
 */
public class PresetColorFragment extends Fragment {
    private LightController mController;

    public static PresetColorFragment newInstance(LightController controller, int sectionNumber) {
        PresetColorFragment fragment = new PresetColorFragment();
        fragment.setController(controller);
        Bundle args = new Bundle();
        args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_presetcolor, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(MainActivity.ARG_SECTION_NUMBER));
    }

    private void setController(LightController controller) {
        mController = controller;
    }
}
