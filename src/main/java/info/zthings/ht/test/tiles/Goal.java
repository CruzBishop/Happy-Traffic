package info.zthings.ht.test.tiles;

import org.json.JSONObject;

import com.badlogic.gdx.graphics.Color;

import info.zthings.ht.test.RenderContextProvider;

public class Goal implements Tile {
	
	public Goal(JSONObject obj) {
		//STUB
	}
	
	@Override
	public Color getDebugCol() {
		//STUB
		return Color.CHARTREUSE;
	}
	
	@Override
	public void debugRender(RenderContextProvider g, int x, int y, int ssize) {
		//STUB
		
	}
	
}
