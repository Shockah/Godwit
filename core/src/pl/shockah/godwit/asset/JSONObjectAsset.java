package pl.shockah.godwit.asset;

import java.util.Map;

import javax.annotation.Nonnull;

import lombok.experimental.Delegate;
import pl.shockah.jay.JSONObject;

public class JSONObjectAsset extends SingleAsset<JSONObject> {
	private interface DelegateExclusions<K, V> {
		void putAll(Map<? extends String, ?> map);
	}

	public JSONObjectAsset(@Nonnull String fileName) {
		super(fileName, JSONObject.class);
	}

	@Nonnull
	@Delegate(excludes = DelegateExclusions.class)
	@Override
	public JSONObject get() {
		return super.get();
	}
}