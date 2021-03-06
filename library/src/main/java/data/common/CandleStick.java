package data.common;

import utility.Builder;

import java.time.LocalDate;

public class CandleStick {

    private final LocalDate date;
    private final double open;
    private final double close;
    private final double high;
    private final double low;
    private final double volume;
    private final ClosingOutcome closingOutcome;


    private CandleStick(LocalDate date, double open, double close, double high, double low, double volume) {
        this.date = date;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;

        if (open < close) {
            closingOutcome = ClosingOutcome.GAIN;
        } else if (open > close) {
            closingOutcome = ClosingOutcome.LOSS;
        } else {
            closingOutcome = ClosingOutcome.FLAT;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getVolume() {
        return volume;
    }

    public ClosingOutcome getClosingOutcome() { return closingOutcome; }

    public double getCloseOpenSpread() {
        return close - open;
    }

    public double getAbsCloseOpenSpread() {
        return Math.abs(close - open);
    }

    public static CandleStickBuilder builder() {
        return new CandleStickBuilder();
    }

    public static class CandleStickBuilder implements Builder<CandleStick> {

        private LocalDate builderDate;
        private double builderOpen;
        private double builderClose;
        private double builderHigh;
        private double builderLow;
        private double builderVolume;

        private CandleStickBuilder() {}

        public CandleStickBuilder date(LocalDate date) {
            this.builderDate = date;
            return this;
        }

        public CandleStickBuilder open(double open) {
            this.builderOpen = open;
            return this;
        }

        public CandleStickBuilder close(double close) {
            this.builderClose = close;
            return this;
        }

        public CandleStickBuilder high(double high) {
            this.builderHigh = high;
            return this;
        }

        public CandleStickBuilder low(double low) {
            this.builderLow = low;
            return this;
        }

        public CandleStickBuilder volume(double volume) {
            this.builderVolume = volume;
            return this;
        }

        @Override
        public CandleStick build() {
            return new CandleStick(builderDate,
                    builderOpen,
                    builderClose,
                    builderHigh,
                    builderLow,
                    builderVolume);
        }
    }

    public enum ClosingOutcome {
        GAIN, LOSS, FLAT
    }
}
