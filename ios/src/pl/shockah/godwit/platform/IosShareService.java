package pl.shockah.godwit.platform;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

import org.robovm.apple.coregraphics.CGPoint;
import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSData;
import org.robovm.apple.foundation.NSMutableArray;
import org.robovm.apple.foundation.NSObject;
import org.robovm.apple.uikit.UIActivityViewController;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UIPopoverArrowDirection;
import org.robovm.apple.uikit.UIPopoverPresentationController;
import org.robovm.apple.uikit.UIView;
import org.robovm.apple.uikit.UIViewController;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import pl.shockah.godwit.MemoryFileHandle;

public class IosShareService implements ShareService {
	@Nonnull
	private final WeakReference<UIViewController> controllerRef;

	public IosShareService(@Nonnull UIViewController controller) {
		this.controllerRef = new WeakReference<>(controller);
	}

	@Nonnull
	private UIViewController getController() {
		UIViewController controller = controllerRef.get();
		if (controller == null)
			throw new IllegalStateException("Lost controller.");
		return controller;
	}

	@Override
	public void share(@Nonnull Pixmap pixmap) {
		MemoryFileHandle memory = new MemoryFileHandle();
		PixmapIO.writePNG(memory, pixmap);
		UIImage image = new UIImage(new NSData(memory.getBytes()));

		NSArray<NSObject> activityItems = new NSMutableArray<>();
		activityItems.add(image);

		UIActivityViewController controller = new UIActivityViewController(activityItems, new NSArray<>());
		UIPopoverPresentationController popover = controller.getPopoverPresentationController();
		UIView view = getController().getView();
		if (popover != null) {
			popover.setSourceView(view);
			popover.setSourceRect(new CGRect(
					new CGPoint(view.getBounds().getMidX(), view.getBounds().getMidY()),
					new CGSize(0, 0)
			));
			popover.setPermittedArrowDirections(UIPopoverArrowDirection.None);
		}
		getController().presentViewController(controller, true, null);
	}
}