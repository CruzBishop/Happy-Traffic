package info.zthings.ht.test.tiles;

import org.json.JSONObject;

import com.badlogic.gdx.graphics.Color;

import info.zthings.ht.test.RenderContextProvider;

public class Road implements TileField {
	
	public Road(JSONObject obj) {
		//STUB
	}
	
	@Override
	public Color getDebugCol() {
		//STUB
		return Color.GRAY;
	}
	
	@Override
	public void debugRender(RenderContextProvider g, int x, int y, int ssize) {
		//STUB
		
	}
	
}
