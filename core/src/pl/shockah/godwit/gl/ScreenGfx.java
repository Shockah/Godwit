package pl.shockah.godwit.gl;

import javax.annotation.Nonnull;

import lombok.Getter;
import pl.shockah.godwit.geom.Rectangle;

public class ScreenGfx extends GfxImpl {
	@Getter
	@Nonnull private Rectangle boundingBox = new Rectangle(0f, 0f);

	@Override
	public void updateCamera() {
		super.updateCamera();
		boundingBox = Rectangle.centered(getCameraPosition(), getSize());
	}
}