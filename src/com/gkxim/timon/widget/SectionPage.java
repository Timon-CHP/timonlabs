/**
 * File: SectionPage.java
 * Creator: Timon.Trinh (timon@gkxim.com)
 * Date: 07-11-2012
 * 
 */
package com.gkxim.timon.widget;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.gkxim.timon.utils.MyLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * @author Timon Trinh
 */
public class SectionPage {

	public static final String SECTIONPAGE_SECTIONID = "sectionid";
	@SuppressWarnings("unused")
	private static final String SECTIONPAGE_SECTIONTYPE = "sectiontype";
	public static final String SECTIONPAGE_SECTIONTITLE = "sectiontitle";
	public static final String SECTIONPAGE_ISSUEID = "issueid";
	public static final String SECTIONPAGE_ISSUEDATE = "issuedate";
	public static final String SECTIONPAGE_LAYOUTWIDTH = "layoutwidth";
	public static final String SECTIONPAGE_BOXWIDTH = "boxwidth";
	public static final String SECTIONPAGE_GAPWIDTH = "gapwidth";
	public static final String SECTIONPAGE_BOXES = "boxes";
	private static final String TAG = "SectionPage";

	private String sectionId;
	private String sectionTitle;
	private String issueId;
	private String issueDate;
	private int layoutWidth;
	private int boxWidth;
	private int gapwidth;

	// TODO: add an array of BoxStory
	private ArrayList<BoxStory> mBoxStories = null;

	/**
	 * 07-11-2012
	 */
	public SectionPage() {
		// Non-agrument contructor suport for Gson serialization
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionTitle() {
		return sectionTitle;
	}

	public void setSectionTitle(String sectionTitle) {
		this.sectionTitle = sectionTitle;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public int getBoxWidth() {
		return boxWidth;
	}

	public int getGapwidth() {
		return gapwidth;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

	public int getLayoutWidth() {
		return layoutWidth;
	}

	public void setLayoutWidth(int layoutWidth) {
		this.layoutWidth = layoutWidth;
	}

	public int getBoxStoryCount() {
		if (mBoxStories != null) {
			return mBoxStories.size();
		}
		return 0;
	}

	public BoxStory[] getBoxes() {
		if (mBoxStories != null && mBoxStories.size() > 0) {
			return mBoxStories.toArray(new BoxStory[mBoxStories.size()]);
		}
		return new BoxStory[0];
	}

	public BoxStory getBoxStory(int index) {
		if (mBoxStories != null && mBoxStories.size() > 0) {
			if (index > -1 && index < mBoxStories.size()) {
				return mBoxStories.get(index);
			}
		}
		return null;
	}

	/**
	 * @Description: Get an array of BoxStory in this issue by boxIndex input.
	 * @param int boxIndex
	 * @return null if
	 */
	public BoxStory[] getBoxStorybyIndex(int boxIndex) {
		BoxStory[] result = null;
		if (mBoxStories != null && mBoxStories.size() > 0) {
			ArrayList<BoxStory> array = new ArrayList<BoxStory>();
			for (BoxStory box : mBoxStories) {
				if (boxIndex == box.getBoxIndex()) {
					array.add(box);
				}
			}
			MyLogger.lf(null, 0, TAG + "=>getBoxStorybyIndex(int) have: "
					+ array.size());
			if (array.size() > 0) {
				result = new BoxStory[array.size()];
				return array.toArray(result);
			}
		}
		return result;
	}

	/**
	 * @Description: Add a BoxStory into current SectionPage
	 * @param BoxStory
	 *            abox
	 * @return the index of the first occurrence of the object, or -1 if it was
	 *         not found.
	 */
	public int addBoxStory(BoxStory abox) {
		if (mBoxStories == null) {
			mBoxStories = new ArrayList<BoxStory>();
		}
		if (mBoxStories.add(abox)) {
			return mBoxStories.indexOf(abox);
		}
		return -1;
	}

	@Override
	public String toString() {
		return "SectionPage [sectionId=" + sectionId + ", sectionTitle="
				+ sectionTitle + ", issueId=" + issueId + ", issueDate="
				+ issueDate + ", layoutWidth=" + layoutWidth + "]";
	}

	public static class SectionPageConverter implements
			JsonSerializer<SectionPage>, JsonDeserializer<SectionPage> {

		@Override
		public SectionPage deserialize(JsonElement element, Type type,
				JsonDeserializationContext context) {
			SectionPage result = null;
			try {
				result = new SectionPage();
				JsonObject jObj = element.getAsJsonObject();
				result.issueId = jObj.getAsJsonPrimitive(SECTIONPAGE_ISSUEID)
						.getAsString();
				if (jObj.has(SECTIONPAGE_ISSUEDATE)) {
					result.issueDate = jObj.getAsJsonPrimitive(
							SECTIONPAGE_ISSUEDATE).getAsString();
				}
				if (jObj.has(SECTIONPAGE_SECTIONID)) {
					result.sectionId = jObj.getAsJsonPrimitive(
							SECTIONPAGE_SECTIONID).getAsString();
				}
				if (jObj.has(SECTIONPAGE_SECTIONTITLE)) {
					result.sectionTitle = jObj.getAsJsonPrimitive(
							SECTIONPAGE_SECTIONTITLE).getAsString();
				}
				if (jObj.has(SECTIONPAGE_LAYOUTWIDTH)) {
					result.layoutWidth = jObj.getAsJsonPrimitive(
							SECTIONPAGE_LAYOUTWIDTH).getAsInt();
				}
				if (jObj.has(SECTIONPAGE_BOXWIDTH)) {
					result.boxWidth = jObj.getAsJsonPrimitive(
							SECTIONPAGE_BOXWIDTH).getAsInt();
				}
				if (jObj.has(SECTIONPAGE_GAPWIDTH)) {
					result.gapwidth = jObj.getAsJsonPrimitive(
							SECTIONPAGE_GAPWIDTH).getAsInt();
				}
				if (!jObj.has(SECTIONPAGE_BOXES)) {
					return result;
				}
				JsonArray array = jObj.getAsJsonArray(SECTIONPAGE_BOXES);
				if (array == null || array.size() == 0) {
					return result;
				}
				Gson gson = (new GsonBuilder()).registerTypeAdapter(
						BoxStory.class, new BoxStory.BoxStoryConverter())
						.create();
				for (JsonElement jsonElement : array) {
					BoxStory aBox = gson.fromJson(jsonElement, BoxStory.class);
					if (aBox != null) {
						result.addBoxStory(aBox);
					}
				}
				MyLogger.lf(null, 0, TAG + "=>deserialize completed: "
						+ result.mBoxStories.size());
			} catch (IllegalArgumentException e) {
				MyLogger.lf(
						null,
						4,
						TAG + "=>deserialize IllegalArgumentException: "
								+ e.getMessage());
			} catch (JsonParseException e) {
				MyLogger.lf(null, 4, TAG + "=>deserialize JsonParseException: "
						+ e.getMessage());
			}
			return result;
		}

		@Override
		public JsonElement serialize(SectionPage arg0, Type arg1,
				JsonSerializationContext arg2) {
			// TODO serialize from object to JsonElement.
			return null;
		}

	}

}
