package info.zthings.ht.test.tiles;

import com.badlogic.gdx.graphics.Color;

import info.zthings.ht.test.RenderContextProvider;

public interface Tile {
	public abstract Color getDebugCol();
	public abstract void debugRender(RenderContextProvider g, int x, int y, int screenWidth, int screenHeight);
}
