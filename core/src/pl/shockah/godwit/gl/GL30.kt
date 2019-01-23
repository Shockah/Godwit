package pl.shockah.godwit.gl

@ExperimentalUnsignedTypes
interface GL30Binding<
		State : GL11State,
		PrimitiveType : GL11PrimitiveType,
		TextureTarget : GL30TextureTarget,
		PixelCopyType : GL30PixelCopyType,
		ClientStateCapability : GL15ClientStateCapability,
		DrawBufferMode : GL30DrawBufferMode,
		PixelFormat : GL30PixelFormat
		> : GL15Binding<
		State,
		PrimitiveType,
		TextureTarget,
		PixelCopyType,
		ClientStateCapability,
		DrawBufferMode,
		PixelFormat
		> {
}

open class GL30TextureTarget internal constructor(
		glConstant: Int
) : GL13TextureTarget(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL30TextureTarget(glConstant)

		val texture1DArray = new(0x8C18) // GL_TEXTURE_1D_ARRAY
		val texture2DArray = new(0x8C1A) // GL_TEXTURE_2D_ARRAY
	}
}

open class GL30PixelCopyType internal constructor(
		glConstant: Int
) : GL11PixelCopyType(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL30TextureTarget(glConstant)

		val depthStencil = new(0x84F9) // GL_DEPTH_STENCIL
	}
}

open class GL30DrawBufferMode internal constructor(
		glConstant: Int
) : GL11DrawBufferMode(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL30DrawBufferMode(glConstant)

		val colorAttachments = Array(32) { new(0x8CE0 + it) } // GL_COLOR_ATTACHMENT0-31
	}
}

open class GL30PixelFormat internal constructor(
		glConstant: Int
) : GL12PixelFormat(glConstant) {
	companion object {
		private fun new(glConstant: Int) = GL30PixelFormat(glConstant)

		val rg = new(0x8227) // GL_RG
		val redInteger = new(0x8D94) // GL_RED_INTEGER
		val greenInteger = new(0x8D95) // GL_GREEN_INTEGER
		val blueInteger = new(0x8D96) // GL_BLUE_INTEGER
		val alphaInteger = new(0x8D97) // GL_ALPHA_INTEGER
		val rgInteger = new(0x8228) // GL_RG_INTEGER
		val rgbInteger = new(0x8D98) // GL_RGB_INTEGER
		val rgbaInteger = new(0x8D99) // GL_RGBA_INTEGER
		val bgrInteger = new(0x8D9A) // GL_BGR_INTEGER
		val bgraInteger = new(0x8D9B) // GL_BGRA_INTEGER
		val depthStencil = new(0x84F9) // GL_DEPTH_STENCIL
	}
}