/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class WarehouseTest
{
    private final ExternalProvider EXTERNAL_PROVIDER = new ExternalProvider()
    {
        @Override
        public boolean transaction()
        {
            return true;
        }
    };

    @Test
    public void testEmptyListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        assertEquals(0, warehouse.get().size());
    }

    @Test
    public void testCdListOfOne()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        warehouse.add(new CD("ABC", "Artist1"));
        assertEquals(1, warehouse.get().size());
    }

    @Test
    public void canGetTitleFromCDInListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        warehouse.add(new CD("ABC", "Artist1"));
        assertEquals("ABC", warehouse.get().get(0).getTitle());
    }

    @Test
    public void canGetArtistFromCDInListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        warehouse.add(new CD("ABC", "Artist1"));
        assertEquals("Artist1", warehouse.get().get(0).getArtist());
    }

    @Test
    public void canAddMultipleCDs()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        warehouse.add(new CD("ABC", "Artist1"));
        warehouse.add(new CD("XYZ", "Artist2"));
        assertEquals(2, warehouse.get().size());
    }

    @Test
    public void searchListingByTitle()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        warehouse.add(new CD("ABC", "Artist1"));

        final CD result = warehouse.searchByTitle("ABC");

        assertEquals("ABC", result.getTitle());
    }

    @Test
    public void searchListingContainingMultipleCDsByTitle()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        warehouse.add(new CD("XYZ", "Artist2"));
        warehouse.add(new CD("ABC", "Artist1"));

        final CD result = warehouse.searchByTitle("ABC");

        assertEquals("ABC", result.getTitle());
    }

    @Test
    public void searchListingByTitleReturnsNullIfNotPresent()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        warehouse.add(new CD("ABC", "Artist1"));

        final CD result = warehouse.searchByTitle("DEF");

        assertNull(result);
    }

    @Test
    public void searchListingByArtistReturnsNullIfNotPresent()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        warehouse.add(new CD("ABC", "Artist1"));

        final CD result = warehouse.searchByArtist("DEF");

        assertNull(result);
    }

    @Test
    public void searchListingByArtist()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        warehouse.add(new CD("ABC", "Artist1"));

        final CD result = warehouse.searchByArtist("Artist1");

        assertEquals("Artist1", result.getArtist());
    }

    @Test
    public void purchaseCD()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        final CD cd = new CD("ABC", "Artist1");
        warehouse.add(cd);

        final boolean success = warehouse.purchase(cd);

        assertTrue(success);
    }

    @Test
    public void purchaseCDThatDoesNotExistInListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        final CD cd = new CD("ABC", "Artist1");
        warehouse.add(cd);

        final CD cdNotInListing = new CD("DEF", "Artist99");
        final boolean success = warehouse.purchase(cdNotInListing);

        assertFalse(success);
    }

    @Test
    public void purchaseCDThatHasOneInStockRemovesFromListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        final CD cd = new CD("ABC", "Artist1");
        warehouse.add(cd);
        warehouse.purchase(cd);

        final List<CD> cds = warehouse.get();

        assertEquals(0, cds.size());
    }

    @Test
    public void purchaseCDWithTwoInStockDoesNotRemoveFromListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        final CD cd = new CD("ABC", "Artist1");
        warehouse.add(cd);
        warehouse.add(cd);
        warehouse.purchase(cd);

        final List<CD> cds = warehouse.get();

        assertEquals(cd, cds.get(0));
    }

    @Test
    public void purchaseCDWithThreeInStockReturnsOnlyOneInListing()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        final CD cd = new CD("ABC", "Artist1");
        warehouse.add(cd);
        warehouse.add(cd);
        warehouse.add(cd);
        warehouse.purchase(cd);

        final List<CD> cds = warehouse.get();

        assertEquals(1, cds.size());
    }

    @Test
    public void purchaseCDsWithTwoInStockAddedAtOnceRemainsInList()
    {
        final Warehouse warehouse = new Warehouse(EXTERNAL_PROVIDER);
        final CD cd = new CD("ABC", "Artist1");
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
            public boolean transaction()
            {
                return false;
            }
        };
        final Warehouse warehouse = new Warehouse(externalProvider);
        final CD cd = new CD("ABC", "Artist1");
        warehouse.add(cd);

        final boolean successful = warehouse.purchase(cd);

        assertFalse(successful);
    }
}
