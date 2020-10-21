package com.axiom.operatio.scenes.production;

import android.view.MotionEvent;

import com.axiom.atom.engine.core.GameScene;
import com.axiom.atom.engine.graphics.GraphicsRender;
import com.axiom.atom.engine.graphics.gles2d.Camera;
import com.axiom.atom.engine.graphics.renderers.BatchRender;
import com.axiom.atom.engine.input.ScaleEvent;
import com.axiom.operatio.model.production.ProductionRenderer;
import com.axiom.operatio.model.production.Production;
import com.axiom.operatio.scenes.production.view.BlocksPanel;
import com.axiom.operatio.scenes.production.controller.InputHandler;
import com.axiom.operatio.scenes.production.view.ModePanel;
import com.axiom.operatio.scenes.production.view.AdjustmentPanel;
import com.axiom.operatio.scenes.production.view.ProductionSceneUI;
import com.axiom.operatio.scenes.inventory.InventoryScene;

import org.json.JSONObject;


public class ProductionScene extends GameScene {

    private Production production;
    private InputHandler inputHandler;
    private ProductionRenderer productionRenderer;

    // Надо сделать сеттеры и геттеры
    public BlocksPanel blocksPanel;
    public ModePanel modePanel;
    public AdjustmentPanel adjustmentPanel;
    public float initialCellWidth = 128;                  // Ширина клетки
    public float initialCellHeight = 128;                 // Высота клетки
    private boolean initialized = false;

    public ProductionScene() {
        production = Production.getInstance(25,20);
    }

    public ProductionScene(JSONObject jsonProduction) {
        production = Production.getInstance(jsonProduction);
    }

    @Override
    public String getSceneName() {
        return "Production";
    }

    @Override
    public void startScene() {
        if (!initialized) {
            sceneManager.addGameScene(new InventoryScene());
            productionRenderer = new ProductionRenderer(production, initialCellWidth, initialCellHeight);
            inputHandler = new InputHandler(this, production, productionRenderer);
            ProductionSceneUI.buildUI(this, getResources(), getSceneWidget(), production);
            blocksPanel = ProductionSceneUI.getBlocksPanel();
            modePanel = ProductionSceneUI.getModePanel();
            adjustmentPanel = ProductionSceneUI.getAdjustmentPanel();
            initialized = true;
        }
    }

    @Override
    public void changeScene() {

    }

    @Override
    public void disposeScene() {
        production.clearBlocks();
    }

    @Override
    public void updateScene(float deltaTimeNs) {
        production.process();
    }

    @Override
    public void preRender(Camera camera) {
        productionRenderer.draw(camera);
    }

    protected StringBuffer fps = new StringBuffer(100);

    @Override
    public void postRender(Camera camera) {
        fps.delete(0, fps.length());
        fps.append("FPS:").append(GraphicsRender.getFPS())
            .append(" Quads:").append(BatchRender.getEntriesCount())
            .append(" Calls:").append(BatchRender.getDrawCallsCount())
            .append(" Time:").append(GraphicsRender.getRenderTime())
            .append("ms");
        float x = camera.getMinX();
        float y = camera.getMinY();
        GraphicsRender.setZOrder(2000);
        GraphicsRender.setColor(0,0,0,1);
        GraphicsRender.drawText(fps, x + 600,y + 20, 2f);
    }

    @Override
    public void onMotion(MotionEvent event, float worldX, float worldY) {
        inputHandler.onMotion(event, worldX, worldY);
    }

    @Override
    public void onScale(ScaleEvent event, float worldX, float worldY) {
        inputHandler.onScale(event, worldX, worldY);
    }


    public Production getProduction() {
        return production;
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
