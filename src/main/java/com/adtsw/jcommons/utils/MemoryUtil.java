package com.adtsw.jcommons.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MemoryUtil {

    private static final double MEGABYTE_FACTOR = 1024L * 1024L;
    private static final DecimalFormat ROUNDED_DOUBLE_DECIMAL_FORMAT;

    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        ROUNDED_DOUBLE_DECIMAL_FORMAT = new DecimalFormat("####0.00", otherSymbols);
        ROUNDED_DOUBLE_DECIMAL_FORMAT.setGroupingUsed(false);
    }

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long getUsedMemory() {
        return getMaxMemory() - getFreeMemory();
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    private static long bytesToMiB(long bytes) {
        return (long) (bytes / MEGABYTE_FACTOR);
    }

    public static long getTotalMemoryInMiB() {
        return bytesToMiB(getTotalMemory());
    }

    public static long getFreeMemoryInMiB() {
        return bytesToMiB(getFreeMemory());
    }

    public static long getUsedMemoryInMiB() {
        return bytesToMiB(getUsedMemory());
    }

    public static long getMaxMemoryInMiB() {
        return bytesToMiB(getMaxMemory());
    }

    public static double getPercentageUsed() {
        return ((double) getUsedMemory() / getMaxMemory()) * 100;
    }

    public static String getPercentageUsedFormatted() {
        double usedPercentage = getPercentageUsed();
        return ROUNDED_DOUBLE_DECIMAL_FORMAT.format(usedPercentage) + "%";
    }
}
