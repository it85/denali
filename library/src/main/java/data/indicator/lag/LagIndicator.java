package data.indicator.lag;

import com.google.inject.Inject;
import data.indicator.Indicator;

import java.util.ArrayList;
import java.util.List;

public abstract class LagIndicator extends Indicator {

    private int period;

    private int startIndex;
    private int endIndex;

    /**
     * Calculates and loads data points for this LagIndicator into {@link Indicator#data}. In theory, this should be
     * called by Guice immediately following construction assuming it resolved our dependency tree correctly.
     */
    @Inject
    void loadData() {
        while (hasNext()) {
            getData().add(calculateDataPoint(getPeriodData()));
        }
    }

    public LagIndicator(int period) {
        this.period = period;

        startIndex = 0;
        endIndex = period;
    }

    public int getPeriod() {
        return period;
    }

    public int getStartIndex() {
        return startIndex;
    }

    boolean hasNext() {
        return endIndex <= getInputData().size();
    }

    List<Double> getPeriodData() {
        List<Double> periodData = new ArrayList<>();

        for (int i = startIndex; i < (startIndex + period); i++) {
            periodData.add(getInputData().get(i));
        }

        incrementIndices();

        return periodData;
    }

    public void incrementIndices() {
        startIndex++;
        endIndex++;
    }


    abstract double calculateDataPoint(List<Double> periodData);
}
