/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

import java.util.List;

public class Warehouse
{
    private final ExternalProvider externalProvider;
    private final Charts charts;
    private final Stock stock = new Stock();

    public Warehouse(ExternalProvider externalProvider, Charts charts)
    {
        this.externalProvider = externalProvider;
        this.charts = charts;
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

    public CD search(String value) {
        return getListing().stream()
                .filter(cd -> cd.getArtist().equals(value) || cd.getTitle().equals(value))
                .findFirst().orElse(null);
    }

    public boolean purchase(CD cd)
    {
        return purchase(cd, 1);
    }


    public boolean purchase(CD cd, int quantity)
    {
        if (stock.isInStock(cd, quantity))
        {
            final boolean isPaymentSuccessful = externalProvider.processPayment();
            if (!isPaymentSuccessful)
            {
                return false;
            }

            charts.notifyOfSale(cd.getArtist(), cd.getTitle(), quantity);
            for (int i = 0; i < quantity; i++)
            {
                stock.decrement(cd);
            }
            return true;
        }

        return false;
    }
}
