package data.indicator;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import data.common.CandleStick;
import data.indicator.period.ExponentialMovingAverage;
import data.indicator.period.RSI;
import data.indicator.period.SimpleMovingAverage;
import data.indicator.period.StochRSI;

import java.util.ArrayList;
import java.util.List;

public class IndicatorModule extends AbstractModule {

    private final List<CandleStick> inputData;

    public IndicatorModule(List<CandleStick> inputData) {
        this.inputData = inputData;
    }

    @Override
    public void configure() {
        bind(new TypeLiteral<List<Double>>() {
        }).to(new TypeLiteral<ArrayList<Double>>() {
        });

        bind(new TypeLiteral<List<CandleStick>>() {
        }).annotatedWith(Names.named("indicator.inputData")).toInstance(inputData);

        // TODO: put the period values in a config and load it from config

        bindConstant().annotatedWith(Names.named("periodIndicator.simpleMovingAverage.period")).to(14);
        bind(SimpleMovingAverage.class);

        bindConstant().annotatedWith(Names.named("periodIndicator.exponentialMovingAverage.period")).to(14);
        bind(ExponentialMovingAverage.class);

        bindConstant().annotatedWith(Names.named("periodIndicator.rsi.period")).to(14);
        bind(RSI.class);

        bindConstant().annotatedWith(Names.named("periodIndicator.stochRsi.period")).to(14);
        bind(StochRSI.class);
    }
}
