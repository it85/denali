package utility.parse;

import data.common.CandleStick;

import javax.annotation.Nonnull;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class Parser {

    @Nonnull
    DateTimeFormatter dateTimeFormatter;

    @Nonnull
    Integer dateIndex;

    @Nonnull
    Integer openIndex;

    @Nonnull
    Integer lowIndex;

    @Nonnull
    Integer closeIndex;

    @Nonnull
    Integer volumeIndex;

    @Nonnull
    Integer highIndex;

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
