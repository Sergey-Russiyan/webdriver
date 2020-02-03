package common.util;

import lombok.SneakyThrows;
import org.apache.log4j.Logger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DateUtils {

    private static final Logger log = Logger.getLogger(DateUtils.class);

    private final LocalDate today = LocalDate.now();
    private final LocalDate nextDay = today.plus(1, ChronoUnit.DAYS);
    private final LocalDate previousDay = today.minus(1, ChronoUnit.DAYS);
    private final LocalDate fiveDaysInFuture = today.plus(5, ChronoUnit.DAYS);

}
