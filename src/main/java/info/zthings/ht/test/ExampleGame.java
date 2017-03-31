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
	public RenderContextProvider renderContext;
	
	@Override
	public void create() {
		renderContext = new RenderContextProvider(new SpriteBatch(), new ShapeRenderer());
		Gdx.graphics.setResizable(false);
		
		//JsonReader reader = new JsonReader();
		//JsonValue json = reader.parse(Gdx.files.internal("testmap.json"));
		JSONObject json = new JSONObject(Gdx.files.internal("testmap.json").readString());
		System.out.println("Reading map with version: "+json.getInt("version"));
		height = json.getInt("height");
		width = json.getInt("width");
		map = new Tile[height][width];
		System.out.println("Map is ["+map.length+"x"+map[0].length+"]");
	
		//LOADING ALL DEFINED TILE-TYPES (IMPLEMENTORS OF Tile) FROM PACKAGE
		HashMap<String, Class<? extends Tile>> tileTypes = new HashMap<>();
		Reflections refPackage = new Reflections("info.zthings.ht");
		for (Class<? extends Tile> reflectionClass : refPackage.getSubTypesOf(Tile.class)) tileTypes.put(reflectionClass.getSimpleName().toLowerCase(), reflectionClass);
		
		//ITERATE THROUGH ALL TILE-CLASSES DEFINED IN JSON
		for (String classKey : json.keySet()) {
			if (classKey.equals("width") || classKey.equals("height") || classKey.equals("version")) continue;
			try {
				Constructor<? extends Tile> tileClassConst = tileTypes.get(classKey).getConstructor(JSONObject.class);
				//ITERATE TROUGH CURRENT CLASS
				for (Object obj : json.getJSONArray(classKey)) {
					JSONObject currTile = (JSONObject) obj;
					//IS CURRENT TILE CLASS A FIELD? (IMPLEMENTOR OF TileField)
					if (TileField.class.isAssignableFrom(tileTypes.get(classKey))) {
						//YES, CREATE TILE OF CURRENT CLASS WITH GIVEN PROPERTIES AT EVERY LOCATION IN THE FIELD
						for (int y = currTile.getInt("y1"); y <= currTile.getInt("y2"); y++)
						for (int x = currTile.getInt("x1"); x <= currTile.getInt("x2"); x++) {
							//COPYING THE PROPERTIES INTO A SECOND JSONObject, JAVA PASSES MAPS BY REFERENCE?
							HashMap<String, Object> m = new HashMap<>();
							m.putAll(currTile.toMap());
							JSONObject properties = new JSONObject(m);
							
							//REMOVE THE FIELD PROPERTIES, THEY ARE INDIVIDUAL TILES
							properties.remove("x1");
							properties.remove("x2");
							properties.remove("y1");
							properties.remove("y2");
							//PUT IN INDIVIDUAL COORDINATES
							properties.put("x", x);
							properties.put("y", y);
							
							//PUT THE NEW TILE IN
							if (map[y][x] != null) {
								if (map[y][x].getClass().getSimpleName().toLowerCase().equals(classKey)) {
									//System.err.println("WARNING: Dirty json, non-overridable double "+classKey+" on ("+x+","+y+")");
									HashMap<String, Object> fusedProperties = new HashMap<>();
									fusedProperties.putAll(map[y][x].getProperties().toMap());
									fusedProperties.putAll(properties.toMap());
									
									map[y][x] = tileClassConst.newInstance(new JSONObject(fusedProperties));
								} else throw new IllegalStateException("There is already an "+map[y][x].getClass().getSimpleName().toLowerCase()+" at ("+x+","+y+"), can't override with "+classKey);
								//TODO actually add override behavior
							} else map[y][x] = tileClassConst.newInstance(properties);
						} //FIXME fill in gaps with walkway (change in json?)
					} else map[currTile.getInt("y")][currTile.getInt("x")] = tileClassConst.newInstance(currTile);
				}
			} catch (NullPointerException e) {throw new IllegalArgumentException("Unknown tile class '"+classKey+"'", e);}
			  catch (ClassCastException e) {throw new IllegalArgumentException("Invalid JSON-format, "+classKey+"-array does contain a non-object value", e);}
			  catch (JSONException e) {throw new IllegalArgumentException("Invalid JSON-format, no x or y value at: "+classKey, e);}
			  catch (ReflectiveOperationException e) {throw new IllegalArgumentException("Invalid Tile-implementation '"+classKey+"'", e);}
			  catch (ArrayIndexOutOfBoundsException e) {throw new IllegalArgumentException("This does not compute :O", e);}
		}
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int tileWidth = Gdx.graphics.getBackBufferWidth() / width;
        int tileHeight = Gdx.graphics.getBackBufferHeight() / height;
		
		for (int y=0; y<map.length; y++) for (int x=0; x<map[y].length; x++) {
		    if (map[x][y] == null) continue;
			map[x][y].debugRender(renderContext, x*tileWidth, y*tileHeight, tileWidth, tileHeight);
			
			renderContext.shapeRenderer.begin(ShapeType.Filled);
			renderContext.shapeRenderer.setColor(map[x][y].getDebugCol());
			renderContext.shapeRenderer.rect(x*tileWidth, y*tileHeight, tileWidth, tileHeight);
			renderContext.shapeRenderer.end();
		}
	}
	
	@Override
	public void dispose() {
		renderContext.dispose();
	}
	
}
