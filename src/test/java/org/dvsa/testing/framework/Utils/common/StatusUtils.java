package org.dvsa.testing.framework.Utils.common;

import activesupport.aws.s3.S3;
import activesupport.aws.s3.util.OurBuckets;
import activesupport.string.Str;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static activesupport.aws.s3.S3.any;
import static activesupport.aws.s3.S3.client;

public class StatusUtils {

    private static String baseFolder = "automation/";

    public static void register(String id, Phase condition) {
        String key = baseFolder.concat(condition.toString() + id);
        deleteAllOtherPhases(key); // Scan and delete any existing statuses
        client().putObject(OurBuckets.QA, key, "");
    }

    public static void deleteAllOtherPhases(String key){
        Phase currentPhase = extractPhase(key);
        Arrays.stream(Phase.values())
                .filter(phase -> phase != currentPhase)
                .map((phase) -> updateKeyToPhase(key, phase))
                .forEach(S3::deleteObject);
    }

    private static String updateKeyToPhase(String key, Phase phase) {
        Phase previousPhase = extractPhase(key);
        return key.replace(previousPhase.toString(), phase.toString());
    }

    private static Phase extractPhase(String key) {
        StringBuilder regexBuilder = new StringBuilder().append("(");

        int length = Phase.values().length;

        for (int idx = 0; idx < length; idx++) {
            String pipe = idx < (length - 1) ? "|" : "";
            regexBuilder.append(Phase.values()[idx]).append(pipe);
        }

        regexBuilder.append(")");
        String regex = regexBuilder.toString();

        return Phase.getEnum(Str.find(regex, key).get());
    }

    public static void update(String id, Phase condition) {
        register(id, condition);
    }

    public static void untilNonAtPhase(Phase phase, long duration, TimeUnit timeUnit) throws InterruptedException {
        untilNonAtPhase(phase, duration, timeUnit, 15000L);
    }

    public static void untilNonAtPhase(Phase phase, long duration, TimeUnit timeUnit, long pollMilliseconds) throws InterruptedException {
     long durationMilli = TimeUnit.MILLISECONDS.convert(duration, timeUnit);
     boolean anyAtPhase = true;

         while(durationMilli >= 0 && anyAtPhase) {
             if (anyAtPhase = anyAtPhase(phase))
                 TimeUnit.MILLISECONDS.sleep(pollMilliseconds);

             durationMilli -= pollMilliseconds;
         }
    }

    public static boolean noneAtPhase(Phase phase){
        return !anyAtPhase(phase);
    }

    public static boolean anyAtPhase(Phase phase){
        return any(OurBuckets.QA, baseFolder.concat(phase.toString()));
    }

    public enum Phase {
        OPEN("open/"),
        CLOSING("closing/"),
        WAITING("waiting/"),
        CLOSED("closed/");

        private String phase;

        Phase(String phase) {
            this.phase = phase;
        }

        public static Phase getEnum(String name) {
            return Arrays.stream(Phase.values())
                    .filter((Phase phase) -> phase.toString().toLowerCase().contains(name.toLowerCase()))
                    .findFirst()
                    .get();
        }

        @Override
        public String toString() {
            return phase;
        }
    }

}
