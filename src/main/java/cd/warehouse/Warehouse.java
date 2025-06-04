/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

import java.util.List;

public class Warehouse
{
    private final ExternalProvider externalProvider;
    private final Stock stock = new Stock();

    public Warehouse(ExternalProvider externalProvider)
    {
        this.externalProvider = externalProvider;
    }

    public List<CD> getListing()
    {
        return stock.getItemsInStock();
    }

    public void add(CD cd)
    {
        stock.add(cd);
    }

    public void add(CD cd, int quantity)
    {
        stock.add(cd, quantity);
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
        if (stock.isInStock(cd))
        {
            final boolean isTransactionSuccessful = externalProvider.processPayment();
            if (!isTransactionSuccessful)
            {
                return false;
            }

            stock.decrement(cd);
            return true;
        }

        return false;
    }

}
