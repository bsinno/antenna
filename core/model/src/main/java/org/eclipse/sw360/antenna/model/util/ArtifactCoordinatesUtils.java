package org.eclipse.sw360.antenna.model.util;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURLBuilder;
import org.eclipse.sw360.antenna.api.exceptions.AntennaExecutionException;
import org.eclipse.sw360.antenna.model.artifact.facts.ArtifactCoordinates;

public class ArtifactCoordinatesUtils {
    private ArtifactCoordinatesUtils() {
        // only utils
    }

    private static PackageURLBuilder mkBaseBuilder(String name, String version) {
        return PackageURLBuilder.aPackageURL()
                .withName(name)
                .withVersion(version);
    }

    public static ArtifactCoordinates mkMavenCoordinates(String artifactId, String groupId, String version) {
        try {
            return new ArtifactCoordinates(mkBaseBuilder(artifactId, version)
                    .withNamespace(groupId)
                    .withType(PackageURL.StandardTypes.MAVEN)
                    .build());
        } catch (MalformedPackageURLException e) {
            throw new AntennaExecutionException("Failed to build package url.", e);
        }
    }

    public static ArtifactCoordinates mkBundleCoordinates(String symbolicName, String bundleVersion) {
        try {
            return new ArtifactCoordinates(mkBaseBuilder(symbolicName, bundleVersion)
                    .withType("p2")
                    .build());
        } catch (MalformedPackageURLException e) {
            throw new AntennaExecutionException("Failed to build package url.", e);
        }
    }

    public static ArtifactCoordinates mkJavaScriptCoordinates(String name, String artifactId, String version) {
        try {
            return new ArtifactCoordinates(mkBaseBuilder(artifactId, version)
                    .withType(PackageURL.StandardTypes.NPM)
                    .withNamespace(name) // TODO: that is broken
                    .build());
        } catch (MalformedPackageURLException e) {
            throw new AntennaExecutionException("Failed to build package url.", e);
        }
    }

    public static ArtifactCoordinates mkDotNetCoordinates(String packageId, String version) {
        try {
            return new ArtifactCoordinates(mkBaseBuilder(packageId, version)
                    .withType(PackageURL.StandardTypes.NUGET)
                    .build());
        } catch (MalformedPackageURLException e) {
            throw new AntennaExecutionException("Failed to build package url.", e);
        }
    }
}
