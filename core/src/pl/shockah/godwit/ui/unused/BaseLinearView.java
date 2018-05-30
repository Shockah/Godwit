package pl.shockah.godwit.ui.unused;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.Vec2;
import pl.shockah.godwit.ui.Alignment;
import pl.shockah.godwit.ui.Orientation;

public class BaseLinearView<T extends BaseLinearView.Attributes> extends ViewGroup<T> {
	@Nonnull
	public final Orientation orientation;

	public float spacing = 0f;

	public BaseLinearView(@Nonnull Orientation orientation) {
		this.orientation = orientation;
	}

	private Attributes getAttributes(@Nonnull View view) {
		Attributes attributes = getAttributesForView(view);
		if (attributes == null)
			throw new NullPointerException(String.format("BaseLinearView %s does not contain view %s.", this, view));
		return attributes;
	}

	@Override
	public void onLayout() {
		super.onLayout();

		float offset = 0f;
		for (View view : getViews()) {
			Vec2 size = bounds.size.subtract(orientation.vector.multiply(offset));
			size = view.getIntrinsicSize(size);
			view.bounds.size = size.mutableCopy();

			Vec2 basePosition = orientation.vector.multiply(offset);
			Vec2 alignmentPosition = bounds.size.subtract(view.bounds.size).multiply(orientation.getPerpendicular().vector).multiply(getAttributes(view).alignment.getNonNanVector());
			view.bounds.position = basePosition.add(alignmentPosition).mutableCopy();

			offset += size.multiply(orientation.vector).getLength() + spacing;
		}
	}

	public static class Attributes {
		@Nonnull
		public Alignment alignment;

		public Attributes(@Nonnull Alignment alignment) {
			this.alignment = alignment;
		}
	}
}