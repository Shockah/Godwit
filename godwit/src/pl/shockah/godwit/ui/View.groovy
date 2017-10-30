package pl.shockah.godwit.ui

import com.badlogic.gdx.graphics.Color
import groovy.transform.CompileStatic
import pl.shockah.godwit.Renderable
import pl.shockah.godwit.geom.Rectangle
import pl.shockah.godwit.geom.Vec2
import pl.shockah.godwit.gl.Gfx

import javax.annotation.Nonnull
import javax.annotation.Nullable

@CompileStatic
class View implements Renderable {
	@Nullable ViewHolder parent = null
	@Nullable Color backgroundColor = null
	@Nonnull Rectangle bounds = new Rectangle(0, 0)

	void onUpdate() {
	}

	void onLayout() {
	}

	@Override
	void onRender(@Nonnull Gfx gfx) {
		if (backgroundColor) {
			gfx.color = backgroundColor
			gfx.drawFilled(bounds)
		}
	}

	@Nonnull Vec2 getIntrinsicSize(@Nonnull Vec2 availableSize) {
		return new Vec2()
	}

	void removeFromParent() {
		assert parent != null
		parent.remove(this)
	}

	static enum Orientation {
		Horizontal(new Vec2(1, 0)),
		Vertical(new Vec2(0, 1))

		@Nonnull final Vec2 vector

		private Orientation(@Nonnull Vec2 vector) {
			this.vector = vector
		}

		@Nonnull Orientation getPerpendicular() {
			switch (this) {
				case Horizontal:
					return Vertical
				case Vertical:
					return Horizontal
			}
			throw new IllegalStateException()
		}
	}

	static enum WritingDirection {
		LeftToRightAndTopToBottom(new Vec2(1, 0), new Vec2(0, 1)),
		RightToLeftAndTopToBottom(new Vec2(-1, 0), new Vec2(0, 1)),
		LeftToRightAndBottomToTop(new Vec2(1, 0), new Vec2(0, -1)),
		RightToLeftAndBottomToTop(new Vec2(-1, 0), new Vec2(0, 1)),
		TopToBottomAndLeftToRight(new Vec2(0, 1), new Vec2(1, 0)),
		BottomToTopAndLeftToRight(new Vec2(0, -1), new Vec2(1, 0)),
		TopToBottomAndRightToLeft(new Vec2(0, 1), new Vec2(-1, 0)),
		BottomToTopAndRightToLeft(new Vec2(0, -1), new Vec2(-1, 0))

		@Nonnull final Vec2 rowVector
		@Nonnull final Vec2 columnVector

		private WritingDirection(@Nonnull Vec2 rowVector, @Nonnull Vec2 columnVector) {
			this.rowVector = rowVector
			this.columnVector = columnVector
		}

		boolean isLeftToRight() {
			return this in [
					LeftToRightAndTopToBottom, LeftToRightAndBottomToTop,
					TopToBottomAndLeftToRight, BottomToTopAndLeftToRight
			]
		}

		boolean isRightToLeft() {
			return !leftToRight
		}

		boolean isTopToBottom() {
			return this in [
					LeftToRightAndTopToBottom, RightToLeftAndTopToBottom,
					TopToBottomAndLeftToRight, TopToBottomAndRightToLeft
			]
		}

		boolean isBottomToTop() {
			return !topToBottom
		}

		boolean isHorizontalFirst() {
			return this in [
					LeftToRightAndTopToBottom, LeftToRightAndBottomToTop,
					RightToLeftAndTopToBottom, RightToLeftAndBottomToTop
			]
		}

		boolean isVerticalFirst() {
			return !horizontalFirst
		}

		@Nonnull WritingDirection getHorizontallyMirrored() {
			switch (this) {
				case LeftToRightAndTopToBottom:
					return RightToLeftAndTopToBottom
				case RightToLeftAndTopToBottom:
					return LeftToRightAndTopToBottom
				case LeftToRightAndBottomToTop:
					return RightToLeftAndBottomToTop
				case RightToLeftAndBottomToTop:
					return LeftToRightAndBottomToTop
				case TopToBottomAndLeftToRight:
					return TopToBottomAndRightToLeft
				case BottomToTopAndLeftToRight:
					return BottomToTopAndRightToLeft
				case TopToBottomAndRightToLeft:
					return TopToBottomAndLeftToRight
				case BottomToTopAndRightToLeft:
					return BottomToTopAndLeftToRight
			}
			throw new IllegalStateException()
		}

		@Nonnull WritingDirection getVerticallyMirrored() {
			switch (this) {
				case LeftToRightAndTopToBottom:
					return LeftToRightAndBottomToTop
				case RightToLeftAndTopToBottom:
					return RightToLeftAndBottomToTop
				case LeftToRightAndBottomToTop:
					return LeftToRightAndTopToBottom
				case RightToLeftAndBottomToTop:
					return RightToLeftAndTopToBottom
				case TopToBottomAndLeftToRight:
					return BottomToTopAndLeftToRight
				case BottomToTopAndLeftToRight:
					return TopToBottomAndLeftToRight
				case TopToBottomAndRightToLeft:
					return BottomToTopAndRightToLeft
				case BottomToTopAndRightToLeft:
					return TopToBottomAndRightToLeft
			}
			throw new IllegalStateException()
		}

		@Nonnull WritingDirection getMirrored() {
			return horizontallyMirrored.verticallyMirrored
		}
	}
}