package com.alex.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.annotation.ElementType;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;
import android.util.Log;

import org.dom4j.*;

import com.alex.dota2.MainActivity;

public class DotADB extends SQLiteOpenHelper implements Runnable {

	private MainActivity activity;
	private String account_id;
	final String CREATE = "create table if not exists match(id integer primary key autoincrement"
			+ ",mach_id varchar(30),time varchar(50),has_won integer,hero_id integer,kills integer,death integer,"
			+ "assiant integer,damage integer,tower_damage integer,hit integer,denies integer,level integer,"
			+ "gold_per_min integer,xp_per_min integer)";
	final String CREATE_HERO_TABLE = "create table if not exists heros(id integer primary key,name varchar(50),"
			+ "localized_name varchar(50))";
	final String CREATE_ITEMS_TABLE = "create table if not exists items(id integer primary key,name varchar(50),alias"
			+ " varchar(50))";

	public DotADB(Context context, String dbName, int version,
			MainActivity activity) {
		super(context, dbName, null, version);
		this.activity = activity;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE);
		db.execSQL(CREATE_HERO_TABLE);
		db.execSQL(CREATE_ITEMS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
            System.out.println("Hello Fuck");
			Document document;
			int resualt_num = Integer.MAX_VALUE;
			document = DocumentHelper
					.parseText(getJsonString("http://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/v001?"
							+ "key=188DE5A4011C5BBD197C5ECF3EABD15F&format=XML&matches_requested=1&account_id="+account_id));

			Element matchesElement = document.getRootElement().element(
					"matches");
			List<Element> list = matchesElement.elements("match");
			List<Element> playerList;
			String start_at_match_id = matchesElement.element("match")
					.elementText("match_id");
			int total_num = Integer.parseInt(document.getRootElement()
					.elementText("total_results"));
			Element resualtElement;
			Log.i("num", "" + total_num);
			int num = 0;
			while (true) {
				document = DocumentHelper
						.parseText(getJsonString("http://api.steampowered.com/IDOTA2Match_570/GetMatchHistory/v001?"
								+ "key=188DE5A4011C5BBD197C5ECF3EABD15F&format=XML&matches_requested=50&account_id="+account_id+"&start_at_match_id="
								+ start_at_match_id));
				resualt_num = Integer.parseInt(document.getRootElement()
						.elementText("num_results"));
				if (resualt_num == 1) {
					break;
				}

				list = document.getRootElement().element("matches")
						.elements("match");
				for (int j = 1; j < list.size(); j++) {
					Element element = (Element) list.get(j);
					String match_id = element.elementText("match_id");
					String start_time = element.elementText("start_time");
					String has_won, hero_id, kills, death, assiant, heal, damage, hits, denies, level, tower_damage; // 各种数据
					String gold_per_min, xp_per_min;
					document = DocumentHelper
							.parseText(getJsonString("https://api.steampowered.com/IDOTA2Match_570/GetMatchDetails/v001?"
									+ "key=188DE5A4011C5BBD197C5ECF3EABD15F&format=XML&match_id="
									+ match_id));
					String win = document.getRootElement().elementText(
							"radiant_win");
					playerList = document.getRootElement().element("players")
							.elements("player");
					for (Object o : playerList) {

						Element elementPlayer = (Element) o;
						Log.i("http", elementPlayer.elementText("account_id")
								+ " " + account_id);
						if (elementPlayer.elementText("account_id").equals(
								account_id)) {

							int slot = Integer.parseInt(elementPlayer
									.elementText("player_slot"));
							boolean radiant_win;
							if (win.equals("true")) {
								radiant_win = true;
							} else {
								radiant_win = false;
							}
							if (slot <= 4 && radiant_win) {
								has_won = "1";
							} else {
								has_won = "0";
							}
							try {
								hero_id = elementPlayer.elementText("hero_id");
								kills = elementPlayer.elementText("kills");
								death = elementPlayer.elementText("deaths");
								assiant = elementPlayer.elementText("assists");
								heal = elementPlayer
										.elementText("hero_healing");
								damage = elementPlayer
										.elementText("hero_damage");
								tower_damage = elementPlayer
										.elementText("tower_damage");
								hits = elementPlayer.elementText("last_hits");
								denies = elementPlayer.elementText("denies");
								level = elementPlayer.elementText("level");
								gold_per_min = elementPlayer
										.elementText("gold_per_min");
								xp_per_min = elementPlayer
										.elementText("xp_per_min");
								getReadableDatabase()
										.execSQL(
												"insert into match values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
												new String[] { match_id,
														start_time, has_won,
														hero_id, kills, death,
														assiant, damage,
														tower_damage, hits,
														denies, level,
														gold_per_min,
														xp_per_min });
								num++;
								Message msg = activity.getHandler()
										.obtainMessage();
								msg.what = 911;
								msg.obj = (Integer) ((int) ((double) num
										/ (double) total_num * 80));
								Log.i("number", msg.obj.toString());
								activity.getHandler().sendMessage(msg);
								break;
							} catch (Exception ex) {
								System.out.println("数据读取出错啦");
								Log.i("http", ex.toString());
							}
						}
					}
				}
				start_at_match_id = ((Element) list.get(list.size() - 1))
						.elementText("match_id");
			}
			document = DocumentHelper
					.parseText(getJsonString("https://api.steampowered.com/IEconDOTA2_570/GetHeroes/v0001?"
							+ "key=188DE5A4011C5BBD197C5ECF3EABD15F&format=XML&language=zh_cn"));
			List<Element> heroList = document.getRootElement()
					.element("heroes").elements("hero");
			for (Element element : heroList) {
				getReadableDatabase().execSQL(
						"insert into heros values(?,?,?)",
						new String[] { element.elementText("id"),
								element.elementText("name"),
								element.elementText("localized_name") });
			}
			Message msg = activity.getHandler().obtainMessage();
			msg.what = 911;
			msg.obj = (Integer) (90);
			activity.getHandler().sendMessage(msg);

			//189803119
			
			document = DocumentHelper.parseText(ParseDota2Items2
					.readFile(activity.getResources().getAssets().open("items.txt")));
			List<Element> itemList = document.getRootElement().elements(
					"DOTA2Item");
			for (Element itemElement : itemList) {
				getReadableDatabase().execSQL(
						"insert into items values(?,?,?)",
						new String[] { itemElement.elementText("ID"),
								itemElement.elementText("Alias"),
								itemElement.elementText("Name") });
			}
			msg = activity.getHandler().obtainMessage();
			msg.what = 911;
			msg.obj = (Integer) (100);
			activity.getHandler().sendMessage(msg);
		} catch (Exception ex) {
			// TODO: handle exception
			Log.i("http", ex.toString());
		}
	}

	public String getJsonString(String urlPath) throws Exception {

		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		InputStream inputStream = connection.getInputStream();
		// 对应的字符编码转换
		Reader reader = new InputStreamReader(inputStream, "UTF-8");
		BufferedReader bufferedReader = new BufferedReader(reader);
		String str = null;
		StringBuffer sb = new StringBuffer();

		while ((str = bufferedReader.readLine()) != null) {
			sb.append(str + "\n");
		}
		// System.out.println(a);
		reader.close();
		connection.disconnect();
		return sb.toString();
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

}
