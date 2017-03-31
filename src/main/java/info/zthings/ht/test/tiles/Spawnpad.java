package info.zthings.ht.test.tiles;

import org.json.JSONObject;

import com.badlogic.gdx.graphics.Color;

import info.zthings.ht.test.RenderContextProvider;

public class Spawnpad implements Tile {
	
	public Spawnpad(JSONObject obj) {
		//STUB
	}
	
	@Override
	public Color getDebugCol() {
		//STUB
		return Color.GOLD;
	}
	
	@Override
	public void debugRender(RenderContextProvider g, int x, int y, int screenWidth, int screenHeight) {
		//STUB
		
	}
	
}
