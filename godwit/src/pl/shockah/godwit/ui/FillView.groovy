package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.gl.Gfx
import pl.shockah.godwit.gl.GfxSlice

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class FillView extends View implements ViewHolder<Void> {
	@Nullable private View innerView = null
	@Nullable private Vec2 cachedSize = null
	@Nonnull Padding padding = new Padding()

	FillView(@Nullable View innerView = null) {
		if (innerView)
			add(innerView)
	}

	@Nullable View getInnerView() {
		return innerView
	}

	@Override
	void add(@Nonnull View view, @Nullable Void attributes) {
		assert innerView == null
		ViewHolder.super.add(view, attributes)
		innerView = view

		if (cachedSize)
			adjustBounds(cachedSize)
	}

	@Override
	void remove(@Nonnull View view) {
		assert innerView == view
		ViewHolder.super.remove(view)
		innerView = null
	}

	@Override
	void onLayout() {
		super.onLayout()
		innerView?.onLayout()
	}

	private void adjustBounds(@Nonnull Vec2 size) {
		cachedSize = new Vec2(size.x, size.y)
		bounds.size = cachedSize
		innerView?.bounds?.position = padding.topLeftVector
		innerView?.bounds?.size = cachedSize - padding.vector
		onLayout()
	}

	@Override
	void onRender(@Nonnull Gfx gfx, float x, float y) {
		if (gfx.size != cachedSize)
			adjustBounds(gfx.size)

		super.onRender(gfx, x, y)
		if (innerView)
			innerView.onRender(new GfxSlice(gfx, innerView.bounds), x, y)
	}

	@Override
	@Nonnull Vec2 getIntrinsicSize(@Nonnull Vec2 availableSize) {
		return (innerView?.getIntrinsicSize(availableSize - padding.vector) ?: new Vec2()) + padding.vector
	}
}