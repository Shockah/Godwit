package pl.shockah.godwit.gl

@ExperimentalUnsignedTypes
interface GL15Binding<
		State : GL11State,
		PrimitiveType : GL11PrimitiveType,
		TextureTarget : GL13TextureTarget,
		PixelCopyType : GL11PixelCopyType,
		ClientStateCapability : GL15ClientStateCapability,
		DrawBufferMode : GL11DrawBufferMode,
		PixelFormat : GL12PixelFormat
		> : GL14Binding<
		State,
		PrimitiveType,
		TextureTarget,
		PixelCopyType,
		ClientStateCapability,
		DrawBufferMode,
		PixelFormat
		> {
}

open class GL15ClientStateCapability internal constructor(
		glConstant: Int
) : GL14ClientStateCapability(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL14ClientStateCapability(glConstant)

		val fogCoordArray = new(0x8457) // GL_FOG_COORD_ARRAY
	}
}