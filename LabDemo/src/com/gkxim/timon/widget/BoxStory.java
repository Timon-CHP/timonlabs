/**
 * File: BoxStory.java
 * Creator: Timon.Trinh (timon@gkxim.com)
 * Date: 07-11-2012
 * 
 */
package com.gkxim.timon.widget;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.gkxim.timon.utils.MyLogger;
import com.google.gson.Gson;
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
public class BoxStory {

	public static final String BOXSTORY_TYPE = "type";
	public static final String BOXSTORY_LAYOUT = "layout";
	public static final String BOXSTORY_BOXINDEX = "boxindex";
	public static final String BOXSTORY_SECTIONREF = "sectionref";
	public static final String BOXSTORY_STORYID = "storyid";
	public static final String BOXSTORY_ELEMENTS = "elements";
	public static final String BOXSTORY_BACKGROUND = "background";
	public static final String BOXSTORY_BGCOLOR1 = "bgcolor1";
	public static final String BOXSTORY_bgcolor2 = "bgcolor2";
	// not need
	public static final String BOXSTORY_BOXHEIGHTCELL = "boxheightcell";
	public static final String BOXSTORY_BOXWIDTHCELL = "boxwidthcell";
	
	public static final String BOXSTORY_BOXHEIGHT = "boxheight";
	public static final String BOXSTORY_BOXWIDTH = "boxwidth";

	private static final String TAG = "BoxStory";

	public static final int BOXSTORY_ELEMENT_MASK = 0xFF;
	public static final int BOXSTORY_ELEMENT_IMAGE = 0x04;
	public static final int BOXSTORY_ELEMENT_TITLE = 0x01;
	public static final int BOXSTORY_ELEMENT_SHORT = 0x02;
	private static final String CONST_STR_BACKGROUND_SECTION = "section";

	// from deserialize
	private int type;
	private int layout;
	private int boxIndex;
	private String sectionRefId;
	private String storyId;
	private String backgroundRefString;

	private int boxElementTypes = 0;

	// TODO: add an array of BoxElement
	private ArrayList<BoxElement> boxElements = null;

