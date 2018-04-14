package pl.shockah.godwit.asset;

import com.badlogic.gdx.graphics.Texture;

import javax.annotation.Nonnull;

import java8.util.stream.Collectors;
import java8.util.stream.RefStreams;
import lombok.experimental.Delegate;
import pl.shockah.godwit.gl.SpriteSheet;
import pl.shockah.jay.JSONList;
import pl.shockah.jay.JSONObject;

public class SpriteSheetAsset extends ComplexAsset<SpriteSheet> {
	@Nonnull protected final Asset<Texture> textureAsset;
	@Nonnull protected final Asset<JSONObject> jsonAsset;

	public SpriteSheetAsset(@Nonnull String textureFileName) {
		this(
				new TextureAsset(textureFileName),
				new JSONObjectAsset(String.format("%s.json", getBaseFileName(textureFileName)))
		);
	}

	public SpriteSheetAsset(@Nonnull Asset<Texture> textureAsset, @Nonnull Asset<JSONObject> jsonAsset) {
		super(textureAsset, jsonAsset);
		this.textureAsset = textureAsset;
		this.jsonAsset = jsonAsset;
	}

	@Nonnull private static String getBaseFileName(@Nonnull String fileName) {
		String[] split = fileName.split("\\.");
		return RefStreams.of(split).limit(split.length - 1).collect(Collectors.joining("."));
	}

	@Delegate
	@Override
	public SpriteSheet get() {
		return super.get();
	}

	@Override
	public SpriteSheet create() {
		Texture texture = textureAsset.get();
		JSONObject json = jsonAsset.get();

		int columns;
		int rows;

		if (json.containsKey("frames")) {
			JSONList<Integer> jsonFrames = json.getList("frames").ofInts();
			columns = jsonFrames[0];
			rows = jsonFrames[1];
		} else if (json.containsKey("columns") && json.containsKey("rows")) {
			columns = json.getInt("columns");
			rows = json.getInt("rows");
		} else if (json.containsKey("width") && json.containsKey("height")) {
			columns = json.getInt("width");
			rows = json.getInt("height");
		} else if (json.containsKey("x") && json.containsKey("y")) {
			columns = json.getInt("x");
			rows = json.getInt("y");
		} else {
			throw new IllegalArgumentException("Cannot parse SpriteSheet size from JSON.");
		}

		int spacing = json.getInt("spacing", 0);
		int margin = json.getInt("margin", 0);

		return new SpriteSheet(texture, json, columns, rows, spacing, margin);
	}
}
