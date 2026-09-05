/*
 * Copyright (C) 2026 The LineageOS Project
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.display;

import android.os.Bundle;

import androidx.preference.SwitchPreferenceCompat;

import com.android.settingslib.widget.SettingsBasePreferenceFragment;

import org.lineageos.settings.R;

public class DisplaySettingsFragment extends SettingsBasePreferenceFragment {

    private static final String KEY_DC_DIMMING = "dc_dimming";

    private SwitchPreferenceCompat mDcDimming;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.display_settings, rootKey);

        mDcDimming = findPreference(KEY_DC_DIMMING);

        if (mDcDimming == null) {
            return;
        }

        if (!DisplayNodes.isDcDimmingSupported()) {
            mDcDimming.setVisible(false);
            return;
        }

        mDcDimming.setChecked(DisplayNodes.isDcDimmingEnabled());

        mDcDimming.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean enabled = (Boolean) newValue;

            if (!DisplayNodes.setDcDimmingEnabled(enabled)) {
                return false;
            }

            DisplayNodes.saveDcDimming(requireContext(), enabled);
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        requireActivity().setTitle(R.string.display_settings_title);

        if (mDcDimming != null && DisplayNodes.isDcDimmingSupported()) {
            mDcDimming.setChecked(DisplayNodes.isDcDimmingEnabled());
        }
    }
}
