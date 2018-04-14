package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;

public class BaseLinearView<T extends BaseLinearView.Attributes> extends ViewGroup<T> {
	@Nonnull public final Orientation orientation;
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
			IVec2 size = bounds.size - orientation.vector * offset;
			size = view.getIntrinsicSize(size);
			view.bounds.size = size.getMutableCopy();

			IVec2 basePosition = orientation.vector * offset;
			IVec2 alignmentPosition = (bounds.size - view.bounds.size) * orientation.getPerpendicular().vector * getAttributes(view).alignment.getNonNanVector();
			view.bounds.position = (basePosition + alignmentPosition).getMutableCopy();

			offset += (size * orientation.vector).getLength() + spacing;
		}
	}

	public static class Attributes {
		@Nonnull public Alignment alignment;

		public Attributes(@Nonnull Alignment alignment) {
			this.alignment = alignment;
		}
	}
}