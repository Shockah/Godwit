package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class FillView extends View implements ViewHolder {
	@Nullable private View innerView = null
	@Nullable private Vec2 cachedSize = null

	FillView(@Nullable View innerView = null) {
		add(innerView)
	}

	@Nullable View getInnerView() {
		return innerView
	}

	@Override
	void add(@Nonnull View view) {
		assert innerView == null
		ViewHolder.super.add(view)
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
		innerView?.bounds?.size = cachedSize
		onLayout()
	}

	@Override
	void onRender(@Nonnull Gfx gfx) {
		if (gfx.size != cachedSize)
			adjustBounds(gfx.size)

		super.onRender(gfx)
		innerView?.onRender(gfx)
	}

	@Override
	@Nonnull Vec2 getIntrinsicSize(@Nonnull Vec2 availableSize) {
		return cachedSize ?: super.getIntrinsicSize(availableSize)
	}
}