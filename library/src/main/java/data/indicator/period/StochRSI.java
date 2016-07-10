package data.indicator.period;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import data.common.CandleStick;

import java.util.List;

public class StochRSI extends PeriodIndicator {

    @Inject
    private RSI rsiCalculator;

    @Inject
    private List<Double> rsi;

    @Inject
    public StochRSI(@Named("periodIndicator.stochRsi.period") int period) {
        super(period);
    }

    @Override
    double calculateDataPoint(List<CandleStick> periodData) {

        rsi.add(rsiCalculator.calculateDataPoint(periodData));

        if (rsi.size() >= getPeriod()) {
            double current = currentRSI();
            double max = maxRSI();
            double min = minRSI();

            return rsi(current, max, min);
        }

        return 0.0;
    }

    private double currentRSI() {
        return rsi.get(rsi.size() - 1);
    }

    private double maxRSI() {
        double max = -1;

        for (double r : rsi) {
            if (r > max) {
                max = r;
            }
        }

        return max;
    }

    private double minRSI() {
        double min = -1;

        for (double r : rsi) {
            if (r < min) {
                min = r;
            }
        }

        return min;
    }

    private double rsi(double current, double max, double min) {
        return (current - min) / (max - min);
    }
}
