package pl.shockah.godwit.gl

@ExperimentalUnsignedTypes
interface GL13Binding<
		State : GL11State,
		PrimitiveType : GL11PrimitiveType,
		TextureTarget : GL13TextureTarget,
		PixelCopyType : GL11PixelCopyType,
		ClientStateCapability : GL11ClientStateCapability,
		DrawBufferMode : GL11DrawBufferMode,
		PixelFormat : GL12PixelFormat
		> : GL12Binding<
		State,
		PrimitiveType,
		TextureTarget,
		PixelCopyType,
		ClientStateCapability,
		DrawBufferMode,
		PixelFormat
		> {
}

open class GL13TextureTarget internal constructor(
		glConstant: Int
) : GL12TextureTarget(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL13TextureTarget(glConstant)

		val textureCubeMap = new(0x8513) // GL_TEXTURE_CUBE_MAP
	}
}