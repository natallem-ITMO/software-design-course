package server.membership.controller;

import clock.Clock;
import clock.SettableClock;
import errors.NoValidMembershipException;
import errors.NoValidVisitException;
import models.UserId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import store.EventStore;
import store.EventStoreJdbc;
import utilities.UserUtilities;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Controller
public class MembershipController {
    UserUtilities userUtilities;

    public MembershipController(EventStore store, Clock clock) {
        userUtilities = new UserUtilities(store, clock);
    }

    @RequestMapping(value = "/deliver-membership", method = RequestMethod.GET)
    @ResponseBody
    public String deliverMembership(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to,
            @RequestParam("id") String id) {
        try {
            userUtilities.deliverMembership(new UserId(id), new Timestamp(from.getTime()), new Timestamp(to.getTime()));
        } catch (NoValidMembershipException | NoValidVisitException e) {
            return e.getMessage();
        }
        return "SUCCESS";
    }

    @RequestMapping(value = "/prolong-membership", method = RequestMethod.GET)
    @ResponseBody
    public String prolongMembership(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date,
            @RequestParam("id") String id) {
        try {
            userUtilities.prolongMembership(new UserId(id), new Timestamp(date.getTime()));
        } catch (NoValidMembershipException | NoValidVisitException e) {
            return e.getMessage();
        }
        return "SUCCESS";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public String prolongMembership(@RequestParam("id") String id) {
        try {
            return userUtilities.getUserInfo(new UserId(id));
        } catch (NoValidMembershipException | NoValidVisitException e) {
            return e.getMessage();
        }
    }



}
