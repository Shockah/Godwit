package pl.shockah.godwit.asset;

import javax.annotation.Nonnull;

public class GodwitAssetParameters<T> {
	@Nonnull public final Class<T> clazz;
	@Nonnull public final String path;

	public GodwitAssetParameters(@Nonnull Class<T> clazz, @Nonnull String path) {
		this.clazz = clazz;
		this.path = path;
	}
}