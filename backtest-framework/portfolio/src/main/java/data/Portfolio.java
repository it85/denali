package data;

import exception.InvalidBuyOrderException;
import exception.InvalidSellOrderException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    private BigDecimal totalBalance;
    private BigDecimal cashBalance;
    private BigDecimal securitiesBalance;

    private Map<String, Position> positions;
    private Map<String, ArrayList<Order>> orderHistory;

    protected Portfolio(double startingBalance){
        this.positions = new HashMap<>();
        this.orderHistory = new HashMap<>();

        this.totalBalance = new BigDecimal(startingBalance);
        this.cashBalance = new BigDecimal(startingBalance);
        this.securitiesBalance = new BigDecimal(0.0);
    }

    protected void buy(String symbol, int shares, BigDecimal price, Date dateOpened) throws InvalidBuyOrderException {

        BigDecimal orderTotal = price.multiply(new BigDecimal(shares));

        if(buyOrderSufficientFunds(orderTotal)){

            if(!positions.containsKey(symbol)){
                Position p = new Position(symbol, price, shares, dateOpened);
                positions.put(symbol, p);
            }else{
                Position p = positions.get(symbol);
                p.add(price, shares);
            }

            this.postBuyAccounting(symbol, shares, price, dateOpened);
        }else{
            throw new InvalidBuyOrderException("Cash Balance: " + this.cashBalance + ", Order Total: " + orderTotal);
        }
    }



    protected void sell(String symbol, int shares, BigDecimal price, Date date) throws InvalidSellOrderException{

        if(positions.containsKey(symbol)) {

            Position position = positions.get(symbol);
            BigDecimal totalProceeds = price.multiply(new BigDecimal(shares));

            if(position.sellOrderSufficientFunds(totalProceeds) && position.sellOrderSufficientShares(shares)){
                position.sell(price, shares);
                this.postSellAccounting(symbol, shares, price, date, position);
            }else{
                throw new InvalidSellOrderException("Sell order total proceeds: " + totalProceeds +
                        ", Position value: " + position.getValue());
            }
        }else{
            throw new InvalidSellOrderException("Position for cusip " + symbol + " does not exist");
        }
    }

    private boolean buyOrderSufficientFunds(BigDecimal orderTotal){
        return (this.cashBalance.compareTo(orderTotal) == 1) && (orderTotal.compareTo(new BigDecimal(0)) != 0);
    }


    private void postBuyAccounting(String symbol, int shares, BigDecimal price, Date date){
        BigDecimal orderTotal = price.multiply(new BigDecimal(shares));
        this.postBuyUpdateBalances(orderTotal);
        this.addBuyOrderToHistory(symbol, shares, price, date);
    }

    private void postSellAccounting(String symbol, int shares, BigDecimal price, Date date, Position position){
        BigDecimal orderTotal = price.multiply(new BigDecimal(shares));
        this.postSellUpdateBalances(orderTotal, position);
        this.addSellOrderToHistory(symbol, shares, price, date);
    }

    private void addBuyOrderToHistory(String symbol, int shares, BigDecimal price, Date date){
        Order order = new BuyOrder(symbol, shares, price, date);

        if(!this.orderHistory.containsKey(symbol) || orderHistory.get(symbol) == null)
            this.orderHistory.put(symbol, new ArrayList<>());

        this.orderHistory.get(symbol).add(order);
    }

    private void addSellOrderToHistory(String symbol, int shares, BigDecimal price, Date date){
        Order order = new SellOrder(symbol, shares, price, date);

        if(!this.orderHistory.containsKey(symbol) || orderHistory.get(symbol) == null)
            this.orderHistory.put(symbol, new ArrayList<>());

        this.orderHistory.get(symbol).add(order);
    }

    private void postBuyUpdateBalances(BigDecimal orderTotal){
        this.cashBalance = this.cashBalance.subtract(orderTotal);
        this.securitiesBalance = this.securitiesBalance.add(orderTotal);
        this.totalBalance = this.cashBalance.add(this.securitiesBalance);
    }

    private void postSellUpdateBalances(BigDecimal totalProceeds, Position position){
        this.cashBalance = this.cashBalance.add(totalProceeds);

        if(!position.getOpen()){
            this.securitiesBalance = new BigDecimal(0);
        }else{
            this.securitiesBalance = this.securitiesBalance.subtract(totalProceeds);
        }

        this.totalBalance = this.cashBalance.add(this.securitiesBalance);
    }

    public BigDecimal getTotalBalance() {
        return totalBalance.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getCashBalance() {
        return cashBalance.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getSecuritiesBalance() {
        return securitiesBalance.setScale(2, RoundingMode.HALF_UP);
    }

    public Map<String, Position> getPositions() {
        return positions;
    }

    public Map<String, ArrayList<Order>> getOrderHistory() {
        return orderHistory;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "totalBalance=" + totalBalance +
                ", cashBalance=" + cashBalance +
                ", securitiesBalance=" + securitiesBalance +
                ", positions=" + positions +
                ", orderHistory=" + orderHistory +
                '}';
    }
}
