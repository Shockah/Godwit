package pl.shockah.godwit.test.android;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import pl.shockah.godwit.GodwitFragmentActivity;
import pl.shockah.godwit.PlatformGodwitAdapter;
import pl.shockah.godwit.State;

public class AndroidLauncher extends GodwitFragmentActivity implements AndroidFragmentApplication.Callbacks {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] testNames = new String[] {
				"AttachedViewport",
				"Clustering",
				"ColorSpace",
				"ColorSpaceEase",
				"Constraint",
				"Easing",
				"GestureRecognizer",
				"GfxFont",
				"MaskEntity",
				"Metronome",
				"PolygonCollision",
				"SequenceFx",
				"ShapeAlpha",
				"Shapes",
				"SpriteSheet",
				"Sprite",
				"TravellingSalesman",
				"TriangleCollision",
				"Ui1"
		};

		ListFragment listFragment = new CustomListFragment();
		listFragment.setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, testNames));

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(android.R.id.content, listFragment);
		transaction.commit();
	}

	public static class CustomListFragment extends ListFragment {
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			String testName = (String)getListAdapter().getItem(position);
			String className = String.format("pl.shockah.godwit.test.%sTest", testName);

			FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
			transaction.replace(android.R.id.content, GameFragment.create(className));
			transaction.commit();
		}
	}

	public static class GameFragment extends AndroidFragmentApplication {
		public static GameFragment create(String className) {
			GameFragment fragment = new GameFragment();
			Bundle args = new Bundle();
			args.putString("className", className);
			fragment.setArguments(args);
			return fragment;
		}

		@SuppressWarnings("unchecked")
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			try {
				Class<? extends State> clazz = (Class<? extends State>)Class.forName(getArguments().getString("className"));
				State state = clazz.newInstance();
				AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
				config.depth = 8;
				config.useAccelerometer = false;
				config.useCompass = false;
				config.useGyroscope = false;
				config.useRotationVectorSensor = false;
				return initializeForView(new PlatformGodwitAdapter(state), config);
			} catch (Exception e) {
				throw new RuntimeException("Cannot start a test.", e);
			}
		}
	}

	@Override
	public void exit() {
	}
}