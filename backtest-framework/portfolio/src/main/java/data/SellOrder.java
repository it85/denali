package data;

import java.math.BigDecimal;
import java.util.Date;

class SellOrder extends Order {

    SellOrder(String cusip, int shares, BigDecimal price, Date date) {
        super(cusip, shares, price, date);
        this.amount = price.multiply(new BigDecimal(shares));
    }
}
