package info.zthings.ht.test.tiles;

import org.json.JSONObject;

import com.badlogic.gdx.graphics.Color;

import info.zthings.ht.test.RenderContextProvider;

public class Road implements TileField {
	private JSONObject properties;
	
	public Road(JSONObject obj) {
		properties = obj;
	}
	
	@Override
	public Color getDebugCol() {
		return Color.GRAY;
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
