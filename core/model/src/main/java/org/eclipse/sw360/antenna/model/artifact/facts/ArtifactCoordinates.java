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

import org.eclipse.sw360.antenna.model.purl.PURL;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ArtifactCoordinates implements ArtifactIdentifier<ArtifactCoordinates> {
    private Map<String,PURL> purls = new HashMap<>();

    private void putPurl(PURL packageURL) {
        purls.put(packageURL.getType(), packageURL);
    }

    private void putPurls(Collection<PURL> packageURLS) {
        packageURLS.forEach(this::putPurl);
    }

    public ArtifactCoordinates(String name, String version) {
        final PURL purl = PURL.builder()
                .withName(name)
                .withVersion(version)
                .withType(PURL.StandardTypes.GENERIC)
                .build();
        purls.put(purl.getType(), purl);
    }

    public ArtifactCoordinates(PURL... purlsToAdd) {
        putPurls(Arrays.asList(purlsToAdd));
    }

    public ArtifactCoordinates(String... purlStringsToAdd) {
        for (String purlStringToAdd: purlStringsToAdd) {
            putPurl(new PURL(purlStringToAdd));
        }
    }

    public ArtifactCoordinates(Collection<PURL> purlsToAdd) {
        putPurls(purlsToAdd);
    }

    public ArtifactCoordinates(Set<String> purlStringsToAdd) {
        for (String purlStringToAdd: purlStringsToAdd) {
            putPurl(new PURL(purlStringToAdd));
        }
    }

    public boolean containsPurl(String purlString)  {
        return containsPurl(new PURL(purlString));
    }

    public boolean containsPurl(PURL purl) {
        return purls.values().contains(purl);
    }

    public Set<PURL> getPurls() {
        return new HashSet<>(purls.values());
    }

    public Optional<PURL> getPurlForType(String type) {
        return Optional.ofNullable(purls.get(type));
    }

    @Override
    public String prettyPrint() {
        return "Set ArtifactCoordinates to " + toString();
    }

    @Override
    public String toString() {
        return purls.values().stream()
                .map(PURL::canonicalize)
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
                        final PURL thisPurl = e.getValue();
                        final PURL otherPurl = artifactCoordinates.purls.get(e.getKey());
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
}
