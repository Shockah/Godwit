package pl.shockah.godwit.test;

import com.badlogic.gdx.graphics.g2d.Sprite;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.State;
import pl.shockah.godwit.asset.SpriteSheetAsset;
import pl.shockah.godwit.fx.FuncFx;
import pl.shockah.godwit.fx.FxInstance;
import pl.shockah.godwit.gl.GfxSprite;
import pl.shockah.godwit.gl.SpriteSheet;

public class SpriteSheetTest extends State {
	@Nonnull private static final SpriteSheetAsset sheet = new SpriteSheetAsset("ninja-run.png");

	@Override
	public void onAddedToParent() {
		super.onAddedToParent();

		loadAsset(sheet);
		SpriteSheet sheet = SpriteSheetTest.sheet.get();

		GfxSprite sprite = new GfxSprite(new Sprite(sheet[0]));
		sprite.center();
		sprite.setPosition(Godwit.getInstance().gfx.getSize() * 0.5f);
		sprite.run(new FuncFx(0.5f, f -> {
			sprite.setRegion(sheet.get((int)(f * sheet.frameCount) % sheet.frameCount));
		}).instance(FxInstance.EndAction.Loop));
		addChild(sprite.asEntity());
	}
}