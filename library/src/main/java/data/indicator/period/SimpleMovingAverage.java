package data.indicator.period;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import data.common.CandleStick;

import java.util.List;

public class SimpleMovingAverage extends PeriodIndicator {

    @Inject
    public SimpleMovingAverage(@Named("periodIndicator.simpleMovingAverage.period") int period) {
        super(period);
    }

    @Override
    double calculateDataPoint(List<CandleStick> periodData) {
        double sum = 0;
        int total = periodData.size();

        for (CandleStick data : periodData) {
            sum += data.getClose();
        }

        return sum / total;
    }
}
