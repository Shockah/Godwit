package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.Setter;
import pl.shockah.godwit.fx.ease.Easing;

public abstract class FxImpl implements Fx {
	@Getter
	private final float duration;

	@Getter @Setter
	@Nonnull private Easing method = Easing.linear;

	public FxImpl(float duration) {
		this.duration = duration;
	}
}