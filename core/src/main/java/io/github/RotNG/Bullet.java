package io.github.RotNG;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    //ustawienie zasięgu i prędkosci pocisków
    private static final float SPEED = 500f;
    private static final float RANGE = 500f;

    //składowe
    private Texture texture;
    private Rectangle bounds;
    private Vector2 position;
    private Vector2 velocity;
    private float distanceTraveled = 0;

    //konstruktor
    public Bullet(Vector2 startPos, Vector2 targetPos) {

        //ustawienie tekstury i pozycji
        texture = new Texture("bullet2.png");
        position = new Vector2(startPos); //ustawiienie pocisku tam gdzie jest gracz

        //predkosc
        velocity = targetPos.sub(startPos).nor().scl(SPEED);

        //hitboxy pociskow
        bounds = new Rectangle(position.x -50, position.y -50, texture.getWidth(), texture.getHeight());
    }

    //aktualizacji pozycji pocisku, oraz sprawdzanie czy nie osiągnął maksymalnego zasięgu
    public boolean update(float deltaTime) {
        Vector2 movement = new Vector2(velocity).scl(deltaTime);
        position.add(movement);
        distanceTraveled += movement.len();
        bounds.setPosition(position);

        return distanceTraveled < RANGE; //Jeśli pocisk przeleciał mniej niż max RANGE, zwraca true, jesli nie - false
    }

    //renderowanie grafiki pocisku
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x -50, position.y -50);
    }

    //getter - udostepnia innym klasom prostokąt kolizji
    public Rectangle getBounds() {
        return bounds;
    }

    //czyszczenie zasobów
    public void dispose() {
        texture.dispose();
    }
}
