package pl.shockah.godwit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.annotation.Nonnull;

public class GodwitAdapter extends ApplicationAdapter {
	@Nonnull public final State initialState;

	GodwitAdapter(@Nonnull State initialState) {
		this.initialState = initialState;
	}

	@Override
	public void create() {
		Godwit.getInstance().moveToState(initialState);
	}

	@Override
	public void resize(int width, int height) {
		Godwit godwit = Godwit.getInstance();
		godwit.gfx.getCamera().setToOrtho(godwit.yPointingDown, width, height);
		Viewport viewport = godwit.gfx.getViewport();
		if (viewport != null)
			viewport.update(width, height, false);
	}

	@Override
	public void render() {
		Godwit.getInstance().tick();
	}
}