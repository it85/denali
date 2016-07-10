package data.common;

import utility.Builder;

public class CandleStick {

    private final double open;
    private final double close;
    private final double high;
    private final double low;
    private final double volume;
    private final ClosingOutcome closingOutcome;


    private CandleStick(double open, double close, double high, double low, double volume) {
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

    public static class CandleStickBuilder implements Builder<CandleStick> {

        private double builderOpen;
        private double builderClose;
        private double builderHigh;
        private double builderLow;
        private double builderVolume;

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
            return new CandleStick(builderOpen,
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
