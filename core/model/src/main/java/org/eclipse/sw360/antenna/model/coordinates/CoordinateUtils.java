package org.eclipse.sw360.antenna.model.coordinates;

public class CoordinateUtils {
    private CoordinateUtils() {
        // only utils
    }

    //public static boolean matchesAsWildcardPattern(Coordinate wildcard, Coordinate purl) {
    //
    //}

    private static Coordinate.Builder mkBaseBuilder(String name, String version) {
        return Coordinate.builder()
                .withName(name)
                .withVersion(version);
    }
}
