package com.rmatushkin.util;

import static java.lang.String.format;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TimeUtil {

    public static String formatMilliseconds(long milliseconds) {
        String sign = "";
        if (milliseconds < 0) {
            sign = "-";
            milliseconds = Math.abs(milliseconds);
        }

        long minutes = milliseconds / MINUTES.toMillis(1);
        long seconds = milliseconds % MINUTES.toMillis(1) / SECONDS.toMillis(1);
        long millis = milliseconds % SECONDS.toMillis(1);

        return sign +
                format("%02d min", minutes) +
                format(" %02d sec", seconds) +
                format(" %03d ms", millis);
    }
}
