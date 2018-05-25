package pl.shockah.godwit.platform;

import org.robovm.apple.dispatch.DispatchQueue;
import org.robovm.apple.foundation.NSURL;
import org.robovm.apple.foundation.NSURLRequest;
import org.robovm.apple.uikit.NSLayoutAttribute;
import org.robovm.apple.uikit.NSLayoutConstraint;
import org.robovm.apple.uikit.NSLayoutRelation;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIBarButtonItem;
import org.robovm.apple.uikit.UIBarButtonSystemItem;
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
		DispatchQueue.getMainQueue().async(() -> {
			UINavigationController navigation = new UINavigationController();

			UIViewController controller = new UIViewController();
			UIWebView webView = new UIWebView();
			webView.setScalesPageToFit(true);
			controller.getView().addSubview(webView);

			webView.loadRequest(new NSURLRequest(new NSURL(url)));

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

			UIBarButtonItem item = new UIBarButtonItem(UIBarButtonSystemItem.Done);
			item.setOnClickListener(self -> navigation.dismissViewController(true, null));
			controller.getNavigationItem().setRightBarButtonItem(item);

			getController().presentViewController(navigation, true, null);
		});
	}

	@Override
	public void openFacebook(@Nonnull String pageId, @Nonnull String pageUniqueUrl) {
		UIApplication app = UIApplication.getSharedApplication();
		if (app.canOpenURL(new NSURL("fb://"))) {
			app.openURL(new NSURL(String.format("fb://page/%s", pageId)));
		} else {
			app.openURL(new NSURL(String.format("https://facebook.com/%s", pageUniqueUrl)));
		}
	}

	@Override
	public void openInstagram(@Nonnull String pageUniqueUrl) {
		UIApplication app = UIApplication.getSharedApplication();
		if (app.canOpenURL(new NSURL("instagram://"))) {
			app.openURL(new NSURL(String.format("instagram://user?username=%s", pageUniqueUrl)));
		} else {
			app.openURL(new NSURL(String.format("https://instagram.com/%s", pageUniqueUrl)));
		}
	}
}