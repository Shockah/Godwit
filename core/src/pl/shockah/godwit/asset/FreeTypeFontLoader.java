package pl.shockah.godwit.asset;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;

import javax.annotation.Nonnull;

import pl.shockah.godwit.Godwit;

public class FreeTypeFontLoader extends SynchronousAssetLoader<BitmapFont, FreeTypeFontLoader.FreeTypeFontParameter> {
	public FreeTypeFontLoader(FileHandleResolver resolver) {
		super(resolver);
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, FreeTypeFontParameter parameter) {
		return null;
	}

	@Override
	public BitmapFont load(AssetManager assetManager, String fileName, FileHandle file, FreeTypeFontParameter parameter) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
		FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
		param.size = parameter.size;
		param.mono = parameter.mono;
		param.hinting = parameter.hinting;
		param.color = parameter.color;
		param.gamma = parameter.gamma;
		param.renderCount = parameter.renderCount;
		param.borderWidth = parameter.borderWidth;
		param.borderColor = parameter.borderColor;
		param.borderStraight = parameter.borderStraight;
		param.borderGamma = parameter.borderGamma;
		param.shadowOffsetX = parameter.shadowOffsetX;
		param.shadowOffsetY = parameter.shadowOffsetY;
		param.shadowColor = parameter.shadowColor;
		param.spaceX = parameter.spaceX;
		param.spaceY = parameter.spaceY;
		param.characters = parameter.characters;
		param.kerning = parameter.kerning;
		param.flip = Godwit.getInstance().yPointingDown != parameter.flip;
		param.genMipMaps = parameter.genMipMaps;
		param.minFilter = parameter.minFilter;
		param.magFilter = parameter.magFilter;
		BitmapFont font = generator.generateFont(param);
		generator.dispose();
		return font;
	}

	public static class FreeTypeFontParameter extends AssetLoaderParameters<BitmapFont> {
		public int size = 12;
		public boolean mono = false;
		@Nonnull public FreeTypeFontGenerator.Hinting hinting = FreeTypeFontGenerator.Hinting.AutoMedium;
		@Nonnull public Color color = Color.WHITE;
		public float gamma = 1.8f;
		public int renderCount = 2;
		public float borderWidth = 0;
		@Nonnull public Color borderColor = Color.BLACK;
		public boolean borderStraight = false;
		public float borderGamma = 1.8f;
		public int shadowOffsetX = 0;
		public int shadowOffsetY = 0;
		@Nonnull public Color shadowColor = new Color(0, 0, 0, 0.75f);
		public int spaceX;
		public int spaceY;
		@Nonnull public String characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		public boolean kerning = true;
		public boolean flip = false;
		public boolean genMipMaps = false;
		@Nonnull public Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;
		@Nonnull public Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;
	}
}