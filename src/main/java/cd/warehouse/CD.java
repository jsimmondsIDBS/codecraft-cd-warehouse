/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

public class CD
{
    private final String title;
    private final String artist;
    private final double price;

    public CD(String title, String artist, double price)
    {
        this.title = title;
        this.artist = artist;
        this.price = price;
    }

    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }

    public double getPrice(Charts charts)
    {
        if (charts.isInTop100(artist, title))
        {
            final double lowestPrice = charts.getLowestPrice(artist, title);

            if (lowestPrice == 0 || lowestPrice > price)
            {
                return price - 1;
            }
            return lowestPrice - 1;
        }

        return price;
    }
}
