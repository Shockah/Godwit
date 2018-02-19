package pl.shockah.godwit.test.test;

import com.badlogic.gdx.graphics.g2d.Sprite;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.State;
import pl.shockah.godwit.asset.SpriteSheetAsset;
import pl.shockah.godwit.fx.FxInstance;
import pl.shockah.godwit.fx.object.ObjectAction2Fx;
import pl.shockah.godwit.gl.GfxSprite;
import pl.shockah.godwit.gl.SpriteSheet;

public class SpriteSheetState extends State {
	@Nonnull private static final SpriteSheetAsset sheet = new SpriteSheetAsset("ninja-run.json", SpriteSheet.class);

	@Override
	public void onCreate() {
		super.onCreate();
		loadAsset(sheet);
		SpriteSheet sheet = SpriteSheetState.sheet.get();

		GfxSprite sprite = new GfxSprite(new Sprite(sheet[0]));
		sprite.center();
		sprite.setPosition(Godwit.getInstance().gfx.getSize() * 0.5f);
		sprite.getFxInstances().add(new ObjectAction2Fx<GfxSprite>(0.5f, (obj, f) -> {
			sprite.setRegion(sheet.get((int)(f * sheet.frameCount) % sheet.frameCount));
		}).instance(FxInstance.EndAction.Loop));
		sprite.asEntity().create(this);
	}
}