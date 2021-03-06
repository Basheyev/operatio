package com.axiom.atom.engine.core;

import android.content.res.Resources;
import android.view.DragEvent;
import android.view.MotionEvent;

import com.axiom.atom.engine.graphics.gles2d.Camera;
import com.axiom.atom.engine.input.ScaleEvent;
import com.axiom.atom.engine.ui.widgets.Panel;
import com.axiom.atom.engine.ui.widgets.Widget;

import java.util.ArrayList;


/**
 * Основной класс игры с которым работает игровой ицкл, графика и ввод
 * (C) Atom Engine, Bolat Basheyev 2020
 */
public abstract class GameScene {

    protected SceneManager sceneManager;
    protected Resources resources;
    public boolean started = false;

    protected Widget sceneWidget = new Widget() { };                     // Корневой виджет сцены
    protected ArrayList<GameObject> objects = new ArrayList<>();         // Объекты сцены
    protected ArrayList<GameObject> addedObjects = new ArrayList<>();    // Служебный список добавления объектов
    protected ArrayList<GameObject> deletedObjects = new ArrayList<>();  // Служебный список удаления объектов

    public abstract String getSceneName();
    public abstract void startScene();                // Вызывается при запуске сцены
    public abstract void disposeScene();              // Вызывается из потока GameLoop
    public abstract void changeScene();               // Вызывается при смене сцены на другую

    //----------------------------------------------------------------------------------------
    // Важно: избегайте создание объектов в этих методах, так они вызываются 60 раз в секунду
    // это будет вызывать задержки при работе Garbage Collector (сборщика мусора
    //----------------------------------------------------------------------------------------
    public abstract void updateScene(float deltaTime);   // Вызывается из потока GameLoop
    public abstract void preRender(Camera camera);       // Вызывается из потока GLThread
    public abstract void postRender(Camera camera);      // Вызывается из потока GLThread
    public abstract void onMotion(MotionEvent event, float worldX, float worldY); // GameLoop
    public void onScale(ScaleEvent event, float worldX, float worldY) {}


    /**
     * Возвращает объекты сцены
     * Важно: не изменяйте список объектов напрямую,<br>
     * вместо этого используйте методы addObject и removeObject;
     * @return список объектов
     */
    public ArrayList<GameObject> getSceneObjects() { return objects; }

    /**
     * Возвращает первый найденный объект сцене в указанной точке в мировых координатах
     * @param worldX координата в игровом мире
     * @param worldY координата в игровом мире
     * @return найденный объект или null если объект не найден
     */
    public GameObject getSceneObjectAt(float worldX, float worldY) {
        if (objects.size()==0) return null;
        GameObject object;
        int amount = objects.size();
        for (int i=amount-1; i>=0; i--) {
            object = objects.get(i);
            if (object.getWorldBounds().collides(worldX,worldY)) {
                return object;
            }
        }
        return null;
    }


    public void addObject(GameObject obj) {
        // исключаем повторное добавление одного и того же объекта
        if (!getSceneObjects().contains(obj)) addedObjects.add(obj);
    }

    public void removeObject(GameObject obj) { deletedObjects.add(obj); }

    public Widget getSceneWidget() {
       return sceneWidget;
    }

    public Resources getResources() {
        return resources;
    }

    public SceneManager getSceneManager() { return sceneManager; }

}
