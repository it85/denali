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

            if(previousAverageGain == 0) {
                return 0.0;
            }

            if(previousAverageLoss == 0) {
                return 100.0;
            }

            double rs = previousAverageGain / previousAverageLoss;
            return rsi(rs);
        }

        double multiplier = getPeriod() - 1;

        double currentOutcome = currentOutcome(periodData);

        if (currentOutcome > 0) {
            double averageGain = ((previousAverageGain * multiplier) + currentOutcome) / getPeriod();
            previousAverageGain = averageGain;

            double averageLoss = (previousAverageLoss * multiplier) / getPeriod();
            previousAverageLoss = averageLoss;

            double rs = averageGain / averageLoss;
            return rsi(rs);
        } else if (currentOutcome < 0) {
            double averageGain = (previousAverageGain * multiplier) / getPeriod();
            previousAverageGain = averageGain;

            double averageLoss = ((previousAverageLoss * multiplier) + currentOutcome) / getPeriod();
            previousAverageLoss = averageLoss;

            double rs = averageGain / averageLoss;
            return rsi(rs);
        } else{
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
                sum += (candleStick.getHigh() - candleStick.getClose());
            }
        }

        return sum / getPeriod();
    }

    private double averageLoss(List<CandleStick> data) {
        double sum = 0;

        for (CandleStick candleStick : data) {
            if (candleStick.getClosingOutcome() == CandleStick.ClosingOutcome.LOSS) {
                sum += (candleStick.getClose() - candleStick.getOpen());
            }
        }

        return sum / getPeriod();
    }

    private double currentOutcome(List<CandleStick> data) {
        CandleStick candleStick = data.get(data.size() - 1);
        return candleStick.getClose() - candleStick.getOpen();
    }
}