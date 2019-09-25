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
    private Map<String,PackageURL> purls = new HashMap<>();

    private void putPurl(PackageURL packageURL) {
        purls.put(packageURL.getType(), packageURL);
    }

    private void putPurls(Collection<PackageURL> packageURLS) {
        packageURLS.forEach(this::putPurl);
    }

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
        purls.put(purl.getType(), purl);
    }

    public ArtifactCoordinates(PackageURL... purlsToAdd) {
        putPurls(Arrays.asList(purlsToAdd));
    }

    public ArtifactCoordinates(String... purlStringsToAdd) throws MalformedPackageURLException {
        for (String purlStringToAdd: purlStringsToAdd) {
            putPurl(new PackageURL(purlStringToAdd));
        }
    }

    public ArtifactCoordinates(Collection<PackageURL> purlsToAdd) {
        putPurls(purlsToAdd);
    }

    public ArtifactCoordinates(Set<String> purlStringsToAdd) throws MalformedPackageURLException {
        for (String purlStringToAdd: purlStringsToAdd) {
            putPurl(new PackageURL(purlStringToAdd));
        }
    }

    public boolean containsPurl(String purlString)  {
        try {
            return containsPurl(new PackageURL(purlString));
        } catch (MalformedPackageURLException e) {
            return false;
        }
    }

    public boolean containsPurl(PackageURL purl) {
        return purls.values().contains(purl);
    }

    public Set<PackageURL> getPurls() {
        return new HashSet<>(purls.values());
    }

    public Optional<PackageURL> getPurlForType(String type) {
        return Optional.ofNullable(purls.get(type));
    }

    @Override
    public String prettyPrint() {
        return "Set ArtifactCoordinates to " + toString();
    }

    @Override
    public String toString() {
        return purls.values().stream()
                .map(PackageURL::canonicalize)
                .collect(Collectors.joining(",","{","}"));
    }

    @Override
    public boolean matches(ArtifactIdentifier artifactIdentifier) {
        if (artifactIdentifier instanceof ArtifactCoordinates) {
            ArtifactCoordinates artifactCoordinates = (ArtifactCoordinates) artifactIdentifier;
            return purls.entrySet()
                    .stream()
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .anyMatch(e -> {
                        final PackageURL thisPurl = e.getValue();
                        final PackageURL otherPurl = artifactCoordinates.purls.get(e.getKey());
                        return thisPurl.equals(otherPurl); // TODO: regex on purls
                    });
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
                Stream.concat(purls.values().stream(), resultWithPrecedence.purls.values().stream())
                        .collect(Collectors.toList()));
    }

    public static class StandardTypes extends PackageURL.StandardTypes {
        public static final String BUNDLE = "p2";

        public static final Set<String> all = Stream.of(
                BITBUCKET, COMPOSER, DEBIAN, DOCKER, GEM, GENERIC, GITHUB, GOLANG, MAVEN, NPM, NUGET, PYPI, RPM,
                BUNDLE)
                .collect(Collectors.toSet());
    }
}
