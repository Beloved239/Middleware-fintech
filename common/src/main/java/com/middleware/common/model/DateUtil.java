package com.middleware.common.model;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtil {
    private final static ZoneId westAfricaZoneId = ZoneId.of("Africa/Lagos");
    private final static ZoneId systemZoneId = ZoneId.systemDefault();

    public static LocalDateTime getZonedLocalDateTime (LocalDateTime localDateTime) {
        if (localDateTime == null)
            return null;
        return localDateTime.atZone(systemZoneId)
                .withZoneSameInstant(westAfricaZoneId)
                .toLocalDateTime();
    }
}
