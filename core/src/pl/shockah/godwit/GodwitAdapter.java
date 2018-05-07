package pl.shockah.godwit;

import com.badlogic.gdx.ApplicationAdapter;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.func.Func0;

public class GodwitAdapter extends ApplicationAdapter {
	@Nonnull public final Func0<State> initialState;

	GodwitAdapter(@Nonnull State initialState) {
		this(() -> initialState);
	}

	GodwitAdapter(@Nonnull Func0<State> initialState) {
		this.initialState = initialState;
	}

	@Override
	public void create() {
		Godwit.setupNewInstance();
		Godwit.getInstance().moveToState(initialState);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Godwit.getInstance().tick();
	}
}