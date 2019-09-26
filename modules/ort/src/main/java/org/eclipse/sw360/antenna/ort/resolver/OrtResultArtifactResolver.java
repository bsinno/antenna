/*
 * Copyright (c) Bosch Software Innovations GmbH 2019.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.sw360.antenna.ort.resolver;

import com.here.ort.model.*;
import com.here.ort.model.Package;
import com.here.ort.model.config.PathExclude;

import org.eclipse.sw360.antenna.model.artifact.Artifact;
import org.eclipse.sw360.antenna.model.artifact.facts.*;
import org.eclipse.sw360.antenna.model.coordinates.Coordinate;
import org.eclipse.sw360.antenna.model.xml.generated.MatchState;
import org.eclipse.sw360.antenna.util.LicenseSupport;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class OrtResultArtifactResolver implements Function<Package, Artifact> {
    private SortedMap<Identifier, Map<LicenseFindings, List<PathExclude>>> licenseFindings;

    public OrtResultArtifactResolver(OrtResult result) {
        licenseFindings = result.collectLicenseFindings(false);
    }

    @Override
    public Artifact apply(Package pkg) {
        Artifact a = new Artifact("OrtResult")
                .addFact(new ArtifactMatchingMetadata(MatchState.EXACT));

        a.addCoordinate(mapCoordinates(pkg));
        mapSourceUrl(pkg).ifPresent(a::addFact);

        mapDeclaredLicense(pkg).ifPresent(a::addFact);
        mapFilename(pkg).ifPresent(a::addFact);
        mapHomepage(pkg).ifPresent(a::addFact);

        mapObservedLicense(pkg).ifPresent(a::addFact);
        mapCopyrights(pkg).ifPresent(a::addFact);

        return a;
    }

    private static Coordinate mapCoordinates(Package pkg) {
        String namespace = pkg.getId().getNamespace();
        String name = pkg.getId().getName();
        String version = pkg.getId().getVersion();

        final Coordinate.Builder builder = Coordinate.builder()
                .withName(name)
                .withVersion(version);
        switch (pkg.getId().getType().toLowerCase()) {
            case "nuget":
            case "dotnet":
                builder.withType(Coordinate.Types.NUGET);
                break;
            case "maven":
                builder.withType(Coordinate.Types.MAVEN);
                builder.withNamespace(namespace);
                break;
            case "npm":
                builder.withType(Coordinate.Types.NPM);
                break;
        }
        return builder.build();
    }

    private static Optional<ArtifactSourceUrl> mapSourceUrl(Package pkg) {
        // Antenna does not currently have the concept of VCS-specific clone URLs, so do not take ORT's VCS URL into
        // account, but only the source artifact URL.
        return Optional.of(pkg.getSourceArtifact().getUrl())
                .filter(a -> !a.isEmpty())
                .map(ArtifactSourceUrl::new);
    }

    private static Optional<DeclaredLicenseInformation> mapDeclaredLicense(Package pkg) {
        return Optional.of(pkg.getDeclaredLicenses())
                .map(LicenseSupport::mapLicenses)
                .map(DeclaredLicenseInformation::new);
    }

    private static Optional<ArtifactFilename> mapFilename(Package pkg) {
        // Antenna's artifact filename (or filename entries) are meant to refer to binary artifacts, so only use the
        // filename from ORT's binary artifact URL here.
        return Optional.of(pkg.getBinaryArtifact())
                .filter(a -> !a.getUrl().isEmpty())
                .map(a -> {
                    String fileName = new File(a.getUrl()).getName();
                    String hash = a.getHash().getValue();
                    String hashAlgorithm = a.getHash().getAlgorithm().toString();
                    return new ArtifactFilename(fileName, hash, hashAlgorithm);
                });
    }

    private static Optional<ArtifactHomepage> mapHomepage(Package pkg) {
        return Optional.of(pkg.getHomepageUrl())
                .map(ArtifactHomepage::new);
    }

    private Optional<ObservedLicenseInformation> mapObservedLicense(Package pkg) {
        Collection<String> licenses = Optional.ofNullable(licenseFindings.get(pkg.getId()))
                .map(Map::keySet)
                .orElse(Collections.emptySet()).stream()
                .map(LicenseFindings::getLicense)
                .collect(Collectors.toList());

        return Optional.of(LicenseSupport.mapLicenses(licenses))
                .map(ObservedLicenseInformation::new);
    }

    private Optional<CopyrightStatement> mapCopyrights(Package pkg) {
        return Optional.ofNullable(licenseFindings.get(pkg.getId()))
                .map(Map::keySet)
                .orElse(Collections.emptySet()).stream()
                .map(LicenseFindings::getCopyrights)
                .flatMap(Collection::stream)
                .map(CopyrightFindings::getStatement)
                .map(CopyrightStatement::new)
                .reduce(CopyrightStatement::mergeWith);
    }
}
