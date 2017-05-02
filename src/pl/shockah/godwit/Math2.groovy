package pl.shockah.godwit

import groovy.transform.CompileStatic

@CompileStatic
final class Math2 {
    static double root(double value, double degree) {
        return Math.pow(value, 1d / degree)
    }

    static float ldirX(float dist, float angle) {
        return -Math.cos(Math.toRadians(angle + 180f)) * dist
    }

    static float ldirY(float dist, float angle) {
        return -Math.sin(Math.toRadians(angle + 180f)) * dist
    }

    static double deltaAngle(double angle1, double angle2) {
        while (angle2 <= -180)
            angle2 += 360
        while (angle2 > 180)
            angle2 -= 360
        while (angle1 <= -180)
            angle1 += 360
        while (angle1 > 180)
            angle1 -= 360

        def r = angle2 - angle1
        return r + ((r > 180) ? -360 : (r < -180) ? 360 : 0)
    }

    static double frac(double value) {
        def sign = Math.signum(value)
        value = Math.abs(value)
        return (value - Math.floor(value)) * sign
    }

    static double min(double... values) {
        def min = values[0]
        for (int i in 1..<values.length) {
            if (values[i] < min)
                min = values[i]
        }
        return min
    }

    static double min(float... values) {
        def min = values[0]
        for (int i in 1..<values.length) {
            if (values[i] < min)
                min = values[i]
        }
        return min
    }

    static double min(long... values) {
        def min = values[0]
        for (int i in 1..<values.length) {
            if (values[i] < min)
                min = values[i]
        }
        return min
    }

    static double min(int... values) {
        def min = values[0]
        for (int i in 1..<values.length) {
            if (values[i] < min)
                min = values[i]
        }
        return min
    }

    static double max(double... values) {
        def max = values[0]
        for (int i in 1..<values.length) {
            if (values[i] > max)
                max = values[i]
        }
        return max
    }

    static double max(float... values) {
        def max = values[0]
        for (int i in 1..<values.length) {
            if (values[i] > max)
                max = values[i]
        }
        return max
    }

    static double max(long... values) {
        def max = values[0]
        for (int i in 1..<values.length) {
            if (values[i] > max)
                max = values[i]
        }
        return max
    }

    static double max(int... values) {
        def max = values[0]
        for (int i in 1..<values.length) {
            if (values[i] > max)
                max = values[i]
        }
        return max
    }
}