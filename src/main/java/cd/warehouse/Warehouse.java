/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Warehouse
{
    private final Map<CD, Integer> stock = new HashMap<>();
    private final ExternalProvider externalProvider;

    public Warehouse(ExternalProvider externalProvider)
    {
        this.externalProvider = externalProvider;
    }

    public List<CD> getListing()
    {
        return stock.keySet().stream()
                .filter(cd -> stock.get(cd) > 0)
                .toList();
    }

    public void add(CD cd)
    {
        if (!stock.containsKey(cd))
        {
            stock.put(cd, 1);
        }
        else
        {
            incrementStock(cd);
        }
    }

    public void add(CD cd, int quantity)
    {
        for (int i = 0; i < quantity; i++)
        {
            add(cd);
        }
    }

    public CD searchByTitle(String title)
    {
        return getListing().stream()
                .filter(cd -> cd.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public CD searchByArtist(String artist)
    {
        return getListing().stream()
                .filter(cd -> cd.getArtist().equals(artist))
                .findFirst()
                .orElse(null);
    }

    public boolean purchase(CD cd)
    {
        final boolean isCDInStock = stock.containsKey(cd) && stock.get(cd) > 0;
        if (isCDInStock)
        {
            final boolean isTransactionSuccessful = externalProvider.processPayment();
            if (!isTransactionSuccessful)
            {
                return false;
            }

            decrementStock(cd);
            return true;
        }

        return false;
    }

    private void incrementStock(CD cd)
    {
        stock.compute(cd, (k, currentStockCount) -> currentStockCount + 1);
    }

    private void decrementStock(CD cd)
    {
        stock.compute(cd, (k, currentStockCount) -> currentStockCount - 1);
    }
}
