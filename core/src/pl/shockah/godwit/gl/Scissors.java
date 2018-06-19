package pl.shockah.godwit.gl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.Deque;
import java.util.LinkedList;

import javax.annotation.Nonnull;

import pl.shockah.godwit.geom.Rectangle;
import pl.shockah.godwit.geom.Vec2;

public final class Scissors {
	@Nonnull
	public final Gfx gfx;

	@Nonnull
	private final Deque<Rectangle> scissors = new LinkedList<>();

	public Scissors(@Nonnull Gfx gfx) {
		this.gfx = gfx;
	}

	@Nonnull
	private Rectangle fix(@Nonnull Rectangle scissor) {
		Rectangle result = scissor.copy();

		result.position.set(Math.round(result.position.x), Math.round(result.position.y));
		result.size.set(Math.round(result.size.x), Math.round(result.size.y));

		if (result.size.x < 0f) {
			result.size.x = -result.size.x;
			result.position.x -= result.size.x;
		}
		if (result.size.y < 0f) {
			result.size.y = -result.size.y;
			result.position.y -= result.size.y;
		}

		return result;
	}

	public void push(@Nonnull Rectangle scissor, @Nonnull Camera camera) {
		Vector3 project;
		Rectangle projected = new Rectangle();

		project = camera.project(new Vector3(scissor.position.x, scissor.position.y, 0f));
		projected.position.set(project.x, project.y + 1);
		project = camera.project(new Vector3(scissor.position.x + scissor.size.x, scissor.position.y + scissor.size.y, 0f));
		projected.size.set(project.x - projected.position.x, project.y - projected.position.y + 1);

		push(projected);
	}

	public void push(@Nonnull Rectangle scissor) {
		scissors.push(fix(scissor));
		recalculate();
	}

	public void pop() {
		scissors.pop();
		recalculate();
	}

	private void recalculate() {
		gfx.flush();
		if (scissors.isEmpty()) {
			Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
		} else {
			Rectangle calculated = new Rectangle(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
			for (Rectangle scissor : scissors) {
				if (calculated.size.x == Float.POSITIVE_INFINITY) {
					calculated.position.set(scissor.position);
					calculated.size.set(scissor.size);
				} else if (scissor.collides(calculated)) {
					float newX1 = Math.max(calculated.position.x, scissor.position.x);
					float newY1 = Math.max(calculated.position.y, scissor.position.y);
					float newX2 = Math.min(calculated.position.x + calculated.size.x, scissor.position.x + scissor.size.x);
					float newY2 = Math.min(calculated.position.y + calculated.size.y, scissor.position.y + scissor.size.y);
					calculated.position.set(newX1, newY1);
					calculated.size.set(newX2 - newX1, newY2 - newY1);
				} else {
					calculated.size.set(Vec2.zero);
					break;
				}
			}
			HdpiUtils.glScissor((int)calculated.position.x, (int)calculated.position.y, (int)calculated.size.x, (int)calculated.size.y);
			Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
		}
	}
}