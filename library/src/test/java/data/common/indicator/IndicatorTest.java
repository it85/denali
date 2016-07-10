package data.common.indicator;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import data.indicator.Indicator;
import data.indicator.lag.ExponentialMovingAverage;
import data.indicator.lag.SimpleMovingAverage;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IndicatorTest {

    private static final double D_FUZZ = 0.0001;

    private List<Double> data;
    private Indicator sma;
    private Indicator ema;

    @Before
    public void setUp() {
        data = new ArrayList<>();

        data.add(1.0);
        data.add(2.0);
        data.add(3.0);
        data.add(4.0);
        data.add(5.0);
        data.add(6.0);
        data.add(7.0);
        data.add(8.0);
        data.add(9.0);
        data.add(10.0);
        data.add(11.0);
        data.add(12.0);
        data.add(13.0);
        data.add(14.0);
        data.add(15.0);

        Injector injector = Guice.createInjector(new IndicatorModuleUnitTest(data));

        sma = injector.getInstance(SimpleMovingAverage.class);
        ema = injector.getInstance(ExponentialMovingAverage.class);
    }

    @Test
    public void guiceInjectionSmokeTest() {
        assertNotNull(sma);
        assertNotNull(ema);

        assertEquals(sma.getInputData().size(), data.size());
        assertEquals(ema.getInputData().size(), data.size());

        assertEquals(sma.getData().size(), 2);
        assertEquals(ema.getData().size(), data.size());

        assertEquals(sma.getData().get(0), 7.5, 0.001);
        assertEquals(sma.getData().get(1), 8.5, 0.001);

        assertEquals(ema.getData().get(0), 1.0, D_FUZZ);
        assertEquals(ema.getData().get(1), 1.1333, D_FUZZ);
        assertEquals(ema.getData().get(2), 1.3822, D_FUZZ);
    }

    protected class IndicatorModuleUnitTest extends AbstractModule {

        private final List<Double> inputData;

        IndicatorModuleUnitTest(List<Double> inputData) {
            this.inputData = inputData;
        }

        @Override
        public void configure() {

            bind(new TypeLiteral<List<Double>>() {
            }).annotatedWith(Names.named("indicator.inputData")).toInstance(inputData);

            bind(new TypeLiteral<List<Double>>() {
            }).to(new TypeLiteral<ArrayList<Double>>() {
            });

            bindConstant().annotatedWith(Names.named("simpleMovingAverage.period")).to(14);
            bind(SimpleMovingAverage.class);

            bindConstant().annotatedWith(Names.named("exponentialMovingAverage.period")).to(14);
            bind(ExponentialMovingAverage.class);
        }
    }
}
