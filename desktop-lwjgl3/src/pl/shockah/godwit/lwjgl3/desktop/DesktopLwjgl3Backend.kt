package pl.shockah.godwit.lwjgl3.desktop

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import pl.shockah.godwit.Backend
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.gl.GLBinding

class DesktopLwjgl3Backend(
		var size: Vec2,
		var title: String? = null
) : Backend {
	override val glBinding: GLBinding by lazy {
		DesktopLwjgl3GLBinding()
	}

	private var windowHandle: Long? = null

	override fun init() {
		if (!glfwInit())
			throw IllegalStateException("Unable to initialize GLFW")

		windowHandle = glfwCreateWindow(size.x.toInt(), size.y.toInt(), title ?: "", NULL, NULL)
		if (windowHandle == NULL)
			throw IllegalStateException("Failed to create the GLFW window");

		windowHandle?.apply {
			glfwMakeContextCurrent(this)
			glfwShowWindow(this)
		}
	}

	override fun dispose() {
		windowHandle?.apply {
			glfwFreeCallbacks(this)
			glfwDestroyWindow(this)
		}
	}

	override fun pause() {
	}

	override fun resume() {
	}

	override fun loop() {
		windowHandle?.apply {
			GL.createCapabilities()
			GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

			while (!glfwWindowShouldClose(this)) {
				glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
				glfwSwapBuffers(this)
				glfwPollEvents()
			}
		}
	}

	override fun update() {
	}

	override fun render() {
	}
}