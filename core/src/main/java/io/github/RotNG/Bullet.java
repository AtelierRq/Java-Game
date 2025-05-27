package io.github.RotNG;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private static final float SPEED = 500f;
    private static final float RANGE = 500f;

    private Texture texture;
    private Rectangle bounds;
    private Vector2 position;
    private Vector2 velocity;
    private float distanceTraveled = 0;

    public Bullet(Vector2 startPos, Vector2 targetPos) {
        texture = new Texture("bullet2.png");
        position = new Vector2(startPos);
        velocity = targetPos.sub(startPos).nor().scl(SPEED);

        bounds = new Rectangle(position.x -50, position.y -50, texture.getWidth(), texture.getHeight());
    }

    public boolean update(float deltaTime) {
        Vector2 movement = new Vector2(velocity).scl(deltaTime);
        position.add(movement);
        distanceTraveled += movement.len();
        bounds.setPosition(position);

        return distanceTraveled < RANGE;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x -50, position.y -50);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
    }
}
