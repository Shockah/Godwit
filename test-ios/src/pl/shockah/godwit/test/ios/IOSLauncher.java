package pl.shockah.godwit.test.ios;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSIndexPath;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UINavigationController;
import org.robovm.apple.uikit.UITableView;
import org.robovm.apple.uikit.UITableViewCell;
import org.robovm.apple.uikit.UITableViewCellStyle;
import org.robovm.apple.uikit.UITableViewController;
import org.robovm.apple.uikit.UIViewController;

import pl.shockah.godwit.Godwit;
import pl.shockah.godwit.PlatformGodwitAdapter;
import pl.shockah.godwit.State;

public class IOSLauncher extends IOSApplication.Delegate {
	@Override
	protected IOSApplication createApplication() {
		IOSApplicationConfiguration config = new IOSApplicationConfiguration();
		config.orientationLandscape = false;
		config.orientationPortrait = true;
		config.useAccelerometer = false;
		config.useCompass = false;
		config.allowIpod = true;
		return new IOSApplication(new PlatformGodwitAdapter(new State()) {
			@Override
			public void create() {
				super.create();

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

				UIApplication app = UIApplication.getSharedApplication();
				UIViewController gdxRoot = app.getKeyWindow().getRootViewController();

				UINavigationController navigationController = new UINavigationController();
				app.getKeyWindow().setRootViewController(navigationController);

				UITableViewController tableViewController = new UITableViewController() {
					@Override
					public long getNumberOfRowsInSection(UITableView tableView, long section) {
						return testNames.length;
					}

					@Override
					public UITableViewCell getCellForRow(UITableView tableView, NSIndexPath indexPath) {
						UITableViewCell cell = tableView.dequeueReusableCell("Cell");
						if (cell == null)
							cell = new UITableViewCell(UITableViewCellStyle.Default, "Cell");

						cell.getTextLabel().setText(testNames[indexPath.getRow()]);
						return cell;
					}

					@Override
					@SuppressWarnings("unchecked")
					public void didSelectRow(UITableView tableView, NSIndexPath indexPath) {
						tableView.deselectRow(indexPath, true);
						String testName = testNames[indexPath.getRow()];
						String className = String.format("pl.shockah.godwit.test.%sTest", testName);

						try {
							Class<? extends State> clazz = (Class<? extends State>)Class.forName(className);
							State state = clazz.newInstance();
							Godwit.getInstance().moveToState(state);
							navigationController.pushViewController(gdxRoot, true);
						} catch (Exception e) {
							throw new RuntimeException("Cannot start a test.", e);
						}
					}
				};

				navigationController.pushViewController(tableViewController, false);
			}
		}, config);
	}

	static void main(String[] argv) {
		NSAutoreleasePool pool = new NSAutoreleasePool();
		UIApplication.main(argv, null, IOSLauncher.class);
		pool.close();
	}
}