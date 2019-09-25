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

import static org.eclipse.sw360.antenna.model.artifact.ArtifactSelectorHelper.compareStringsAsWildcard;

/*
 * A thin wrapper around com.github.packageurl.PackageURL
 * It just delegates
 */
public class Coordinate {
    private final PackageURL packageURL;

    /*
     * Package protected constructor to enforce the usage of the builder but to also allow extension in this package
     */
    Coordinate(PackageURL packageURL) {
        this.packageURL = packageURL;
    }

    /*
     * Package protected constructor to enforce the usage of the builder but to also allow extension in this package
     */
    Coordinate(String type, String namespace, String name, String version, TreeMap<String, String> qualifiers, String subpath) {
        try {
            packageURL = new PackageURL(type, namespace, name, version, qualifiers, subpath);
        } catch (MalformedPackageURLException e) {
            throw new AntennaExecutionException("Failed to create PackageURL in Coordinate", e);
        }
    }

    public PackageURL getPackageURL() {
        return packageURL;
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

    public boolean matches(String packageUrlString) {
        return matches(Coordinate.of(packageUrlString));
    }

    public boolean matches(Coordinate coordinate) {
        return compareStringsAsWildcard(getScheme(), coordinate.getScheme()) &&
                compareStringsAsWildcard(getType(), coordinate.getScheme()) &&
                compareStringsAsWildcard(getNamespace(), coordinate.getScheme()) &&
                compareStringsAsWildcard(getName(), coordinate.getScheme()) &&
                compareStringsAsWildcard(getVersion(), coordinate.getScheme()) &&
                compareStringsAsWildcard(getSubpath(), coordinate.getSubpath());
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

    public static class Types extends PackageURL.StandardTypes {
        public static final String BUNDLE = "p2";

        public static final Set<String> all = Stream.of(
                BITBUCKET, COMPOSER, DEBIAN, DOCKER, GEM, GENERIC, GITHUB, GOLANG, MAVEN, NPM, NUGET, PYPI, RPM,
                BUNDLE)
                .collect(Collectors.toSet());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(String packageURLString) {
        return new Builder(packageURLString);
    }

    public static Coordinate of(String packageURLString) {
        return new Builder(packageURLString).build();
    }

    public static Coordinate of(PackageURL packageURL) {
        return new Builder(packageURL).build();
    }

    /*
     * A thin wrapper around com.github.packageurl.PackageURLBuilder
     * It just delegates
     */
    public static class Builder {
        private final PackageURLBuilder packageURLBuilder = PackageURLBuilder.aPackageURL();

        public Builder() {
        }

        public Builder(String packageURLString) {
            try {
                final PackageURL packageURL = new PackageURL(packageURLString);
                initializeFromPURL(packageURL);
            } catch (MalformedPackageURLException e) {
                throw new AntennaExecutionException("Failed to build PackageURL in Builder for Coordinate", e);
            }
        }

        public Builder(PackageURL packageURL) {
                initializeFromPURL(packageURL);
        }

        private void initializeFromPURL(PackageURL packageURL) {
            withType(packageURL.getType());
            withNamespace(packageURL.getNamespace());
            withName(packageURL.getName());
            withVersion(packageURL.getVersion());
            withSubpath(packageURL.getSubpath());
            packageURL.getQualifiers()
                    .forEach(this::withQualifier);
        }

        public Builder withType(String type) {
            packageURLBuilder.withType(type);
            return this;
        }

        public Builder withNamespace(String namespace) {
            packageURLBuilder.withNamespace(namespace);
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
                final PackageURL packageURL = packageURLBuilder.build();
                if (MavenCoordinate.TYPE.equals(packageURL.getType())){
                    return new MavenCoordinate(packageURL);
                }
                if (BundleCoordinate.TYPE.equals(packageURL.getType())){
                    return new BundleCoordinate(packageURL);
                }
                if (JavaScriptCoordinate.TYPE.equals(packageURL.getType())){
                    return new JavaScriptCoordinate(packageURL);
                }
                if (DotNetCoordinate.TYPE.equals(packageURL.getType())){
                    return new DotNetCoordinate(packageURL);
                }
                return new Coordinate(packageURLBuilder.build());
            } catch (MalformedPackageURLException e) {
                throw new AntennaExecutionException("Failed to build PackageURL in Builder for Coordinate", e);
            }
        }
    }
}
