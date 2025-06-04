/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.util.List;

public class WarehouseTest
{
    private final Charts charts = mock(Charts.class);

    private final ExternalProvider EXTERNAL_PROVIDER = new ExternalProvider()
    {
        @Override
        public boolean processPayment()
        {
            return true;
        }
    };

    @Test
    public void testEmptyListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        assertEquals(0, warehouse.getListing().size());
    }

    @Test
    public void testCdListOfOne()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("ABC", "Artist1", 9.99));
        assertEquals(1, warehouse.getListing().size());
    }

    @Test
    public void canGetListingTitleFromCDInListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("ABC", "Artist1", 9.99));
        assertEquals("ABC", warehouse.getListing().get(0).getTitle());
    }

    @Test
    public void canGetListingArtistFromCDInListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("ABC", "Artist1", 9.99));
        assertEquals("Artist1", warehouse.getListing().get(0).getArtist());
    }

    @Test
    public void canAddMultipleCDs()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("ABC", "Artist1", 9.99));
        warehouse.add(new CD("XYZ", "Artist2", 9.99));
        assertEquals(2, warehouse.getListing().size());
    }

    @Test
    public void searchListingByTitle()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("ABC", "Artist1", 9.99));

        final CD result = warehouse.search("ABC");

        assertEquals("ABC", result.getTitle());
    }

    @Test
    public void searchListingContainingMultipleCDsByTitle()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("XYZ", "Artist2", 9.99));
        warehouse.add(new CD("ABC", "Artist1", 9.99));

        final CD result = warehouse.search("ABC");

        assertEquals("ABC", result.getTitle());
    }

    @Test
    public void searchListingByTitleReturnsNullIfNotPresent()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("ABC", "Artist1", 9.99));

        final CD result = warehouse.search("DEF");

        assertNull(result);
    }

    @Test
    public void searchListingByArtistReturnsNullIfNotPresent()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("ABC", "Artist1", 9.99));

        final CD result = warehouse.search("DEF");

        assertNull(result);
    }

    @Test
    public void searchListingByArtist()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("ABC", "Artist1", 9.99));

        final CD result = warehouse.search("Artist1");

        assertEquals("Artist1", result.getArtist());
    }

    @Test
    public void search() {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        warehouse.add(new CD("ABC", "Artist1", 9.99));
        warehouse.add(new CD("XYZ", "Artist2", 9.99));

        final CD result = warehouse.search("XYZ");

        assertEquals("Artist2", result.getArtist());

    }

    @Test
    public void purchaseCD()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd);

        final boolean success = warehouse.purchase(cd);

        assertTrue(success);
    }

    @Test
    public void purchaseCDThatDoesNotExistInListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd);

        final CD cdNotInListing = new CD("DEF", "Artist99", 9.99);
        final boolean success = warehouse.purchase(cdNotInListing);

        assertFalse(success);
    }

    @Test
    public void purchaseCDThatHasOneInStockRemovesFromListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd);
        warehouse.purchase(cd);

        final List<CD> cds = warehouse.getListing();

        assertEquals(0, cds.size());
    }

    @Test
    public void purchaseCDWithTwoInStockDoesNotRemoveFromListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd);
        warehouse.add(cd);
        warehouse.purchase(cd);

        final List<CD> cds = warehouse.getListing();

        assertEquals(cd, cds.get(0));
    }

    @Test
    public void purchaseCDWithThreeInStockReturnsOnlyOneInListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd);
        warehouse.add(cd);
        warehouse.add(cd);
        warehouse.purchase(cd);

        final List<CD> cds = warehouse.getListing();

        assertEquals(1, cds.size());
    }

    @Test
    public void purchaseCDsWithTwoInStockAddedAtOnceRemainsInList()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd, 2);

        final boolean transaction1Succeeds = warehouse.purchase(cd);
        final boolean transaction2Succeeds = warehouse.purchase(cd);
        final boolean transaction3Succeeds = warehouse.purchase(cd);

        assertTrue(transaction1Succeeds);
        assertTrue(transaction2Succeeds);
        assertFalse(transaction3Succeeds);
    }

    @Test
    public void purchasingFailsInsufficientFunds()
    {
        final ExternalProvider externalProvider = new ExternalProvider()
        {
            @Override
            public boolean processPayment()
            {
                return false;
            }
        };
        final Warehouse warehouse = new Warehouse(externalProvider, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd);

        final boolean successful = warehouse.purchase(cd);

        assertFalse(successful);
    }

    @Test
    public void purchasingCDNotifiesCharts()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd);

        warehouse.purchase(cd);

        verify(charts).notifyOfSale("Artist1", "ABC", 1);
    }

    @Test
    public void purchasingTwoCDsWhenOnlyOneInStock()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd);

        final boolean isTransactionSuccessful = warehouse.purchase(cd, 2);

        assertFalse(isTransactionSuccessful);
    }

    @Test
    public void purchasingTwoCDsSucceeds()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd, 2);

        final boolean isTransaction1Successful = warehouse.purchase(cd, 2);
        final boolean isTransaction2Successful = warehouse.purchase(cd, 1);

        assertTrue(isTransaction1Successful);
        assertFalse(isTransaction2Successful);
    }

    @Test
    public void purchasingMultipleCDsNotifiesCharts()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER, charts);
        final CD cd = new CD("ABC", "Artist1", 9.99);
        warehouse.add(cd, 2);

        warehouse.purchase(cd, 2);

        verify(charts).notifyOfSale("Artist1", "ABC", 2);
    }

    @Test
    public void inTop100NoCompetitorPrice()
    {
        when(charts.isInTop100("Artist1", "ABC")).thenReturn(true);
        final CD cd = new CD("ABC", "Artist1", 9.99);

        final double price = cd.getPrice(charts);

        assertEquals(9.99, price, 0);
    }

    @Test
    public void priceWhenNotInTop100()
    {
        when(charts.isInTop100("Artist1", "ABC")).thenReturn(false);
        final CD cd = new CD("ABC", "Artist1", 9.99);

        final double price = cd.getPrice(charts);

        assertEquals(9.99, price, 0);
    }

    @Test
    public void priceGuaranteeTopCompetitorsPrice()
    {
        when(charts.isInTop100("Artist1", "ABC")).thenReturn(true);
        when(charts.getLowestPrice("Artist1", "ABC")).thenReturn(8.99);
        final CD cd = new CD("ABC", "Artist1", 9.99);

        final double price = cd.getPrice(charts);

        assertEquals(7.99, price, 0);
    }

    @Test
    public void priceGuaranteeTopCompetitorsPriceHigherThanOurs()
    {
        when(charts.isInTop100("Artist1", "ABC")).thenReturn(true);
        when(charts.getLowestPrice("Artist1", "ABC")).thenReturn(10.0);
        final CD cd = new CD("ABC", "Artist1", 9.99);

        final double price = cd.getPrice(charts);

        assertEquals(9.0, price, 0);
    }
}
