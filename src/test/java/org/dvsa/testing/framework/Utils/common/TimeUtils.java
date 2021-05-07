package org.dvsa.testing.framework.Utils.common;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class TimeUtils {

    public static LocalTime closestTimeFrom(LocalTime from, List<LocalTime> timesCollection) {
        List<LocalTime> sortedTimes = timesCollection.stream().sorted(LocalTime::compareTo).collect(Collectors.toList());
        return sortedTimes.stream().filter(from::isBefore).findFirst().get();
    }

}
