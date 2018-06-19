package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxFont;

public class UiLabel extends UiControl<Rectangle> {
	@Nonnull
	public GfxFont font;

	@Nullable
	public String text;

	public UiLabel(@Nonnull GfxFont font) {
		this.font = font;
	}

	@Nullable
	@Override
	public Rectangle getGestureShape() {
		return getBounds();
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (text != null && !text.equals("")) {
			gfx.getScissors().push(getBounds(), getCameraGroup().getCamera());
			font.setText(text);
			font.setMaxWidth(size.x);
			font.render(gfx, getBounds().position.add(size.multiply(font.getAlignment().getVector())));
			gfx.getScissors().pop();
		}
	}
}