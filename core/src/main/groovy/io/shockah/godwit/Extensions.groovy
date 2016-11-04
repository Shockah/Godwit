package io.shockah.godwit

import groovy.transform.CompileStatic

/**
 * Created by michaldolas on 04.11.16.
 */
@CompileStatic
class Extensions {
    static double limit(double self, double min, double max) {
        Math.min(Math.max(self, min), max)
    }

    static double limit(float self, float min, float max) {
        Math.min(Math.max(self, min), max)
    }

    static double limit(long self, long min, long max) {
        Math.min(Math.max(self, min), max)
    }

    static double limit(int self, int min, int max) {
        Math.min(Math.max(self, min), max)
    }
}