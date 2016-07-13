package data.indicator.period;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import data.common.CandleStick;

import java.util.List;

public class RSI extends PeriodIndicator {

    double previousAverageGain;
    double previousAverageLoss;

    @Inject
    public RSI(@Named("periodIndicator.rsi.period") int period) {
        super(period);
    }

    @Override
    double calculateDataPoint(List<CandleStick> periodData) {
        if (getData().isEmpty()) {
            previousAverageGain = averageGain(periodData);
            previousAverageLoss = averageLoss(periodData);

            if (previousAverageGain == 0) {
                return 0.0;
            }

            if (previousAverageLoss == 0) {
                return 100.0;
            }

            double rs = previousAverageGain / previousAverageLoss;
            return rsi(rs);
        }

        double multiplier = getPeriod() - 1;

        double currentOutcome = currentCloseOpenSpread(periodData);

        if (currentOutcome > 0) {
            double averageGain = ((previousAverageGain * multiplier) + Math.abs(currentOutcome)) / getPeriod();
            previousAverageGain = averageGain;

            double averageLoss = (previousAverageLoss * multiplier) / getPeriod();
            previousAverageLoss = averageLoss;

            double rs = averageGain / averageLoss;
            return rsi(rs);
        } else if (currentOutcome < 0) {
            double averageGain = (previousAverageGain * multiplier) / getPeriod();
            previousAverageGain = averageGain;

            double averageLoss = ((previousAverageLoss * multiplier) + Math.abs(currentOutcome)) / getPeriod();
            previousAverageLoss = averageLoss;

            double rs = averageGain / averageLoss;
            return rsi(rs);
        } else {
            double rs = previousAverageGain / previousAverageLoss;
            return rsi(rs);
        }
    }

    private double rsi(double rs) {
        return 100 - (100 / (1 + rs));
    }

    private double averageGain(List<CandleStick> data) {
        double sum = 0;

        for (CandleStick candleStick : data) {
            if (candleStick.getClosingOutcome() == CandleStick.ClosingOutcome.GAIN) {
                sum += candleStick.getCloseOpenSpread();
            }
        }

        return Math.abs(sum / getPeriod());
    }

    private double averageLoss(List<CandleStick> data) {
        double sum = 0;

        for (CandleStick candleStick : data) {
            if (candleStick.getClosingOutcome() == CandleStick.ClosingOutcome.LOSS) {
                sum += candleStick.getCloseOpenSpread();
            }
        }

        return Math.abs(sum / getPeriod());
    }

    /**
     * Returns the change of the most current candleStick in the data list parameter.
     */
    private double currentCloseOpenSpread(List<CandleStick> data) {
        return data.get(data.size() - 1).getCloseOpenSpread();
    }
}
