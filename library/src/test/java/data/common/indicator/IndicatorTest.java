package data.common.indicator;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import data.common.CandleStick;
import data.indicator.Indicator;
import data.indicator.IndicatorModule;
import data.indicator.period.ExponentialMovingAverage;
import data.indicator.period.RSI;
import data.indicator.period.SimpleMovingAverage;
import data.indicator.period.StochRSI;
import org.junit.Before;
import org.junit.Test;
import utility.parse.CSVParser;
import utility.parse.Parser;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * This unit test is only used to verify that injection is working correctly for all indicators and that bare-bones
 * calculations are outputting values as expected. The accuracy of these indicators is better tested in other unit tests.
 */
public class IndicatorTest {

    private static final double D_FUZZ = 0.0001;

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
        assertEquals(sma.getData().size(), 118);

        assertEquals(sma.getData().get(0), 2084.8157, D_FUZZ);
        assertEquals(sma.getData().get(1), 2080.965, D_FUZZ);
    }

    @Test
    public void emaTest() {
        assertEquals(ema.getData().size(), data.size());

        assertEquals(ema.getData().get(0), 2137.1599, D_FUZZ);
        assertEquals(ema.getData().get(1), 2136.1919, D_FUZZ);
        assertEquals(ema.getData().get(2), 2131.0863, D_FUZZ);
    }

    @Test
    public void rsiTest() {
        assertEquals(rsi.getData().size(), 118);

        assertEquals(rsi.getData().get(0), 16.4225, D_FUZZ);
        assertEquals(rsi.getData().get(1), 21.3269, D_FUZZ);
    }

    @Test
    public void stochRsiTest() {
        assertEquals(stochRsi.getData().size(), 118);

        assertEquals(stochRsi.getData().get(0), 0.0, D_FUZZ);
        assertEquals(stochRsi.getData().get(1), 0.0, D_FUZZ);
        assertEquals(stochRsi.getData().get(12), 0.0, D_FUZZ);
        assertEquals(stochRsi.getData().get(13), 0.9924, D_FUZZ);
        assertEquals(stochRsi.getData().get(14), 0.944, D_FUZZ);
    }

    private void initializeInputData() {
        Config parentConf = ConfigFactory.load("application.conf");

        Config dataInputConf = parentConf.getConfig("application.data_input");

        int dateIndex = dataInputConf.getInt("indices.date");
        int openIndex = dataInputConf.getInt("indices.open");
        int highIndex = dataInputConf.getInt("indices.high");
        int lowIndex = dataInputConf.getInt("indices.low");
        int closeIndex = dataInputConf.getInt("indices.close");
        int volumeIndex = dataInputConf.getInt("indices.volume");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dataInputConf.getString("data_format"));

        Config applicationTestConf = parentConf.getConfig("application-test");

        String fileLocation = applicationTestConf.getString("test_input_data_name");
        boolean dailyData = applicationTestConf.getBoolean("test_input_data_daily");

        Parser parser = CSVParser.builder()
                .dateTimeFormatter(dtf)
                .dateIndex(dateIndex)
                .openIndex(openIndex)
                .highIndex(highIndex)
                .lowIndex(lowIndex)
                .closeIndex(closeIndex)
                .volumeIndex(volumeIndex)
                .dailyData(dailyData)
                .build();

        data = parser.parse(new File(fileLocation));
    }
}
