package pl.shockah.godwit.entity;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.EntityManager;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.gl.BlendMode;
import pl.shockah.godwit.gl.GfxContextManager;
import pl.shockah.unicorn.func.Func0;

public class EntityTreeManager extends EntityManager {
	@Getter
	@Nullable
	private State state;

	@Getter
	@Nullable
	private Func0<State> movingToState;

	@Getter
	@Nonnull
	private final Entity rootEntity = new RenderGroup();

	public EntityTreeManager(@Nullable State state) {
		this(state == null ? null : () -> state);
	}

	public EntityTreeManager(@Nullable Func0<State> state) {
		super(new EntityTreeGestureManager());
		movingToState = state;
	}

	public void moveToState(@Nullable State state) {
		moveToState(() -> state);
	}

	public void moveToState(@Nullable Func0<State> state) {
		movingToState = state;
	}

	@Override
	protected void update() {
		super.update();
		Godwit godwit = Godwit.getInstance();

		if (movingToState != null) {
			State newState = movingToState.call();
			movingToState = null;

			if (newState != null) {
				if (state != null)
					state.removeFromParent();
				state = newState;
				movingToState = null;
				rootEntity.addChild(state);
			}
		}

		rootEntity.update();
		godwit.inputManager.gestureManager.update();
	}

	@Override
	protected void render() {
		super.render();
		Godwit godwit = Godwit.getInstance();

		GfxContextManager.bindSurface(null);
		godwit.gfx.clear(Color.CLEAR);
		godwit.gfx.setBlendMode(BlendMode.normal);
		rootEntity.render(godwit.gfx);
		godwit.gfx.endTick();
		GfxContextManager.bindSurface(null);
	}
}