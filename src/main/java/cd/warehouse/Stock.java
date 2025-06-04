package cd.warehouse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock {
    private final Map<CD, Integer> stock = new HashMap<CD, Integer>();

    public Stock() {
    }

    public void add(CD cd) {
        if (!stock.containsKey(cd)) {
            stock.put(cd, 1);
        } else {
            increment(cd);
        }
    }

    public void add(CD cd, int quantity) {
        for (int i = 0; i < quantity; i++) {
            add(cd);
        }
    }

    public void increment(CD cd) {
        stock.compute(cd, (k, currentStockCount) -> currentStockCount + 1);
    }

    public void decrement(CD cd) {
        stock.compute(cd, (k, currentStockCount) -> currentStockCount - 1);
    }

    public List<CD> getItemsInStock() {
        return stock.keySet().stream()
                .filter(this::isInStock)
                .toList();
    }

    boolean isInStock(CD cd)
    {
        return this.stock.containsKey(cd) && this.stock.get(cd) > 0;
    }

    boolean isInStock(CD cd, int quantity)
    {
        return this.stock.containsKey(cd) && this.stock.get(cd) >= quantity;
    }
}