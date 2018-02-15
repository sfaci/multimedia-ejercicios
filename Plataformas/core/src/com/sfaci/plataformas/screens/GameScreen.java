package com.sfaci.plataformas.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.sfaci.plataformas.characters.Enemigo;
import com.sfaci.plataformas.characters.Mario;
import com.sfaci.plataformas.managers.R;
import org.w3c.dom.css.Rect;

import static com.sfaci.plataformas.utils.Constantes.ALTURA_EN_CELDAS;
import static com.sfaci.plataformas.utils.Constantes.ANCHURA_CELDA;
import static com.sfaci.plataformas.utils.Constantes.ANCHURA_EN_CELDAS;


/**
 * Pantalla de juego
 */
public class GameScreen implements Screen {

    Batch batch;
    OrthographicCamera camara;
    TiledMap mapa;
    OrthogonalTiledMapRenderer mapRenderer;
    Mario mario;
    Array<Enemigo> enemigos;

    @Override
    public void show() {

        camara = new OrthographicCamera();
        // Fija la anchura y altura de la camara en base al número de tiles que se mostrarán
        camara.setToOrtho(false, ANCHURA_EN_CELDAS * ANCHURA_CELDA, ALTURA_EN_CELDAS * ANCHURA_CELDA);
        camara.update();

        mapa = new TmxMapLoader().load("levels/level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(mapa);
        batch = mapRenderer.getBatch();

        mapRenderer.setView(camara);

        mario = new Mario(0, 100, 3, R.getTextura("mario_idle_right"));
        cargarEnemigos();
    }

    @Override
    public void render(float dt) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();

        controlarCamara();
        comprobarColisiones();

        batch.begin();
        mario.render(batch);
        for (Enemigo enemigo : enemigos)
            enemigo.render(batch);
        batch.end();

        comprobarTeclado(dt);
        mario.update(dt);
        for (Enemigo enemigo : enemigos)
            enemigo.update(dt);
    }

    /**
     * Comprueba las colisiones con los objetos que forman el suelo
     */
    private void comprobarColisiones() {

        MapLayer capaSuelo = mapa.getLayers().get("objects");
        for (MapObject objetoMapa : capaSuelo.getObjects()) {
            if (objetoMapa.getProperties().containsKey("suelo")) {
                RectangleMapObject objectoRect = (RectangleMapObject) objetoMapa;
                Rectangle rect = objectoRect.getRectangle();
                // Comprueba si el jugador colisiona con algún objeto suelo
                if (mario.rect.overlaps(rect)) {
                    mario.posicion.y = rect.y + rect.getHeight();
                    mario.rect.y = mario.posicion.y;
                    mario.saltando = false;
                }
                // Comprueba si algún enemigo colisiona con algún objeto suelo
                for (Enemigo enemigo : enemigos) {
                    if (enemigo.rect.overlaps(rect)) {
                        enemigo.posicion.y = rect.y + rect.getHeight();
                        enemigo.rect.y = enemigo.posicion.y;
                    }
                }
            }
        }
    }

    /**
     * Comprueba la entrada de teclado
     * @param dt
     */
    private void comprobarTeclado(float dt) {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            mario.desplazar(50 * dt);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            mario.desplazar(-50 * dt);
        }
        else
            mario.estado = Mario.Estado.QUIETO;

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            if (!mario.saltando) {
                mario.saltar();
            }
        }
    }

    private void cargarEnemigos() {

        enemigos = new Array<Enemigo>();

        MapLayer capaObjetos = mapa.getLayers().get("objects");
        for (MapObject objetoMapa : capaObjetos.getObjects()) {
            if (objetoMapa.getProperties().containsKey("enemy")) {
                if (objetoMapa instanceof TiledMapTileMapObject) {
                    TiledMapTileMapObject objetoRect = (TiledMapTileMapObject) objetoMapa;
                    Enemigo enemigo = new Enemigo(objetoRect.getX(), objetoRect.getY(), 1, R.getTextura("enemy"));
                    enemigos.add(enemigo);
                }
            }
        }
    }

    /**
     * Controla la cámara para que siempre enfoque al jugador
     */
    private void controlarCamara() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}