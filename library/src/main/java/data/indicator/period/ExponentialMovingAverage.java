package data.indicator.period;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import data.common.CandleStick;

import java.util.ArrayList;
import java.util.List;

public class ExponentialMovingAverage extends PeriodIndicator {

    private double multiplier;

    @Inject
    public ExponentialMovingAverage(@Named("periodIndicator.exponentialMovingAverage.period") int period) {
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
    public void loadData() {
        while (hasNext()) {
            List<CandleStick> list = new ArrayList<>();
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
    double calculateDataPoint(List<CandleStick> periodData) {
        double priceToday = periodData.get(0).getClose();

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
