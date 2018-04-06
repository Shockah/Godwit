package pl.shockah.godwit.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

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

	public static class WebViewActivity extends Activity {
		private WebView mWebView;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			mWebView = new WebView(this);
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