package com.axiom.operatio.scenes.production.controller;

import android.util.Log;
import android.view.MotionEvent;

import com.axiom.atom.engine.graphics.gles2d.Camera;
import com.axiom.atom.engine.sound.SoundRenderer;
import com.axiom.operatio.model.Production;
import com.axiom.operatio.model.ProductionRenderer;
import com.axiom.operatio.model.block.Block;
import com.axiom.operatio.model.buffer.Buffer;
import com.axiom.operatio.model.machines.Machine;
import com.axiom.operatio.model.transport.Conveyor;
import com.axiom.operatio.scenes.production.ProductionScene;


//  TODO Удаление блока производства
public class HandleBlockDelete {

    private ProductionScene scene;
    private Production production;
    private ProductionRenderer productionRenderer;

    private boolean dragging = false;
    private float cursorX, cursorY;
    private int lastCol, lastRow;

    public HandleBlockDelete(ProductionScene scn, Production prod, ProductionRenderer prodRender) {
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
                cursorX = worldX;
                cursorY = worldY;
                dragging = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                dragging = false;
                if (column >= 0 && row >= 0 && lastCol==column && lastRow==row) {
                    Block block = production.getBlockAt(column, row);
                    if (block!=null) {
                        if (scene.modePanel.getToggledButton()!=null) {
                            int choice = Integer.parseInt(scene.modePanel.getToggledButton());
                            if (choice==2) production.removeBlock(block);
                        }
                        Log.i("PROD COL=" + column + ", ROW=" + row, block.toString());
                    }
                }
        }
    }


}