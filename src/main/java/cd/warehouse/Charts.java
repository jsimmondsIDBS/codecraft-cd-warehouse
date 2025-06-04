/*
 * Copyright (C) 1993-2025 ID Business Solutions Limited
 * All rights reserved
 */
package cd.warehouse;

public interface Charts
{
    void notifyOfSale(String artist, String title, int quantity);

    boolean isInTop100(String artist, String title);

    double getLowestPrice(String artist, String title);
}
