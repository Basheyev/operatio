package com.axiom.operatio.scenes.production.controller;

import android.view.MotionEvent;

import com.axiom.atom.engine.graphics.gles2d.Camera;
import com.axiom.atom.engine.sound.SoundRenderer;
import com.axiom.operatio.model.Production;
import com.axiom.operatio.model.ProductionRenderer;
import com.axiom.operatio.model.block.Block;
import com.axiom.operatio.model.buffer.Buffer;
import com.axiom.operatio.model.machine.Machine;
import com.axiom.operatio.model.conveyor.Conveyor;
import com.axiom.operatio.scenes.production.ProductionScene;


// TODO Zoom in/out
public class CameraMoveHandler {

    private InputHandler inputHandler;
    private ProductionScene scene;
    private Production production;
    private ProductionRenderer productionRenderer;

    protected boolean dragging = false;
    private float cursorX, cursorY;
    private int lastCol, lastRow;

    public CameraMoveHandler(InputHandler inputHandler, ProductionScene scn, Production prod, ProductionRenderer prodRender) {
        this.inputHandler = inputHandler;
        this.production = prod;
        this.productionRenderer = prodRender;
        this.scene = scn;
    }

    public void onMotion(MotionEvent event, float worldX, float worldY) {
        int column = productionRenderer.getProductionColumn(worldX);
        int row = productionRenderer.getProductionRow(worldY);

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastCol = column;
                lastRow = row;
                dragging = true;
                cursorX = worldX;
                cursorY = worldY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (dragging) {
                    Camera camera = Camera.getInstance();
                    float x = camera.getX() + (cursorX - worldX);
                    float y = camera.getY() + (cursorY - worldY);
                    // Проверка границ карты
                    if (x - Camera.WIDTH / 2 < 0) x = Camera.WIDTH / 2;
                    if (y - Camera.HEIGHT / 2 < 0) y = Camera.HEIGHT / 2;
                    if (x + Camera.WIDTH / 2 > production.getColumns() * scene.initialCellWidth)
                        x = production.getColumns() * scene.initialCellWidth - Camera.WIDTH / 2;
                    if (y + Camera.HEIGHT / 2 > production.getRows() * scene.initialCellHeight)
                        y = production.getRows() * scene.initialCellHeight - Camera.HEIGHT / 2;
                    camera.lookAt(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (dragging && column >= 0 && row >= 0 && lastCol==column && lastRow==row) {
                    Block block = production.getBlockAt(column, row);
                    if (block!=null) {
                        if (block instanceof Machine) SoundRenderer.playSound(scene.snd1);
                        if (block instanceof Conveyor) SoundRenderer.playSound(scene.snd2);
                        if (block instanceof Buffer) SoundRenderer.playSound(scene.snd3);

                    }
                    dragging = false;
                }
                if (production.isBlockSelected() && production.getSelectedCol()==column && production.getSelectedRow()==row) {
                    production.unselectBlock();
                } else production.selectBlock(column, row);

        }
    }

}