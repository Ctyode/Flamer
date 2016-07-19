package org.flamie.flamethrower.util;

import android.os.Handler;

public class Util {

    private static Handler handler;

    public void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

}
