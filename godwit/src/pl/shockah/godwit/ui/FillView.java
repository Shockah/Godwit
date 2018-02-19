package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.Getter;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.ImmutableVec2;
import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.gl.GfxSlice;

public class FillView extends ViewHolder<Void> {
	@Getter
	@Nullable private View innerView = null;

	@Nullable private Vec2 cachedSize = null;
	@Nonnull public Padding padding = new Padding();

	public FillView() {
		this(null);
	}

	public FillView(@Nullable View innerView) {
		if (innerView != null)
			add(innerView);
	}

	@Override
	public void add(@Nonnull View view, @Nullable Void attributes) {
		if (innerView != null)
			throw new IllegalStateException();
		super.add(view, attributes);
		innerView = view;

		if (cachedSize != null)
			adjustBounds(cachedSize);
	}

	@Override
	public void remove(@Nonnull View view) {
		if (innerView != null)
			throw new IllegalStateException();
		super.remove(view);
		innerView = null;
	}

	@Override
	public void onLayout() {
		super.onLayout();
		if (innerView != null)
			innerView.onLayout();
	}

	private void adjustBounds(@Nonnull IVec2 size) {
		cachedSize = size.getMutableCopy();
		bounds.size = cachedSize;
		if (innerView != null) {
			innerView.bounds.position = padding.getTopLeftVector().getMutableCopy();
			innerView.bounds.size = (cachedSize - padding.getVector()).getMutableCopy();
		}
		onLayout();
	}

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		if (!gfx.getSize().equals(cachedSize))
			adjustBounds(gfx.getSize());

		super.render(gfx, v);
		if (innerView != null)
			innerView.render(new GfxSlice(gfx, innerView.bounds), v);
	}

	@Override
	@Nonnull public IVec2 getIntrinsicSize(@Nonnull IVec2 availableSize) {
		return (innerView != null ? innerView.getIntrinsicSize(availableSize - padding.getVector()) : new ImmutableVec2()) + padding.getVector();
	}
}