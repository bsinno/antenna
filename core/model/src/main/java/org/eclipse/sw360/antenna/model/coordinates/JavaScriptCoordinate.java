package org.eclipse.sw360.antenna.model.coordinates;

import com.github.packageurl.PackageURL;
import org.eclipse.sw360.antenna.api.exceptions.AntennaExecutionException;

public class JavaScriptCoordinate extends Coordinate {
    public static final String TYPE = Types.NPM;

    public JavaScriptCoordinate(PackageURL packageURL) {
        super(packageURL);
        if (!TYPE.equals(getType())) {
            throw new AntennaExecutionException("The type " + getType() + " is not supported by " + getClass().getName());
        }
    }

    public JavaScriptCoordinate(String name, String version) {
        super(TYPE, null, name, version, null, null);
    }

    public JavaScriptCoordinate(String namespace, String name, String version) {
        super(TYPE, namespace, name, version, null, null);
    }

    public JavaScriptCoordinate(Coordinate coordinate) {
        this(coordinate.getPackageURL());
    }
    public String toString() {
        return getNamespace() + "/" + getName() + "-" + getVersion();
    }
}
