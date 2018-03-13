package pl.shockah.godwit;

import java.util.Comparator;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.util.SafeList;
import pl.shockah.util.SortedLinkedList;

public class RenderGroupEntity extends Entity {
	@Nonnull private static final Comparator<? super Entity> depthComparator = (o1, o2) -> -Float.compare(o1.getDepth(), o2.getDepth());

	@Nonnull public final SafeList<Entity> renderOrder = new SafeList<>(new SortedLinkedList<>(depthComparator));

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		renderOrder.update();
		for (Entity entity : renderOrder.get()) {
			entity.render(gfx, v + entity.getPointInEntity(this));
		}
	}
}