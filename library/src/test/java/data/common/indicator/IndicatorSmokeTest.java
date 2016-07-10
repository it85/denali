package data.common.indicator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import data.common.CandleStick;
import data.indicator.Indicator;
import data.indicator.IndicatorModule;
import data.indicator.period.ExponentialMovingAverage;
import data.indicator.period.RSI;
import data.indicator.period.SimpleMovingAverage;
import data.indicator.period.StochRSI;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This unit test is only used to verify that injection is working correctly for all indicators and that bare-bones
 * calculations are outputting values as expected. The accuracy of these indicators is better tested in other unit tests.
 */
public class IndicatorSmokeTest {

    private static final double D_FUZZ = 0.0001;
    private static final int DATA_SIZE = 28;

    private final List<Indicator> indicators = new ArrayList<>();

    private List<CandleStick> data;
    private Indicator sma;
    private Indicator ema;
    private Indicator rsi;
    private Indicator stochRsi;

    @Before
    public void setUp() {
        initializeInputData();

        Injector injector = Guice.createInjector(new IndicatorModule(data));

        sma = injector.getInstance(SimpleMovingAverage.class);
        indicators.add(sma);

        ema = injector.getInstance(ExponentialMovingAverage.class);
        indicators.add(ema);

        rsi = injector.getInstance(RSI.class);
        indicators.add(rsi);

        stochRsi = injector.getInstance(StochRSI.class);
        indicators.add(stochRsi);

        for (Indicator i : indicators) {
            i.loadData();
        }
    }

    @Test
    public void guiceInjectionSmokeTest() {
        assertNotNull(sma);
        assertNotNull(ema);
        assertNotNull(rsi);
        assertNotNull(stochRsi);

        assertEquals(sma.getInputData().size(), data.size());
        assertEquals(ema.getInputData().size(), data.size());
        assertEquals(rsi.getInputData().size(), data.size());
        assertEquals(stochRsi.getInputData().size(), data.size());
    }

    @Test
    public void smaTest() {
        assertEquals(sma.getData().size(), 15);

        assertEquals(sma.getData().get(0), 7.5, 0.001);
        assertEquals(sma.getData().get(1), 8.5, 0.001);
    }

    @Test
    public void emaTest() {
        assertEquals(ema.getData().size(), data.size());

        assertEquals(ema.getData().get(0), 1.0, D_FUZZ);
        assertEquals(ema.getData().get(1), 1.1333, D_FUZZ);
        assertEquals(ema.getData().get(2), 1.3822, D_FUZZ);
    }

    @Test
    public void rsiTest() {
        assertEquals(rsi.getData().size(), 15);

        assertEquals(rsi.getData().get(0), 100.0, D_FUZZ);
        assertEquals(rsi.getData().get(1), 100.0, D_FUZZ);
    }

    @Test
    public void stochRsiTest() {
        assertEquals(stochRsi.getData().size(), 15);

        assertEquals(stochRsi.getData().get(0), 0.0, D_FUZZ);
        assertEquals(stochRsi.getData().get(1), 0.0, D_FUZZ);
        assertEquals(stochRsi.getData().get(12), 0.0, D_FUZZ);
        assertEquals(stochRsi.getData().get(13), 1.0, D_FUZZ);
        assertEquals(stochRsi.getData().get(14), 1.0, D_FUZZ);
    }

    private void initializeInputData() {
        data = new ArrayList<>();

        for (int i = 0; i < DATA_SIZE; i++) {
            double val = (double) i;

            // initialize a nonsense candlestick
            CandleStick candleStick = new CandleStick.CandleStickBuilder()
                    .open(val)
                    .close(val + 1)
                    .high(val + 2)
                    .low(val + 3)
                    .volume(val + 4)
                    .build();

            data.add(candleStick);
        }
    }
}
