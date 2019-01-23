package pl.shockah.godwit.gl

@ExperimentalUnsignedTypes
interface GL31Binding<
		State : GL11State,
		PrimitiveType : GL11PrimitiveType,
		TextureTarget : GL31TextureTarget,
		PixelCopyType : GL30PixelCopyType,
		ClientStateCapability : GL15ClientStateCapability,
		DrawBufferMode : GL30DrawBufferMode,
		PixelFormat : GL30PixelFormat
		> : GL30Binding<
		State,
		PrimitiveType,
		TextureTarget,
		PixelCopyType,
		ClientStateCapability,
		DrawBufferMode,
		PixelFormat
		> {
}

open class GL31TextureTarget internal constructor(
		glConstant: Int
) : GL30TextureTarget(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL31TextureTarget(glConstant)

		val textureRectangle = new(0x84F5) // GL_TEXTURE_RECTANGLE
		val textureBuffer = new(0x8C2A) // GL_TEXTURE_BUFFER
	}
}