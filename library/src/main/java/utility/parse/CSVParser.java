package utility.parse;

import data.common.CandleStick;
import utility.Builder;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CSVParser extends Parser {

    private static final String DELIMITER = ",";
    private static final String TIME_ADDENDUM = " 00:00:00";

    private final boolean dailyData;

    private BufferedReader reader;
    private boolean readReverse;
    private String LINE;

    private CSVParser(DateTimeFormatter dateTimeFormatter,
                      Integer dateIndex,
                      Integer openIndex,
                      Integer lowIndex,
                      Integer closeIndex,
                      Integer volumeIndex,
                      Integer highIndex,
                      boolean dailyData,
                      boolean readReverse) {
        super(dateTimeFormatter, dateIndex, openIndex, lowIndex, closeIndex, volumeIndex, highIndex);

        this.dailyData = dailyData;
        this.readReverse = readReverse;
    }

    @Override
    public List<CandleStick> parse(File file) {
        List<CandleStick> output = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(file));

            if (readReverse) {
                parseAndLoadDataReversed(output, reader);
            } else {
                parseAndLoadData(output, reader);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return output;
    }

    private void parseAndLoadData(List<CandleStick> output, BufferedReader reader) throws IOException {
        while ((LINE = reader.readLine()) != null) {

            String[] data = LINE.split(DELIMITER);

            String dateString = data[dateIndex];
            dateString = dailyData ? (dateString + TIME_ADDENDUM) : dateString;

            LocalDate date = LocalDate.parse(dateString, dateTimeFormatter);
            double open = Double.parseDouble(data[openIndex]);
            double low = Double.parseDouble(data[lowIndex]);
            double close = Double.parseDouble(data[closeIndex]);
            double volume = Double.parseDouble(data[volumeIndex]);
            double high = Double.parseDouble(data[highIndex]);

            CandleStick candleStick = CandleStick.builder()
                    .date(date)
                    .open(open)
                    .low(low)
                    .close(close)
                    .volume(volume)
                    .high(high)
                    .build();

            output.add(candleStick);
        }
    }

    private void parseAndLoadDataReversed(List<CandleStick> output, BufferedReader reader) throws IOException {
        Stack<String[]> dataStack = new Stack<>();

        while ((LINE = reader.readLine()) != null) {
            String[] data = LINE.split(DELIMITER);
            dataStack.push(data);
        }

        while (!dataStack.empty()) {
            String[] data = dataStack.pop();
            String dateString = data[dateIndex];
            dateString = dailyData ? (dateString + TIME_ADDENDUM) : dateString;

            LocalDate date = LocalDate.parse(dateString, dateTimeFormatter);
            double open = Double.parseDouble(data[openIndex]);
            double low = Double.parseDouble(data[lowIndex]);
            double close = Double.parseDouble(data[closeIndex]);
            double volume = Double.parseDouble(data[volumeIndex]);
            double high = Double.parseDouble(data[highIndex]);

            CandleStick candleStick = CandleStick.builder()
                    .date(date)
                    .open(open)
                    .low(low)
                    .close(close)
                    .volume(volume)
                    .high(high)
                    .build();

            output.add(candleStick);
        }
    }

    public static CSVParserBuilder builder() {
        return new CSVParserBuilder();
    }

    public static class CSVParserBuilder implements Builder {

        DateTimeFormatter builderDateTimeFormatter;
        Integer builderDateIndex;
        Integer builderOpenIndex;
        Integer builderLowIndex;
        Integer builderCloseIndex;
        Integer builderVolumeIndex;
        Integer builderHighIndex;
        boolean builderDailyData;
        boolean builderReadReverse;

        private CSVParserBuilder() {}

        public CSVParserBuilder dateTimeFormatter(DateTimeFormatter formatter) {
            builderDateTimeFormatter = formatter;
            return this;
        }

        public CSVParserBuilder dateIndex(int index) {
            builderDateIndex = index;
            return this;
        }

        public CSVParserBuilder openIndex(int index) {
            builderOpenIndex = index;
            return this;
        }

        public CSVParserBuilder lowIndex(int index) {
            builderLowIndex = index;
            return this;
        }

        public CSVParserBuilder closeIndex(int index) {
            builderCloseIndex = index;
            return this;
        }

        public CSVParserBuilder volumeIndex(int index) {
            builderVolumeIndex = index;
            return this;
        }

        public CSVParserBuilder highIndex(int index) {
            builderHighIndex = index;
            return this;
        }

        public CSVParserBuilder dailyData(boolean dailyData) {
            builderDailyData = dailyData;
            return this;
        }

        public CSVParserBuilder readReverse(boolean readReverse) {
            builderReadReverse = readReverse;
            return this;
        }

        @Override
        public CSVParser build() {
            return new CSVParser(builderDateTimeFormatter,
                    builderDateIndex,
                    builderOpenIndex,
                    builderLowIndex,
                    builderCloseIndex,
                    builderVolumeIndex,
                    builderHighIndex,
                    builderDailyData,
                    builderReadReverse);
        }

    }


}
