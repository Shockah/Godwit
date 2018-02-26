package pl.shockah.godwit.test.test.ui;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;

import javax.annotation.Nonnull;

import pl.shockah.godwit.State;
import pl.shockah.godwit.test.Configurable;
import pl.shockah.godwit.ui.Alignment;
import pl.shockah.godwit.ui.BaseLinearView;
import pl.shockah.godwit.ui.FillView;
import pl.shockah.godwit.ui.LinearView;
import pl.shockah.godwit.ui.Orientation;
import pl.shockah.godwit.ui.Padding;
import pl.shockah.godwit.ui.ViewEntity;

public class LinearViewTest extends State implements Configurable {
	@Override
	public void configure(@Nonnull Lwjgl3ApplicationConfiguration config) {
		config.setWindowedMode(256, 256);
	}

	@Override
	public void onAddedToParent() {
		super.onAddedToParent();

		LinearView linearView = new LinearView(Orientation.Horizontal);
		linearView.backgroundColor = Color.DARK_GRAY;
		linearView.spacing = 4f;

		FillView view1 = new FillView();
		view1.padding = new Padding(8f);
		view1.backgroundColor = Color.CHARTREUSE;
		linearView.add(view1, new BaseLinearView.Attributes(Alignment.Vertical.Middle));

		FillView view2 = new FillView();
		view2.padding = new Padding(12f, 96f);
		view2.backgroundColor = Color.SLATE;
		linearView.add(view2, new BaseLinearView.Attributes(Alignment.Vertical.Top));

		FillView view3 = new FillView();
		view3.padding = new Padding(64f, 12f);
		view3.backgroundColor = Color.FIREBRICK;
		linearView.add(view3, new BaseLinearView.Attributes(Alignment.Vertical.Middle));

		FillView view4 = new FillView();
		view4.padding = new Padding(12f, 96f);
		view4.backgroundColor = Color.CORAL;
		linearView.add(view4, new BaseLinearView.Attributes(Alignment.Vertical.Bottom));

		addChild(new ViewEntity(new FillView(linearView)));
	}
}