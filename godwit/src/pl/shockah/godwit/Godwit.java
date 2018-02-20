package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java8.util.stream.StreamSupport;
import lombok.Getter;
import pl.shockah.godwit.asset.SpriteSheetLoader;
import pl.shockah.godwit.gl.BlendMode;
import pl.shockah.godwit.gl.GfxContextManager;
import pl.shockah.godwit.gl.GfxImpl;
import pl.shockah.godwit.gl.SpriteSheet;

public final class Godwit {
	@Getter(lazy = true)
	@Nonnull private static final Godwit instance = new Godwit();

	@Getter
	@Nullable protected State state;

	@Nullable protected State movingToState;
	@Nonnull public final GfxImpl gfx = new GfxImpl();
	@Nonnull public final AssetManager assetManager = new AssetManager();
	@Nonnull public final InputManager inputManager = new InputManager();
	protected boolean isFirstTick = true;
	public boolean waitForDeltaToStabilize = true;
	public boolean renderFirstTickWhenWaitingForDeltaToStabilize = false;
	public boolean yPointingDown = true;
	@Nonnull private final List<Float> deltas = new ArrayList<>();
	private boolean created = false;

	private Godwit() {
	}

	public void moveToState(@Nullable State state) {
		movingToState = state;
	}

	public void create() {
		if (created)
			throw new IllegalStateException();

		FileHandleResolver resolver = new InternalFileHandleResolver();
		assetManager.setLoader(SpriteSheet.class, new SpriteSheetLoader(resolver));

		Gdx.input.setInputProcessor(inputManager.multiplexer);
	}

	public void tick() {
		if (waitForDeltaToStabilize) {
			deltas.add(Gdx.graphics.getDeltaTime());
			if (isFirstTick) {
				isFirstTick = false;
				if (renderFirstTickWhenWaitingForDeltaToStabilize) {
					runStateCreation();
					runRender();
				}
				return;
			} else {
				runRender();
				if (deltas.size() >= 5) {
					float min = (float)StreamSupport.stream(deltas).mapToDouble(v -> v).min().getAsDouble();
					float max = (float)StreamSupport.stream(deltas).mapToDouble(v -> v).max().getAsDouble();
					float average = (float)StreamSupport.stream(deltas).mapToDouble(v -> v).sum() / deltas.size();
					deltas.remove(0);
					if (min == 0f)
						return;

					float difference = max - min;
					float min2 = Math.min(difference, average);
					float max2 = Math.max(difference, average);
					if (min2 / max2 > 0.2f)
						return;
					waitForDeltaToStabilize = false;
				} else {
					return;
				}
			}
		}

		runStateCreation();
		runUpdate();
		runRender();
	}

	private void runStateCreation() {
		if (movingToState != null) {
			if (state != null)
				state.destroy();
			state = movingToState;
			movingToState = null;
			gfx.resetCamera();
			if (state != null)
				state.create();
		}
	}

	private void runUpdate() {
		if (state != null)
			state.update();
	}

	private void runRender() {
		GfxContextManager.bindSurface(null);
		gfx.updateCamera();
		gfx.clear(Color.BLACK);
		gfx.setBlendMode(BlendMode.normal);

		if (state != null)
			state.render(gfx);

		gfx.endTick();
		GfxContextManager.bindSurface(null);
	}
}