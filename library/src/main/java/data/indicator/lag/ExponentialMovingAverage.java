package data.indicator.lag;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.ArrayList;
import java.util.List;

public class ExponentialMovingAverage extends LagIndicator {

    private double multiplier;

    @Inject
    public ExponentialMovingAverage(@Named("exponentialMovingAverage.period") int period) {
        super(period);
        setMultiplier();
    }

    private void setMultiplier() {
        multiplier = 2.0 / (getPeriod() + 1);
    }

    /**
     * EMA doesn't need periodData list but all other "lag" indicators do so we fill a list with a single value to
     * conform to the API that all other lag indicators would use.
     */
    @Override
    void loadData() {
        while (hasNext()) {
            List<Double> list = new ArrayList<>();
            list.add(getInputData().get(getStartIndex()));
            getData().add(calculateDataPoint(list));
            incrementIndices();
        }
    }

    @Override
    boolean hasNext() {
        return getStartIndex() < getInputData().size();
    }

    @Override
    double calculateDataPoint(List<Double> periodData) {
        double priceToday = periodData.get(0);

        if (getData().size() == 0) {
            return priceToday;
        }

        double previousEMS = getData().get(getData().size() - 1);
        return calculateEMA(priceToday, previousEMS);
    }

    private double calculateEMA(double priceToday, double previousEMA) {
        return (priceToday * multiplier) + (previousEMA * (1 - multiplier));
    }
}
