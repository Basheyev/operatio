package com.axiom.operatio.model.production.buffer;


import android.content.res.Resources;

import com.axiom.atom.R;
import com.axiom.atom.engine.core.SceneManager;
import com.axiom.atom.engine.graphics.gles2d.Camera;
import com.axiom.atom.engine.graphics.renderers.Sprite;
import com.axiom.operatio.model.production.block.BlockRenderer;

public class ExportBufferRenderer extends BlockRenderer {

    protected static Sprite buffersFrames = null;
    protected ExportBuffer exportBuffer;
    protected Sprite sprite;

    public ExportBufferRenderer(ExportBuffer exportBuffer) {
        this.exportBuffer = exportBuffer;
        if (buffersFrames == null) {
            Resources resources = SceneManager.getResources();
            buffersFrames = new Sprite(resources, R.drawable.blocks, 8, 11);
        }
        sprite = buffersFrames.getAsSprite(66);
        sprite.setZOrder(7);
    }

    public void draw(Camera camera, float x, float y, float width, float height) {
        sprite.draw(camera,x,y, width, height);
    }


}


