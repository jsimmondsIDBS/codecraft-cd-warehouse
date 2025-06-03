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

public class CDListingTest
{
    @Test
    public void testEmptyListing()
    {
        final CDListing cdListing = new CDListing();
        assertEquals(0, cdListing.get().size());
    }

    @Test
    public void testCdListOfOne()
    {
        final CDListing cdListing = new CDListing();
        cdListing.add(new CD("ABC", "Artist1"));
        assertEquals(1, cdListing.get().size());
    }

    @Test
    public void canGetTitleFromCDInListing()
    {
        final CDListing cdListing = new CDListing();
        cdListing.add(new CD("ABC", "Artist1"));
        assertEquals("ABC", cdListing.get().get(0).getTitle());
    }

    @Test
    public void canGetArtistFromCDInListing()
    {
        final CDListing cdListing = new CDListing();
        cdListing.add(new CD("ABC", "Artist1"));
        assertEquals("Artist1", cdListing.get().get(0).getArtist());
    }

    @Test
    public void canAddMultipleCDs()
    {
        final CDListing cdListing = new CDListing();
        cdListing.add(new CD("ABC", "Artist1"));
        cdListing.add(new CD("XYZ", "Artist2"));
        assertEquals(2, cdListing.get().size());
    }

    @Test
    public void searchListingByTitle()
    {
        final CDListing cdListing = new CDListing();
        cdListing.add(new CD("ABC", "Artist1"));

        final CD result = cdListing.searchByTitle("ABC");

        assertEquals("ABC", result.getTitle());
    }

    @Test
    public void searchListingContainingMultipleCDsByTitle()
    {
        final CDListing cdListing = new CDListing();
        cdListing.add(new CD("XYZ", "Artist2"));
        cdListing.add(new CD("ABC", "Artist1"));

        final CD result = cdListing.searchByTitle("ABC");

        assertEquals("ABC", result.getTitle());
    }

    @Test
    public void searchListingByTitleReturnsNullIfNotPresent()
    {
        final CDListing cdListing = new CDListing();
        cdListing.add(new CD("ABC", "Artist1"));

        final CD result = cdListing.searchByTitle("DEF");

        assertNull(result);
    }

    @Test
    public void searchListingByArtistReturnsNullIfNotPresent()
    {
        final CDListing cdListing = new CDListing();
        cdListing.add(new CD("ABC", "Artist1"));

        final CD result = cdListing.searchByArtist("DEF");

        assertNull(result);
    }

    @Test
    public void searchListingByArtist()
    {
        final CDListing cdListing = new CDListing();
        cdListing.add(new CD("ABC", "Artist1"));

        final CD result = cdListing.searchByArtist("Artist1");

        assertEquals("Artist1", result.getArtist());
    }

    @Test
    public void purchaseCD()
    {
        final CDListing cdListing = new CDListing();
        final CD cd = new CD("ABC", "Artist1");
        cdListing.add(cd);

        final boolean success = cdListing.purchase(cd);

        assertTrue(success);
    }

    @Test
    public void purchaseCDThatDoesNotExistInListing()
    {
        final CDListing cdListing = new CDListing();
        final CD cd = new CD("ABC", "Artist1");
        cdListing.add(cd);

        final CD cdNotInListing = new CD("DEF", "Artist99");
        final boolean success = cdListing.purchase(cdNotInListing);

        assertFalse(success);
    }

    @Test
    public void purchaseCDThatHasOneInStockRemovesFromListing()
    {
        final CDListing cdListing = new CDListing();
        final CD cd = new CD("ABC", "Artist1");
        cdListing.add(cd);
        cdListing.purchase(cd);

        final List<CD> cds = cdListing.get();

        assertEquals(0, cds.size());
    }

    @Test
    public void purchaseCDWithTwoInStockDoesNotRemoveFromListing()
    {
        final CDListing cdListing = new CDListing();
        final CD cd = new CD("ABC", "Artist1");
        cdListing.add(cd);
        cdListing.add(cd);
        cdListing.purchase(cd);

        final List<CD> cds = cdListing.get();

        assertEquals(cd, cds.get(0));
    }

    @Test
    public void purchaseCDWithThreeInStockReturnsOnlyOneInListing()
    {
        final CDListing cdListing = new CDListing();
        final CD cd = new CD("ABC", "Artist1");
        cdListing.add(cd);
        cdListing.add(cd);
        cdListing.add(cd);
        cdListing.purchase(cd);

        final List<CD> cds = cdListing.get();

        assertEquals(1, cds.size());

    }
}
