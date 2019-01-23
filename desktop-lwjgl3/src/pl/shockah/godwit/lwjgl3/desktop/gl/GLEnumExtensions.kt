package pl.shockah.godwit.lwjgl3.desktop.gl

import org.lwjgl.opengl.ARBIndirectParameters
import org.lwjgl.opengl.GL43.*
import pl.shockah.godwit.gl.raw.*

val GLShaderType.glConstant: Int
	get() = when (this) {
		GLShaderType.Vertex -> GL_VERTEX_SHADER
		GLShaderType.Fragment -> GL_FRAGMENT_SHADER
		GLShaderType.Geometry -> GL_GEOMETRY_SHADER
		GLShaderType.TessellationControl -> GL_TESS_CONTROL_SHADER
		GLShaderType.TessellationEvaluation -> GL_TESS_EVALUATION_SHADER
	}

val GLBufferTarget.glConstant: Int
	get() = when (this) {
		GLBufferTarget.Array -> GL_ARRAY_BUFFER
		GLBufferTarget.ElementArray -> GL_ELEMENT_ARRAY_BUFFER
		GLBufferTarget.PixelPack -> GL_PIXEL_PACK_BUFFER
		GLBufferTarget.PixelUnpack -> GL_PIXEL_UNPACK_BUFFER
		GLBufferTarget.TransformFeedback -> GL_TRANSFORM_FEEDBACK_BUFFER
		GLBufferTarget.Uniform -> GL_UNIFORM_BUFFER
		GLBufferTarget.Texture -> GL_TEXTURE_BUFFER
		GLBufferTarget.CopyRead -> GL_COPY_READ_BUFFER
		GLBufferTarget.CopyWrite -> GL_COPY_WRITE_BUFFER
		GLBufferTarget.DrawIndirect -> GL_DRAW_INDIRECT_BUFFER
		GLBufferTarget.AtomicCounter -> GL_ATOMIC_COUNTER_BUFFER
		GLBufferTarget.DispatchIndirect -> GL_DISPATCH_INDIRECT_BUFFER
		GLBufferTarget.ShaderStorage -> GL_SHADER_STORAGE_BUFFER
		GLBufferTarget.ARBParameter -> ARBIndirectParameters.GL_PARAMETER_BUFFER_ARB
	}

val GLBufferDataUsage.glConstant: Int
	get() = when (this) {
		GLBufferDataUsage.StreamDraw -> GL_STREAM_DRAW
		GLBufferDataUsage.StreamRead -> GL_STREAM_READ
		GLBufferDataUsage.StreamCopy -> GL_STREAM_COPY
		GLBufferDataUsage.StaticDraw -> GL_STATIC_DRAW
		GLBufferDataUsage.StaticRead -> GL_STATIC_READ
		GLBufferDataUsage.StaticCopy -> GL_STATIC_COPY
		GLBufferDataUsage.DynamicDraw -> GL_DYNAMIC_DRAW
		GLBufferDataUsage.DynamicRead -> GL_DYNAMIC_READ
		GLBufferDataUsage.DynamicCopy -> GL_DYNAMIC_COPY
	}

val GLType.glConstant: Int
	get() = when (this) {
		GLType.Byte -> GL_BYTE
		GLType.UnsignedByte -> GL_UNSIGNED_BYTE
		GLType.Short -> GL_SHORT
		GLType.UnsignedShort -> GL_UNSIGNED_SHORT
		GLType.Int -> GL_INT
		GLType.UnsignedInt -> GL_UNSIGNED_INT
		GLType.HalfFloat -> GL_HALF_FLOAT
		GLType.Float -> GL_FLOAT
		GLType.Double -> GL_DOUBLE
		GLType.UnsignedInt_2_10_10_10_Reversed -> GL_UNSIGNED_INT_2_10_10_10_REV
		GLType.Int_2_10_10_10_Reversed -> GL_INT_2_10_10_10_REV
		GLType.Fixed -> GL_FIXED
	}

val GLDrawMode.glConstant: Int
	get() = when (this) {
		GLDrawMode.Points -> GL_POINTS
		GLDrawMode.LineStrip -> GL_LINE_STRIP
		GLDrawMode.LineLoop -> GL_LINE_LOOP
		GLDrawMode.Lines -> GL_LINES
		GLDrawMode.LineStripAdjacency -> GL_LINE_STRIP_ADJACENCY
		GLDrawMode.LinesAdjacency -> GL_LINES_ADJACENCY
		GLDrawMode.TriangleStrip -> GL_TRIANGLE_STRIP
		GLDrawMode.TriangleFan -> GL_TRIANGLE_FAN
		GLDrawMode.Triangles -> GL_TRIANGLES
		GLDrawMode.TriangleStripAdjacency -> GL_TRIANGLE_STRIP_ADJACENCY
		GLDrawMode.TrianglesAdjacency -> GL_TRIANGLES_ADJACENCY
		GLDrawMode.Patches -> GL_PATCHES
	}