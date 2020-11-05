package com.axiom.operatio.scenes.market;

import android.graphics.Color;

import com.axiom.atom.engine.core.geometry.AABB;
import com.axiom.atom.engine.graphics.GraphicsRender;
import com.axiom.atom.engine.graphics.gles2d.Camera;
import com.axiom.atom.engine.ui.widgets.Button;
import com.axiom.atom.engine.ui.widgets.Caption;
import com.axiom.atom.engine.ui.widgets.Panel;
import com.axiom.operatio.model.market.Market;
import com.axiom.operatio.scenes.inventory.MaterialsPanel;

import static android.graphics.Color.WHITE;

public class MarketPanel extends Panel {

    private Caption caption;
    private Market market;
    private double[] values;
    private int counter = 0;
    private int currentMarket = 0;
    private MaterialsPanel materialsPanel;
    private Button sellButton, buyButton;

    public MarketPanel(MaterialsPanel materialsPanel, Market market) {
        super();
        this.market = market;
        this.materialsPanel = materialsPanel;

        values = new double[96];
        counter = 0;

        setLocalBounds(900, 430, 1000, 550);
        setColor(0xCC505050);
        caption = new Caption("Market");
        caption.setTextScale(1.5f);
        caption.setTextColor(WHITE);
        caption.setLocalBounds(30, getHeight() - 100, 300, 100);
        addChild(caption);

        buyButton = new Button("BUY");
        buyButton.setLocalBounds(25, 25, 100, 60);
        buyButton.setTextScale(1.5f);
        buyButton.setTextColor(WHITE);
        buyButton.setColor(Color.RED);
        addChild(buyButton);

        sellButton = new Button("SELL");
        sellButton.setLocalBounds(175, 25, 100, 60);
        sellButton.setTextScale(1.5f);
        sellButton.setTextColor(WHITE);
        sellButton.setColor(Color.GREEN);
        addChild(sellButton);
    }


    public void updateValues() {
        synchronized (values) {
            values[counter] = market.getValue(currentMarket);
            counter++;
            if (counter >= values.length) {
                System.arraycopy(values, 1, values, 0,counter - 1);
                counter--;
            }
        }
    }


    @Override
    public void draw(Camera camera) {
        super.draw(camera);

        AABB wBounds = getWorldBounds();
        AABB scissor = getScissors();

        GraphicsRender.setZOrder(zOrder + 1);

        float floor = 200;
        float x = wBounds.min.x + 25;
        float y = wBounds.min.y + floor;
        float oldX = x;
        float oldY = y;
        GraphicsRender.drawLine(x,y,x + values.length * 10,y);
        for (int i=0; i<counter; i++) {
            x = wBounds.min.x + i * 10 + 25;
            y = wBounds.min.y + floor + (int) (values[i] * 5);
            if (oldY > y) GraphicsRender.setColor(Color.RED); else GraphicsRender.setColor(Color.GREEN);
            GraphicsRender.drawLine(oldX, oldY,x, y);
           // GraphicsRender.drawRectangle(x-3,y-3,6,6);
            oldX = x;
            oldY = y;
        }

        /*
        for (int i=0; i<Market.COMMODITY_COUNT; i++) {
            double demand = market.getValue(i);
            if (demand>=0) GraphicsRender.setColor(Color.GREEN); else GraphicsRender.setColor(Color.RED);
            GraphicsRender.drawLine(i * 25, getHeight() / 2, i * 25, (int) (getHeight() / 2 + demand));
        }*/

    }

}
