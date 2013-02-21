/**
 * File: BoxViewFrameLayout.java
 * Creator: Timon.Trinh (timon@gkxim.com)
 * Date: 21-11-2012
 * 
 */
package com.gkxim.timon.widget;

import com.gkxim.timon.labs.R;
import com.gkxim.timon.utils.MyLogger;
import com.gkxim.timon.utils.UIUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

/**
 *
 */
public class BoxViewFrameLayout extends FrameLayout {

	private static final int DEFINE_BOX_COL_MASK = 0x0F;
	private static final int DEFINE_BOX_ROW_MASK = 0xF0;
	private static final int DEFINE_BOX_ELEMENT_RULES_STRETCH = 0;
	private static final int DEFINE_BOX_ELEMENT_RULES_LEFT_TOP = 1;
	private static final int DEFINE_BOX_ELEMENT_RULES_RIGHT_TOP = 2;
	private static final int DEFINE_BOX_ELEMENT_RULES_LEFT_BOTTOM = 3;
	private static final int DEFINE_BOX_ELEMENT_RULES_RIGHT_BOTTOM = 4;
	private static final int DEFINE_BOX_ELEMENT_RULES_OVERLAP_TOP = 5;
	private static final int DEFINE_BOX_ELEMENT_RULES_OVERLAP_BOTTOM = 6;
	private static final int DEFINE_BOX_ELEMENT_RULES_OVERLAP_LEFT = 7;
	private static final int DEFINE_BOX_ELEMENT_RULES_OVERLAP_RIGHT = 8;

	private static final String TAG = "BoxViewFrameLayout";
	private static final int BOX_SIZE = 150;
	private static final int BOXES_MIDDLE_PADDING = 5;
	private static final boolean BOX_HAS_TOUCH_HIGHLIGHT = true;

	private int mBoxCellSize = -1;
	private int mNumberOfColumn = 1;
	private int mNumberOfRow = 1;

	private TextView mTitle = null;
	private ImageView mImage = null;
	private TextView mShortContent = null;
	private boolean mNeedBackground = false;
	private int mMultiPaddingNum = 1;
	private Drawable mForegroundDrawable;
	private Rect mCachedBounds = new Rect();

