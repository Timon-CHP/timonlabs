/**
 * File: BoxLayout.java
 * Creator: Timon.Trinh (timon@gkxim.com)
 * Date: 09-11-2012
 * 
 */
package com.gkxim.timon.widget;

import com.gkxim.timon.labs.R;
import com.gkxim.timon.utils.MyLogger;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * @author Timon Trinh A BoxLayout is a GUI object which is stand for a Section
 *         Page.
 */
public class BoxLayout extends TableLayout {

	private static final String TAG = "BoxLayout";
	private static final int DEFAULT_NUMBER_MAX_COLUM = 2;
	private static final boolean BOX_HAS_TOUCH_ANIMATION = true;
	private static final int BOXES_MIDDLE_PADDING = 5;

	// Storing number of colum (included span colum) for last inserted row.
	private int mNumberColLastRow = 0;
	private int mNumberMaxColumn = DEFAULT_NUMBER_MAX_COLUM;
	private int mPaddingSide = Integer.MIN_VALUE;
	private View.OnClickListener mOnItemClickListener;
	private View.OnClickListener mOnClickListener;

	private Animation mAnimation;
	private String mSectionTitle = null;
	private String mSectionId = null;
	private Drawable mBGDrawble1 = null;
	private Drawable mBGDrawble2 = null;

	/**
	 * 09-11-2012
	 */
	public BoxLayout(Context context) {
		super(context);
	}

