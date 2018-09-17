package pl.shockah.godwit;

import com.badlogic.gdx.ApplicationAdapter;

import javax.annotation.Nonnull;

public class GodwitAdapter extends ApplicationAdapter {
	@Nonnull
	public final EntityManager manager;

	GodwitAdapter(EntityManager manager) {
		this.manager = manager;
	}

	@Override
	public void create() {
		Godwit.setup(manager);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void render() {
		Godwit.getInstance().tick();
	}
}