	/**
	 * 21-11-2012
	 */
	public BoxViewFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initBoxView();
	}

	/**
	 * 21-11-2012
	 */
	public BoxViewFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initBoxView();
	}

	/**
	 * 21-11-2012
	 */
	public BoxViewFrameLayout(Context context) {
		super(context);
		initBoxView();
	}

	private void initBoxView() {
		mBoxCellSize = BOX_SIZE;
//		if (mBoxCellSize < 0) {
//			mBoxCellSize = this.getContext().getResources()
//					.getInteger(R.integer.boxcellsize);
//		}
		// Retrieve the drawable resource assigned to the android.R.attr.selectableItemBackground
        // theme attribute from the current theme.
		if (UIUtils.hasHoneycomb()) {
			TypedArray a = getContext()
	                .obtainStyledAttributes(new int[]{android.R.attr.selectableItemBackground});
	        mForegroundDrawable = a.getDrawable(0);	
	        a.recycle();
		}else {
			mForegroundDrawable = new ColorDrawable(Color.CYAN);
		}
		inflateBoxView();
	}

	private void inflateBoxView() {
		inflate(this.getContext(), R.layout.boxview, this);
		this.setAlwaysDrawnWithCacheEnabled(true);
		mTitle = (TextView) this.findViewById(R.id.boxview_title);
	}

	public int getNumberOfColumSpan() {
		return mNumberOfColumn;
	}

	public int getNumberOfRowSpan() {
		return mNumberOfRow;
	}

	public int getBoxSupposeWidth() {
		int padding = getResources().getInteger(BOXES_MIDDLE_PADDING);
		return (mBoxCellSize * mNumberOfColumn + mMultiPaddingNum
				* (mNumberOfColumn - 1) * padding);
	}

	public int getBoxSupposeHeight() {
		return (mBoxCellSize * mNumberOfRow + mMultiPaddingNum
				* (mNumberOfRow - 1) * getPaddingTop());
	}

	public boolean isNeedGardientBackground() {
		return mNeedBackground;
	}

	public boolean isBGSection() {
		BoxStory tag = (BoxStory) getTag();
		if (tag != null) {
			return tag.isBGOnSection();
		}
		return false;
	}

	public String getBoxIndex() {
		BoxStory tag = (BoxStory) getTag();
		if (tag != null) {
			return String.valueOf(tag.getBoxIndex());
		}
		return "";
	}

	public String getStoryId() {
		BoxStory tag = (BoxStory) getTag();
		if (tag != null) {
			return tag.getStoryId();
		}
		return "";
	}

	public String getSectionRefId() {
		BoxStory tag = (BoxStory) getTag();
		if (tag != null) {
			return tag.getSectionRefId();
		}
		return "";
	}

	public void setBoxStory(BoxStory boxStory) {
		MyLogger.lf(this.getContext(), 0, TAG + "=>setBoxStory.");
		if (boxStory != null) {
			int storyLayout = boxStory.getLayout();
			mNumberOfColumn = storyLayout & DEFINE_BOX_COL_MASK;
			mNumberOfRow = ((storyLayout & DEFINE_BOX_ROW_MASK) >> 4);
			boolean hasImage = false;
			boolean hasTitle = false;
			boolean hasShortCont = false;

			if (boxStory.hasElementType(BoxElement.BOXELEMENT_TYPE_IMAGE)) {
				mImage = (ImageView) this.findViewById(R.id.boxview_image);
				if (mImage != null) {
					BoxElement[] imageBoxes = boxStory
							.getBoxElementbyType(BoxElement.BOXELEMENT_TYPE_IMAGE);
					if (imageBoxes != null) {
						BoxElement elm = imageBoxes[0];
						// NOTE: start loading from ImageFetcher
						// use only 1 time when setBoxStory.
//						UIUtils.loadToImageView(elm.getContent(), mImage);
						applyRules(mImage, elm);
					}
					hasImage = true;
				} else {
					MyLogger.lf(this.getContext(), 4, TAG
							+ "=>setBoxStory failed on find ImageView.");
				}
			} else {
				mNeedBackground = true;
			}
			if (boxStory
					.hasElementType(BoxElement.BOXELEMENT_TYPE_SHORTCONTENT)) {
				mShortContent = (TextView) this
						.findViewById(R.id.boxview_shortcontent);
				if (mShortContent != null) {
					BoxElement[] shortTextElms = boxStory
							.getBoxElementbyType(BoxElement.BOXELEMENT_TYPE_SHORTCONTENT);
					if (shortTextElms != null) {
						BoxElement elm = shortTextElms[0];
						mShortContent.setTextColor(elm.getTextColor());
						// mShortContent.setTextSize(elm.getTextSize());
						// FIXME: tweak text size on device
						mShortContent.setText(elm.getContent());
						applyRules(mShortContent, elm);
						hasShortCont = true;
					}
				} else {
					MyLogger.lf(this.getContext(), 4, TAG
							+ "=>setBoxStory failed on find short TextView.");
				}
			}
			if (boxStory.hasElementType(BoxElement.BOXELEMENT_TYPE_TITLE)) {
				if (mTitle != null) {
					BoxElement[] titleElms = boxStory
							.getBoxElementbyType(BoxElement.BOXELEMENT_TYPE_TITLE);
					if (titleElms != null) {
						BoxElement elm = titleElms[0];
						mTitle.setTextColor(elm.getTextColor());
						// mTitle.setTextSize(elm.getTextSize());
						// FIXME: tweak text size on device
						mTitle.setText(elm.getContent());
						applyRules(mTitle, elm);
						hasTitle = true;
					}
				} else {
					MyLogger.lf(this.getContext(), 4, TAG
							+ "=>setBoxStory failed on find title TextView.");
				}
			}
			// finally update the boxView's size
			MyLogger.lf(this.getContext(), 5, TAG + "=>setBoxStory, type: "
					+ storyLayout + ", row: " + mNumberOfRow + ", col: "
					+ mNumberOfColumn + ", with "
					+ (hasImage ? "imgage, " : "")
					+ (hasTitle ? "title, " : "")
					+ (hasShortCont ? "short content." : ""));
			this.setTag(boxStory);
		}
	}

	private void applyRules(View view, BoxElement elm) {
		int alignmentInBox = elm.getAlignmentInBox();
		int elmW = elm.getWidthCell();
		boolean bOverlapTB = false;
		boolean bOverlapLR = false;
		FrameLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
		int padding = getResources().getInteger(BOXES_MIDDLE_PADDING);

		switch (alignmentInBox) {
		case DEFINE_BOX_ELEMENT_RULES_RIGHT_TOP:
			lp.gravity = Gravity.RIGHT | Gravity.TOP;

			break;
		case DEFINE_BOX_ELEMENT_RULES_LEFT_BOTTOM:
			lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
			break;
		case DEFINE_BOX_ELEMENT_RULES_RIGHT_BOTTOM:
			lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
			break;
		case DEFINE_BOX_ELEMENT_RULES_OVERLAP_TOP:
			bOverlapTB = true;
			lp.gravity = Gravity.TOP;
			break;
		case DEFINE_BOX_ELEMENT_RULES_OVERLAP_BOTTOM:
			bOverlapTB = true;
			lp.gravity = Gravity.BOTTOM;
			if (view instanceof TextView) {
				((TextView) view).setGravity(Gravity.BOTTOM);
			}
			break;
		case DEFINE_BOX_ELEMENT_RULES_OVERLAP_LEFT:
			bOverlapLR = true;
			lp.gravity = Gravity.LEFT;
			break;
		case DEFINE_BOX_ELEMENT_RULES_OVERLAP_RIGHT:
			bOverlapLR = true;
			lp.gravity = Gravity.RIGHT;
			break;
		case DEFINE_BOX_ELEMENT_RULES_STRETCH:
			lp.width = getBoxSupposeWidth();
			lp.height = getBoxSupposeHeight();
			break;
		case DEFINE_BOX_ELEMENT_RULES_LEFT_TOP:
		default:
			lp.gravity = Gravity.LEFT | Gravity.TOP;
			break;
		}

		if (bOverlapTB) {
			lp.width = mBoxCellSize * mNumberOfColumn + mMultiPaddingNum
					* (mNumberOfColumn - 1) * padding;
			lp.height = (mBoxCellSize * mNumberOfRow + mMultiPaddingNum
					* (mNumberOfRow - 1) * getPaddingTop()) / 2;
		} else if (bOverlapLR) {
			lp.width = mBoxCellSize * elmW + mMultiPaddingNum * (elmW - 1)
					* padding;
			lp.height = (mBoxCellSize * mNumberOfRow + mMultiPaddingNum
					* (mNumberOfRow - 1) * getPaddingTop());
		} else if (alignmentInBox != DEFINE_BOX_ELEMENT_RULES_STRETCH) {
			lp.width = mBoxCellSize * elmW + mMultiPaddingNum * (elmW - 1)
					* padding;
			lp.height = mBoxCellSize;
		}

		if ((view instanceof ImageView)
				&& alignmentInBox != DEFINE_BOX_ELEMENT_RULES_STRETCH) {
			((ImageView)view).setScaleType(ScaleType.FIT_XY);
			if (mNumberOfColumn > 1) {
				mNeedBackground = true;
			}
			// XXX: image could be able to "eat" the padding in box.
			lp.width = mBoxCellSize * elmW + mMultiPaddingNum * (elmW)
					* padding;
		} else if (view instanceof TextView) {
//			if (TNPreferenceManager.isIgnoreTextGravity()) {
//				lp.gravity = Gravity.LEFT | Gravity.TOP;
//			}
			TextView tv = (TextView) view;
			lp.height = LayoutParams.MATCH_PARENT;
			tv.setGravity(lp.gravity);
			tv.setPadding(padding, padding, padding, padding);
		}

		view.setLayoutParams(lp);
		view.setVisibility(VISIBLE);
	}

	@TargetApi(16)
	@SuppressWarnings("deprecation")
	public void setBoxBackground(Drawable background) {
		if (UIUtils.hasJellyBean()) {
			setBackground(background);
		} else {
			setBackgroundDrawable(background);
		}
		background.setBounds(0, 0, getWidth(), getHeight());
	}

	
	/* (non-Javadoc)
	 * @see android.widget.FrameLayout#drawableStateChanged()
	 */
	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		if (mForegroundDrawable.isStateful()) {
            mForegroundDrawable.setState(getDrawableState());
        }
        // Trigger a redraw.
        invalidate();
	}
	

	/* (non-Javadoc)
	 * @see android.widget.FrameLayout#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mCachedBounds.set(0, 0, w, h);
	}
	

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (BOX_HAS_TOUCH_HIGHLIGHT) {
			mForegroundDrawable.setBounds(mCachedBounds);
	        mForegroundDrawable.draw(canvas);	
		}
	}

}
