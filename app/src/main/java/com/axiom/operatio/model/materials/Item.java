package com.axiom.operatio.model.materials;

import com.axiom.operatio.model.Production;
import com.axiom.operatio.model.block.Block;

/**
 * Содержит информацию о предмете
 */
public class Item {

    protected Material material;            // Материал
    protected Block owner;                  // Блок владелец
    protected long cycleOwned;              // Цикл производства (захват)
    protected long timeOwned;               // Время в миллисекундах (захват)

    //----------------------------------------------------------------------------

    public Item(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public Block getOwner() {
        return owner;
    }

    public long getCycleOwned() {
        return cycleOwned;
    }

    public long getTimeOwned() { return timeOwned; }

    public void setOwner(Block owner) {
        this.owner = owner;
        this.cycleOwned = Production.getCurrentCycle();
        this.timeOwned = System.currentTimeMillis();
    }


}