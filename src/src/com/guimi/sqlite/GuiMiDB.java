package com.guimi.sqlite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.guimi.entities.FavoriteOutfit;
import com.guimi.entities.Item;
import com.guimi.entities.Match;
import com.guimi.entities.MyOutfit;
import com.guimi.entities.Outfit;
import com.guimi.entities.PersonInfo;
import com.guimi.entities.PublishedOutfit;
import com.guimi.entities.Tag;
import com.guimi.uploadPhoto.combineUtil.PublishedActivity;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorJoiner.Result;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class GuiMiDB extends SQLiteOpenHelper {

	public static enum CLOTHTYPE {
		TOPS, BOTTOMS, DRESS, SHOES, BAG, ACCESSORIES
	};

	private ConnectServer cs;
	private ImageHandeller ih;
	private final static String DATABASE_NAME = "GUIMI.db";
	private final static int DATABASE_VERSION = 1;
	private final static String[] TABLE_NAME = { "TOPS", "BOTTOMS", "DRESS",
			"SHOES", "BAG", "ACCESSORIES" };
	private final static String[] TOPCOLUMNS = { "topstype", "style", "color",
			"topslength", "topssleeve" };
	private final static String[] BOTCOLUMNS = { "bottomstype", "style",
			"color", "bottomslength" };
	private final static String[] DRECOLUMNS = { "dresstype", "style", "color",
			"dresslength", "dressmodel", "dressfabric" };
	private final static String[] SHOCOLUMNS = { "shoestype", "style", "color",
			"shoesspecial", "heelHeight" };
	private final static String[] BAGCOLUMNS = { "bagtype", "style", "color",
			"bagspecial", "bagsize", "bagmaterial" };
	private final static String[] ACCCOLUMNS = { "accessoriestype", "style",
			"color" };
	private final static String[] PREFIX = { "TOP", "BOT", "DRE", "SHO", "BAG",
			"ACC" };
	private List<String[]> COLUMNS = new ArrayList<String[]>();
	public volatile static GuiMiDB localDB;

	private GuiMiDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		cs = ConnectServer.getInstance();
		ih = ImageHandeller.getInstance(context);
		COLUMNS.add(TOPCOLUMNS);
		COLUMNS.add(BOTCOLUMNS);
		COLUMNS.add(DRECOLUMNS);
		COLUMNS.add(SHOCOLUMNS);
		COLUMNS.add(BAGCOLUMNS);
		COLUMNS.add(ACCCOLUMNS);
	}

	// set GuiMiDB as an singleton class
	public static GuiMiDB getInstance(Context context) {
		if (localDB == null)
			synchronized (GuiMiDB.class) {
				if (localDB == null)
					localDB = new GuiMiDB(context);
			}
		return localDB;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE TOPS("
				+ "_id INTEGER primary key autoincrement,"
				+ "clothId char(10)," + "topstype INTEGER," + "style INTEGER,"
				+ "color INTEGER," + "topslength INTEGER,"
				+ "topssleeve INTEGER);");
		db.execSQL("CREATE TABLE BOTTOMS("
				+ "_id INTEGER primary key autoincrement,"
				+ "clothId char(10)," + "bottomstype INTEGER,"
				+ "style INTEGER," + "color INTEGER,"
				+ "bottomslength INTEGER);");
		db.execSQL("CREATE TABLE DRESS("
				+ "_id INTEGER primary key autoincrement,"
				+ "clothId char(10)," + "dresstype INTEGER," + "style INTEGER,"
				+ "color INTEGER," + "dresslength INTEGER,"
				+ "dressmodel INTEGER," + "dressfabric INTEGER);");
		db.execSQL("CREATE TABLE SHOES("
				+ "_id INTEGER primary key autoincrement,"
				+ "clothId char(10)," + "shoestype INTEGER," + "style INTEGER,"
				+ "color INTEGER," + "shoesspecial INTEGER,"
				+ "heelHeight INTEGER);");
		db.execSQL("CREATE TABLE BAG("
				+ "_id INTEGER primary key autoincrement,"
				+ "clothId char(10)," + "bagtype INTEGER," + "style INTEGER,"
				+ "color INTEGER," + "bagspecial INTEGER," + "bagsize INTEGER,"
				+ "bagmaterial INTEGER);");
		db.execSQL("CREATE TABLE ACCESSORIES("
				+ "_id INTEGER primary key autoincrement,"
				+ "clothId char(10)," + "accessoriestype INTEGER,"
				+ "style INTEGER," + "color INTEGER);");

		db.execSQL("CREATE TABLE STYLE("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE COLOR("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE TOPSTYPE("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE TOPSLENGTH("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE TOPSSLEEVE("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE BOTTOMSTYPE("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE BOTTOMSLENGTH("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE DRESSTYPE("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE DRESSLENGTH("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE DRESSMODEL("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE DRESSFABRIC("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE SHOESTYPE("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE SHOESSPECIAL("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE HEELHEIGHT("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE BAGTYPE("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE BAGSPECIAL("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE BAGSIZE("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE BAGMATERIAL("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");
		db.execSQL("CREATE TABLE ACCESSORIESTYPE("
				+ "id INTEGER primary key autoincrement,"
				+ "tagname varchar(10));");

		db.execSQL("CREATE TABLE WARDROBE("
				+ "itemId INTEGER primary key autoincrement,"
				+ "userId char(11)," + "clothId char(10),"
				+ "image varchar(50)," + "description varchar(50),"
				+ "state INTEGER);");
		db.execSQL("CREATE TABLE MYAREA("
				+ "outfitId INTEGER primary key autoincrement,"
				+ "userId char(11)," + "image varchar(50),"
				+ "groupId INTEGER," + "isPublished INTEGER,"
				+ "state INTEGER);");
		db.execSQL("CREATE TABLE CLOTHGROUP("
				+ "itemId INTEGER primary key autoincrement,"
				+ "groupId INTEGER, clothId char(10));");
		db.execSQL("CREATE TABLE MYPUBLISH("
				+ "itemId INTEGER primary key autoincrement,"
				+ "outfitId INTEGER," + "myAreaId INTEGER,"
				+ "userId char(11)," + "image varchar(50)," + "likes INTEGER,"
				+ "outfitDescription text," + "clothesDescription text,"
				+ "publishTime char(10));");
		db.execSQL("CREATE TABLE MYFAVORITES(userId char(11),outfitId int);");
		db.execSQL("CREATE TABLE LIKE(userId char(11),outfitId int);");
		db.execSQL("CREATE TABLE DISLIKE(userId char(11),outfitId int);");

		preInsert(db);
		Log.i("db", "create succeed");
	}

	private void preInsert(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		TagContent[] tagContents = new TagContent[19];
		tagContents[0] = new TagContent("accessoriestype", new String[] { "围巾",
				"腰带", "帽子", "手套", "手表", "发饰", "手链", "项链", "戒指" });
		tagContents[1] = new TagContent("bagmaterial", new String[] { "防水材质",
				"帆布", "磨砂皮", "漆皮", "PU皮", "毛绒系", "棉麻", "编织" });
		tagContents[2] = new TagContent("bagsize", new String[] { "大包", "常规款",
				"小包", "迷你包" });
		tagContents[3] = new TagContent("bagspecial", new String[] { "邮差包",
				"机车包", "水桶包", "信封包", "晚宴包", "钱包" });
		tagContents[4] = new TagContent("bagtype", new String[] { "单肩包", "双肩包",
				"斜挎包", "手拿包", "手提包" });
		tagContents[5] = new TagContent("heelheight", new String[] { "平跟",
				"矮跟", "中跟", "高跟", "超高跟" });
		tagContents[6] = new TagContent("shoesspecial", new String[] { "马丁靴",
				"机车靴", "牛津鞋", "松糕鞋", "雪地靴", "过膝靴", "人字拖" });
		tagContents[7] = new TagContent("shoestype", new String[] { "单鞋", "凉鞋",
				"运动鞋", "帆布鞋", "拖鞋", "靴子", "皮鞋" });
		tagContents[8] = new TagContent("dressfabric", new String[] { "雪纺",
				"毛呢", "棉麻", "绸缎", "蕾丝", "牛仔", "轻薄花呢", "西装布" });
		tagContents[9] = new TagContent("dressmodel", new String[] { "伞裙",
				"百褶裙", "A字裙", "包臀裙", "蓬蓬裙", "花苞裙", "蛋糕裙" });
		tagContents[10] = new TagContent("dresslength", new String[] { "超短裙",
				"短裙", "及膝裙", "中长款", "长裙" });
		tagContents[11] = new TagContent("dresstype", new String[] { "连衣裙",
				"背带裙", "半身裙", "打底裙" });
		tagContents[12] = new TagContent("bottomslength", new String[] { "长裤",
				"九分裤", "七分裤", "五分裤", "短裤" });
		tagContents[13] = new TagContent("bottomstype", new String[] { "牛仔裤",
				"小脚裤", "哈伦裤", "休闲裤", "打底裤", "运动裤", "背带裤", "裙裤", "连体裤", "西装裤",
				"加绒裤" });
		tagContents[14] = new TagContent("topssleeve", new String[] { "吊带",
				"无袖", "短袖", "中袖", "长袖" });
		tagContents[15] = new TagContent("topslength", new String[] {
				"超短款（40cm以下）", "短款（40-50cm）", "常规款（50-60cm）", "中长款（60-70cm）",
				"长款（70-80cm）", "超长款（80cm以上）" });
		tagContents[16] = new TagContent("topstype", new String[] { "T恤",
				"雪纺衫", "针织衫", "针织开衫", "衬衫", "卫衣", "卫衣外套", "外套", "西装", "风衣",
				"大衣", "棉袄", "羽绒服" });
		tagContents[17] = new TagContent("color", new String[] { "白", "灰", "黑",
				"红", "橙", "黄", "绿", "蓝", "紫", "棕花", "黑白", "条纹" });
		tagContents[18] = new TagContent("style", new String[] { "通勤", "甜美",
				"运动", "复古", "学院", "英伦", "休闲街头", "日系", "韩系", "欧美", "民族风", "中性",
				"简洁", "森女", "OL" });

		for (int i = 0; i < tagContents.length; i++) {
			for (int j = 0; j < tagContents[i].getSize(); j++)
				db.execSQL("insert into "
						+ tagContents[i].getTitle().toUpperCase()
						+ "(id,tagname) values (" + j + ",'"
						+ tagContents[i].getContent(j) + "')");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 6; i++)
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[i]);
		db.execSQL("DROP TABLE IF EXISTS WARDROBE");
		onCreate(db);
	}

	public int publishOutfit(int myAreaId, String outfitDescription,
			String clothDescription, String pictureUrl) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String currentTime = formatter.format(curDate);
		if (ih.uploadPicture(pictureUrl)) {
			int outfitId = cs.uploadOutfit(outfitDescription, clothDescription,
					pictureUrl, currentTime);
			if (outfitId == -1)
				return 1;
			else {
				Log.i("db", outfitId + "");
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues publish = new ContentValues();
				publish.put("outfitId", outfitId);
				publish.put("myAreaId", myAreaId);
				publish.put("userId", PersonInfo.userId);
				publish.put("outfitDescription", outfitDescription);
				publish.put("clothesDescription", clothDescription);
				publish.put("image", pictureUrl);
				publish.put("publishTime", currentTime);
				publish.put("likes", 0);
				Log.i("db", "publish" + db.insert("MYPUBLISH", null, publish));
				if (myAreaId != -1) {
					db.execSQL("UPDATE MYAREA set isPublished = 0 WHERE outfitId = "
							+ myAreaId
							+ " and userId = '"
							+ PersonInfo.userId
							+ "'");
				}
				return 0;
			}
		}
		return 1;
	}

	public List<PublishedOutfit> getMyPublish() {
		Log.i("db", "start");
		List<PublishedOutfit> myPublish = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT  * FROM MYPUBLISH where userId = '"
				+ PersonInfo.userId + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			myPublish = new ArrayList<PublishedOutfit>();
			for (int i = 0; i < cursor.getCount(); i++) {
				PublishedOutfit publishedOutfit = new PublishedOutfit(
						cursor.getInt(cursor.getColumnIndex("outfitId")),
						cursor.getString(cursor.getColumnIndex("image")),
						cursor.getString(cursor.getColumnIndex("publishTime")),
						cursor.getInt(cursor.getColumnIndex("likes")),
						cursor.getString(cursor
								.getColumnIndex("outfitDescription")),
						cursor.getString(cursor
								.getColumnIndex("clothesDescription")));
				int j = cs.getLikes(cursor.getColumnIndex("outfitId"));
				if (j > 0) {
					publishedOutfit.setLikes(j);
					db.execSQL("UPDATE MYPUBLISH set likes = " + j
							+ " WHERE outfitId = "
							+ cursor.getColumnIndex("outfitId")
							+ " and userId = '" + PersonInfo.userId + "'");
				}
				Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				options.inPurgeable = true;
				options.inInputShareable = true;
				BitmapFactory.decodeFile(
						cursor.getString(cursor.getColumnIndex("image")),
						options);
				publishedOutfit.setPictureSize(options.outWidth,
						options.outHeight);
				myPublish.add(publishedOutfit);
				cursor.moveToNext();
			}
		}
		return myPublish;
	}

	public int deletePublish(int outfitId) {
		int i = cs.deletePublish(outfitId);
		if (i == 0) {
			SQLiteDatabase db = this.getWritableDatabase();
			String sql = "SELECT myAreaId FROM MYPUBLISH where outfitId = "
					+ outfitId + " and userId = '" + PersonInfo.userId + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst())
				if (cursor.getInt(0) != -1)
					db.execSQL("UPDATE MYAREA set isPublished = 1 WHERE outfitId = "
							+ cursor.getInt(0)
							+ " and userId = '"
							+ PersonInfo.userId + "'");
			db.execSQL("delete from MYPUBLISH where userId = '"
					+ PersonInfo.userId + "' and outfitId = " + outfitId + "");
		}
		return i;
	}

	public int deletePublishInMyArea(int myAreaId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "SELECT outfitId FROM MYPUBLISH where myAreaId = "
				+ myAreaId + " and userId = '" + PersonInfo.userId + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			Log.i("db", "" + cursor.getInt(cursor.getColumnIndex("outfitId")));
			int result = cs.deletePublish(cursor.getInt(cursor
					.getColumnIndex("outfitId")));
			if (result == 0) {
				db.execSQL("delete from MYPUBLISH where userId = '"
						+ PersonInfo.userId + "' and myAreaId = " + myAreaId
						+ "");
				db.execSQL("UPDATE MYAREA set isPublished = 1 WHERE outfitId = "
						+ myAreaId
						+ " and userId = '"
						+ PersonInfo.userId
						+ "'");
				return 0;
			}
		}
		return 1;
	}

	// return all the content of one tag
	public String[] getTagContent(String tagTitle) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(tagTitle, null, null, null, null, null, null);
		String[] tags = new String[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()) {
			tags[i] = cursor.getString(1);
			i++;
		}
		return tags;
	}

	// return all the tag titles of one kind of cloth
	public String[] getTagTitles(int type) {
		return COLUMNS.get(type);
	}

	// get all the clothes of one type
	public List<Item> getItemsByType(int type) {
		List<Item> result = null;
		SQLiteDatabase db = this.getReadableDatabase();
		// select clothes by type
		String clothes = "SELECT  * FROM WARDROBE where state <> 2 and userId = '"
				+ PersonInfo.userId
				+ "' "
				+ "and clothId like '%"
				+ PREFIX[type] + "%' order by itemId desc";
		Cursor cursor = db.rawQuery(clothes, null);
		// if cursor is not empty
		if (cursor.moveToFirst()) {
			result = new ArrayList<Item>();
			for (int n = 0; n < cursor.getCount(); n++) {
				// select one item in specific table by clothId
				String current_sql_sel = "SELECT  * FROM " + TABLE_NAME[type]
						+ " where clothId = '"
						+ cursor.getString(cursor.getColumnIndex("clothId"))
						+ "'";
				Cursor cl = db.rawQuery(current_sql_sel, null);
				if (cl.moveToFirst()) {
					// new a cloth
					Item cloth = new Item(cursor.getString(cursor
							.getColumnIndex("itemId")), cursor.getString(cursor
							.getColumnIndex("clothId")),
							cursor.getString(cursor.getColumnIndex("image")),
							tagToString(cl.getInt(2), cl.getColumnName(2)
									.toUpperCase()), cursor.getString(cursor
									.getColumnIndex("description")));
					// add optional tags
					for (int j = 3; j < cl.getColumnCount(); j++)
						if (cl.getInt(j) != 0) {
							Tag tag = new Tag(cl.getColumnName(j), tagToString(
									cl.getInt(j), cl.getColumnName(j)
											.toUpperCase()));
							cloth.addTag(tag);
						}
					// add cloth into result list
					result.add(cloth);
				}
				// cursor move to next row
				cursor.moveToNext();
			}
		}
		return result;
	}

	// get all the clothes
	public List<Item> getItems() {
		List<Item> result = null;
		SQLiteDatabase db = this.getReadableDatabase();
		// select all the clothes order by insert time
		String clothes = "SELECT  * FROM WARDROBE where state <> 2 and userId = '"
				+ PersonInfo.userId + "' order by itemId desc";
		Cursor cursor = db.rawQuery(clothes, null);
		// if cursor is not empty
		if (cursor.moveToFirst()) {
			result = new ArrayList<Item>();
			for (int n = 0; n < cursor.getCount(); n++) {
				// get prefix of clothId to decide what kind of cloth is it
				String pre = cursor.getString(cursor.getColumnIndex("clothId"))
						.substring(0, 3);
				for (int i = 0; i < 6; i++)
					if (pre.equals(PREFIX[i])) {
						// select cloth in specific table by clothId
						String current_sql_sel = "SELECT  * FROM "
								+ TABLE_NAME[i]
								+ " where clothId = '"
								+ cursor.getString(cursor
										.getColumnIndex("clothId")) + "'";
						Cursor cl = db.rawQuery(current_sql_sel, null);
						if (cl.moveToFirst()) {
							// new a cloth
							Item cloth = new Item(cursor.getString(cursor
									.getColumnIndex("itemId")),
									cursor.getString(cursor
											.getColumnIndex("clothId")),
									cursor.getString(cursor
											.getColumnIndex("image")),
									tagToString(cl.getInt(2),
											cl.getColumnName(2).toUpperCase()),
									cursor.getString(cursor
											.getColumnIndex("description")));
							// add optional tags
							for (int j = 3; j < cl.getColumnCount(); j++)
								if (cl.getInt(j) != 0) {
									Tag tag = new Tag(cl.getColumnName(j),
											tagToString(cl.getInt(j), cl
													.getColumnName(j)
													.toUpperCase()));
									cloth.addTag(tag);
								}
							// add cloth into result list
							result.add(cloth);
						}
					}
				cursor.moveToNext();
			}
		}
		return result;
	}

	// add a cloth
	public void addCloth(int clothType, int[][] tags, String picUrl,
			String description) {
		int strid = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues cloth = new ContentValues();
		// get the max _id inserted in the table, then get the id of the
		// going-to-inserting row
		Cursor cursor = db.rawQuery("select max(_id) from "
				+ TABLE_NAME[clothType], null);
		if (cursor.moveToFirst())
			strid = cursor.getInt(0) + 1;
		cursor.close();
		// use the row id to make clothId
		String clothId = PREFIX[clothType]
				+ ("000000" + strid).substring(("000000" + strid).length() - 7);
		cloth.put("clothId", clothId);
		// assign tag columns form the tags array
		for (int i = 0; i < tags.length; i++) {
			cloth.put(COLUMNS.get(clothType)[tags[i][0]], tags[i][1]);
		}
		// insert the row into the specific item table
		db.insert(TABLE_NAME[clothType], null, cloth);
		ContentValues WARDROBE = new ContentValues();
		// assign all the columns in wardrobe table
		WARDROBE.put("userId", PersonInfo.userId);
		WARDROBE.put("clothId", clothId);
		WARDROBE.put("image", picUrl);
		WARDROBE.put("description", description);
		WARDROBE.put("state", 1);
		// insert the row into wardrobe table
		Log.i("db", "add succeed" + db.insert("WARDROBE", null, WARDROBE));
	}

	// delete a cloth
	public void deleteCloth(String clothId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "clothId = ?";
		String[] whereValue = { clothId };
		ContentValues cv = new ContentValues();
		cv.put("state", 2);
		db.update("WARDROBE", cv, where, whereValue);
		for (int i = 0; i < 6; i++)
			if (clothId.subSequence(0, 3).equals(PREFIX[i]))
				db.delete(TABLE_NAME[i], where, whereValue);
	}

	// change tagId to tag content
	private String tagToString(int i, String tagName) {
		SQLiteDatabase db = this.getReadableDatabase();
		String current_sql_sel = "SELECT  * FROM " + tagName + " where id = "
				+ i;
		Cursor tag = db.rawQuery(current_sql_sel, null);
		tag.moveToFirst();
		return tag.getString(1);
	}

	class TagContent {
		String tagTitle;
		String[] tagContent;

		public TagContent(String tagTitle, String[] tagContent) {
			// TODO Auto-generated constructor stub
			this.tagTitle = tagTitle;
			this.tagContent = tagContent;
		}

		public String getTitle() {
			return tagTitle;
		}

		public String getContent(int i) {
			return tagContent[i];
		}

		public int getSize() {
			return tagContent.length;
		}
	}

	public int getMyOutfitCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		// select all the clothes order by insert time
		String myOutfits = "SELECT  * FROM MYAREA where state <> 2 and userId = '"
				+ PersonInfo.userId + "'";
		Cursor cursor = db.rawQuery(myOutfits, null);
		return cursor.getCount();
	}

	public int getMyPublishCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		// select all the clothes order by insert time
		String myOutfits = "SELECT  * FROM MYPUBLISH where userId = '"
				+ PersonInfo.userId + "'";
		Cursor cursor = db.rawQuery(myOutfits, null);
		return cursor.getCount();
	}

	public int getMyFavoriteCount() {
		SQLiteDatabase db = this.getReadableDatabase();
		// select all the clothes order by insert time
		String myOutfits = "SELECT  * FROM MYFAVORITES where userId = '"
				+ PersonInfo.userId + "'";
		Cursor cursor = db.rawQuery(myOutfits, null);
		return cursor.getCount();
	}

	// get all outfits in my area
	public List<MyOutfit> getOutfits() {
		List<MyOutfit> result = null;
		SQLiteDatabase db = this.getReadableDatabase();
		// select all the clothes order by insert time
		String myOutfits = "SELECT  * FROM MYAREA where state <> 2 and userId = '"
				+ PersonInfo.userId + "' order by outfitId desc";
		Cursor cursor = db.rawQuery(myOutfits, null);
		// if cursor is not empty
		if (cursor.moveToFirst()) {
			result = new ArrayList<MyOutfit>();
			for (int n = 0; n < cursor.getCount(); n++) {
				MyOutfit outfit = new MyOutfit(cursor.getInt(cursor
						.getColumnIndex("outfitId")), cursor.getString(cursor
						.getColumnIndex("image")));
				if (cursor.getInt(cursor.getColumnIndex("groupId")) != 0) {
					String clothes = "SELECT  * FROM CLOTHGROUP where groupId = "
							+ cursor.getInt(cursor.getColumnIndex("groupId"));
					Cursor cl = db.rawQuery(clothes, null);
					cl.moveToFirst();
					for (int j = 0; j < cl.getCount(); j++) {
						outfit.addCloth(cl.getString(cl
								.getColumnIndex("clothId")));
						cl.moveToNext();
					}
				}
				if (cursor.getInt(cursor.getColumnIndex("isPublished")) == 0)
					outfit.setPublished(true);
				Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				options.inPurgeable = true;
				options.inInputShareable = true;
				BitmapFactory.decodeFile(
						cursor.getString(cursor.getColumnIndex("image")),
						options);
				outfit.setPictureSize(options.outWidth, options.outHeight);
				result.add(outfit);
				cursor.moveToNext();
			}
		}
		return result;
	}

	public List<FavoriteOutfit> getMyFavorites() {
		List<FavoriteOutfit> myFavorites = new ArrayList<FavoriteOutfit>();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM MYFAVORITES where userId = '"
				+ PersonInfo.userId + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				Outfit outfit = cs.getOutfit(cursor.getInt(cursor
						.getColumnIndex("outfitId")));
				if (outfit == null)
					return null;
				else if (outfit.getOutfitID() == 0) {
					FavoriteOutfit favoriteOutfit = new FavoriteOutfit(0, 0,
							"", "", "", "", "");
					myFavorites.add(favoriteOutfit);
				} else {
					FavoriteOutfit favoriteOutfit = new FavoriteOutfit(
							outfit.getOutfitID(), outfit.getOutfitUrl(),
							outfit.getUploadTime(), outfit.getUploaderName(),
							outfit.getOutfitDescription(),
							outfit.getClothDescription());
					favoriteOutfit.setPictureSize(outfit.getPictureWidth(),
							outfit.getPictureHeight());
					myFavorites.add(favoriteOutfit);
				}
				cursor.moveToNext();
			}
		}
		return myFavorites;
	}

	public Vector<Match> getMatchs(int orderType, int flagOutfitId,
			boolean beforeOrAfter) {
		Vector<Match> match = cs.getMatchs(orderType, flagOutfitId,
				beforeOrAfter);
		if (match == null)
			return null;
		else {
			Vector<Match> matchs = new Vector<Match>(cs.getMatchs(orderType,
					flagOutfitId, beforeOrAfter));
			SQLiteDatabase db = this.getReadableDatabase();

			for (int i = 0; i < matchs.size(); i++) {
				Match temp = matchs.get(i);
				String sql = "SELECT  * FROM MYFAVORITES where outfitId = "
						+ temp.getMatchId() + " and userId = '"
						+ PersonInfo.userId + "'";
				Cursor cursor = db.rawQuery(sql, null);
				if (cursor.moveToFirst())
					temp.setMyFavorite(true);
				sql = "SELECT  * FROM LIKE where outfitId = "
						+ temp.getMatchId() + " and userId = '"
						+ PersonInfo.userId + "'";
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToFirst())
					temp.setMyLike(true);
				sql = "SELECT  * FROM DISLIKE where outfitId = "
						+ temp.getMatchId() + " and userId = '"
						+ PersonInfo.userId + "'";
				cursor = db.rawQuery(sql, null);
				if (cursor.moveToFirst())
					temp.setMyDislike(true);
			}
			return matchs;
		}
	}

	public Outfit getOutfit(int outfitId) {
		Outfit outfit = cs.getOutfit(outfitId);
		if (outfit.getOutfitID() != 0) {
			SQLiteDatabase db = this.getReadableDatabase();
			String sql = "SELECT  * FROM MYFAVORITES where outfitId = "
					+ outfitId + " and userId = '" + PersonInfo.userId + "'";
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst())
				outfit.setFavorited(true);
			sql = "SELECT  * FROM LIKE where outfitId = " + outfitId
					+ " and userId = '" + PersonInfo.userId + "'";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst())
				outfit.setLiked(true);
			sql = "SELECT  * FROM DISLIKE where outfitId = " + outfitId
					+ " and userId = '" + PersonInfo.userId + "'";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToFirst())
				outfit.setDisliked(true);
		}
		return outfit;
	}

	// upload an outfit to my area
	public void addMyOutfit(String pictureUrl, String[] clothIds,
			boolean isPublished) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* ContentValues */
		ContentValues myOutfit = new ContentValues();
		myOutfit.put("userId", PersonInfo.userId);
		myOutfit.put("image", pictureUrl);
		if (clothIds != null) {
			Cursor cursor = db.rawQuery("select max(groupId) from CLOTHGROUP",
					null);
			int groupId;
			if (cursor.moveToFirst())
				groupId = cursor.getInt(0) + 1;
			else
				groupId = 0;
			Log.i("db", "groupId:" + groupId);
			cursor.close();
			for (int i = 0; i < clothIds.length; i++) {
				ContentValues group = new ContentValues();
				group.put("groupId", groupId);
				group.put("clothId", clothIds[i]);
				Log.i("db", "group" + db.insert("CLOTHGROUP", null, group));
			}
			myOutfit.put("groupId", groupId);
		} else
			myOutfit.put("groupId", 0);
		if (isPublished)
			myOutfit.put("isPublished", 0);
		else
			myOutfit.put("isPublished", 1);
		myOutfit.put("state", 1);
		Log.i("db", "add succeed" + db.insert("MYAREA", null, myOutfit));
	}

	// delete an outfit in my area
	public void deleteMyOutfit(int outfitId) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "outfitId = ?";
		String[] whereValue = { outfitId + "" };
		ContentValues cv = new ContentValues();
		cv.put("state", 2);
		db.update("MYAREA", cv, where, whereValue);
	}

	// find if the picture is used in GuiMi program
	public boolean imageIsUsed(String url) {
		SQLiteDatabase db = this.getReadableDatabase();
		String myOutfits = "SELECT  * FROM MYAREA where state <> 2 and image = '"
				+ url + "' order by itemId desc";
		Cursor cursor = db.rawQuery(myOutfits, null);
		if (cursor.moveToFirst())
			return true;
		else {
			String cloth = "SELECT  * FROM WARDROBE where state <> 2 and image = '"
					+ url + "' order by itemId desc";
			cursor = db.rawQuery(cloth, null);
			if (cursor.moveToFirst())
				return true;
			else
				return false;
		}
	}

	// like an outfit
	public int like(int outfitId, String uploaderId) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (uploaderId.equals(PersonInfo.userId))
			return 2;
		else {
			int result = cs.likeOrDislike(outfitId, 1);
			if (result == 0)
				db.execSQL("insert into LIKE(userId,outfitId) values ('"
						+ PersonInfo.userId + "'," + outfitId + ")");
			return result;
		}
	}

	// dislike an outfit
	public int dislike(int outfitId, String uploaderId) {
		SQLiteDatabase db = this.getWritableDatabase();
		if (uploaderId.equals(PersonInfo.userId))
			return 2;
		else {
			int result = cs.likeOrDislike(outfitId, 3);
			if (result == 0)
				db.execSQL("insert into DISLIKE(userId,outfitId) values ('"
						+ PersonInfo.userId + "'," + outfitId + ")");
			return result;
		}
	}

	// cancel like of an outfit
	public int cancelLike(int outfitId) {
		int result = cs.likeOrDislike(outfitId, 2);
		if (result == 0) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from LIKE where userId = '" + PersonInfo.userId
					+ "' and outfitId = " + outfitId + "");
			return 0;
		} else
			return result;
	}

	// cancel dislike of an outfit
	public int cancelDislike(int outfitId) {
		int result = cs.likeOrDislike(outfitId, 4);
		if (result == 0) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from DISLIKE where userId = '"
					+ PersonInfo.userId + "' and outfitId = " + outfitId + "");
			return 0;
		} else
			return result;
	}

	// store up an outfit
	public int addToFavorite(int outfitId, String uploaderId) {
		if (uploaderId.equals(PersonInfo.userId))
			return 2;
		else {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("insert into MYFAVORITES(userId,outfitId) values ('"
					+ PersonInfo.userId + "'," + outfitId + ")");
			return 0;
		}
	}

	// cancel store up of an outfit
	public int cancelFavorite(int outfitId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("delete from MYFAVORITES where userId = '"
				+ PersonInfo.userId + "' and outfitId = " + outfitId + "");
		return 0;
	}

	// test if wifi is connected
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}
		return false;
	}
}