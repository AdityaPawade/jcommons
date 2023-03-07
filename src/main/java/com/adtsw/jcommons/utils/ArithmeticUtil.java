package com.adtsw.jcommons.utils;

public class ArithmeticUtil {

    private static final double EPSILON = 0.000000000000001d;

    public static <V extends Number> V add(V lhs, V rhs) {
        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return (V) Integer.valueOf(((Integer) lhs) + ((Integer) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return (V) Double.valueOf(((Double) lhs) + ((Double) rhs));
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return (V) Float.valueOf(((Float) lhs) + ((Float) rhs));
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return (V) Long.valueOf(((Long) lhs) + ((Long) rhs));
        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> V subtract(V lhs, V rhs) {
        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return (V) Integer.valueOf(((Integer) lhs) - ((Integer) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return (V) Double.valueOf(((Double) lhs) - ((Double) rhs));
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return (V) Float.valueOf(((Float) lhs) - ((Float) rhs));
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return (V) Long.valueOf(((Long) lhs) - ((Long) rhs));
        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> V multiply(V lhs, V rhs) {
        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return (V) Integer.valueOf(((Integer) lhs) * ((Integer) rhs));
        if (lhs.getClass() == Integer.class && rhs.getClass() == Double.class)
            return (V) Double.valueOf(((Integer) lhs) * ((Double) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return (V) Double.valueOf(((Double) lhs) * ((Double) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Integer.class)
            return (V) Double.valueOf(((Double) lhs) * ((Integer) rhs));
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return (V) Float.valueOf(((Float) lhs) * ((Float) rhs));
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return (V) Long.valueOf(((Long) lhs) * ((Long) rhs));
        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> V divide(V lhs, V rhs) {
        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return (V) Integer.valueOf(((Integer) lhs) / ((Integer) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return (V) Double.valueOf(((Double) lhs) / ((Double) rhs));
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return (V) Float.valueOf(((Float) lhs) / ((Float) rhs));
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return (V) Long.valueOf(((Long) lhs) / ((Long) rhs));
        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> V mod(V lhs, V rhs) {

        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return (V) Integer.valueOf(((Integer) lhs) % ((Integer) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Integer.class)
            return (V) Double.valueOf(((Double) lhs) % ((Integer) rhs));
        if (lhs.getClass() == Float.class && rhs.getClass() == Integer.class)
            return (V) Float.valueOf(((Float) lhs) % ((Integer) rhs));
        if (lhs.getClass() == Long.class && rhs.getClass() == Integer.class)
            return (V) Long.valueOf(((Long) lhs) % ((Integer) rhs));

        if (lhs.getClass() == Integer.class && rhs.getClass() == Double.class)
            return (V) Double.valueOf(((Integer) lhs) % ((Double) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return (V) Double.valueOf(((Double) lhs) % ((Double) rhs));
        if (lhs.getClass() == Float.class && rhs.getClass() == Double.class)
            return (V) Double.valueOf(((Float) lhs) % ((Double) rhs));
        if (lhs.getClass() == Long.class && rhs.getClass() == Double.class)
            return (V) Double.valueOf(((Long) lhs) % ((Double) rhs));

        if (lhs.getClass() == Integer.class && rhs.getClass() == Float.class)
            return (V) Float.valueOf(((Integer) lhs) % ((Float) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Float.class)
            return (V) Double.valueOf(((Double) lhs) % ((Float) rhs));
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return (V) Float.valueOf(((Float) lhs) % ((Float) rhs));
        if (lhs.getClass() == Long.class && rhs.getClass() == Float.class)
            return (V) Float.valueOf(((Long) lhs) % ((Float) rhs));

        if (lhs.getClass() == Integer.class && rhs.getClass() == Long.class)
            return (V) Long.valueOf(((Integer) lhs) % ((Long) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Long.class)
            return (V) Double.valueOf(((Double) lhs) % ((Long) rhs));
        if (lhs.getClass() == Float.class && rhs.getClass() == Long.class)
            return (V) Float.valueOf(((Float) lhs) % ((Long) rhs));
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return (V) Long.valueOf(((Long) lhs) % ((Long) rhs));

        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> Double pow(V lhs, V rhs) {
        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return Math.pow(Double.valueOf((Integer) lhs), Double.valueOf((Integer) rhs));
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return Math.pow((Double) lhs, (Double) rhs);
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return Math.pow(Double.valueOf((Float) lhs), Double.valueOf((Float) rhs));
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return Math.pow(Double.valueOf((Long) lhs), Double.valueOf((Long) rhs));
        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> boolean lessThan(V lhs, V rhs) {
        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return ((Integer) lhs) < ((Integer) rhs);
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return ((Double) lhs) < ((Double) rhs);
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return ((Float) lhs) < ((Float) rhs);
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return ((Long) lhs) < ((Long) rhs);
        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> boolean lessThanOrEqualTo(V lhs, V rhs) {
        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return ((Integer) lhs) <= ((Integer) rhs);
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return ((Double) lhs) <= ((Double) rhs);
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return ((Float) lhs) <= ((Float) rhs);
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return ((Long) lhs) <= ((Long) rhs);
        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> boolean moreThan(V lhs, V rhs) {
        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return ((Integer) lhs) > ((Integer) rhs);
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return ((Double) lhs) > ((Double) rhs);
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return ((Float) lhs) > ((Float) rhs);
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return ((Long) lhs) > ((Long) rhs);
        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> boolean moreThanOrEqualTo(V lhs, V rhs) {
        if (lhs.getClass() == Integer.class && rhs.getClass() == Integer.class)
            return ((Integer) lhs) >= ((Integer) rhs);
        if (lhs.getClass() == Double.class && rhs.getClass() == Double.class)
            return ((Double) lhs) >= ((Double) rhs);
        if (lhs.getClass() == Float.class && rhs.getClass() == Float.class)
            return ((Float) lhs) >= ((Float) rhs);
        if (lhs.getClass() == Long.class && rhs.getClass() == Long.class)
            return ((Long) lhs) >= ((Long) rhs);
        throw new IllegalArgumentException("unsupported types: " + lhs.getClass() + " & " + rhs.getClass());
    }

    public static <V extends Number> boolean equalTo(V lhs, V rhs) {
        if (lhs == null) {
            return lhs == null;
        } else if (rhs == null) {
            return false;
        } else {
            return Math.abs(lhs.doubleValue() - rhs.doubleValue()) < EPSILON;
        }
    }
}
