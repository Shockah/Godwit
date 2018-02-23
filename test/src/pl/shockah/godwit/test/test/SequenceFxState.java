package pl.shockah.godwit.test.test;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.State;
import pl.shockah.godwit.fx.FxInstance;
import pl.shockah.godwit.fx.RunnableFx;
import pl.shockah.godwit.fx.SequenceFx;
import pl.shockah.godwit.fx.WaitFx;
import pl.shockah.godwit.fx.ease.PennerEasing;
import pl.shockah.godwit.fx.raw.RawAction1Fx;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxSprite;

public class SequenceFxState extends State {
	private GfxSprite sprite;

	@Override
	public void onAddedToParent() {
		super.onAddedToParent();

		AssetManager assetManager = Godwit.getInstance().getAssetManager();
		assetManager.load("question-mark.png", Texture.class);
		assetManager.finishLoading();

		sprite = new GfxSprite(new Sprite(assetManager.get("question-mark.png", Texture.class)));
		sprite.center();

		getFxInstances().add(SequenceFx.ofRaw(
				new RawAction1Fx(2f, f -> {
					sprite.rotate(f * 90f);
				}).withMethod(PennerEasing.elasticInOut).additive().repeat(3),
				new WaitFx(1f),
				new RawAction1Fx(0.5f, f -> {
					sprite.setAlpha(1f - f);
				}).withMethod(PennerEasing.sineInOut),
				new RawAction1Fx(1f, f -> {
					sprite.setAlpha(f);
				}).withMethod(PennerEasing.bounceOut),
				new RunnableFx(() -> {
					float scale = sprite.getScale();
					getFxInstances().add(new RawAction1Fx(1f, f -> {
						sprite.setScale(scale * (1f + f * 0.1f));
					}).withMethod(PennerEasing.elasticInOut).instance());
				}),
				new WaitFx(1f)
		).instance(FxInstance.EndAction.Loop));
	}

	@Override
	public void renderSelf(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.renderSelf(gfx, v);
		gfx.draw(sprite, gfx.getSize() / 2 + v);
	}
}