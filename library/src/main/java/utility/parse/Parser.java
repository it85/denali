package utility.parse;

import data.common.CandleStick;

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class Parser {

    final DateTimeFormatter dateTimeFormatter;
    final Integer dateIndex;
    final Integer openIndex;
    final Integer lowIndex;
    final Integer closeIndex;
    final Integer volumeIndex;
    final Integer highIndex;

    Parser(DateTimeFormatter dateTimeFormatter,
           Integer dateIndex,
           Integer openIndex,
           Integer lowIndex,
           Integer closeIndex,
           Integer volumeIndex,
           Integer highIndex) {
        this.dateTimeFormatter = dateTimeFormatter;
        this.dateIndex = dateIndex;
        this.openIndex = openIndex;
        this.lowIndex = lowIndex;
        this.closeIndex = closeIndex;
        this.volumeIndex = volumeIndex;
        this.highIndex = highIndex;
    }

    public abstract List<CandleStick> parse(File file);
}
