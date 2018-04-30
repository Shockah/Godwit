package pl.shockah.godwit.test;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.State;
import pl.shockah.godwit.fx.FxInstance;
import pl.shockah.godwit.fx.RunnableFx;
import pl.shockah.godwit.fx.WaitFx;
import pl.shockah.godwit.fx.raw.RawFuncFx;
import pl.shockah.godwit.fx.raw.SequenceRawFx;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxSprite;
import pl.shockah.unicorn.ease.PennerEasing;

public class SequenceFxTest extends State {
	private GfxSprite sprite;

	@Override
	public void onAddedToParent() {
		super.onAddedToParent();

		AssetManager assetManager = Godwit.getInstance().getAssetManager();
		assetManager.load("question-mark.png", Texture.class);
		assetManager.finishLoading();

		sprite = new GfxSprite(new Sprite(assetManager.get("question-mark.png", Texture.class)));
		sprite.center();

		run(new SequenceRawFx(
				new RawFuncFx(2f, f -> {
					sprite.rotate(f * 90f);
				}).withMethod(PennerEasing.elasticInOut).additive().repeat(3),
				new WaitFx(1f),
				new RawFuncFx(0.5f, f -> {
					sprite.setAlpha(1f - f);
				}).withMethod(PennerEasing.sineInOut),
				new RawFuncFx(1f, f -> {
					sprite.setAlpha(f);
				}).withMethod(PennerEasing.bounceOut),
				new RunnableFx(() -> {
					float scale = sprite.getScale();
					getFxInstances().add(new RawFuncFx(1f, f -> {
						sprite.setScale(scale * (1f + f * 0.1f));
					}).withMethod(PennerEasing.elasticInOut).instance());
				}),
				new WaitFx(1f)
		).instance(FxInstance.EndAction.Loop));
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.render(gfx, v);
		gfx.draw(sprite, gfx.getSize().multiply(0.5f).add(v));
	}
}