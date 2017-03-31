package info.zthings.ht.test.tiles;

import org.json.JSONObject;

import com.badlogic.gdx.graphics.Color;

import info.zthings.ht.test.RenderContextProvider;

public class Goal implements Tile {
	private JSONObject properties;
	
	public Goal(JSONObject obj) {
		properties = obj;
	}
	
	@Override
	public Color getDebugCol() {
		return Color.GREEN;
	}
	
	@Override
	public void debugRender(RenderContextProvider g, int x, int y, int screenWidth, int screenHeight) {
		//STUB
		
	}

	@Override
	public JSONObject getProperties() {
		return properties;
	}
	
}
