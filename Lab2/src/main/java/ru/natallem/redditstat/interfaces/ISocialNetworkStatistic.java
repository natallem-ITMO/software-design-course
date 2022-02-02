package ru.natallem.redditstat.interfaces;


import java.util.List;

public interface ISocialNetworkStatistic {
    List<Integer> getStatisticByHours(String tag, int hours);
}
