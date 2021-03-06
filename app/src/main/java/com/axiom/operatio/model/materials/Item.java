package com.axiom.operatio.model.materials;

import com.axiom.operatio.model.common.JSONSerializable;
import com.axiom.operatio.model.production.Production;
import com.axiom.operatio.model.production.block.Block;
import com.axiom.operatio.model.production.inserter.Inserter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Содержит информацию о предмете
 */
public class Item implements JSONSerializable {

    protected Material material;        // Материал предмета
    protected Block owner;              // Блок владелец
    protected long cycleOwned;          // Цикл производства (захват)
    protected long timeOwned;           // Время в миллисекундах (захват)
    protected int sourceDirection;      // Направление из которого пришел предмет
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

    public void setOwner(Production production, Block owner) {
        Block previousOwner = this.owner;
        this.owner = owner;
        this.cycleOwned = production.getCurrentCycle();
        this.timeOwned = production.getClock();
        this.sourceDirection = findSourceDirection(previousOwner);
    }


    private int findSourceDirection(Block sourceBlock) {
        if (sourceBlock == null || sourceBlock instanceof Inserter) return Block.NONE; else
        if (owner.getNeighbour(Block.LEFT)==sourceBlock) return Block.LEFT; else
        if (owner.getNeighbour(Block.UP)==sourceBlock) return Block.UP; else
        if (owner.getNeighbour(Block.RIGHT)==sourceBlock) return Block.RIGHT; else
        if (owner.getNeighbour(Block.DOWN)==sourceBlock) return Block.DOWN; else {
            return Block.NONE;
        }
    }

    public int getSourceDirection() {
        return sourceDirection;
    }


    //---------------------------------------------------------------------------

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("material", material.getID());
            jsonObject.put("cycleOwned", cycleOwned);
            jsonObject.put("timeOwned", timeOwned);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }


    public static Item deserialize(JSONObject jsonObject, Block owner) {
        try {
            int materialID = jsonObject.getInt("material");
            Material material = Material.getMaterial(materialID);
            Item item = new Item(material);
            item.owner = owner;
            item.cycleOwned = jsonObject.getLong("cycleOwned");
            item.timeOwned = jsonObject.getLong("timeOwned");
            return item;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}
