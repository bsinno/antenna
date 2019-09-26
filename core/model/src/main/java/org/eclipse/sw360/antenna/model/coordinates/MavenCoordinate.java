package org.eclipse.sw360.antenna.model.coordinates;

import com.github.packageurl.PackageURL;
import org.eclipse.sw360.antenna.api.exceptions.AntennaExecutionException;

public class MavenCoordinate extends Coordinate {
    public static final String TYPE = Types.MAVEN;

    public MavenCoordinate(PackageURL packageURL) {
        super(packageURL);
        if (!TYPE.equals(getType())) {
            throw new AntennaExecutionException("The type " + getType() + " is not supported by " + getClass().getName());
        }
    }

    public MavenCoordinate(String artifactId, String groupId, String version) {
        super(TYPE, groupId, artifactId, version, null, null);
    }

    public MavenCoordinate(Coordinate coordinate) {
        this(coordinate.getPackageURL());
    }

    public String getGroupId() {
        return getNamespace();
    }

    public String getArtifactId() {
        return getName();
    }

    public String toString() {
        return getNamespace() + ":" + getName() + ":" + getVersion();
    }
}
