package pl.shockah.godwit.platform;

import com.badlogic.gdx.graphics.Pixmap;

import org.robovm.apple.avfoundation.AVAuthorizationStatus;
import org.robovm.apple.avfoundation.AVCaptureDevice;
import org.robovm.apple.avfoundation.AVMediaType;
import org.robovm.apple.photos.PHAuthorizationStatus;
import org.robovm.apple.photos.PHPhotoLibrary;
import org.robovm.apple.uikit.UIAlertAction;
import org.robovm.apple.uikit.UIAlertActionStyle;
import org.robovm.apple.uikit.UIAlertController;
import org.robovm.apple.uikit.UIAlertControllerStyle;
import org.robovm.apple.uikit.UIDevice;
import org.robovm.apple.uikit.UIImage;
import org.robovm.apple.uikit.UIImagePickerController;
import org.robovm.apple.uikit.UIImagePickerControllerDelegateAdapter;
import org.robovm.apple.uikit.UIImagePickerControllerEditingInfo;
import org.robovm.apple.uikit.UIImagePickerControllerSourceType;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.apple.uikit.UIUserInterfaceIdiom;
import org.robovm.apple.uikit.UIViewController;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

import pl.shockah.unicorn.func.Action1;

public class IosImagePickerService extends ImagePickerService {
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

	@Nonnull private static PermissionState mapNativeState(@Nonnull PHAuthorizationStatus status) {
		switch (status) {
			case NotDetermined:
				return PermissionState.Unknown;
			case Denied:
				return PermissionState.Denied;
			case Authorized: case Restricted:
				return PermissionState.Authorized;
		}
		throw new IllegalArgumentException("Status shouldn't be null.");
	}

	@Nonnull private static PermissionState mapNativeState(@Nonnull AVAuthorizationStatus status) {
		switch (status) {
			case NotDetermined:
				return PermissionState.Unknown;
			case Denied:
				return PermissionState.Denied;
			case Authorized: case Restricted:
				return PermissionState.Authorized;
		}
		throw new IllegalArgumentException("Status shouldn't be null.");
	}

	@Override
	@Nonnull public PermissionState getPermissionState(@Nonnull Source source) {
		switch (source) {
			case Library:
				return mapNativeState(PHPhotoLibrary.getAuthorizationStatus());
			case Camera:
				return mapNativeState(AVCaptureDevice.getAuthorizationStatusForMediaType(AVMediaType.Video));
		}
		throw new IllegalArgumentException("Source shouldn't be null.");
	}

	@SuppressWarnings("deprecated")
	@Override
	public boolean isCameraAvailable() {
		return UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceType.Camera) && AVCaptureDevice.getDevicesForMediaType(AVMediaType.Video).size() != 0;
	}

	@Override
	public void requestPermission(@Nonnull Source source, @Nonnull Action1<PermissionState> newStateDelegate) {
		PermissionState permissionState = getPermissionState(source);
		if (permissionState != PermissionState.Unknown) {
			newStateDelegate.call(permissionState);
			return;
		}

		switch (source) {
			case Library:
				PHPhotoLibrary.requestAuthorization(authorizationStatus -> {
					newStateDelegate.call(mapNativeState(authorizationStatus));
				});
				break;
			case Camera:
				AVCaptureDevice.requestAccessForMediaType(AVMediaType.Video, granted -> {
					newStateDelegate.call(granted ? PermissionState.Authorized : PermissionState.Denied);
				});
				break;
		}
	}

	@Override
	public void getPixmapViaImagePicker(@Nonnull Action1<Pixmap> pixmapDelegate, @Nonnull Action1<PermissionException> permissionExceptionDelegate) {
		UIAlertController alert = new UIAlertController(null, null, UIAlertControllerStyle.ActionSheet);
		if (UIDevice.getCurrentDevice().getUserInterfaceIdiom() == UIUserInterfaceIdiom.Pad)
			alert.getPopoverPresentationController().setSourceRect(UIScreen.getMainScreen().getBounds());

		alert.addAction(new UIAlertAction("Camera", UIAlertActionStyle.Default, action -> {
			requestPermissionAndShowImagePickerController(Source.Camera, pixmapDelegate, permissionExceptionDelegate);
		}));
		alert.addAction(new UIAlertAction("Photos", UIAlertActionStyle.Default, action -> {
			requestPermissionAndShowImagePickerController(Source.Library, pixmapDelegate, permissionExceptionDelegate);
		}));
		alert.addAction(new UIAlertAction("Cancel", UIAlertActionStyle.Cancel, action -> {
			alert.dismissViewController(true, null);
		}));
		
		getController().presentViewController(alert, true, null);
	}

	private void requestPermissionAndShowImagePickerController(@Nonnull Source source, @Nonnull Action1<Pixmap> pixmapDelegate, @Nonnull Action1<PermissionException> permissionExceptionDelegate) {
		requestPermission(source, permissionState -> {
			if (permissionState == PermissionState.Authorized) {
				switch (source) {
					case Library:
						showImagePickerController(UIImagePickerControllerSourceType.PhotoLibrary, pixmapDelegate);
						break;
					case Camera:
						showImagePickerController(UIImagePickerControllerSourceType.Camera, pixmapDelegate);
						break;
				}
			} else {
				permissionExceptionDelegate.call(new PermissionException(source));
			}
		});
	}

	private void showImagePickerController(@Nonnull UIImagePickerControllerSourceType sourceType, @Nonnull Action1<Pixmap> pixmapDelegate) {
		UIImagePickerController picker = new UIImagePickerController();
		picker.setDelegate(new UIImagePickerControllerDelegateAdapter() {
			@Override
			public void didFinishPickingMedia(UIImagePickerController picker, UIImagePickerControllerEditingInfo info) {
				picker.dismissViewController(true, () -> {
					UIImage image = info.getEditedImage();
					byte[] bytes = image.toPNGData().getBytes();
					pixmapDelegate.call(new Pixmap(bytes, 0, bytes.length));
				});
			}
		});
		picker.setAllowsEditing(true);
		picker.setSourceType(sourceType);
		getController().presentViewController(picker, true, null);
	}
}