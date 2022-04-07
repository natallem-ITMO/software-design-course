package ru.natallem.redditstat.interfaces;

public interface IRequestQueryConstructor {
    String constructQuery(String tag, int hoursAgo);
}
