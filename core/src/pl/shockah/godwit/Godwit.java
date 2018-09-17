package pl.shockah.godwit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java8.util.stream.StreamSupport;
import lombok.Getter;
import pl.shockah.godwit.asset.FreeTypeFontLoader;
import pl.shockah.godwit.asset.JSONObjectLoader;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.GfxSprite;
import pl.shockah.godwit.gl.ScreenGfx;
import pl.shockah.godwit.platform.PlatformServiceProvider;
import pl.shockah.jay.JSONObject;
import pl.shockah.unicorn.func.Action1;
import pl.shockah.unicorn.func.Func0;
import pl.shockah.unicorn.rand.Randomizer;

public final class Godwit {
	@Nullable
	private static Godwit instance;

	@Nonnull
	public final EntityManager manager;

	@Getter
	@Nonnull
	private AssetManager assetManager = new AssetManager();

	@Nonnull
	public final PlatformServiceProvider platformServiceProvider = new PlatformServiceProvider();

	@Nonnull
	public final ScreenGfx gfx = new ScreenGfx();

	@Nonnull
	public final InputManager inputManager;

	@Nonnull
	public final Randomizer random = new Randomizer();

	public boolean waitForDeltaToStabilize = true;

	public boolean yPointingDown = true;

	@Nullable
	public Action1<AssetManager> assetManagerSetupCallback;

	@Nonnull
	private final List<Float> deltas = new ArrayList<>();

	@Nonnull
	private Vec2 ppi = new Vec2(1f, 1f);

	private boolean isFirstTick = true;

	@Getter
	@Nonnull
	private Func0<AssetManager> assetManagerFactory = AssetManager::new;

	@Getter
	private float deltaTime = 0f;

	@Getter(lazy = true)
	private final Texture pixelTexture = getInitialPixelTexture();

	@Getter(lazy = true)
	private final GfxSprite pixelSprite = getInitialPixelSprite();

	private Godwit(@Nonnull EntityManager manager) {
		this.manager = manager;
		inputManager = new InputManager(manager.gestureManager);
		setupAssetManager();
	}

	public static synchronized void setup(@Nonnull EntityManager manager) {
		instance = new Godwit(manager);
		Gdx.input.setInputProcessor(instance.inputManager.multiplexer);
	}

	@Nonnull
	public static synchronized Godwit getInstance() {
		if (instance == null)
			throw new IllegalStateException("Godwit.setup() has to be ran first.");
		return instance;
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

	@Nonnull
	public Vec2 getPpi() {
		return ppi;
	}

	public void tick() {
		deltaTime = Gdx.graphics.getDeltaTime();
		ppi = new Vec2(Gdx.graphics.getPpiX(), Gdx.graphics.getPpiY());

		if (waitForDeltaToStabilize) {
			deltas.add(deltaTime);
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

		if (isFirstTick) {
			isFirstTick = false;
			manager.initialize();
		}
		manager.update();
		manager.render();
	}

	@Nonnull
	private Texture getInitialPixelTexture() {
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888);
		pixmap.drawPixel(0, 0, Color.WHITE.toIntBits());
		Texture texture = new Texture(pixmap);
		pixmap.dispose();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
		return texture;
	}

	@Nonnull
	private GfxSprite getInitialPixelSprite() {
		return new GfxSprite(new Sprite(getPixelTexture()));
	}
}