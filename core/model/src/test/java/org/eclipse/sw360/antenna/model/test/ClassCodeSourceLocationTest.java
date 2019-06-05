package org.eclipse.sw360.antenna.model.test;

import org.eclipse.sw360.antenna.model.util.ClassCodeSourceLocation;
import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static junit.framework.TestCase.fail;

public class ClassCodeSourceLocationTest {

    @Test
    public void pathFromStringDoesntThrowInvalidPathExceptionTest() throws URISyntaxException{
        try {
            Paths.get(ClassCodeSourceLocation.getClassCodeSourceLocationAsString(this.getClass()));
        } catch (InvalidPathException e) {
            fail("Should not have thrown invalid path exception");
        }
    }

    @Test
    public void pathFromUriDoesntThrowInvalidPathExceptionTest() throws URISyntaxException{
        try {
            Paths.get(ClassCodeSourceLocation.getClassCodeSourceLocationURI(this.getClass()));
        } catch (InvalidPathException e) {
            fail("Should not have thrown invalid path exception");
        }
    }
}
