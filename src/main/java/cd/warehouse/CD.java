/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

/**
 * @author jsimmonds
 *
 * <p><b>(C) Copyright 2025 IDBS Ltd</b></p>
 */
public class CD
{
    private final String title;
    private final String artist;

    public CD(String title, String artist)
    {
        this.title = title;
        this.artist = artist;
    }

    public String getTitle()
    {
        return title;
    }

    public String getArtist()
    {
        return artist;
    }
}
