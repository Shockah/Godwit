package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.NinePatch;

public abstract class UiButton extends AbstractUiButton<Rectangle> {
	@Nonnull
	public Padding padding = new Padding();

	public UiButton(@Nullable Listener listener) {
		super(listener);
	}

	@Nonnull
	public UiButton setPadding(@Nonnull Padding padding) {
		this.padding = padding;
		return this;
	}

	@Override
	public void updateGestureShape() {
		Rectangle bounds = getBounds();
		gestureShape = new Rectangle(
				bounds.position.x - padding.left.getPixels(),
				bounds.position.y - padding.top.getPixels(),
				bounds.size.x + padding.left.getPixels() + padding.right.getPixels(),
				bounds.size.y + padding.top.getPixels() + padding.bottom.getPixels()
		);
	}

	public static class NinePatchButton extends UiButton {
		@Nonnull
		public final NinePatch normal;

		@Nonnull
		public final NinePatch pressed;

		public NinePatchButton(@Nonnull NinePatch normal, @Nonnull NinePatch pressed, @Nonnull Listener listener) {
			super(listener);
			this.normal = normal;
			this.pressed = pressed;
		}

		@Override
		public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
			super.render(gfx, v);

			pl.shockah.godwit.gl.NinePatch ninePatch = isPressed ? pressed : normal;
			ninePatch.rectangle = getBounds();
			ninePatch.render(gfx, v);
		}

		@Nonnull
		@Override
		public NinePatchButton setPadding(@Nonnull Padding padding) {
			super.setPadding(padding);
			return this;
		}
	}
}