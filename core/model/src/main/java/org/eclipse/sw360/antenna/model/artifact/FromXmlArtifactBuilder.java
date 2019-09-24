/*
 * Copyright (c) Bosch Software Innovations GmbH 2018.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.sw360.antenna.model.artifact;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURLBuilder;
import org.eclipse.sw360.antenna.api.exceptions.AntennaExecutionException;
import org.eclipse.sw360.antenna.model.artifact.facts.*;
import org.eclipse.sw360.antenna.model.util.ArtifactCoordinatesUtils;
import org.eclipse.sw360.antenna.model.xml.generated.DeclaredLicense;
import org.eclipse.sw360.antenna.model.xml.generated.LicenseInformation;
import org.eclipse.sw360.antenna.model.xml.generated.MatchState;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlAccessorType(XmlAccessType.FIELD)
public class FromXmlArtifactBuilder implements IArtifactBuilder {
    protected MavenCoordinatesBuilder mavenCoordinates;
    protected BundleCoordinatesBuilder bundleCoordinates;
    protected JavaScriptCoordinatesBuilder javaScriptCoordinates;
    protected DotNetCoordinatesBuilder dotNetCoordinates;
    protected String filename;
    protected String hash;
    protected DeclaredLicense declaredLicense;
    protected Boolean isProprietary;
    @XmlSchemaType(name = "string")
    protected MatchState matchState;
    protected String copyrightStatement;
    protected String modificationStatus;

    @Override
    public Artifact build() {
        final Artifact artifact = new Artifact("from XML")
                .addFact(new ArtifactFilename(filename, hash))
                .addFact(mavenCoordinates)
                .addFact(bundleCoordinates)
                .addFact(javaScriptCoordinates)
                .addFact(dotNetCoordinates)
                .addFact(new CopyrightStatement(copyrightStatement))
                .addFact(new ArtifactMatchingMetadata(matchState))
                .addFact(new ArtifactModificationStatus(modificationStatus));
        if(declaredLicense != null) {
            final LicenseInformation licenseInfo = declaredLicense.getLicenseInfo().getValue();
            artifact.addFact(new DeclaredLicenseInformation(licenseInfo));
        }
        if(isProprietary != null) {
            artifact.setProprietary(isProprietary);
        }

        return artifact;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class MavenCoordinatesBuilder
            implements ArtifactFactBuilder {

        private String artifactId;
        private String groupId;
        private String version;

        public MavenCoordinatesBuilder setArtifactId(String value) {
            this.artifactId = value;
            return this;
        }

        public MavenCoordinatesBuilder setGroupId(String value) {
            this.groupId = value;
            return this;
        }

        public MavenCoordinatesBuilder setVersion(String value) {
            this.version = value;
            return this;
        }

        @Override
        public ArtifactCoordinates build() {
            return ArtifactCoordinatesUtils.mkMavenCoordinates(artifactId, groupId, version);
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BundleCoordinatesBuilder
            implements ArtifactFactBuilder {
        protected String symbolicName;
        protected String bundleVersion;

        public void setSymbolicName(String value) {
            this.symbolicName = value;
        }

        public void setBundleVersion(String value) {
            this.bundleVersion = value;
        }

        @Override
        public ArtifactCoordinates build() {
            return ArtifactCoordinatesUtils.mkBundleCoordinates(symbolicName, bundleVersion);
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class JavaScriptCoordinatesBuilder
            implements ArtifactFactBuilder {
        protected String artifactId;
        protected String name;
        protected String version;

        public JavaScriptCoordinatesBuilder setArtifactId(String value) {
            this.artifactId = value;
            return this;
        }

        public JavaScriptCoordinatesBuilder setName(String value) {
            this.name = value;
            return this;
        }

        public JavaScriptCoordinatesBuilder setVersion(String value) {
            this.version = value;
            return this;
        }

        @Override
        public ArtifactCoordinates build() {
            return ArtifactCoordinatesUtils.mkJavaScriptCoordinates(name, artifactId, version);
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DotNetCoordinatesBuilder
            implements ArtifactFactBuilder {
        protected String packageId;
        protected String version;

        public DotNetCoordinatesBuilder setPackageId(String value) {
            this.packageId = value;
            return this;
        }

        public DotNetCoordinatesBuilder setVersion(String value) {
            this.version = value;
            return this;
        }

        @Override
        public ArtifactCoordinates build() {
            return ArtifactCoordinatesUtils.mkDotNetCoordinates(packageId, version);
        }
    }
}
