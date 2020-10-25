package com.axiom.operatio.scenes.inventory;

import android.view.MotionEvent;

import com.axiom.atom.R;
import com.axiom.atom.engine.core.GameScene;
import com.axiom.atom.engine.core.SceneManager;
import com.axiom.atom.engine.graphics.GraphicsRender;
import com.axiom.atom.engine.graphics.gles2d.Camera;
import com.axiom.atom.engine.graphics.renderers.BatchRender;
import com.axiom.atom.engine.graphics.renderers.Sprite;
import com.axiom.atom.engine.sound.SoundRenderer;
import com.axiom.atom.engine.ui.listeners.ClickListener;
import com.axiom.atom.engine.ui.widgets.Button;
import com.axiom.atom.engine.ui.widgets.Widget;
import com.axiom.operatio.model.production.Production;
import com.axiom.operatio.scenes.market.MarketPanel;

/**
 * Сцена склада
 */
public class InventoryScene extends GameScene {

    protected boolean initialized = false;
    protected Production production;
    protected MaterialsPanel materialsPanel;
    protected TechnologyPanel technologyPanel;
    protected MarketPanel marketPanel;
    protected static Sprite background;
    protected static int tickSound;

    public InventoryScene(Production production) {
        this.production = production;
    }

    @Override
    public String getSceneName() {
        return "Inventory";
    }

    @Override
    public void startScene() {
        if (!initialized) buildUI();
        materialsPanel.updateData();
    }

    @Override
    public void changeScene() {

    }

    @Override
    public void disposeScene() {

    }

    @Override
    public void updateScene(float deltaTime) {

    }

    @Override
    public void preRender(Camera camera) {
        background.zOrder = 0;
        background.draw(camera,camera.getMinX(),camera.getMinY(), Camera.WIDTH,Camera.HEIGHT);
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
        GraphicsRender.setColor(1,1,1,1);
        GraphicsRender.drawText(fps, x + 600,y + 20, 2f);
    }

    @Override
    public void onMotion(MotionEvent event, float worldX, float worldY) {

    }


    protected void buildUI() {

        background = new Sprite(SceneManager.getResources(), R.drawable.background);
        tickSound = SoundRenderer.loadSound(R.raw.tick_snd);

        ClickListener exitListener = new ClickListener() {
            @Override
            public void onClick(Widget w) {
                SoundRenderer.playSound(tickSound);
                SceneManager.getInstance().setActiveScene("Production");
            }
        };

        Widget widget = getSceneWidget();

        Button exitButton = new Button("Production");
        exitButton.setTextColor(1,1,1,1);
        exitButton.setTextScale(1.5f);
        exitButton.setLocalBounds(1600,995,300,70);
        exitButton.setColor(0.8f, 0.5f, 0.5f, 0.9f);
        exitButton.setClickListener(exitListener);
        widget.addChild(exitButton);

        materialsPanel = new MaterialsPanel(production,this);
        widget.addChild(materialsPanel);

        technologyPanel = new TechnologyPanel(materialsPanel);
        widget.addChild(technologyPanel);

        marketPanel = new MarketPanel(materialsPanel, production.getMarket());
        widget.addChild(marketPanel);

        initialized = true;

    }


}
