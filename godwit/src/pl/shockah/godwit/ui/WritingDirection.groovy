package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.Vec2

import javax.annotation.Nonnull

@CompileStatic
enum WritingDirection {
	LeftToRightAndTopToBottom(new Vec2(1, 0), new Vec2(0, 1), new Vec2(0, 0)),
	RightToLeftAndTopToBottom(new Vec2(-1, 0), new Vec2(0, 1), new Vec2(1, 0)),
	LeftToRightAndBottomToTop(new Vec2(1, 0), new Vec2(0, -1), new Vec2(0, 1)),
	RightToLeftAndBottomToTop(new Vec2(-1, 0), new Vec2(0, -1), new Vec2(1, 1)),
	TopToBottomAndLeftToRight(new Vec2(0, 1), new Vec2(1, 0), new Vec2(0, 0)),
	BottomToTopAndLeftToRight(new Vec2(0, -1), new Vec2(1, 0), new Vec2(0, 1)),
	TopToBottomAndRightToLeft(new Vec2(0, 1), new Vec2(-1, 0), new Vec2(1, 0)),
	BottomToTopAndRightToLeft(new Vec2(0, -1), new Vec2(-1, 0), new Vec2(1, 1))

	@Nonnull final Vec2 rowVector
	@Nonnull final Vec2 columnVector
	@Nonnull final Vec2 corner

	private WritingDirection(@Nonnull Vec2 rowVector, @Nonnull Vec2 columnVector, @Nonnull Vec2 corner) {
		this.rowVector = rowVector
		this.columnVector = columnVector
		this.corner = corner
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