package com.sfaci.holalibgdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.sfaci.holalibgdx.managers.ResourceManager;

/**
 * Created by dam on 13/01/17.
 */
public class Jugador extends Personaje {

    public Jugador(float x, float y) {
        super(x, y, "jugador_derecha", "jugador_izquierda");
    }

    public void mover(Vector2 movimiento) {
        posicion.add(movimiento);

        rect.setPosition(posicion);
    }
}
