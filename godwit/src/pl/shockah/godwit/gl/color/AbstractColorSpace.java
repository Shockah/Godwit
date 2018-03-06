package pl.shockah.godwit.gl.color;

public abstract class AbstractColorSpace<CS extends AbstractColorSpace<CS>> implements ColorSpace<CS> {
	public float alpha;

	public AbstractColorSpace(float alpha) {
		this.alpha = alpha;
	}
}