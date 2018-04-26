package pl.shockah.godwit.test;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxFont;
import pl.shockah.godwit.ui.Alignment;

public class GfxFontTest extends State {
	private GfxFont font;

	@Override
	public void onAddedToParent() {
		super.onAddedToParent();

		AssetManager assetManager = Godwit.getInstance().getAssetManager();
		assetManager.load("arial-32.fnt", BitmapFont.class, new BitmapFontLoader.BitmapFontParameter() {{
			flip = true;
		}});
		assetManager.finishLoading();

		font = new GfxFont(assetManager.get("arial-32.fnt", BitmapFont.class));
		font.setAlignment(Alignment.Horizontal.Center.and(Alignment.Vertical.Middle));
		font.setText("test");
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);
		font.setScale((float)Math.sin(System.currentTimeMillis() / 1000.0) + 1f);
		font.render(gfx, v.add(gfx.getSize().multiply(0.5f)));
	}
}