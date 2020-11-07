package com.axiom.operatio.model.market;

import com.axiom.atom.engine.data.JSONSerializable;
import com.axiom.operatio.model.inventory.Inventory;
import com.axiom.operatio.model.materials.Item;
import com.axiom.operatio.model.materials.Material;
import com.axiom.operatio.model.production.Production;

import org.json.JSONObject;

public class Market implements JSONSerializable {

    public static final int COMMODITY_COUNT = 64;
    private double largeCycle;
    private final double[] faceValue;
    private final double[] marketValue;
    private final double[] marketCycle;
    private final double[] marketBias;
    private long cycle;

    public Market() {
        faceValue = new double[COMMODITY_COUNT];
        marketValue = new double[COMMODITY_COUNT];
        marketCycle = new double[COMMODITY_COUNT];
        marketBias = new double[COMMODITY_COUNT];
        largeCycle = Math.random() * 2 * Math.PI;
        for (int i=0; i<faceValue.length; i++) {
            faceValue[i] = Material.getMaterial(i).getPrice();
            marketValue[i] = faceValue[i];
            marketBias[i] = Math.random() * 2 * Math.PI;
            marketCycle[i] = marketBias[i];
        }
        cycle = 0;
    }


    public void process() {
        for (int i=0; i<marketValue.length; i++) {
            marketValue[i] = evaluateNextValue(i);
        }
        cycle++;
    }

    private double evaluateNextValue(int commodity) {
        // Считаем среднюю цена между номиналом и текущей (более менее справедливая)
        double value = (faceValue[commodity] + marketValue[commodity]) / 2;
        // Добавляем до +-5% влияния большого цикла рынка
        value += (value * 0.05) * Math.cos(largeCycle + marketBias[commodity]);
        // Добавляем до +-2% влияния короткого цикла рынка
        value += (value * 0.02) * Math.sin(marketCycle[commodity]);
        // Добавляем до +-10% случайного шума
        value += (value * 0.10) * (Math.random() - 0.5);
        // Устанавливаем новую цену товара на рынке
        marketValue[commodity] = value;
        // Делаем шаг по короткому циклу рынка
        marketCycle[commodity] += 0.2;
        // Далем маленький шаг по длинному циклу рынка
        largeCycle += 0.0005 * Math.random();
        // Возвращаем новую стоимость товара
        return value;
    }

    public double getValue(int commodity) {
        return marketValue[commodity];
    }

    public double getFaceValue(int commmodity) {
        return faceValue[commmodity];
    }

    public void setFaceValue(int commodity, double price) {
        faceValue[commodity] = price;
    }

    public double getDemandBySupply(int commodity) {
        return 2 + Math.cos(largeCycle + marketBias[commodity]);
    }

    public long getCycle() {
        return cycle;
    }

    public void buyOrder(Production production, Inventory inventory, int commodity, int amount) {
        double commodityPrice = getValue(commodity);
        for (int i=0; i < amount; i++) {
            if (!production.decreaseCashBalance(commodityPrice)) break;
            inventory.push(new Item(Material.getMaterial(commodity)));
        }
    }

    public void sellOrder(Production production, Inventory inventory, int commodity, int amount) {
        Item item;
        double commodityPrice = getValue(commodity);
        for (int i=0; i < amount; i++) {
            item = inventory.poll(Material.getMaterial(commodity));
            if (item==null) break;
            production.increaseCashBalance(commodityPrice);
        }
    }

    @Override
    public JSONObject serialize() {
        return null;
    }

}
