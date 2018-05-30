package pl.shockah.godwit.gl;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Shape;
import pl.shockah.unicorn.func.Action0;
import pl.shockah.unicorn.func.Action1;

public class GfxSlice extends Gfx {
	private interface DelegateExlusions {
		int getWidth();
		int getHeight();
		void draw(Renderable renderable, IVec2 v);
		void draw(Renderable renderable, float x, float y);
		void draw(Renderable renderable);
		void drawFilled(Shape.Filled shape, IVec2 v);
		void drawFilled(Shape.Filled shape, float x, float y);
		void drawFilled(Shape.Filled shape);
		void drawOutline(Shape.Outline shape, IVec2 v);
		void drawOutline(Shape.Outline shape, float x, float y);
		void drawOutline(Shape.Outline shape);
		void drawPoint(IVec2 v);
		void drawPoint(float x, float y);

		IVec2 getSize();
		void setColor(float r, float g, float b, float a);
		void setColor(float r, float g, float b);
		void prepareSprites(Action0 f);
		void prepareSprites(Action1<Gfx> f);
		void prepareShapes(ShapeRenderer.ShapeType type, Action0 f);
		void prepareShapes(ShapeRenderer.ShapeType type, Action1<Gfx> f);
		void clear();
	}

	@Nonnull
	@Delegate(excludes = DelegateExlusions.class)
	public final Gfx wrapped;

	@Nonnull
	public final Rectangle bounds;

	public GfxSlice(@Nonnull Gfx wrapped, @Nonnull Rectangle bounds) {
		this.wrapped = wrapped;
		this.bounds = bounds;
	}

	@Override
	public int getWidth() {
		return (int)bounds.size.x;
	}

	@Override
	public int getHeight() {
		return (int)bounds.size.y;
	}

	@Override
	protected void internalEndTick() {
		wrapped.internalEndTick();
	}

	@Override
	protected void prepareContext() {
		wrapped.prepareContext();
	}

	@Override
	public void draw(@Nonnull Renderable renderable, @Nonnull IVec2 v) {
		wrapped.draw(renderable, v.x() + bounds.position.x, v.y() + bounds.position.y);
	}

	@Override
	public void drawFilled(@Nonnull Shape.Filled shape, @Nonnull IVec2 v) {
		wrapped.drawFilled(shape, v.x() + bounds.position.x, v.y() + bounds.position.y);
	}

	@Override
	public void drawOutline(@Nonnull Shape.Outline shape, @Nonnull IVec2 v) {
		wrapped.drawOutline(shape, v.x() + bounds.position.x, v.y() + bounds.position.y);
	}

	@Override
	public void drawPoint(@Nonnull IVec2 v) {
		wrapped.drawPoint(v.x() + bounds.position.x, v.y() + bounds.position.y);
	}
}