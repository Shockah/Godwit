package pl.shockah.godwit.test.test;

import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.fx.FxInstance;
import pl.shockah.godwit.fx.RunnableFx;
import pl.shockah.godwit.fx.SequenceFx;
import pl.shockah.godwit.fx.WaitFx;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;

public class MetronomeTest extends State {
	private boolean state = false;

	@Override
	public void onAddedToParent() {
		super.onAddedToParent();

		run(SequenceFx.ofRaw(
				new RunnableFx(() -> {
					state = !state;
					System.out.println(state);
				}),
				new WaitFx(1f)
		).instance(FxInstance.EndAction.Loop));
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		gfx.clear(state ? Color.GRAY : Color.DARK_GRAY);
	}
}