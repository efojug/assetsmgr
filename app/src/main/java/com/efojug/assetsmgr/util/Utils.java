package com.efojug.assetsmgr.util;

import android.content.Context;
import android.content.res.Configuration;

public class Utils {
    public static boolean isDarkThemeEnabled(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
