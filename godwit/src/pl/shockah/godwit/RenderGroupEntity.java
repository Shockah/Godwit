package pl.shockah.godwit;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.util.SafeList;
import pl.shockah.util.SortedLinkedList;

public class RenderGroupEntity extends Entity {
	@Nonnull public final SafeList<Entity> renderOrder = new SafeList<>(new SortedLinkedList<>(depthComparator));

	@Override
	public void render(@Nonnull Gfx gfx, @Nonnull IVec2 v) {
		for (Entity entity : renderOrder.get()) {
			entity.render(gfx, entity.getPointIn(entity.getRenderGroup()));
		}
	}
}