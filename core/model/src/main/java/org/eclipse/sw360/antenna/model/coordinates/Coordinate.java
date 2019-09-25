package org.eclipse.sw360.antenna.model.coordinates;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURLBuilder;
import org.eclipse.sw360.antenna.api.exceptions.AntennaExecutionException;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * A thin wrapper around com.github.packageurl.PackageURL
 * It just delegates
 */
public final class Coordinate {
    private final PackageURL packageURL;

    public Coordinate(PackageURL packageURL) {
        this.packageURL = packageURL;
    }

    public Coordinate(String purl) {
        try {
            packageURL = new PackageURL(purl);
        } catch (MalformedPackageURLException e) {
            throw new AntennaExecutionException("Failed to create PackageURL in Coordinate", e);
        }
    }

    public Coordinate(String type, String name) {
        try {
            packageURL = new PackageURL(type, name);
        } catch (MalformedPackageURLException e) {
            throw new AntennaExecutionException("Failed to create PackageURL in Coordinate", e);
        }
    }

    public Coordinate(String type, String namespace, String name, String version, TreeMap<String, String> qualifiers, String subpath) {
        try {
            packageURL = new PackageURL(type, namespace, name, version, qualifiers, subpath);
        } catch (MalformedPackageURLException e) {
            throw new AntennaExecutionException("Failed to create PackageURL in Coordinate", e);
        }
    }

    public String getScheme() {
        return packageURL.getScheme();
    }

    public final String getType() {
        return packageURL.getType();
    }

    public String getNamespace() {
        return packageURL.getNamespace();
    }

    public String getName() {
        return packageURL.getName();
    }

    public String getVersion() {
        return packageURL.getVersion();
    }

    public Map<String, String> getQualifiers() {
        return packageURL.getQualifiers();
    }

    public String getSubpath() {
        return packageURL.getSubpath();
    }

    public String toString() {
        return packageURL.toString();
    }

    public String canonicalize() {
        return packageURL.canonicalize();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate coordinate = (Coordinate) o;
        return Objects.equals(packageURL, coordinate.packageURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packageURL);
    }

    public static class StandardTypes extends PackageURL.StandardTypes {
        public static final String BUNDLE = "p2";

        public static final Set<String> all = Stream.of(
                BITBUCKET, COMPOSER, DEBIAN, DOCKER, GEM, GENERIC, GITHUB, GOLANG, MAVEN, NPM, NUGET, PYPI, RPM,
                BUNDLE)
                .collect(Collectors.toSet());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PackageURLBuilder packageURLBuilder = PackageURLBuilder.aPackageURL();

        public Builder withType(String type) {
            packageURLBuilder.withType(type);
            return this;
        }

        public Builder withGroupId(String namespace) {
            packageURLBuilder.withNamespace(namespace);
            return this;
        }

        public Builder withNamespace(String namespace) {
            packageURLBuilder.withNamespace(namespace);
            return this;
        }

        public Builder withArtifactId(String name) {
            packageURLBuilder.withName(name);
            return this;
        }

        public Builder withName(String name) {
            packageURLBuilder.withName(name);
            return this;
        }

        public Builder withVersion(String version) {
            packageURLBuilder.withVersion(version);
            return this;
        }

        public Builder withSubpath(String subpath) {
            packageURLBuilder.withSubpath(subpath);
            return this;
        }

        public Builder withQualifier(String key, String value) {
            packageURLBuilder.withQualifier(key, value);
            return this;
        }

        public Coordinate build() {
            try {
                return new Coordinate(packageURLBuilder.build());
            } catch (MalformedPackageURLException e) {
                throw new AntennaExecutionException("Failed to build PackageURL in Builder for Coordinate", e);
            }
        }
    }
}
