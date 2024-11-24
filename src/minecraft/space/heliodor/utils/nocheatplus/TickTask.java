package space.heliodor.utils.nocheatplus;

public class TickTask {
    /** The Constant lagMaxTicks. */
    public static final int lagMaxTicks = 80;

    /** Last n tick durations, measured from run to run.*/
    private static final long[] tickDurations = new long[lagMaxTicks];

    /** Tick durations summed up in packs of n (nxn time covered). */
    private static final long[] tickDurationsSq = new long[lagMaxTicks];

    /** Maximally covered time on ms for lag tracking, roughly. */
    private static final long lagMaxCoveredMs = 50L * (1L + lagMaxTicks * (1L + lagMaxTicks));

    /** The tick. */
    protected static int tick = 0;

    /** The time start. */
    protected static long timeStart = 0;

    /** The time last. */
    protected static long timeLast = 0;

    /** Lock flag set on disable. */
    protected static boolean locked = true;
    public static final float getLag(final long ms, final boolean exact) {
        if (ms < 0) {
            // Account for freezing (i.e. check timeLast, might be an extra method)!
            return getLag(0, exact);
        }
        else if (ms > lagMaxCoveredMs) {
            return getLag(lagMaxCoveredMs, exact);
        }
        final int tick = TickTask.tick;
        if (tick == 0) {
            return 1f;
        }
        final int add = ms > 0 && (ms % 50) == 0 ? 0 : 1;
        // TODO: Consider: Put "exact" block here, subtract a tick if appropriate?
        final int totalTicks = Math.min(tick, add + (int) (ms / 50));
        final int maxTick = Math.min(lagMaxTicks, totalTicks);
        long sum = tickDurations[maxTick - 1];
        long covered = maxTick * 50;

        // Only count fully covered:
        if (totalTicks > lagMaxTicks) {
            int maxTickSq = Math.min(lagMaxTicks, totalTicks / lagMaxTicks);
            if (lagMaxTicks * maxTickSq == totalTicks) {
                maxTickSq -= 1;
            }
            sum += tickDurationsSq[maxTickSq - 1];
            covered += lagMaxTicks * 50 * maxTickSq;
        }

        if (exact) {
            // Attempt to count in the current tick.
            final long passed = System.currentTimeMillis() - timeLast;
            if (passed > 50) {
                // Only count in in the case of "overtime".
                covered += 50;
                sum += passed;
            }
        }
        // TODO: Investigate on < 1f.
        return Math.max(1f, (float) sum / (float) covered);
    }
}
