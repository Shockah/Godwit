package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java8.util.stream.StreamSupport;
import lombok.Getter;
import pl.shockah.func.Action1;
import pl.shockah.func.Func0;
import pl.shockah.godwit.asset.FreeTypeFontLoader;
import pl.shockah.godwit.asset.JSONObjectLoader;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.MutableVec2;
import pl.shockah.godwit.gl.BlendMode;
import pl.shockah.godwit.gl.GfxContextManager;
import pl.shockah.godwit.gl.GfxSprite;
import pl.shockah.godwit.gl.ScreenGfx;
import pl.shockah.godwit.platform.PlatformServiceProvider;
import pl.shockah.godwit.rand.Randomizer;
import pl.shockah.jay.JSONObject;

public final class Godwit {
	@Nullable private static Godwit instance;

	@Getter
	@Nullable private State state;

	@Getter
	@Nullable private Func0<State> movingToState;

	@Getter
	@Nonnull private AssetManager assetManager = new AssetManager();

	@Nonnull public final PlatformServiceProvider platformServiceProvider = new PlatformServiceProvider();

	@Nonnull public final ScreenGfx gfx = new ScreenGfx();
	@Nonnull public final InputManager inputManager = new InputManager();
	@Nonnull public final Randomizer random = new Randomizer();
	public boolean waitForDeltaToStabilize = true;
	public boolean renderFirstTickWhenWaitingForDeltaToStabilize = false;
	public boolean yPointingDown = true;

	@Nullable public Action1<AssetManager> assetManagerSetupCallback;

	@Nonnull private final List<Float> deltas = new ArrayList<>();
	@Nonnull private final Entity rootEntity = new RenderGroup();
	@Nonnull private MutableVec2 ppi = new MutableVec2(1f, 1f);
	private boolean isFirstTick = true;

	@Getter
	@Nonnull private Func0<AssetManager> assetManagerFactory = AssetManager::new;

	@Getter
	private float deltaTime = 0f;

	@Getter(lazy = true)
	private final Texture pixelTexture = getInitialPixelTexture();

	@Getter(lazy = true)
	private final GfxSprite pixelSprite = getInitialPixelSprite();

	private Godwit() {
		setupAssetManager();
		Gdx.input.setInputProcessor(inputManager.multiplexer);
	}

	static synchronized void setupNewInstance() {
		instance = new Godwit();
	}

	@Nonnull public static synchronized Godwit getInstance() {
		if (instance == null)
			setupNewInstance();
		return instance;
	}

	public void moveToState(@Nullable State state) {
		moveToState(() -> state);
	}

	public void moveToState(@Nullable Func0<State> state) {
		movingToState = state;
	}

	public void setAssetManager(@Nonnull AssetManager assetManager) {
		this.assetManager = assetManager;
		setupAssetManager();
	}

	private void setupAssetManager() {
		//Texture.setAssetManager(assetManager);
		assetManager.setLoader(JSONObject.class, new JSONObjectLoader(assetManager.getFileHandleResolver()));
		assetManager.setLoader(BitmapFont.class, ".ttf", new FreeTypeFontLoader(assetManager.getFileHandleResolver()));
		if (assetManagerSetupCallback != null)
			assetManagerSetupCallback.call(assetManager);
	}

	public void setAssetManagerFactory(@Nonnull Func0<AssetManager> assetManagerFactory) {
		this.assetManagerFactory = assetManagerFactory;
		setAssetManager(assetManagerFactory.call());
	}

	@Nonnull public IVec2 getPpi() {
		return ppi;
	}

	public void tick() {
		deltaTime = Gdx.graphics.getDeltaTime();
		ppi.x = Gdx.graphics.getPpiX();
		ppi.y = Gdx.graphics.getPpiY();

		if (waitForDeltaToStabilize) {
			deltas.add(deltaTime);
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
			State newState = movingToState.call();
			movingToState = null;

			if (newState != null) {
				if (state != null)
					state.removeFromParent();
				state = newState;
				movingToState = null;
				gfx.resetCamera();
				if (state != null)
					rootEntity.addChild(state);
			}
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

		HdpiUtils.glScissor(horizontalGutter * 2, verticalGutter, Gdx.graphics.getWidth() - horizontalGutter * 4, Gdx.graphics.getHeight());
		Gdx.gl20.glEnable(GL20.GL_SCISSOR_TEST);
	}
}