package pl.shockah.godwit.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.JsonReader
import groovy.transform.CompileStatic
import pl.shockah.godwit.gl.SpriteSheet
import pl.shockah.godwit.util.JSON

@CompileStatic
class SpriteSheetLoader extends SynchronousAssetLoader<SpriteSheet, SpriteSheetParameter> {
	SpriteSheetLoader(FileHandleResolver resolver) {
		super(resolver)
	}

	@Override
	Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SpriteSheetParameter parameter) {
		String textureFileName = "${fileName.split("\\.").dropRight(1).join(".")}.png"
		Array<AssetDescriptor> dependencies = new Array<>()
		dependencies.add(new AssetDescriptor(textureFileName, Texture.class, new TextureLoader.TextureParameter()))
		return dependencies
	}

	@Override
	SpriteSheet load(AssetManager assetManager, String fileName, FileHandle file, SpriteSheetParameter parameter) {
		JSON json = new JSON(new JsonReader().parse(file))

		int columns
		int rows
		if (json.has("frames")) {
			JSON jsonFrames = json["frames"]
			assert jsonFrames.isArray()
			columns = jsonFrames[0].asInt()
			rows = jsonFrames[0].asInt()
		} else if (json.has("columns") && json.has("rows")) {
			columns = json["columns"].asInt()
			rows = json["rows"].asInt()
		} else if (json.has("width") && json.has("height")) {
			columns = json["width"].asInt()
			rows = json["height"].asInt()
		} else if (json.has("x") && json.has("y")) {
			columns = json["x"].asInt()
			rows = json["y"].asInt()
		} else {
			throw new IllegalArgumentException()
		}

		int spacing = json.has("spacing") && json["spacing"].isNumber() ? json["spacing"].asInt() : 0
		int margin = json.has("margin") && json["margin"].isNumber() ? json["margin"].asInt() : 0

		String textureFileName = "${fileName.split("\\.").dropRight(1).join(".")}.png"
		Texture texture = assetManager.get(textureFileName, Texture.class)
		return new SpriteSheet(texture, columns, rows, spacing, margin)
	}

	static class SpriteSheetParameter extends AssetLoaderParameters<SpriteSheet> {
	}
}