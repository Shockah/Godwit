package pl.shockah.godwit.test

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureRegion
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit
import pl.shockah.godwit.State
import pl.shockah.godwit.animfx.FxInstance
import pl.shockah.godwit.animfx.object.ObjectClosureFx
import pl.shockah.godwit.asset.Asset
import pl.shockah.godwit.gl.GfxSprite
import pl.shockah.godwit.gl.SpriteSheet

@CompileStatic
class SpriteSheetState extends State {
	private static final Asset<SpriteSheet> sheet = new Asset<>("ninja-run.json", SpriteSheet.class)

	@Override
	protected void onCreate() {
		super.onCreate()
		loadAsset(sheet)
		SpriteSheet sheet = sheet.get()

		TextureRegion region = sheet[0]
		new GfxSprite(new Sprite(region)).asEntity().with {
			sprite.center()
			sprite.position = Godwit.instance.gfx.size * 0.5f

			fxes.add(new ObjectClosureFx<GfxSprite.Entity>(0.45f, { GfxSprite.Entity obj, float f, float previous ->
				sprite.region = sheet[(f * sheet.frameCount as int) % sheet.frameCount]
			}).instance(FxInstance.EndAction.Loop))

			create(this)
		}
	}
}