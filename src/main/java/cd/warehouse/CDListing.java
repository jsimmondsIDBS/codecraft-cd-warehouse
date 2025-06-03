/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CDListing
{
    private final Map<CD, Integer> stock = new HashMap<>();

    public List<CD> get()
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
            final int currentStockCount = stock.get(cd);
            stock.put(cd, currentStockCount + 1);
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
        return get().stream()
                .filter(cd -> cd.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public CD searchByArtist(String artist)
    {
        return get().stream()
                .filter(cd -> cd.getArtist().equals(artist))
                .findFirst()
                .orElse(null);
    }

    public boolean purchase(CD cd)
    {
        if (stock.containsKey(cd) && stock.get(cd) > 0)
        {
            final int currentStockCount = stock.get(cd);
            stock.put(cd, currentStockCount - 1);
            return true;
        }

        return false;
    }
}
