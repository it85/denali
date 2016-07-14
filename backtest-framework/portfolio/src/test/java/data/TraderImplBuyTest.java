package data;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import exception.InvalidBuyOrderException;

public class TraderImplBuyTest {

    private static TraderImpl trader;
    private final static double STARTING_BALANCE = 10000;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        trader = new TraderImpl(STARTING_BALANCE);
    }

    @Before
    public void setUp() throws Exception {
        trader = new TraderImpl(STARTING_BALANCE);
        trader.getPortfolio().getPositions().clear();
    }

    @After
    public void tearDown() throws Exception {
        trader = new TraderImpl(STARTING_BALANCE);
        trader.getPortfolio().getPositions().clear();
    }

    @Test
    public void testTrader(){
        assertNotNull(trader.getPortfolio());

    }

    @Test
    public void testTraderPortfolio(){

        BigDecimal startingBalanceBD = new BigDecimal("10000.00");
        BigDecimal securitiesBalanceBD = new BigDecimal("0.00");

        assertEquals(startingBalanceBD, trader.getPortfolio().getTotalBalance());
        assertEquals(startingBalanceBD, trader.getPortfolio().getCashBalance());
        assertEquals(securitiesBalanceBD, trader.getPortfolio().getSecuritiesBalance());

        assertNotNull(trader.getPortfolio().getPositions());
        assertEquals(trader.getPortfolio().getPositions().size(), 0);
    }

    @Test(expected=InvalidBuyOrderException.class)
    public void testBuyInsufficientFunds() throws InvalidBuyOrderException {

        String cusip = "SPY";
        int shares = 500;
        double buyPrice = 1000;
        Date dateOpened = new Date();

        trader.buy(cusip, shares, buyPrice, dateOpened);
    }

    @Test
    public void testBuyContainsPosition() {

        String cusip = "SPY";
        int shares = 50;
        double buyPrice = 3.75;
        Date dateOpened = new Date();

        try {
            trader.buy(cusip, shares, buyPrice, dateOpened);
        } catch (InvalidBuyOrderException e) {
            e.printStackTrace();
        }

        assertTrue(trader.getPortfolio().getPositions().containsKey(cusip));
    }

    @Test
    public void testBuyContainsPosition2() {

        String cusip = "SPY";
        int shares = 50;
        double buyPrice = 3.75;
        Date dateOpened = new Date();

        try {
            trader.buy(cusip, shares, buyPrice, dateOpened);
            trader.buy(cusip, shares, buyPrice, dateOpened);
        } catch (InvalidBuyOrderException e) {
            e.printStackTrace();
        }

        assertTrue(trader.getPortfolio().getPositions().containsKey(cusip));
        assertEquals(1, trader.getPortfolio().getPositions().size());
    }

    @Test
    public void testBuyNewPosition() {

        String cusip = "SPY";
        int shares = 50;
        double buyPrice = 3.75;
        Date dateOpened = new Date();

        assertEquals(0, trader.getPortfolio().getPositions().size());
        assertFalse(trader.getPortfolio().getPositions().containsKey(cusip));

        try {
            trader.buy(cusip, shares, buyPrice, dateOpened);
        } catch (InvalidBuyOrderException e) {
            e.printStackTrace();
        }

        BigDecimal securitiesBalance = new BigDecimal("187.50");
        BigDecimal cashBalance = new BigDecimal("9812.50");

        BigDecimal startingBalanceBD = new BigDecimal("10000.00");

        assertEquals(startingBalanceBD, trader.getPortfolio().getTotalBalance());
        assertEquals(securitiesBalance, trader.getPortfolio().getSecuritiesBalance());
        assertEquals(cashBalance, trader.getPortfolio().getCashBalance());
        assertEquals(1, trader.getPortfolio().getPositions().size());

        assertTrue(trader.getPortfolio().getPositions().containsKey(cusip));
    }

    @Test
    public void testBuyPositionVWAP() {

        String cusip = "SPY";
        int shares = 50;
        double buyPrice = 3.75;
        Date dateOpened = new Date();

        try {
            trader.buy(cusip, shares, buyPrice, dateOpened);
        } catch (InvalidBuyOrderException e) {
            e.printStackTrace();
        }

        BigDecimal firstVwap = new BigDecimal(buyPrice);
        assertEquals(firstVwap, trader.getPortfolio().getPositions().get(cusip).getVwap());

        shares = 75;
        buyPrice = 3.455;

        try {
            trader.buy(cusip, shares, buyPrice, dateOpened);
        } catch (InvalidBuyOrderException e) {
            e.printStackTrace();
        }

        BigDecimal secondVwap = new BigDecimal("3.57");
        assertEquals(secondVwap, trader.getPortfolio().getPositions().get(cusip).getVwap());
    }

    @Test(expected=InvalidBuyOrderException.class)
    public void testBuyZeroShares() throws InvalidBuyOrderException {

        String cusip = "SPY";
        int shares = 0;
        double buyPrice = 3.75;
        Date dateOpened = new Date();

        trader.buy(cusip, shares, buyPrice, dateOpened);
    }

    @Test
    public void testBuyOrderHistory(){
        String cusip = "SPY";
        int shares = 50;
        double buyPrice = 3.75;
        Date dateOpened = new Date();

        assertEquals(0, trader.getPortfolio().getPositions().size());
        assertFalse(trader.getPortfolio().getPositions().containsKey(cusip));

        try {
            trader.buy(cusip, shares, buyPrice, dateOpened);
        } catch (InvalidBuyOrderException e) {
            e.printStackTrace();
        }

        BigDecimal securitiesBalance = new BigDecimal("187.50");
        BigDecimal cashBalance = new BigDecimal("9812.50");

        BigDecimal startingBalanceBD = new BigDecimal("10000.00");

        assertEquals(startingBalanceBD, trader.getPortfolio().getTotalBalance());
        assertEquals(securitiesBalance, trader.getPortfolio().getSecuritiesBalance());
        assertEquals(cashBalance, trader.getPortfolio().getCashBalance());
        assertEquals(1, trader.getPortfolio().getPositions().size());

        assertTrue(trader.getPortfolio().getPositions().containsKey(cusip));

        assertNotNull(trader.getPortfolio().getOrderHistory());
        assertNotNull(trader.getPortfolio().getOrderHistory().get(cusip));
        assertEquals(1, trader.getPortfolio().getOrderHistory().get(cusip).size());

        try {
            trader.buy(cusip, shares, buyPrice, dateOpened);
        } catch (InvalidBuyOrderException e) {
            e.printStackTrace();
        }

        assertEquals(2, trader.getPortfolio().getOrderHistory().get(cusip).size());
    }

    @Test
    public void testGetPortfolio() {
        Portfolio p = trader.getPortfolio();
        assertNotNull(p);
    }
}
