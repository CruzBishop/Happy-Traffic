package info.zthings.ht.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public class RenderContextProvider implements Disposable {
	public SpriteBatch spriteBatch;
	public ShapeRenderer spriteRenderer;
	
	public RenderContextProvider(SpriteBatch spriteBatch, ShapeRenderer sr) {
		this.spriteBatch = spriteBatch;
		this.spriteRenderer = sr;
	}
	
	@Override
	public void dispose() {
		spriteBatch.dispose();
		spriteRenderer.dispose();
	}
	
}
