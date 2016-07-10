package data.indicator;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import data.indicator.lag.ExponentialMovingAverage;
import data.indicator.lag.SimpleMovingAverage;

import java.util.ArrayList;
import java.util.List;

public class IndicatorModule extends AbstractModule {

    private final List<Double> inputData;

    public IndicatorModule(List<Double> inputData) {
        this.inputData = inputData;
    }

    @Override
    public void configure() {
        bind(new TypeLiteral<List<Double>>() {} ).to(new TypeLiteral<ArrayList<Double>>() {});

        bind(new TypeLiteral<List<Double>>() {
        }).annotatedWith(Names.named("indicator.inputData")).toInstance(inputData);

        bindConstant().annotatedWith(Names.named("simpleMovingAverage.period")).to(14);
        bind(SimpleMovingAverage.class);

        bindConstant().annotatedWith(Names.named("exponentialMovingAverage.period")).to(14);
        bind(ExponentialMovingAverage.class);
    }
}
