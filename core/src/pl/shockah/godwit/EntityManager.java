package pl.shockah.godwit;

import javax.annotation.Nonnull;

import pl.shockah.godwit.gesture.GestureManager;

public abstract class EntityManager {
	@Nonnull
	public final GestureManager gestureManager;

	protected EntityManager(@Nonnull GestureManager gestureManager) {
		this.gestureManager = gestureManager;
	}

	protected void initialize() {
	}

	protected void update() {
	}

	protected void render() {
	}
}