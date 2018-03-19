package pl.shockah.godwit.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UIImagePickerController;
import org.robovm.apple.uikit.UIImagePickerControllerDelegateAdapter;
import org.robovm.apple.uikit.UIImagePickerControllerEditingInfo;
import org.robovm.apple.uikit.UIImagePickerControllerSourceType;
import org.robovm.apple.uikit.UIViewController;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import pl.shockah.func.Action1;

public class IosImagePickerService implements ImagePickerService {
	@Nonnull private final WeakReference<UIViewController> controllerRef;

	public IosImagePickerService(@Nonnull UIViewController controller) {
		this.controllerRef = new WeakReference<>(controller);
	}

	@Nonnull private UIViewController getController() {
		UIViewController controller = controllerRef.get();
		if (controller == null)
			throw new IllegalStateException("Lost controller.");
		return controller;
	}

	@Override
	public void openImagePicker(@Nonnull Action1<Texture> delegate) {
		UIAlertController alert = new UIAlertController("Choose source:", null, UIAlertControllerStyle.Alert);
		alert.addAction(new UIAlertAction("Camera", UIAlertActionStyle.Default, action -> {
			showImagePickerController(UIImagePickerControllerSourceType.Camera, delegate);
		}));
		alert.addAction(new UIAlertAction("Photos", UIAlertActionStyle.Default, action -> {
			showImagePickerController(UIImagePickerControllerSourceType.PhotoLibrary, delegate);
		}));
		getController().presentViewController(alert, true, null);
	}

	private void showImagePickerController(@Nonnull UIImagePickerControllerSourceType sourceType, @Nonnull Action1<Texture> delegate) {
		UIImagePickerController picker = new UIImagePickerController();
		picker.setDelegate(new UIImagePickerControllerDelegateAdapter() {
			@Override
			public void didFinishPickingMedia(UIImagePickerController picker, UIImagePickerControllerEditingInfo info) {
				UIImage image = (UIImage)info.get(new NSString("UIImagePickerControllerEditedImage"));
				byte[] bytes = image.toPNGData().getBytes();
				Gdx.app.postRunnable(() -> {
					Pixmap pixmap = new Pixmap(bytes, 0, bytes.length);
					Texture texture = new Texture(pixmap);
					pixmap.dispose();
					delegate.call(texture);
				});
			}
		});
		picker.setAllowsEditing(true);
		picker.setSourceType(sourceType);
		getController().presentViewController(picker, true, null);
	}
}