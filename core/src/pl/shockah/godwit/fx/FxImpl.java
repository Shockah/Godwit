package pl.shockah.godwit.fx;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.Setter;
import pl.shockah.unicorn.ease.Easing;

public abstract class FxImpl extends AbstractFx {
	@Getter
	private final float duration;

	@Getter @Setter
	@Nonnull private Easing method = Easing.linear;

	public FxImpl(float duration) {
		this.duration = duration;
	}
}