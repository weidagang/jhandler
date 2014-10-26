package jhandler.internal;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class TimeUtils {
    public static long uptime() {
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
        return rb.getUptime();
    }
}
