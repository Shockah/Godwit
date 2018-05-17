package pl.shockah.godwit.fx;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import lombok.Getter;
import lombok.Setter;
import pl.shockah.godwit.Godwit;
import pl.shockah.unicorn.collection.SafeList;

public abstract class AbstractAnimatable implements Animatable {
	@Nonnull
	@Getter
	public final SafeList<FxInstance> fxInstances = new SafeList<>(new ArrayList<>());

	@Getter
	@Setter
	public float animationSpeed = 1f;

	public void run(@Nonnull FxInstance instance) {
		getFxInstances().add(instance);
	}

	public void run(@Nonnull Fx fx) {
		getFxInstances().add(fx.instance());
	}

	public void run(@Nonnull Runnable func) {
		getFxInstances().add(new RunnableFx(func).instance());
	}

	public void runDelayed(float delay, @Nonnull Fx fx) {
		run(new SequenceFx(new WaitFx(delay), fx));
	}

	public void runDelayed(float delay, @Nonnull Runnable func) {
		run(new SequenceFx(new WaitFx(delay), new RunnableFx(func)));
	}

	public void updateFx() {
		SafeList<FxInstance> fxes = getFxInstances();
		fxes.update();
		float delta = Godwit.getInstance().getDeltaTime() * animationSpeed;
		for (FxInstance fx : fxes.get()) {
			fx.updateBy(delta);
			if (fx.isStopped())
				fxes.remove(fx);
		}
	}
}