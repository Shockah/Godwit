package pl.shockah.godwit.gl

@ExperimentalUnsignedTypes
interface GL12Binding<
		State : GL11State,
		PrimitiveType : GL11PrimitiveType,
		TextureTarget : GL12TextureTarget,
		PixelCopyType : GL11PixelCopyType,
		ClientStateCapability : GL11ClientStateCapability,
		DrawBufferMode : GL11DrawBufferMode,
		PixelFormat : GL12PixelFormat
		> : GL11Binding<
		State,
		PrimitiveType,
		TextureTarget,
		PixelCopyType,
		ClientStateCapability,
		DrawBufferMode,
		PixelFormat
		> {
}

open class GL12TextureTarget internal constructor(
		glConstant: Int
) : GL11TextureTarget(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL12TextureTarget(glConstant)

		val texture3D = new(0x806F) // GL_TEXTURE_3D
	}
}

open class GL12PixelFormat internal constructor(
		glConstant: Int
) : GL11PixelFormat(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL12PixelFormat(glConstant)

		val bgr = new(0x80E0) // GL_BGR
		val bgra = new(0x80E1) // GL_BGRA
	}
}