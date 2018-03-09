package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java8.util.stream.StreamSupport;
import lombok.Getter;
import pl.shockah.godwit.asset.JSONObjectLoader;
import pl.shockah.godwit.gl.BlendMode;
import pl.shockah.godwit.gl.GfxContextManager;
import pl.shockah.godwit.gl.GfxImpl;
import pl.shockah.godwit.gl.GfxSprite;
import pl.shockah.godwit.rand.Randomizer;
import pl.shockah.jay.JSONObject;

public final class Godwit {
	@Getter(lazy = true)
	@Nonnull private static final Godwit instance = new Godwit();

	@Getter
	@Nullable private State state;

	@Getter
	@Nullable private State movingToState;

	@Getter
	@Nonnull private AssetManager assetManager = new AssetManager();

	@Nonnull public final GfxImpl gfx = new GfxImpl();
	@Nonnull public final InputManager inputManager = new InputManager();
	@Nonnull public final Randomizer random = new Randomizer();
	public boolean waitForDeltaToStabilize = true;
	public boolean renderFirstTickWhenWaitingForDeltaToStabilize = false;
	public boolean yPointingDown = true;

	@Nonnull private final List<Float> deltas = new ArrayList<>();
	@Nonnull private final Entity rootEntity = new RenderGroup();
	private boolean isFirstTick = true;

	@Getter(lazy = true)
	private final Texture pixelTexture = getInitialPixelTexture();

	@Getter(lazy = true)
	private final GfxSprite pixelSprite = getInitialPixelSprite();

	private Godwit() {
		setupAssetManager();
		Gdx.input.setInputProcessor(inputManager.multiplexer);
	}

	public void moveToState(@Nullable State state) {
		movingToState = state;
	}

	public void setAssetManager(@Nonnull AssetManager assetManager) {
		this.assetManager = assetManager;
		setupAssetManager();
	}

	private void setupAssetManager() {
		assetManager.setLoader(JSONObject.class, new JSONObjectLoader(assetManager.getFileHandleResolver()));
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

	@Nonnull private Texture getInitialPixelTexture() {
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
		pixmap.drawPixel(0, 0, Color.WHITE.toIntBits());
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		return texture;
	}

	@Nonnull private GfxSprite getInitialPixelSprite() {
		return new GfxSprite(new Sprite(getPixelTexture()));
	}

	private void runStateCreation() {
		if (movingToState != null) {
			if (state != null)
				state.removeFromParent();
			state = movingToState;
			movingToState = null;
			gfx.resetCamera();
			if (state != null)
				rootEntity.addChild(state);
		}
	}

	private void runUpdate() {
		rootEntity.update();
	}

	private void runRender() {
		GfxContextManager.bindSurface(null);
		gfx.updateCamera();
		setupScissor();
		gfx.clear(Color.CLEAR);
		gfx.setBlendMode(BlendMode.normal);
		rootEntity.render(gfx);
		gfx.endTick();
		GfxContextManager.bindSurface(null);
	}

	private void setupScissor() {
		Viewport viewport = gfx.getViewport();
		int leftGutter = viewport != null ? viewport.getLeftGutterWidth() : 0;
		int rightGutter = viewport != null ? viewport.getRightGutterWidth() : 0;
		int topGutter = viewport != null ? viewport.getTopGutterHeight() : 0;
		int bottomGutter = viewport != null ? viewport.getBottomGutterHeight() : 0;

		int horizontalGutter = leftGutter + rightGutter;
		int verticalGutter = topGutter + bottomGutter;

		Gdx.gl20.glScissor(horizontalGutter * 2, verticalGutter, Gdx.graphics.getWidth() - horizontalGutter * 4, Gdx.graphics.getHeight());
		Gdx.gl20.glEnable(GL20.GL_SCISSOR_TEST);
	}
}