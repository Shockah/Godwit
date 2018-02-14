package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit
import pl.shockah.godwit.State
import pl.shockah.godwit.animfx.*
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.GfxSprite

import javax.annotation.Nonnull

@CompileStatic
class AnimationState extends State {
	private List<Easing> methods = [
			Easing.linear,
			SmoothstepEasing.smoothstep, SmoothstepEasing.smoothstep2, SmoothstepEasing.smoothstep3,
			PennerEasing.sineIn, PennerEasing.cubicIn, PennerEasing.quadIn, PennerEasing.quarticIn, PennerEasing.quinticIn,
			PennerEasing.exponentialIn, PennerEasing.circularIn, PennerEasing.backIn, PennerEasing.elasticIn, PennerEasing.bounceIn,
			PennerEasing.sineOut, PennerEasing.cubicOut, PennerEasing.quadOut, PennerEasing.quarticOut, PennerEasing.quinticOut,
			PennerEasing.exponentialOut, PennerEasing.circularOut, PennerEasing.backOut, PennerEasing.elasticOut, PennerEasing.bounceOut,
			PennerEasing.sineInOut, PennerEasing.cubicInOut, PennerEasing.quadInOut, PennerEasing.quarticInOut, PennerEasing.quinticInOut,
			PennerEasing.exponentialInOut, PennerEasing.circularInOut, PennerEasing.backInOut, PennerEasing.elasticInOut, PennerEasing.bounceInOut
	]
	private List<GfxSprite> sprites = []

	private float f = 0f

	@Override
	protected void onCreate() {
		super.onCreate()

		Godwit.instance.assetManager.load("question-mark.png", Texture.class)
		Godwit.instance.assetManager.finishLoading()

		for (Easing method : methods) {
			GfxSprite sprite = new GfxSprite(new Sprite(Godwit.instance.assetManager.get("question-mark.png", Texture.class)))
			//sprite.setOriginCenter()
			sprite.setSize(16f, 16f)
			sprite.x = sprites.size() * 18f + 2f
			sprite.y = 2f
			sprites.add(sprite)

			Fx fx = new AppliedFx<GfxSprite>(sprite, 5f) {
				@Override
				void update(float f, float previous) {
					object.y = Easing.linear.ease(2 + Gdx.graphics.height * 0.2f, Gdx.graphics.height * 0.8f - 16, f)
				}
			}
			fx.method = method
			fxes.add(fx.instance(FxInstance.EndAction.ReverseLoop))
		}
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		super.onRender(gfx, x, y)

		for (GfxSprite sprite in sprites) {
			gfx.draw(sprite, x, y)
		}
	}
}