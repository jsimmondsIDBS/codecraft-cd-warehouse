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
            final double competitorsPrice = charts.getLowestPrice(artist, title);
            final double priceDifference = competitorsPrice - price;
            if (priceDifference < 1)
            {
                return getGuaranteedPrice(competitorsPrice);
            }
        }

        return price;
    }

    private double getGuaranteedPrice(double competitorsPrice)
    {
        final double guaranteedPrice = competitorsPrice - 1;
        if (guaranteedPrice < 0)
        {
            return price;
        }

        return guaranteedPrice;
    }
}
