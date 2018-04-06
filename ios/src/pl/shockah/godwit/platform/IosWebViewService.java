package pl.shockah.godwit.platform;

import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.NSLayoutAttribute;
import org.robovm.apple.uikit.NSLayoutConstraint;
import org.robovm.apple.uikit.NSLayoutRelation;
import org.robovm.apple.uikit.UIBarButtonItem;
import org.robovm.apple.uikit.UIBarButtonItemStyle;
import org.robovm.apple.uikit.UIBarStyle;
import org.robovm.apple.uikit.UINavigationController;
import org.robovm.apple.uikit.UIViewController;
import org.robovm.apple.uikit.UIWebView;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;

public class IosWebViewService implements WebViewService {
	@Nonnull private final WeakReference<UIViewController> controllerRef;

	public IosWebViewService(@Nonnull UIViewController controller) {
		this.controllerRef = new WeakReference<>(controller);
	}

	@Nonnull private UIViewController getController() {
		UIViewController controller = controllerRef.get();
		if (controller == null)
			throw new IllegalStateException("Lost controller.");
		return controller;
	}

	@Override
	public void show(@Nonnull String url) {
		UINavigationController navigation = new UINavigationController();
		navigation.getNavigationBar().setBarStyle(UIBarStyle.BlackTranslucent);

		UIViewController controller = new UIViewController();
		UIWebView webView = new UIWebView();
		controller.getView().addSubview(webView);

		webView.setTranslatesAutoresizingMaskIntoConstraints(false);
		controller.getView().addConstraint(new NSLayoutConstraint(
				webView, NSLayoutAttribute.Leading, NSLayoutRelation.Equal,
				controller.getView(), NSLayoutAttribute.Leading,
				1f, 0f
		));
		controller.getView().addConstraint(new NSLayoutConstraint(
				webView, NSLayoutAttribute.Trailing, NSLayoutRelation.Equal,
				controller.getView(), NSLayoutAttribute.Trailing,
				1f, 0f
		));
		controller.getView().addConstraint(new NSLayoutConstraint(
				webView, NSLayoutAttribute.Top, NSLayoutRelation.Equal,
				controller.getView(), NSLayoutAttribute.Top,
				1f, 0f
		));
		controller.getView().addConstraint(new NSLayoutConstraint(
				webView, NSLayoutAttribute.Bottom, NSLayoutRelation.Equal,
				controller.getView(), NSLayoutAttribute.Bottom,
				1f, 0f
		));

		navigation.pushViewController(controller, false);

		navigation.getNavigationItem().setLeftBarButtonItem(new UIBarButtonItem(NSString.getLocalizedString("Close"), UIBarButtonItemStyle.Done, button -> {
			navigation.dismissViewController(true, null);
		}));

		getController().presentViewController(navigation, true, null);
	}
}