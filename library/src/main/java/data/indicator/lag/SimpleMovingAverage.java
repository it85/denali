package data.indicator.lag;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.List;

public class SimpleMovingAverage extends LagIndicator {

    @Inject
    public SimpleMovingAverage(@Named("simpleMovingAverage.period") int period) {
        super(period);
    }

    @Override
    double calculateDataPoint(List<Double> periodData) {
        double sum = 0;
        int total = periodData.size();

        for (Double data : periodData) {
            sum += data;
        }

        return sum / total;
    }
}
