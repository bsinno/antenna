/*
 * Copyright (c) Bosch Software Innovations GmbH 2018-2019.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.sw360.antenna.maven;

import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURLBuilder;
import org.eclipse.sw360.antenna.model.coordinates.MavenCoordinate;
import org.eclipse.sw360.antenna.testing.AntennaTestWithMockedContext;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class IArtifactRequesterTest extends AntennaTestWithMockedContext {

    private IArtifactRequester artifactRequester = new IArtifactRequester() {
        @Override
        public Optional<File> requestFile(MavenCoordinate mavenCoordinate, Path targetDirectory, ClassifierInformation classifierInformation) {
            return Optional.empty();
        }
    };

    private MavenCoordinate mavenCoordinate;

    @Before
    public void before() throws Exception {
        mavenCoordinate = new MavenCoordinate("artifactId", "groupId", "version");
    }

    @Test
    public void getExpectedJarBaseNameTest() {
        final String expectedJarBaseName = artifactRequester.getExpectedJarBaseName(mavenCoordinate, ClassifierInformation.DEFAULT_JAR);
        assertThat(expectedJarBaseName).endsWith(MavenInvokerRequester.JAR_EXTENSION);
        assertThat(expectedJarBaseName).contains(mavenCoordinate.getName());
        assertThat(expectedJarBaseName).contains(mavenCoordinate.getVersion());
        assertThat(expectedJarBaseName).doesNotContain("/");
    }

    @Test
    public void getExpectedJarBaseNameTestSource() {
        final String expectedJarBaseName = artifactRequester.getExpectedJarBaseName(mavenCoordinate, ClassifierInformation.DEFAULT_SOURCE_JAR);
        assertThat(expectedJarBaseName).endsWith("-sources" + MavenInvokerRequester.JAR_EXTENSION);
        assertThat(expectedJarBaseName).contains(mavenCoordinate.getName());
        assertThat(expectedJarBaseName).contains(mavenCoordinate.getVersion());
        assertThat(expectedJarBaseName).doesNotContain("/");
    }

}