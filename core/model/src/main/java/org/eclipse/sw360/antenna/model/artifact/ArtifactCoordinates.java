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

import org.eclipse.sw360.antenna.model.artifact.facts.ArtifactIdentifier;
import org.eclipse.sw360.antenna.model.coordinates.Coordinate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ArtifactCoordinates implements ArtifactIdentifier<ArtifactCoordinates>, ArtifactFact<ArtifactCoordinates> {
    private final Map<String, Coordinate> coordinates = new HashMap<>();

    private void putCoordinate(Coordinate coordinate) {
        if (coordinate != null) {
            coordinates.put(coordinate.getType(), coordinate);
        }
    }

    private void putCoordinates(Collection<Coordinate> coordinates) {
        coordinates.forEach(this::putCoordinate);
    }

    public ArtifactCoordinates(String name, String version) {
        final Coordinate coordinate = Coordinate.builder()
                .withName(name)
                .withVersion(version)
                .withType(Coordinate.Types.GENERIC)
                .build();
        coordinates.put(coordinate.getType(), coordinate);
    }

    public ArtifactCoordinates(Coordinate... coordinatesToAdd) {
        putCoordinates(Arrays.asList(coordinatesToAdd));
    }

    public ArtifactCoordinates(String... coordinateStringsToAdd) {
        for (String coordinateStringToAdd: coordinateStringsToAdd) {
            putCoordinate(Coordinate.builder(coordinateStringToAdd).build());
        }
    }

    public ArtifactCoordinates(Collection<Coordinate> coordinatesToAdd) {
        putCoordinates(coordinatesToAdd);
    }

    public ArtifactCoordinates(Set<String> coordinateStringsToAdd) {
        for (String coordinateStringToAdd: coordinateStringsToAdd) {
            putCoordinate(Coordinate.of(coordinateStringToAdd));
        }
    }

    public boolean containsPurl(String coordinateString)  {
        return containsPurl(Coordinate.of(coordinateString));
    }

    public boolean containsPurl(Coordinate coordinate) {
        return coordinates.values().contains(coordinate);
    }

    public Set<Coordinate> getCoordinates() {
        return new HashSet<>(coordinates.values());
    }

    public Optional<Coordinate> getCoordinateForType(String type) {
        return Optional.ofNullable(coordinates.get(type));
    }

    @Override
    public String prettyPrint() {
        return "Set ArtifactCoordinates to " + toString();
    }

    @Override
    public String toString() {
        return coordinates.values().stream()
                .map(Coordinate::canonicalize)
                .collect(Collectors.joining(",","{","}"));
    }

    @Override
    public boolean matches(ArtifactIdentifier artifactIdentifier) {
        if (artifactIdentifier instanceof ArtifactCoordinates) {
            ArtifactCoordinates artifactCoordinates = (ArtifactCoordinates) artifactIdentifier;
            return coordinates.entrySet()
                    .stream()
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .anyMatch(e -> {
                        final Coordinate thisCoordinate = e.getValue();
                        final Coordinate otherCoordinate = artifactCoordinates.coordinates.get(e.getKey());
                        return thisCoordinate.matches(otherCoordinate);
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
        return coordinates.size() == 0;
    }

    @Override
    public ArtifactCoordinates mergeWith(ArtifactCoordinates resultWithPrecedence) {
        return new ArtifactCoordinates(
                Stream.concat(coordinates.values().stream(), resultWithPrecedence.coordinates.values().stream())
                        .collect(Collectors.toList()));
    }
}
