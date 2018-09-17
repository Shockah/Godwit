package pl.shockah.godwit.gesture;

import com.badlogic.gdx.InputAdapter;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import pl.shockah.godwit.GodwitLogger;

public abstract class GestureManager extends InputAdapter {
	@Nonnull
	public final GodwitLogger logger = new GodwitLogger(getClass());

	@Nonnull
	public final Map<Integer, Touch> touches = new HashMap<>();

	@Nonnull
	public final Set<GestureRecognizer> recognizers = new LinkedHashSet<>();

	@Nonnull
	protected final Set<ContinuousGestureRecognizer> currentContinuousRecognizers = new LinkedHashSet<>();

	public abstract void update();
}