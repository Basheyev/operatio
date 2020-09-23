package com.axiom.operatio.scenes.mainmenu;

import android.graphics.Color;
import android.util.Log;

import com.axiom.atom.engine.core.SceneManager;
import com.axiom.atom.engine.graphics.gles2d.Camera;
import com.axiom.atom.engine.ui.listeners.ClickListener;
import com.axiom.atom.engine.ui.widgets.Button;
import com.axiom.atom.engine.ui.widgets.Panel;
import com.axiom.atom.engine.ui.widgets.Widget;
import com.axiom.operatio.scenes.production.ProductionScene;

import java.util.ArrayList;

public class MenuPanel extends Panel {

    public final int panelColor = 0xCC505050;
    protected ProductionScene pc = null;
    protected String toggledButton;

    protected ClickListener listener = new ClickListener() {
        @Override
        public void onClick(Widget w) {
            int choice = Integer.parseInt(w.getTag());
            switch (choice) {
                case 1:
                    SceneManager sm = SceneManager.getInstance();
                    if (pc==null) {
                        pc = new ProductionScene();
                        sm.addGameScene(pc);
                    }
                    sm.setActiveScene(pc.getSceneName());
                    break;
                case 2:
                    break;
                default:
                    SceneManager.exitGame();
            }
        }
    };


    public MenuPanel() {
        super();
        setLocalBounds(Camera.WIDTH/2 - 300,Camera.HEIGHT/2 - 300,600,600);
        setColor(panelColor);
        buildButtons();
    }


    private void buildButtons() {
        Widget button;
        String caption;

        for (int i =1; i<4; i++) {
            if (i==1) caption = "START GAME"; else
            if (i==2) caption = "OPTIONS"; else caption = "EXIT GAME";
            button = new Button(caption);
            button.setTag(""+i);
            button.setLocalBounds(50, 550 - ( i * 150), 500, 120);
            button.setColor(Color.GRAY);
            button.setClickListener(listener);
            this.addChild(button);
        }

    }

    public String getToggledButton() {
        return toggledButton;
    }


}
