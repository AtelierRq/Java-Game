package io.github.RotNG;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.List;


public class Player {
    private Texture texture;
    private Rectangle bounds;

    private static final float SPEED = 200;
    private static final float SCALE = 0.5f;

    public Player() {
        texture = new Texture("Wizard.png");
        float width = texture.getWidth() * SCALE;
        float height = texture.getHeight() * SCALE;
        bounds = new Rectangle(0, 0, width, height);
    }

    public void update(float deltaTime, List<Tree> obstacles) {
        float oldX = bounds.x;
        float oldY = bounds.y;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) bounds.y += SPEED * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) bounds.y -= SPEED * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) bounds.x -= SPEED * deltaTime;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) bounds.x += SPEED * deltaTime;

        // Sprawdzenie kolizji
        for (Tree tree : obstacles) {
            if (bounds.overlaps(tree.getBounds())) {
                bounds.x = oldX;
                bounds.y = oldY;
                break;
            }
        }
    }

    public void render(SpriteBatch batch, OrthographicCamera camera) {
        float screenX = bounds.x;
        float screenY = bounds.y;
        batch.draw(texture, screenX, screenY, bounds.width * SCALE, bounds.height * SCALE);
    }

    public float getX() { return bounds.x + bounds.width / 2; }
    public float getY() { return bounds.y + bounds.height / 2; }

    public void dispose() {
        texture.dispose();
    }
}
