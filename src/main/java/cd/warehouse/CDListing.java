/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

import java.util.ArrayList;
import java.util.List;

public class CDListing
{
    private final List<CD> listing = new ArrayList<>();

    List<CD> get()
    {
        return listing;
    }

    public void add(CD cd)
    {
        listing.add(cd);
    }

    public CD searchByTitle(String title)
    {
        return listing.stream()
                .filter(cd -> cd.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public CD searchByArtist(String artist)
    {
        return listing.stream()
                .filter(cd -> cd.getArtist().equals(artist))
                .findFirst()
                .orElse(null);
    }

    public boolean purchase(CD cd)
    {
        return listing.remove(cd);
    }
}
