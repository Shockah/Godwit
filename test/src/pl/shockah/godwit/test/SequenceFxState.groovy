package pl.shockah.godwit.test

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit
import pl.shockah.godwit.State
import pl.shockah.godwit.animfx.*
import pl.shockah.godwit.animfx.ease.PennerEasing
import pl.shockah.godwit.animfx.raw.RawClosureFx
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.GfxSprite

import javax.annotation.Nonnull

@CompileStatic
class SequenceFxState extends State {
	GfxSprite sprite

	@Override
	protected void onCreate() {
		super.onCreate()

		Godwit.instance.assetManager.load("question-mark.png", Texture.class)
		Godwit.instance.assetManager.finishLoading()

		sprite = new GfxSprite(new Sprite(Godwit.instance.assetManager.get("question-mark.png", Texture.class)))
		sprite.center()

		fxes.add(SequenceFx.ofRaw([
				new RawClosureFx(2f, { float f, float previous ->
					sprite.rotation += f * 90f as float
				}).withMethod(PennerEasing.elasticInOut).additive().repeat(3),
				new WaitFx(1f),
				new RawClosureFx(0.5f, { float f, float previous ->
					sprite.alpha = 1f - f as float
				}).withMethod(PennerEasing.sineInOut),
				new RawClosureFx(1f, { float f, float previous ->
					sprite.alpha = f
				}).withMethod(PennerEasing.bounceOut),
				new ActionFx({
					float scale = sprite.scale
					fxes.add(new RawClosureFx(1f, { float f, float previous ->
						sprite.scale = scale * (1f + f * 0.1f) as float
					}).withMethod(PennerEasing.elasticInOut).instance())
				}),
				new WaitFx(1f)
		]).instance(FxInstance.EndAction.Loop))
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		super.onRender(gfx, x, y)
		gfx.draw(sprite, gfx.size / 2)
	}
}