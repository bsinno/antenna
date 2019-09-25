package org.eclipse.sw360.antenna.model.purl;

public class PURLUtils {
    private PURLUtils() {
        // only utils
    }

    //public static boolean matchesAsWildcardPattern(PURL wildcard, PURL purl) {
    //
    //}

    private static PURL.Builder mkBaseBuilder(String name, String version) {
        return PURL.builder()
                .withName(name)
                .withVersion(version);
    }

    public static PURL mkMavenPURL(String artifactId, String groupId, String version) {
        return mkBaseBuilder(artifactId, version)
                .withNamespace(groupId)
                .withType(PURL.StandardTypes.MAVEN)
                .build();
    }

    public static PURL mkBundlePURL(String symbolicName, String bundleVersion) {
        return mkBaseBuilder(symbolicName, bundleVersion)
                .withType(PURL.StandardTypes.BUNDLE)
                .build();
    }

    public static PURL mkJavaScriptCoordinates(String name, String artifactId, String version) {
        return mkBaseBuilder(artifactId, version)
                .withType(PURL.StandardTypes.NPM)
                .withNamespace(name) // TODO: that is broken
                .build();
    }

    public static PURL mkDotNetCoordinates(String packageId, String version) {
        return mkBaseBuilder(packageId, version)
                .withType(PURL.StandardTypes.NUGET)
                .build();
    }
}
