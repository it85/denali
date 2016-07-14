package data;

import java.math.BigDecimal;
import java.util.Date;

abstract class Order {

    BigDecimal price;
    BigDecimal amount;
    Date date;
    int shares;
    String cusip;

    Order(String cusip, int shares, BigDecimal price, Date date){
        this.cusip = cusip;
        this.shares = shares;
        this.price = price;
        this.date = date;
    }
}
