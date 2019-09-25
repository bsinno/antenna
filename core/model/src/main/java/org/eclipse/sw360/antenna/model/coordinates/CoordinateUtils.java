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

    public static Coordinate mkMavenPURL(String artifactId, String groupId, String version) {
        return mkBaseBuilder(artifactId, version)
                .withNamespace(groupId)
                .withType(Coordinate.StandardTypes.MAVEN)
                .build();
    }

    public static Coordinate mkBundlePURL(String symbolicName, String bundleVersion) {
        return mkBaseBuilder(symbolicName, bundleVersion)
                .withType(Coordinate.StandardTypes.BUNDLE)
                .build();
    }

    public static Coordinate mkJavaScriptCoordinates(String name, String artifactId, String version) {
        return mkBaseBuilder(artifactId, version)
                .withType(Coordinate.StandardTypes.NPM)
                .withNamespace(name) // TODO: that is broken
                .build();
    }

    public static Coordinate mkDotNetCoordinates(String packageId, String version) {
        return mkBaseBuilder(packageId, version)
                .withType(Coordinate.StandardTypes.NUGET)
                .build();
    }
}
