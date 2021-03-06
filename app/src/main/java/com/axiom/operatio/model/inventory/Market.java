package com.axiom.operatio.model.inventory;

import com.axiom.operatio.model.common.JSONSerializable;
import com.axiom.operatio.model.ledger.Ledger;
import com.axiom.operatio.model.inventory.Inventory;
import com.axiom.operatio.model.materials.Item;
import com.axiom.operatio.model.materials.Material;
import com.axiom.operatio.model.production.Production;

import org.json.JSONObject;

/**
 * Модель рынка - симуляция экономических циклов и колебаний
 */
public class Market implements JSONSerializable {

    public static final int HISTORY_LENGTH = Ledger.HISTORY_LENGTH * 3;

    private double largeCycle;
    private final double[] faceValue;
    private final double[] marketValue;
    private final double[] marketCycle;
    private final double[] marketBias;
    private final double[][] historyValues;
    private final double[] historyMaxValue;
    private final int[] historyLengthCounter;

    private Production production;
    protected long lastCycleTime;                                  // Время последнего цикла (миллисекунды)
//    protected long cycleMilliseconds = Production.CYCLE_TIME * 3;  // Длительносить цикла (миллисекунды)
    private long cycle;


    public Market(Production production) {
        this.production = production;
        faceValue = new double[Material.COUNT];
        marketValue = new double[Material.COUNT];
        marketCycle = new double[Material.COUNT];
        marketBias = new double[Material.COUNT];
        largeCycle = Math.random() * 2 * Math.PI;
        for (int i=0; i<faceValue.length; i++) {
            faceValue[i] = Material.getMaterial(i).getPrice();
            marketValue[i] = faceValue[i];
            marketBias[i] = Math.random() * 2 * Math.PI;
            marketCycle[i] = marketBias[i];
        }
        historyValues = new double[Material.COUNT][HISTORY_LENGTH];
        historyMaxValue = new double[Material.COUNT];
        historyLengthCounter = new int[Material.COUNT];
        cycle = 0;
    }


    public synchronized void process() {
        long cycleMilliseconds = production.getCycleMilliseconds() * 3;  // Длительносить цикла (миллисекунды)

        long now = production.getClock();
        if (now - lastCycleTime < cycleMilliseconds) return;

        for (int commodityID = 0; commodityID < marketValue.length; commodityID++) {
            // Посчитаем очередное значение
            marketValue[commodityID] = evaluateNextValue(commodityID);

            // Сохраним в историю цен
            historyValues[commodityID][historyLengthCounter[commodityID]] = marketValue[commodityID];
            historyLengthCounter[commodityID]++;
            if (historyLengthCounter[commodityID] >= HISTORY_LENGTH) {
                System.arraycopy(historyValues[commodityID], 1,
                        historyValues[commodityID], 0,
                        historyLengthCounter[commodityID] - 1);
                historyLengthCounter[commodityID]--;
            }
            // Найдем максимальное значение
            historyMaxValue[commodityID] = 0;
            for (int j = 0; j < historyLengthCounter[commodityID]; j++) {
                if (historyValues[commodityID][j] > historyMaxValue[commodityID]) {
                    historyMaxValue[commodityID] = historyValues[commodityID][j];
                }
            }
        }

        lastCycleTime = now;
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

    public synchronized double getValue(int commodity) {
        return marketValue[commodity];
    }

    public synchronized double getFaceValue(int commmodity) {
        return faceValue[commmodity];
    }

    public synchronized void setFaceValue(int commodity, double price) {
        faceValue[commodity] = price;
    }

    public synchronized double getDemandBySupply(int commodity) {
        return 2 + Math.cos(largeCycle + marketBias[commodity]);
    }

    public synchronized long getCycle() {
        return cycle;
    }

    public synchronized void buyOrder(Inventory inventory, int commodity, int amount) {
        double commodityPrice = getValue(commodity);
        int quantity = 0;
        int expenseType = Ledger.EXPENSE_MATERIAL_BOUGHT;
        for (int i=0; i < amount; i++) {
            if (!production.getLedger().creditCashBalance(expenseType, commodityPrice)) break;
            inventory.push(new Item(Material.getMaterial(commodity)));
            quantity++;
        }
        if (quantity > 0) {
            production.getLedger().materialBought(commodity, quantity, commodityPrice);
        }
    }

    public synchronized int sellOrder(Inventory inventory, int commodity, int amount) {
        Item item;
        double commodityPrice = getValue(commodity);
        int quantity = 0;
        int incomeType = Ledger.REVENUE_MATERIAL_SOLD;
        Material material = Material.getMaterial(commodity);
        if (inventory.getBalance(material)==0) return 0;
        for (int i=0; i < amount; i++) {
            item = inventory.peek(material);
            if (item==null) break;
            inventory.poll(material);
            production.getLedger().debitCashBalance(incomeType, commodityPrice);
            quantity++;
        }
        if (quantity > 0) {
            production.getLedger().materialSold(commodity, quantity, commodityPrice);
        }
        return quantity;
    }

    public synchronized int getHistoryLength(int commodity) {
        return historyLengthCounter[commodity];
    }

    public synchronized double getHistoryMaxValue(int commodity) {
        return historyMaxValue[commodity];
    }

    public synchronized void getHistoryValues(int commodity, double[] destination) {
        System.arraycopy(historyValues[commodity], 0, destination, 0, historyLengthCounter[commodity]);
    }

    @Override
    public JSONObject toJSON() {
        return null; // Не сериализуем, так как особого смысла прошлые данные не несут
    }

}
