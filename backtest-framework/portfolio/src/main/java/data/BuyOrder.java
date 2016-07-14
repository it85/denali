package data;

import java.math.BigDecimal;
import java.util.Date;

class BuyOrder extends Order{

    BuyOrder(String cusip, int shares, BigDecimal price, Date date) {
        super(cusip, shares, price, date);
        this.amount = price.multiply(new BigDecimal(shares)).multiply(new BigDecimal(-1));
    }
}
