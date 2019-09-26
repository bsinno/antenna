package org.eclipse.sw360.antenna.model.coordinates;

import com.github.packageurl.PackageURL;
import org.eclipse.sw360.antenna.api.exceptions.AntennaExecutionException;

public class BundleCoordinate extends Coordinate {
    public static final String TYPE = Types.P2;

    public BundleCoordinate(PackageURL packageURL) {
        super(packageURL);
        if (!TYPE.equals(getType())) {
            throw new AntennaExecutionException("The type " + getType() + " is not supported by " + getClass().getName());
        }
    }

    public BundleCoordinate(String symbolicName, String bundleVersion) {
        super(TYPE, null, symbolicName, bundleVersion, null, null);
    }

    public BundleCoordinate(Coordinate coordinate) {
        this(coordinate.getPackageURL());
    }

    public String getSymbolicName() {
        return getName();
    }

    public String getBundleVersion() {
        return getVersion();
    }

    public String toString() {
        return getSymbolicName() + ":" + getBundleVersion();
    }
}
