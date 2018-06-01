package pl.shockah.godwit;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.asset.Asset;
import pl.shockah.godwit.constraint.AbstractConstrainable;
import pl.shockah.godwit.constraint.Constrainable;
import pl.shockah.godwit.constraint.Constraint;
import pl.shockah.godwit.gl.Gfx;
import pl.shockah.godwit.platform.SafeAreaService;
import pl.shockah.godwit.ui.Padding;
import pl.shockah.unicorn.UnexpectedException;

public class State extends RenderGroup {
	@Nonnull
	public final Constrainable safeArea = new AbstractConstrainable();

	@Nonnull
	public final CameraGroup game = new CameraGroup();

	@Nonnull
	public final CameraGroup ui = new CameraGroup() {
		@Override
		protected void updateViewportIfNeeded(@Nonnull Gfx gfx) {
			super.updateViewportIfNeeded(gfx);

			SafeAreaService service = Godwit.getInstance().platformServiceProvider.get(SafeAreaService.class);
			if (service == null)
				throw new UnexpectedException("SafeAreaService should always be available.");

			Padding padding = service.getSafeAreaPadding();
			safeArea.setAttribute(Constraint.Attribute.Width, size.x - padding.left.getPixels() - padding.right.getPixels());
			safeArea.setAttribute(Constraint.Attribute.Height, size.y - padding.top.getPixels() - padding.bottom.getPixels());
		}
	};

	@Nonnull
	private final List<Asset<?>> retainedAssets = new ArrayList<>();

	public State() {
		addChild(game);
		addChild(ui);
	}

	@Override
	public void updateSelf() {
		super.updateSelf();
	}

	@Override
	public void addChild(@Nonnull Entity entity) {
		if (entity != game && entity != ui)
			throw new UnsupportedOperationException("Entities shouldn't be added to States directly. Use State#game or State#ui instead.");
		else
			super.addChild(entity);
	}

	@Override
	public void onRemovedFromHierarchy() {
		super.onRemovedFromHierarchy();
		for (Asset<?> asset : retainedAssets) {
			asset.unload();
		}
		retainedAssets.clear();
	}

	public void loadAsset(Asset<?> asset) {
		asset.load();
		retainedAssets.add(asset);
	}
}