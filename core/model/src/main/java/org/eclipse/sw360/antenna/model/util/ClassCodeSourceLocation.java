package org.eclipse.sw360.antenna.model.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClassCodeSourceLocation {

    public static String getClassCodeSourceLocationAsString(Class<?> clazz) throws URISyntaxException {
        URI codeSourceLocationUri = getClassCodeSourceLocationURI(clazz);

        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")) {
            String path = codeSourceLocationUri.getPath();
            try {
                Path testPath = Paths.get(path);
                return path;
            } catch (InvalidPathException e) {
                return path.replaceFirst("/", "");
            }
        } else {
            return codeSourceLocationUri.toString();
        }
    }

    public static URI getClassCodeSourceLocationURI(Class<?> clazz) throws URISyntaxException {
        return clazz.getProtectionDomain().getCodeSource().getLocation().toURI();
    }
}
