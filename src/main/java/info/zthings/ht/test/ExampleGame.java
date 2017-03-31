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
	public int height;
	public int width;
	public RenderContextProvider contextProvider;
	
	@Override
	public void create() {
		contextProvider = new RenderContextProvider(new SpriteBatch(), new ShapeRenderer());
		Gdx.graphics.setResizable(false);
		
		//JsonReader reader = new JsonReader();
		//JsonValue json = reader.parse(Gdx.files.internal("testmap.json"));
		JSONObject json = new JSONObject(Gdx.files.internal("testmap.json").readString());
		System.out.println("Reading map with version: "+json.getInt("version"));
		height = json.getInt("height");
		width = json.getInt("width");
		map = new Tile[height][width];
		System.out.println("Map is ["+map.length+"x"+map[0].length+"]");
		
		HashMap<String, Class<? extends Tile>> tiletypes = new HashMap<>();
		Reflections reflections = new Reflections("info.zthings.ht");
		for (Class<? extends Tile> reflectionClass : reflections.getSubTypesOf(Tile.class)) tiletypes.put(reflectionClass.getSimpleName().toLowerCase(), reflectionClass);

		for (String key : json.keySet()) {
			if (key.equals("width") || key.equals("height") || key.equals("version")) continue;
			try {
				Constructor<? extends Tile> constructor = tiletypes.get(key).getConstructor(JSONObject.class);
				for (Object obj : json.getJSONArray(key)) {
					JSONObject objectOne = (JSONObject) obj;
					if (TileField.class.isAssignableFrom(tiletypes.get(key))) {
						boolean numberedX = objectOne.keySet().contains("x1"); //TODO Maybe find the class and load it per-class instead of this
						System.out.println("Numbered coordinate setting is " + numberedX);

						if (numberedX) {
							System.out.println("x1 = " + objectOne.getInt("x1"));
							System.out.println("y1 = " + objectOne.getInt("y1"));
							System.out.println("x2 = " + objectOne.getInt("x2"));
							System.out.println("y2 = " + objectOne.getInt("y2"));
							for (int yCoordinate = objectOne.getInt("y1"); yCoordinate <= objectOne.getInt("y2"); yCoordinate++) {
								for (int xCoordinate = objectOne.getInt("x1"); xCoordinate <= objectOne.getInt("x2"); xCoordinate++) {
									JSONObject objectToInsert = new JSONObject();//objectOne;
									objectToInsert.put("x", xCoordinate);
									objectToInsert.put("y", yCoordinate);

									map[yCoordinate][xCoordinate] = constructor.newInstance(objectToInsert);
								}
							}
						} else {
							System.out.println("x = " + objectOne.getInt("x"));
							System.out.println("y = " + objectOne.getInt("y"));
							map[objectOne.getInt("y")][objectOne.getInt("x")] = constructor.newInstance(objectOne);
						}

					} else map[objectOne.getInt("y")][objectOne.getInt("x")] = constructor.newInstance(objectOne);
				}
			} catch (NullPointerException e) {throw new IllegalArgumentException("Unknown tile class '"+key+"'", e);}
			  catch (ClassCastException e) {throw new IllegalArgumentException("Invalid JSON-format, "+key+"-array does contain a non-object value", e);}
			  catch (JSONException e) {throw new IllegalArgumentException("Invalid JSON-format, no x or y value at: "+key, e);}
			  catch (ReflectiveOperationException e) {throw new IllegalArgumentException("Invalid Tile-implementation '"+key+"'", e);}
			  catch (ArrayIndexOutOfBoundsException e) {throw new IllegalArgumentException("This does not compute :O", e);}
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int tileWidth = Gdx.graphics.getBackBufferWidth() / width;
        int tileHeight = Gdx.graphics.getBackBufferHeight() / height;
		
		for (int y=0; y<map.length; y++) for (int x=0; x<map[y].length; x++) {
		    if (map[x][y] == null) {
		        //Nothing at this location
		        continue;
            }
			map[x][y].debugRender(contextProvider, x*tileWidth, y*tileHeight, tileWidth, tileHeight);
			
			contextProvider.spriteRenderer.begin(ShapeType.Filled);
			contextProvider.spriteRenderer.setColor(map[x][y].getDebugCol());
			contextProvider.spriteRenderer.rect(x*tileWidth, y*tileHeight, tileWidth, tileHeight);
			contextProvider.spriteRenderer.end();
		}
	}
	
	@Override
	public void dispose() {
		contextProvider.dispose();
	}
	
}
