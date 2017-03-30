package info.zthings.ht.test;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

public class RenderContextProvider implements Disposable {
	public SpriteBatch sb;
	public ShapeRenderer sr;
	
	public RenderContextProvider(SpriteBatch sb, ShapeRenderer sr) {
		this.sb = sb;
		this.sr = sr;
	}
	
	@Override
	public void dispose() {
		sb.dispose();
		sr.dispose();
	}
	
}
