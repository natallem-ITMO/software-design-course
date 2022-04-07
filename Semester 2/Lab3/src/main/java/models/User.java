package models;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class User {
    UserId id;
    List<Membership> membershipList = new ArrayList<>();
    List<Visit> visitList = new ArrayList<>();

    public User(UserId id, List<Membership> membershipList) {
        this.id = id;
        this.membershipList = membershipList;
    }

    public User(UserId id) {
        this.id = id;
    }

    public UserId getId() {
        return id;
    }

    public List<Visit> getVisitList() {
        return visitList;
    }

    public List<Membership> getMembershipList() {
        return membershipList;
    }

    public boolean hasValidMembership(Instant now) {
        if (membershipList.isEmpty()) {
            return false;
        }
        Membership currentMembership = membershipList.get(membershipList.size() - 1);
        Instant expire = currentMembership.expires.toInstant();
        Instant start = currentMembership.start.toInstant();
        return now.isAfter(start) && now.isBefore(expire);
    }

    public boolean canProlongMembership(Instant now, Timestamp newExpiringDate) {
        Membership currentMembership = membershipList.get(membershipList.size() - 1);
        Instant expire = currentMembership.expires.toInstant();
        return hasValidMembership(now) && newExpiringDate.toInstant().isAfter(expire);
    }

    public boolean isInGym() {
        return !visitList.isEmpty() && visitList.get(visitList.size() - 1).getWentOutTime() == null;
    }

    @Override
    public String toString() {
        return "User with id " + id.getUserId() + "\n. Available memberships:" +
                getMembershipList().stream().map(Objects::toString).collect(Collectors.joining(System.lineSeparator()))
                + "\n. Visites: " + getVisitList().stream().map(Objects::toString).collect(Collectors.joining(System.lineSeparator()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return id.equals(((User) obj).id)
                    && visitList.equals(((User) obj).visitList)
                    && membershipList.equals(((User) obj).membershipList);
        }
        return false;
    }
}