	/**
	 * 09-11-2012
	 */
	public BoxLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.BoxLayout);
		// get number of max colum if need
		int maxColumns = a.getInt(R.styleable.BoxLayout_maxColumns, -1);
		if (maxColumns > DEFAULT_NUMBER_MAX_COLUM) {
			mNumberMaxColumn = maxColumns;
		}
		initBoxLayout();
	}

	@TargetApi(11)
	private void initBoxLayout() {
		// NOTE: initialize Boxlayout if need
		// set boxlayout's padding, this should be implied in XML layoyut
		// this.setPadding(leftRightpadding, getPaddingTop(), leftRightpadding,
		// getPaddingBottom());
		MyLogger.lf(this.getContext(), 0, TAG + "=>initBoxLayout: ["
				+ getWidth() + ", " + getHeight() + "].");
		initOnClickListnener();
		mSectionTitle = "";
		mPaddingSide = getResources().getInteger(BOXES_MIDDLE_PADDING);
		if (BOX_HAS_TOUCH_ANIMATION) {
			mAnimation = AnimationUtils.loadAnimation(getContext(),
					R.anim.boxclick_tween);
			mAnimation.setFillAfter(false);
		}
	}

	/**
	 * @return the Section's title
	 */
	public String getSectionTitle() {
		return mSectionTitle;
	}

	/**
	 * @return the Section's id
	 */
	public String getSectionId() {
		return mSectionId;
	}

	public void setNumberMaxColumn(int mNumberMaxColumn) {
		this.mNumberMaxColumn = mNumberMaxColumn;
	}

	public void setBackGroundDrawble1(Drawable drawable) {
		mBGDrawble1 = drawable;
	}

	public void setBackGroundDrawble2(Drawable drawable) {
		mBGDrawble2 = drawable;
	}

	public void addBoxView(View view) {
		BoxViewFrameLayout boxView = (BoxViewFrameLayout) view;
		int colSpan = boxView.getNumberOfColumSpan();
		TableRow currentTR = null;
		if (mNumberColLastRow <= 0
				|| (mNumberColLastRow + colSpan) > mNumberMaxColumn) {
			addRow(-1);
			mNumberColLastRow = 0;
		}
		currentTR = (TableRow) getChildAt(getChildCount() - 1);
		if (currentTR != null) {
			// TableRow.LayoutParams lparams = new TableRow.LayoutParams(
			// TableRow.LayoutParams.MATCH_PARENT,
			// TableRow.LayoutParams.WRAP_CONTENT);

			TableRow.LayoutParams lparams = (TableRow.LayoutParams) boxView
					.getLayoutParams();
			if (lparams == null) {
				lparams = new TableRow.LayoutParams(
						TableRow.LayoutParams.MATCH_PARENT,
						boxView.getBoxSupposeHeight());
			}
			lparams.gravity = Gravity.CENTER_VERTICAL;

			if (colSpan > 1) {
				lparams.span = colSpan;
				MyLogger.lf(this.getContext(), 0, TAG + "=>addBoxView: span= "
						+ colSpan + ", in colCount= " + mNumberColLastRow);
			}
			mNumberColLastRow += colSpan;
			if (mNumberColLastRow <= mNumberMaxColumn) {
				lparams.rightMargin = mPaddingSide;
			}
			boxView.setLayoutParams(lparams);
			if (boxView.isNeedGardientBackground()) {
				Drawable bgDr = mBGDrawble1;
				if (!boxView.isBGSection()) {
					bgDr = mBGDrawble2;
				}
				boxView.setBoxBackground(bgDr);
			}
			currentTR.addView(boxView, -1);
		} else {
			MyLogger.lf(this.getContext(), 5, TAG
					+ "=>addBoxView failed: failed to create a row.");
		}
	}

	/**
	 * @Description: Privately add new row into table at index. -1 if the row
	 *               should be added at last.
	 * @param rowIndex
	 * @return The instance of new row if succeed, otherwise null.
	 */
	private TableRow addRow(int rowIndex) {
		TableRow result = new TableRow(this.getContext());
		result.setPadding(mPaddingSide, mPaddingSide, mPaddingSide, 0);
		this.addViewInLayout(result, rowIndex, generateDefaultLayoutParams(),
				true);
		MyLogger.lf(this.getContext(), 5, TAG + "=>added new row at: "
				+ rowIndex);
		return result;
	}

	public int getBoxPaddingSide() {
		if (mPaddingSide == Integer.MIN_VALUE) {
			mPaddingSide = getResources().getInteger(BOXES_MIDDLE_PADDING);
		}
		return mPaddingSide;
	}

	/**
	 * @Description: Set Section page content for BoxLayout.
	 *               <p>
	 *               This is the definition time for BoxLayout which will
	 *               display for a specified Section.
	 *               </p>
	 * @param SectionPage
	 *            page
	 */
	public void setPage(SectionPage page) {
		if (page != null && page.getBoxStoryCount() <= 0) {
			MyLogger.lf(this.getContext(), 4, TAG
					+ "=>setPage: there is no box.");
			return;
		}
		if (getChildCount() > 0) {
			removeAllViewsInLayout();
		}
		// Definition for a specified Section
		mSectionId = page.getSectionId();
		mSectionTitle = page.getSectionTitle();
//		if (mSectionTitle == null || mSectionTitle.length() <= 0) {
//			mSectionTitle = TNPreferenceManager
//					.getSectionTitleFromPref(mSectionId);
//		}
//		mBGDrawble1 = TNPreferenceManager.getBackgroundDrawable1(mSectionId);
//		mBGDrawble2 = TNPreferenceManager.getBackgroundDrawable2(mSectionId);

		BoxStory[] boxes = page.getBoxes();
		BoxViewFrameLayout bV = null;
		for (BoxStory boxStory : boxes) {
			bV = new BoxViewFrameLayout(this.getContext());
			// bV.setMargin(0, 0, mPaddingSide, 0);
			bV.setOnClickListener(mOnItemClickListener);
			bV.setBoxStory(boxStory);
			addBoxView(bV);
		}
	}

	/**
	 * @Description: Handling for boxView clicked.
	 */
	private void initOnClickListnener() {
		MyLogger.lf(null, 0, TAG + "=>initOnClickListnener.");
		if (mOnItemClickListener == null) {
			mOnItemClickListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// NOTE: Handling boxview click here.
					if (v instanceof BoxViewFrameLayout) {
						BoxViewFrameLayout bvf = (BoxViewFrameLayout) v;
						MyLogger.lf(BoxLayout.this.getContext(), 0, TAG
								+ "=>clicked: " + bvf.getBoxIndex() + ", "
								+ bvf.getStoryId());
						if (BOX_HAS_TOUCH_ANIMATION) {
							v.startAnimation(mAnimation);
						}
						if (mOnClickListener != null) {
							mOnClickListener.onClick(v);
						}
					}
				}
			};
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.View#setOnClickListener(android.view.View.OnClickListener)
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		if (mOnClickListener != null) {
			mOnClickListener = null;
		}
		mOnClickListener = l;
	}

}
