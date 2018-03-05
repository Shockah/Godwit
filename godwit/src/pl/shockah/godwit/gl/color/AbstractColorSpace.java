package pl.shockah.godwit.gl.color;

public abstract class AbstractColorSpace implements ColorSpace {
	public float alpha;

	public AbstractColorSpace(float alpha) {
		this.alpha = alpha;
	}
}