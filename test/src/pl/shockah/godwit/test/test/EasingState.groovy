package pl.shockah.godwit.test.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit
import pl.shockah.godwit.State
import pl.shockah.godwit.animfx.FxInstance
import pl.shockah.godwit.animfx.ease.Easing
import pl.shockah.godwit.animfx.ease.PennerEasing
import pl.shockah.godwit.animfx.ease.SmoothstepEasing
import pl.shockah.godwit.animfx.object.ObjectClosureFx
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.gl.GfxSprite

@CompileStatic
class EasingState extends State {
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

	@Override
	void onCreate() {
		super.onCreate()

		Godwit.instance.assetManager.load("question-mark.png", Texture.class)
		Godwit.instance.assetManager.finishLoading()

		for (int i in 0..<methods.size()) {
			Easing method = methods[i]
			new GfxSprite(new Sprite(Godwit.instance.assetManager.get("question-mark.png", Texture.class))).asEntity().tap {
				sprite.size = new Vec2(16f, 16f)
				sprite.x = i * 18f + 2f as float
				sprite.y = 2f

				sprite.fxes.add(new ObjectClosureFx<GfxSprite>(5f, { GfxSprite obj, float f, float previous ->
					obj.y = Easing.linear.ease(2 + Gdx.graphics.height * 0.2f as float, Gdx.graphics.height * 0.8f - 16 as float, f)
				}).withMethod(method).instance(FxInstance.EndAction.ReverseLoop))
			}.create(this)
		}
	}
}