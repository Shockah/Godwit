package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.List;

import javax.annotation.Nonnull;

import java8.util.Lists;
import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.State;
import pl.shockah.godwit.fx.FxInstance;
import pl.shockah.godwit.fx.ease.Easing;
import pl.shockah.godwit.fx.ease.PennerEasing;
import pl.shockah.godwit.fx.ease.SmoothstepEasing;
import pl.shockah.godwit.fx.raw.RawFuncFx;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.GfxSprite;

public class EasingTest extends State {
	@Nonnull private final List<Easing> methods = Lists.of(
			Easing.linear,
			SmoothstepEasing.smoothstep, SmoothstepEasing.smoothstep2, SmoothstepEasing.smoothstep3,
			PennerEasing.sineIn, PennerEasing.cubicIn, PennerEasing.quadIn, PennerEasing.quarticIn, PennerEasing.quinticIn,
			PennerEasing.exponentialIn, PennerEasing.circularIn, PennerEasing.backIn, PennerEasing.elasticIn, PennerEasing.bounceIn,
			PennerEasing.sineOut, PennerEasing.cubicOut, PennerEasing.quadOut, PennerEasing.quarticOut, PennerEasing.quinticOut,
			PennerEasing.exponentialOut, PennerEasing.circularOut, PennerEasing.backOut, PennerEasing.elasticOut, PennerEasing.bounceOut,
			PennerEasing.sineInOut, PennerEasing.cubicInOut, PennerEasing.quadInOut, PennerEasing.quarticInOut, PennerEasing.quinticInOut,
			PennerEasing.exponentialInOut, PennerEasing.circularInOut, PennerEasing.backInOut, PennerEasing.elasticInOut, PennerEasing.bounceInOut
	);

	@Override
	public void onAddedToParent() {
		super.onAddedToParent();

		AssetManager assetManager = Godwit.getInstance().getAssetManager();
		assetManager.load("question-mark.png", Texture.class);
		assetManager.finishLoading();
		Texture texture = assetManager.get("question-mark.png", Texture.class);

		for (int i = 0; i < methods.size(); i++) {
			Easing method = methods[i];
			GfxSprite sprite = new GfxSprite(new Sprite(texture));
			sprite.setSize(new ImmutableVec2(16f, 16f));

			GfxSprite.Entity entity = sprite.asEntity();
			entity.position = new Vec2(i * 18f + 2f, 2f);
			sprite.run(new RawFuncFx(5f, f -> {
				entity.position.y = Easing.linear.ease(2 + Gdx.graphics.getHeight() * 0.2f, Gdx.graphics.getHeight() * 0.8f - 16, f);
			}).withMethod(method).instance(FxInstance.EndAction.ReverseLoop));

			addChild(entity);
		}
	}
}