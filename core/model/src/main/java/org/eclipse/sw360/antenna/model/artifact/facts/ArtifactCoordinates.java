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

package org.eclipse.sw360.antenna.model.artifact.facts;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import com.github.packageurl.PackageURLBuilder;
import org.eclipse.sw360.antenna.api.exceptions.AntennaExecutionException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArtifactCoordinates implements ArtifactIdentifier<ArtifactCoordinates> {
    private Set<PackageURL> purls = new HashSet<>();

    public ArtifactCoordinates(String name, String version) {
        final PackageURL purl;
        try {
            purl = PackageURLBuilder.aPackageURL()
                    .withName(name)
                    .withVersion(version)
                    .withType(PackageURL.StandardTypes.GENERIC)
                    .build();
        } catch (MalformedPackageURLException e) {
            throw new AntennaExecutionException("Failed to build package url.", e);
        }
        purls.add(purl);
    }

    public ArtifactCoordinates(PackageURL... purlsToAdd) {
        purls.addAll(Arrays.asList(purlsToAdd));
    }

    public ArtifactCoordinates(String... purlStringsToAdd) throws MalformedPackageURLException {
        for (String purlStringToAdd: purlStringsToAdd) {
            purls.add(new PackageURL(purlStringToAdd));
        }
    }

    public ArtifactCoordinates(Collection<PackageURL> purlsToAdd) {
        this.purls.addAll(purlsToAdd);
    }

    public boolean containsPurl(String purlString) throws MalformedPackageURLException {
        return containsPurl(new PackageURL(purlString));
    }

    public boolean containsPurl(PackageURL purl) {
        return purls.contains(purl);
    }

    public Set<PackageURL> getPurls() {
        return new HashSet<>(purls);
    }

    @Override
    public String prettyPrint() {
        return "Set ArtifactCoordinates to " + toString();
    }

    @Override
    public String toString() {
        return purls.stream()
                .map(PackageURL::canonicalize)
                .collect(Collectors.joining(",","{","}"));
    }

    @Override
    public boolean matches(ArtifactIdentifier artifactIdentifier) {
        if (artifactIdentifier instanceof ArtifactCoordinates) {
            ArtifactCoordinates artifactCoordinates = (ArtifactCoordinates) artifactIdentifier;
            return purls.stream()
                    .anyMatch(artifactCoordinates::containsPurl);
        }
        return false;
    }

    @Override
    public String getFactContentName() {
        return "ArtifactCoordinates";
    }

    @Override
    public boolean isEmpty() {
        return purls.size() == 0;
    }

    @Override
    public ArtifactCoordinates mergeWith(ArtifactCoordinates resultWithPrecedence) {
        return new ArtifactCoordinates(
                Stream.concat(purls.stream(), resultWithPrecedence.purls.stream())
                        .collect(Collectors.toList()));
    }
}
