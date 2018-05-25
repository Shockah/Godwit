package pl.shockah.godwit.platform;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.annotation.Nonnull;

import pl.shockah.godwit.GodwitFragmentActivity;

public class AndroidWebViewService implements WebViewService {
	@Nonnull private final WeakReference<GodwitFragmentActivity> activityRef;

	public AndroidWebViewService(@Nonnull GodwitFragmentActivity activity) {
		this.activityRef = new WeakReference<>(activity);
	}

	@Nonnull private GodwitFragmentActivity getActivity() {
		GodwitFragmentActivity activity = activityRef.get();
		if (activity == null)
			throw new IllegalStateException("Lost context.");
		return activity;
	}

	@Override
	public void show(@Nonnull String url) {
		Intent intent = new Intent(getActivity(), WebViewActivity.class);
		Bundle b = new Bundle();
		b.putString("url", url);
		intent.putExtras(b);
		getActivity().startActivity(intent);
	}

	@Override
	public void openFacebook(@Nonnull String pageId, @Nonnull String pageUniqueUrl) {
		String url = String.format("https://facebook.com/%s", pageUniqueUrl);
		Uri uri = Uri.parse(url);
		try {
			ApplicationInfo applicationInfo = getActivity().getPackageManager().getApplicationInfo("com.facebook.katana", 0);
			if (applicationInfo.enabled)
				uri = Uri.parse(String.format("fb://facewebmodal/f?href=%s", url));
		} catch (PackageManager.NameNotFoundException ignored) {
		}
		getActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));
	}

	@Override
	public void openInstagram(@Nonnull String pageUniqueUrl) {
		String url = String.format("http://instagram.com/_u/%s", pageUniqueUrl);
		Uri uri = Uri.parse(url);

		Intent insta = new Intent(Intent.ACTION_VIEW, uri);
		insta.setPackage("com.instagram.android");

		if (isIntentAvailable(insta))
			getActivity().startActivity(insta);
		else
			getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://instagram.com/%s", pageUniqueUrl))));
	}

	private boolean isIntentAvailable(@Nonnull Intent intent) {
		final PackageManager packageManager = getActivity().getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public static class WebViewActivity extends Activity {
		private WebView mWebView;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			mWebView = new WebView(this);
			mWebView.setInitialScale(1);
			mWebView.getSettings().setLoadWithOverviewMode(true);
			mWebView.getSettings().setUseWideViewPort(true);
			mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
			mWebView.setScrollbarFadingEnabled(false);
			mWebView.getSettings().setBuiltInZoomControls(true);
			mWebView.getSettings().setDisplayZoomControls(false);
			mWebView.loadUrl(getIntent().getExtras().getString("url"));
			mWebView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}
			});

			this.setContentView(mWebView);
		}

		@Override
		public boolean onKeyDown(final int keyCode, final KeyEvent event) {
			if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
				mWebView.goBack();
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	}
}