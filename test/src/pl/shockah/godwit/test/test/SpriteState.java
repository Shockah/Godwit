package pl.shockah.godwit.test.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.State;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.geom.Line;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxSprite;

public class SpriteState extends State {
	private GfxSprite sprite;
	private int movingCountdown = 0;

	@Override
	public void onCreate() {
		super.onCreate();

		AssetManager assetManager = Godwit.getInstance().assetManager;
		assetManager.load("abstract-480-320.jpg", Texture.class);
		assetManager.finishLoading();

		sprite = new GfxSprite(new Sprite(assetManager.get("abstract-480-320.jpg", Texture.class)));
		sprite.center();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		boolean l = Gdx.input.isKeyPressed(Input.Keys.LEFT);
		boolean r = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
		boolean u = Gdx.input.isKeyPressed(Input.Keys.UP);
		boolean d = Gdx.input.isKeyPressed(Input.Keys.DOWN);

		int offsetX = (r ? 1 : 0) - (l ? 1 : 0);
		int offsetY = (d ? 1 : 0) - (u ? 1 : 0);
		sprite.setOrigin(sprite.getOrigin() + new ImmutableVec2(offsetX, offsetY) * 4);
		sprite.offset = sprite.getOrigin().getMutableCopy();

		if (l || r || u || d) {
			sprite.setRotation(0f);
			movingCountdown = 90;
		} else {
			if (movingCountdown > 0)
				movingCountdown--;
			else
				sprite.rotate(1f);
		}
	}

	@Override
	public void onRender(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		super.onRender(gfx, v);

		gfx.setColor(Color.WHITE);
		gfx.draw(sprite, gfx.getSize() / 2 + v);

		if (movingCountdown > 0) {
			gfx.setColor(Color.RED);
			gfx.drawOutline(new Line(new Vec2(gfx.getWidth() / 2, 0f), new Vec2(gfx.getWidth() / 2, gfx.getHeight())));
			gfx.drawOutline(new Line(new Vec2(0f, gfx.getHeight() / 2), new Vec2(gfx.getWidth(), gfx.getHeight() / 2)));
		}
	}
}