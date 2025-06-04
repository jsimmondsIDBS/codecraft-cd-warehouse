package cd.warehouse;

public class GuaranteedPrice
{
    public double calculate(double competitorsPrice, double price, Charts charts)
    {
        final double priceDifference = competitorsPrice - price;
        if (priceDifference < 1)
        {
            return getGuaranteedPrice(competitorsPrice, price);
        }

        return price;
    }

    private double getGuaranteedPrice(double competitorsPrice, double price)
    {
        if (competitorsPrice > 0 && competitorsPrice < 1)
        {
            return competitorsPrice;
        }

        final double guaranteedPrice = competitorsPrice - 1;
        if (guaranteedPrice < 0)
        {
            return price;
        }

        return guaranteedPrice;
    }
}