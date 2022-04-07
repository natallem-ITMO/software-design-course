package server.report.controller;

import clock.Clock;
import clock.SettableClock;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import store.EventStore;
import utilities.StatisticUtility;
import utilities.UserUtilities;

import java.time.Instant;
import java.util.Date;

@Controller
public class ReportController {
    StatisticUtility statisticUtility;

    public ReportController(EventStore store, Clock clock) {
        statisticUtility = new StatisticUtility(store, clock);
    }

    @RequestMapping(value = "/date-stats", method = RequestMethod.GET)
    @ResponseBody
    public String dateStats() {
        return statisticUtility.getDayStatistics();
    }

    @RequestMapping(value = "/average-stats", method = RequestMethod.GET)
    @ResponseBody
    public String averageStats() {
        return statisticUtility.getAverageStatistics();
    }

}
