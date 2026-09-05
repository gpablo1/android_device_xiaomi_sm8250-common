/*
 * Copyright (C) 2026 The LineageOS Project
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.display;

import android.content.Context;
import android.content.SharedPreferences;
import org.lineageos.settings.utils.FileUtils;

public final class DisplayNodes {

    private static final String PREFS = "display_settings";
    private static final String KEY_DC_DIMMING = "dc_dimming";
    public static final String DC_DIMMING_NODE =
            "/sys/devices/platform/soc/soc:qcom,dsi-display-primary/dimlayer_exposure";

    private DisplayNodes() {
    }

    public static boolean isDcDimmingSupported() {
        return FileUtils.fileExists(DC_DIMMING_NODE);
    }

    public static boolean isDcDimmingEnabled() {
        return "1".equals(FileUtils.readOneLine(DC_DIMMING_NODE));
    }

    public static boolean setDcDimmingEnabled(boolean enabled) {
        try {
            FileUtils.writeLine(DC_DIMMING_NODE, enabled ? "1" : "0");
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void saveDcDimming(Context context, boolean enabled) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(KEY_DC_DIMMING, enabled)
                .apply();
    }

    public static void restoreDcDimming(Context context) {
        if (!isDcDimmingSupported()) {
            return;
        }

        SharedPreferences prefs =
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        if (!prefs.contains(KEY_DC_DIMMING)) {
            return;
        }

        boolean enabled = prefs.getBoolean(KEY_DC_DIMMING, false);

        setDcDimmingEnabled(enabled);
    }
}
