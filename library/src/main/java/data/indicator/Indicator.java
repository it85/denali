package data.indicator;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import data.common.CandleStick;

import java.util.ArrayList;
import java.util.List;

public abstract class Indicator {

    /**
     * Input data that is used to calculate {@link Indicator#data} encapsulated by this Indicator. In most cases, inputData
     * will be price data, e.g., SPX or volume data. {@link Indicator#loadData()} needs to be explicitly called somewhere
     * to initiate the calculators for each indicator. We could use Guice to inject method call but
     */
    @Inject
    @Named("indicator.inputData")
    private List<CandleStick> inputData;

    @Inject
    private List<Double> data;

    public abstract void loadData();

    public List<CandleStick> getInputData() {
        return inputData;
    }

    public List<Double> getData() {
        return data;
    }
}
