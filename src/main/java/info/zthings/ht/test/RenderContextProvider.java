package info.zthings.ht.test;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public class RenderContextProvider implements Disposable {
	public SpriteBatch spriteBatch;
	public ShapeRenderer shapeRenderer;
	public BitmapFont font;
	
	public RenderContextProvider(SpriteBatch spriteBatch, ShapeRenderer shapeRenderer) {
		this.spriteBatch = spriteBatch;
		this.shapeRenderer = shapeRenderer;
		this.font = new BitmapFont();
	}
	
	@Override
	public void dispose() {
		spriteBatch.dispose();
		shapeRenderer.dispose();
		font.dispose();
	}
	
}
