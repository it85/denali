package data.indicator;

import app.DenaliModule;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.typesafe.config.Config;
import data.common.CandleStick;
import data.indicator.period.ExponentialMovingAverage;
import data.indicator.period.RSI;
import data.indicator.period.SimpleMovingAverage;
import data.indicator.period.StochRSI;

import java.util.ArrayList;
import java.util.List;

public class IndicatorModule extends DenaliModule {

    private final List<CandleStick> inputData;

    public IndicatorModule(List<CandleStick> inputData) {
        super(IndicatorModule.class.getSimpleName());
        this.inputData = inputData;
    }

    @Override
    public void configure() {
        bind(new TypeLiteral<List<Double>>() {
        }).to(new TypeLiteral<ArrayList<Double>>() {
        });

        bind(new TypeLiteral<List<CandleStick>>() {
        }).annotatedWith(Names.named("indicator.inputData")).toInstance(inputData);

        bindConstant().annotatedWith(Names.named("periodIndicator.simpleMovingAverage.period")).to(getConfig().getInt("periodIndicator.simpleMovingAverage.period"));
        bind(SimpleMovingAverage.class);

        bindConstant().annotatedWith(Names.named("periodIndicator.exponentialMovingAverage.period")).to(getConfig().getInt("periodIndicator.exponentialMovingAverage.period"));
        bind(ExponentialMovingAverage.class);

        bindConstant().annotatedWith(Names.named("periodIndicator.rsi.period")).to(getConfig().getInt("periodIndicator.rsi.period"));
        bind(RSI.class);

        bindConstant().annotatedWith(Names.named("periodIndicator.stochRsi.period")).to(getConfig().getInt("periodIndicator.stochRsi.period"));
        bind(StochRSI.class);
    }
}
