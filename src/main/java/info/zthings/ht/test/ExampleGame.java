package info.zthings.ht.test;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.reflections.Reflections;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import info.zthings.ht.test.tiles.Tile;
import info.zthings.ht.test.tiles.TileField;

public class ExampleGame extends ApplicationAdapter {
	public Tile[][] map;
	public RenderContextProvider g;
	
	@Override
	public void create() {
		g = new RenderContextProvider(new SpriteBatch(), new ShapeRenderer());
		
		//JsonReader reader = new JsonReader();
		//JsonValue json = reader.parse(Gdx.files.internal("testmap.json"));
		JSONObject json = new JSONObject(Gdx.files.internal("testmap.json").readString());
		System.out.println("Reading map with version: "+json.getInt("version"));
		map = new Tile[json.getInt("height")][json.getInt("width")];
		System.out.println("Map is ["+map.length+"x"+map[0].length+"]");
		
		HashMap<String, Class<? extends Tile>> tiletypes = new HashMap<>();
		Reflections ref = new Reflections("info.zthings.ht");
		for (Class<? extends Tile> c : ref.getSubTypesOf(Tile.class)) tiletypes.put(c.getSimpleName().toLowerCase(), c);

		for (String k : json.keySet()) {
			if (k == "width" || k == "height" || k == "version") continue;
			try {
				Constructor<? extends Tile> c = tiletypes.get(k).getConstructor(JSONObject.class);
				for (Object obj : json.getJSONArray(k)) {
					JSONObject jobj = (JSONObject)obj;
					if (TileField.class.isAssignableFrom(tiletypes.get(k))) {
						System.out.println(jobj.getInt("x2"));
						System.out.println(jobj.getInt("x2"));
						System.out.println(jobj.getInt("x2"));
						System.out.println(5<=jobj.getInt("x2"));
						for (int y=jobj.getInt("y1"); y<=jobj.getInt("y2"); y++)
						for (int x=jobj.getInt("x1"); x<=jobj.getInt("x2"); x++) {
							JSONObject jobj2 = jobj;
							jobj2.remove("x1");
							jobj2.remove("x2");
							jobj2.remove("y1");
							jobj2.remove("y2");
							jobj2.put("x", x);
							jobj2.put("y", y);
							
							map[y][x] = c.newInstance(jobj);
						}
					} else map[jobj.getInt("y")][jobj.getInt("x")] = c.newInstance(jobj);
				}
			} catch (NullPointerException e) {throw new IllegalArgumentException("Unknown tile class '"+k+"'", e);}
			  catch (ClassCastException e) {throw new IllegalArgumentException("Invalid JSON-format, "+k+"-array does contain a non-object value", e);}
			  catch (JSONException e) {throw new IllegalArgumentException("Invalid JSON-format, no x or y value at: "+k, e);}
			  catch (ReflectiveOperationException e) {throw new IllegalArgumentException("Invalid Tile-implementation '"+k+"'", e);}
			  catch (ArrayIndexOutOfBoundsException e) {throw new IllegalArgumentException("This does not compute :O", e);}
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		int ssize = 100; //TODO should be in ui
		
		for (int y=0; y<map.length; y++) for (int x=0; x<map[y].length; x++) {
			map[x][y].debugRender(g, x*ssize, y*ssize, ssize);
			
			g.sr.begin(ShapeType.Filled);
			g.sr.setColor(map[x][y].getDebugCol());
			g.sr.rect(x*ssize, y*ssize, ssize, ssize);
		}
	}
	
	@Override
	public void dispose() {
		g.dispose();
	}
	
}
