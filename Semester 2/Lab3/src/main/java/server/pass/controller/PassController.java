package server.pass.controller;

import clock.Clock;
import clock.SettableClock;
import errors.NoValidMembershipException;
import errors.NoValidVisitException;
import models.UserId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import store.EventStore;
import utilities.UserUtilities;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Controller
public class PassController {
    UserUtilities userUtilities;

    public PassController(EventStore store, Clock clock) {
        userUtilities = new UserUtilities(store, clock);
    }

    @RequestMapping(value = "/can-enter", method = RequestMethod.GET)
    @ResponseBody
    public String deliverMembership(@RequestParam("id") String id) {
        try {
            userUtilities.enterUser(new UserId(id));
        } catch (NoValidMembershipException | NoValidVisitException e){
            return e.getMessage();
        }
        return "Client can enter";
    }

    @RequestMapping(value = "/went-out", method = RequestMethod.GET)
    @ResponseBody
    public String prolongMembership( @RequestParam("id") String id) {
        try {
            userUtilities.wentOutUser(new UserId(id));
        } catch (NoValidMembershipException | NoValidVisitException e){
            return e.getMessage();
        }
        return "Client can went out";
    }
}
