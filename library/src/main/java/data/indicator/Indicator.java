package data.indicator;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.List;

public abstract class Indicator {

    /**
     * Input data that is used to calculate {@link Indicator#data} encapsulated by this Indicator. In most cases, inputData
     * will be price data, e.g., SPX or volume data.
     */
    @Inject
    @Named("indicator.inputData")
    private List<Double> inputData;

    @Inject
    private List<Double> data;

    public List<Double> getInputData() {
        return inputData;
    }

    public List<Double> getData() {
        return data;
    }
}
