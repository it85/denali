package app;

import java.util.Date;

import data.common.Recommendation;
import exception.InvalidBuyOrderException;
import exception.InvalidSellOrderException;

/**
 * Defines a set of behaviors for a Trader instance. This is the interface that will be exposed to the world.
 * This API should primarily be used for back-testing against some algorithm library.
 * @author Isaac T
 */
public interface Trader {

    void processRecommendation(Recommendation recommendation);
    void buy(String symbol, int shares, double price, Date date) throws InvalidBuyOrderException;
    void sell(String symbol, int shares, double price, Date date) throws InvalidSellOrderException;

}
