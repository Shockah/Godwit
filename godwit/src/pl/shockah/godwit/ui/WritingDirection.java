package pl.shockah.godwit.ui;

import javax.annotation.Nonnull;

import java8.util.Lists;
import pl.shockah.godwit.geom.IVec2;
import pl.shockah.godwit.geom.Vec2;

public enum WritingDirection {
	LeftToRightAndTopToBottom(new Vec2(1, 0), new Vec2(0, 1)),
	RightToLeftAndTopToBottom(new Vec2(-1, 0), new Vec2(0, 1)),
	LeftToRightAndBottomToTop(new Vec2(1, 0), new Vec2(0, -1)),
	RightToLeftAndBottomToTop(new Vec2(-1, 0), new Vec2(0, -1)),
	TopToBottomAndLeftToRight(new Vec2(0, 1), new Vec2(1, 0)),
	BottomToTopAndLeftToRight(new Vec2(0, -1), new Vec2(1, 0)),
	TopToBottomAndRightToLeft(new Vec2(0, 1), new Vec2(-1, 0)),
	BottomToTopAndRightToLeft(new Vec2(0, -1), new Vec2(-1, 0));

	@Nonnull public final IVec2 rowVector;
	@Nonnull public final IVec2 columnVector;

	WritingDirection(@Nonnull IVec2 rowVector, @Nonnull IVec2 columnVector) {
		this.rowVector = rowVector;
		this.columnVector = columnVector;
	}

	@Nonnull public IVec2 getCorner() {
		return new Vec2(isLeftToRight() ? 0 : 1, isTopToBottom() ? 0 : 1);
	}

	public boolean isLeftToRight() {
		return Lists.of(
				LeftToRightAndTopToBottom, LeftToRightAndBottomToTop,
				TopToBottomAndLeftToRight, BottomToTopAndLeftToRight
		).contains(this);
	}

	public boolean isRightToLeft() {
		return !isLeftToRight();
	}

	public boolean isTopToBottom() {
		return Lists.of(
				LeftToRightAndTopToBottom, RightToLeftAndTopToBottom,
				TopToBottomAndLeftToRight, TopToBottomAndRightToLeft
		).contains(this);
	}

	public boolean isBottomToTop() {
		return !isTopToBottom();
	}

	public boolean isHorizontalFirst() {
		return Lists.of(
				LeftToRightAndTopToBottom, LeftToRightAndBottomToTop,
				RightToLeftAndTopToBottom, RightToLeftAndBottomToTop
		).contains(this);
	}

	public boolean isVerticalFirst() {
		return !isHorizontalFirst();
	}

	@Nonnull public WritingDirection getHorizontallyMirrored() {
		switch (this) {
			case LeftToRightAndTopToBottom:
				return RightToLeftAndTopToBottom;
			case RightToLeftAndTopToBottom:
				return LeftToRightAndTopToBottom;
			case LeftToRightAndBottomToTop:
				return RightToLeftAndBottomToTop;
			case RightToLeftAndBottomToTop:
				return LeftToRightAndBottomToTop;
			case TopToBottomAndLeftToRight:
				return TopToBottomAndRightToLeft;
			case BottomToTopAndLeftToRight:
				return BottomToTopAndRightToLeft;
			case TopToBottomAndRightToLeft:
				return TopToBottomAndLeftToRight;
			case BottomToTopAndRightToLeft:
				return BottomToTopAndLeftToRight;
		}
		throw new IllegalStateException();
	}

	@Nonnull public WritingDirection getVerticallyMirrored() {
		switch (this) {
			case LeftToRightAndTopToBottom:
				return LeftToRightAndBottomToTop;
			case RightToLeftAndTopToBottom:
				return RightToLeftAndBottomToTop;
			case LeftToRightAndBottomToTop:
				return LeftToRightAndTopToBottom;
			case RightToLeftAndBottomToTop:
				return RightToLeftAndTopToBottom;
			case TopToBottomAndLeftToRight:
				return BottomToTopAndLeftToRight;
			case BottomToTopAndLeftToRight:
				return TopToBottomAndLeftToRight;
			case TopToBottomAndRightToLeft:
				return BottomToTopAndRightToLeft;
			case BottomToTopAndRightToLeft:
				return TopToBottomAndRightToLeft;
		}
		throw new IllegalStateException();
	}

	@Nonnull public WritingDirection getMirrored() {
		return getHorizontallyMirrored().getVerticallyMirrored();
	}
}