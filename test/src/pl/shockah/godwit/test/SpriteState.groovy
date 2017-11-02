package pl.shockah.godwit.test

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import groovy.transform.CompileStatic
import pl.shockah.godwit.Godwit
import pl.shockah.godwit.State
import pl.shockah.godwit.geom.Line
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.GfxSprite

import javax.annotation.Nonnull

@CompileStatic
class SpriteState extends State {
	private GfxSprite sprite
	private int movingCountdown = 0

	@Override
	protected void onCreate() {
		super.onCreate()

		Godwit.instance.assetManager.load("abstract-480-320.jpg", Texture.class)
		Godwit.instance.assetManager.finishLoading()

		sprite = new GfxSprite(new Sprite(Godwit.instance.assetManager.get("abstract-480-320.jpg", Texture.class)))
		sprite.setOriginCenter()
	}

	@Override
	protected void onUpdate() {
		super.onUpdate()

		boolean l = Gdx.input.isKeyPressed(Input.Keys.LEFT)
		boolean r = Gdx.input.isKeyPressed(Input.Keys.RIGHT)
		boolean u = Gdx.input.isKeyPressed(Input.Keys.UP)
		boolean d = Gdx.input.isKeyPressed(Input.Keys.DOWN)

		int offsetX = (r ? 1 : 0) - (l ? 1 : 0)
		int offsetY = (d ? 1 : 0) - (u ? 1 : 0)
		sprite.setOrigin(sprite.origin + new Vec2(offsetX, offsetY) * 4)

		if (l || r || u || d) {
			sprite.rotation = 0f
			movingCountdown = 90
		} else {
			if (movingCountdown > 0)
				movingCountdown--
			else
				sprite.rotation += 1f
		}
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		super.onRender(gfx, x, y)

		gfx.withColor(Color.WHITE) {
			draw(sprite, size / 2)
		}

		if (movingCountdown > 0) {
			gfx.withColor(Color.RED) {
				drawOutline(new Line(new Vec2(width / 2 as float, 0f), new Vec2(width / 2 as float, height)))
				drawOutline(new Line(new Vec2(0f, height / 2 as float), new Vec2(width, height / 2 as float)))
			}
		}
	}
}