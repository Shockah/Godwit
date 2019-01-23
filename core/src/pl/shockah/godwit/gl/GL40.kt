package pl.shockah.godwit.gl

@ExperimentalUnsignedTypes
interface GL40Binding<
		State : GL11State,
		PrimitiveType : GL40PrimitiveType,
		TextureTarget : GL40TextureTarget,
		PixelCopyType : GL30PixelCopyType,
		ClientStateCapability : GL15ClientStateCapability,
		DrawBufferMode : GL30DrawBufferMode,
		PixelFormat : GL30PixelFormat
		> : GL32Binding<
		State,
		PrimitiveType,
		TextureTarget,
		PixelCopyType,
		ClientStateCapability,
		DrawBufferMode,
		PixelFormat
		> {
}

open class GL40PrimitiveType internal constructor(
		glConstant: Int
) : GL32PrimitiveType(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL40PrimitiveType(glConstant)

		val patches = new(0xE) // GL_PATCHES
	}
}

open class GL40TextureTarget internal constructor(
		glConstant: Int
) : GL32TextureTarget(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL40TextureTarget(glConstant)

		val textureCubeMapArray = new(0x9009) // GL_TEXTURE_CUBE_MAP_ARRAY
	}
}