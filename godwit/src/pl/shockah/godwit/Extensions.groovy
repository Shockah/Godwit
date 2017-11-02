package pl.shockah.godwit

import groovy.transform.CompileStatic

@CompileStatic
class Extensions {
	static double limit(double self, double min, double max) {
		return Math.min(Math.max(self, min), max)
	}

	static float limit(float self, float min, float max) {
		return Math.min(Math.max(self, min), max)
	}

	static long limit(long self, long min, long max) {
		return Math.min(Math.max(self, min), max)
	}

	static int limit(int self, int min, int max) {
		return Math.min(Math.max(self, min), max)
	}
}