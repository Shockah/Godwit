package pl.shockah.godwit.ui

import groovy.transform.CompileStatic
import pl.shockah.godwit.geom.IVec2
import pl.shockah.godwit.geom.ImmutableVec2

import javax.annotation.Nonnull

@CompileStatic
enum WritingDirection {
	LeftToRightAndTopToBottom(new ImmutableVec2(1, 0), new ImmutableVec2(0, 1)),
	RightToLeftAndTopToBottom(new ImmutableVec2(-1, 0), new ImmutableVec2(0, 1)),
	LeftToRightAndBottomToTop(new ImmutableVec2(1, 0), new ImmutableVec2(0, -1)),
	RightToLeftAndBottomToTop(new ImmutableVec2(-1, 0), new ImmutableVec2(0, -1)),
	TopToBottomAndLeftToRight(new ImmutableVec2(0, 1), new ImmutableVec2(1, 0)),
	BottomToTopAndLeftToRight(new ImmutableVec2(0, -1), new ImmutableVec2(1, 0)),
	TopToBottomAndRightToLeft(new ImmutableVec2(0, 1), new ImmutableVec2(-1, 0)),
	BottomToTopAndRightToLeft(new ImmutableVec2(0, -1), new ImmutableVec2(-1, 0))

	@Nonnull final IVec2 rowVector
	@Nonnull final IVec2 columnVector

	private WritingDirection(@Nonnull IVec2 rowVector, @Nonnull IVec2 columnVector) {
		this.rowVector = rowVector
		this.columnVector = columnVector
	}

	@Nonnull IVec2 getCorner() {
		return new ImmutableVec2(isLeftToRight() ? 0 : 1, isTopToBottom() ? 0 : 1)
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