	/**
	 * 07-11-2012
	 */
	public BoxStory() {
		// Non-agrument contructor suport for Gson serialization
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLayout() {
		return layout;
	}

	public void setLayout(int layout) {
		this.layout = layout;
	}

	public int getBoxIndex() {
		return boxIndex;
	}

	public void setBoxIndex(int boxIndex) {
		this.boxIndex = boxIndex;
	}

	public String getSectionRefId() {
		return sectionRefId;
	}

	public void setSectionRefId(String sectionRefId) {
		this.sectionRefId = sectionRefId;
	}

	public String getStoryId() {
		return storyId;
	}

	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	public boolean hasElementType(int elementType) {
		switch (elementType) {
		case BoxElement.BOXELEMENT_TYPE_IMAGE:
			return (((boxElementTypes & BOXSTORY_ELEMENT_MASK) & BOXSTORY_ELEMENT_IMAGE) > 0) ? true
					: false;
		case BoxElement.BOXELEMENT_TYPE_TITLE:
		case BoxElement.BOXELEMENT_TYPE_SHORTCONTENT:
			return (((boxElementTypes & BOXSTORY_ELEMENT_MASK) & elementType) > 0) ? true
					: false;
		default:
			break;
		}
		return false;
	}

	public BoxElement[] getBoxElement() {
		BoxElement[] result = null;
		if (boxElements != null && boxElements.size() > 0) {
			result = new BoxElement[boxElements.size()];
			return boxElements.toArray(result);
		}
		return result;
	}

	public BoxElement getBoxElement(int index) {
		if (boxElements != null && boxElements.size() > 0) {
			if (index > -1 && index < boxElements.size()) {
				return boxElements.get(index);
			}
		}
		return null;
	}

	/**
	 * @Description: Get an array of BoxElement in this issue by boxType input.
	 * @param int boxType
	 * @return null if
	 */
	public BoxElement[] getBoxElementbyType(int boxType) {
		BoxElement[] result = null;
		if (boxElements != null && boxElements.size() > 0) {
			ArrayList<BoxElement> array = new ArrayList<BoxElement>();
			for (BoxElement boxE : boxElements) {
				if (boxType == boxE.getType()) {
					array.add(boxE);
				}
			}
			MyLogger.lf(null, 0,
					TAG + "=>getBoxStorybyType(int) have: " + array.size());
			if (array.size() > 0) {
				result = new BoxElement[array.size()];
				return array.toArray(result);
			}
		}
		return result;
	}

	/**
	 * @Description: Add a BoxElement into current BoxStory
	 * @param BoxElement
	 *            abox
	 * @return the index of the first occurrence of the object, or -1 if it was
	 *         not found.
	 */
	public int addBoxElement(BoxElement abox) {
		if (boxElements == null) {
			boxElements = new ArrayList<BoxElement>();
		}
		if (boxElements.add(abox)) {
			switch (abox.getType()) {
			case BoxElement.BOXELEMENT_TYPE_IMAGE:
				boxElementTypes |= BOXSTORY_ELEMENT_IMAGE;
				break;
			case BoxElement.BOXELEMENT_TYPE_TITLE:
				boxElementTypes |= BOXSTORY_ELEMENT_TITLE;
				break;
			case BoxElement.BOXELEMENT_TYPE_SHORTCONTENT:
				boxElementTypes |= BOXSTORY_ELEMENT_SHORT;
				break;
			default:
				break;
			}
			return boxElements.indexOf(abox);
		}
		return -1;
	}

	public static class BoxStoryConverter implements JsonSerializer<BoxStory>,
			JsonDeserializer<BoxStory> {

		@Override
		public BoxStory deserialize(JsonElement element, Type type,
				JsonDeserializationContext context) {
			BoxStory result = null;
			try {
				result = new BoxStory();
				JsonObject jObj = element.getAsJsonObject();
				if (jObj.has(BOXSTORY_TYPE)) {
					result.type = jObj.getAsJsonPrimitive(BOXSTORY_TYPE)
							.getAsInt();
				}
				if (jObj.has(BOXSTORY_LAYOUT)) {
					result.layout = jObj.getAsJsonPrimitive(BOXSTORY_LAYOUT)
							.getAsInt();
				}
				if (jObj.has(BOXSTORY_BOXINDEX)) {
					result.boxIndex = jObj
							.getAsJsonPrimitive(BOXSTORY_BOXINDEX).getAsInt();
				}
				if (jObj.has(BOXSTORY_SECTIONREF)) {
					result.sectionRefId = jObj.getAsJsonPrimitive(
							BOXSTORY_SECTIONREF).getAsString();
				}
				if (jObj.has(BOXSTORY_STORYID)) {
					result.storyId = jObj.getAsJsonPrimitive(BOXSTORY_STORYID)
							.getAsString();
				}
				if (jObj.has(BOXSTORY_BACKGROUND)) {
					result.backgroundRefString = jObj.getAsJsonPrimitive(
							BOXSTORY_BACKGROUND).getAsString();
				}
				if (!jObj.has(BOXSTORY_ELEMENTS)) {
					return result;
				}
				JsonArray array = jObj.getAsJsonArray(BOXSTORY_ELEMENTS);
				if (array == null || array.size() == 0) {
					return result;
				}
				Gson gson = new Gson();
				for (JsonElement jsonElement : array) {
					BoxElement aBox = gson.fromJson(jsonElement,
							BoxElement.class);
					if (aBox != null) {
						result.addBoxElement(aBox);
					}
				}
				MyLogger.lf(null, 0, TAG + "=>deserialize completed: "
						+ result.boxElements.size());
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
		public JsonElement serialize(BoxStory arg0, Type arg1,
				JsonSerializationContext arg2) {
			// TODO serialize to JSON for BoxStory.
			return null;
		}

	}

	public boolean isBGOnSection() {
		return backgroundRefString.equalsIgnoreCase(CONST_STR_BACKGROUND_SECTION);
	}

}
