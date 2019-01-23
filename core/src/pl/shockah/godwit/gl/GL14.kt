package pl.shockah.godwit.gl

@ExperimentalUnsignedTypes
interface GL14Binding<
		State : GL11State,
		PrimitiveType : GL11PrimitiveType,
		TextureTarget : GL13TextureTarget,
		PixelCopyType : GL11PixelCopyType,
		ClientStateCapability : GL14ClientStateCapability,
		DrawBufferMode : GL11DrawBufferMode,
		PixelFormat : GL12PixelFormat
		> : GL13Binding<
		State,
		PrimitiveType,
		TextureTarget,
		PixelCopyType,
		ClientStateCapability,
		DrawBufferMode,
		PixelFormat
		> {
}

open class GL14ClientStateCapability internal constructor(
		glConstant: Int
) : GL11ClientStateCapability(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL14ClientStateCapability(glConstant)

		val secondaryColorArray = new(0x845E) // GL_SECONDARY_COLOR_ARRAY
	}
}