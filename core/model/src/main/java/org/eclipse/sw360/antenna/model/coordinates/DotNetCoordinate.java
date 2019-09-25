package org.eclipse.sw360.antenna.model.coordinates;

import com.github.packageurl.PackageURL;
import org.eclipse.sw360.antenna.api.exceptions.AntennaExecutionException;

public class DotNetCoordinate extends Coordinate {
    public static final String TYPE = Types.NUGET;

    public DotNetCoordinate(PackageURL packageURL) {
        super(packageURL);
        if (!TYPE.equals(getType())) {
            throw new AntennaExecutionException("The type " + getType() + " is not supported by " + getClass().getName());
        }
    }

    public DotNetCoordinate(String packageId, String version) {
        super(TYPE, null, packageId, version, null, null);
    }

    public DotNetCoordinate(Coordinate coordinate) {
        this(coordinate.getPackageURL());
    }

    public String getPackageId() {
        return getName();
    }

    public String toString() {
        return getPackageId() + ":" + getVersion();
    }
}
