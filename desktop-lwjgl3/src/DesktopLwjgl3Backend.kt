package pl.shockah.godwit.desktop.lwjgl3

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import pl.shockah.godwit.Backend
import pl.shockah.godwit.geom.Vec2

class DesktopLwjgl3Backend(
		var size: Vec2,
		var title: String? = null
) : Backend {
	private var windowHandle: Long? = null

	override fun init() {
		if (!glfwInit())
			throw IllegalStateException("Unable to initialize GLFW")

		windowHandle = glfwCreateWindow(size.x.toInt(), size.y.toInt(), title ?: "", NULL, NULL)
		if (windowHandle == NULL)
			throw IllegalStateException("Failed to create the GLFW window");

		windowHandle?.let { windowHandle ->
			glfwMakeContextCurrent(windowHandle)
			glfwShowWindow(windowHandle)
		}
	}

	override fun dispose() {
		windowHandle?.let { windowHandle ->
			glfwFreeCallbacks(windowHandle)
			glfwDestroyWindow(windowHandle)
		}
	}

	override fun pause() {
	}

	override fun resume() {
	}

	override fun loop() {
		windowHandle?.let { windowHandle ->
			GL.createCapabilities()
			GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

			while (!glfwWindowShouldClose(windowHandle)) {
				glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
				glfwSwapBuffers(windowHandle)
				glfwPollEvents()
			}
		}
	}

	override fun update() {
	}

	override fun render() {
	}
}