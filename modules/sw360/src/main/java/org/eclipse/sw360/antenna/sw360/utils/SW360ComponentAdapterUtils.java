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
package org.eclipse.sw360.antenna.sw360.utils;

import com.github.packageurl.PackageURL;
import org.eclipse.sw360.antenna.model.artifact.Artifact;
import org.eclipse.sw360.antenna.model.artifact.facts.ArtifactCoordinates;
import org.eclipse.sw360.antenna.model.artifact.facts.ArtifactSourceUrl;
import org.eclipse.sw360.antenna.sw360.rest.resource.components.SW360Component;
import org.eclipse.sw360.antenna.sw360.rest.resource.components.SW360ComponentType;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SW360ComponentAdapterUtils {
    public static String createComponentName(Artifact artifact) {
        return artifact.askFor(ArtifactCoordinates.class)
                .map(ArtifactCoordinates::getPurls)
                .flatMap(purls -> purls.stream()
                        .findFirst()) // TODO: ugly hack
                .map(PackageURL::getName)
                .orElse(artifact.toString()); // TODO: ugly hack
    }

    public static String createComponentVersion(Artifact artifact) {
        return artifact.askFor(ArtifactCoordinates.class)
                .map(ArtifactCoordinates::getPurls)
                .flatMap(purls -> purls.stream()
                        .findFirst()) // TODO: ugly hack
                .map(PackageURL::getVersion)
                .orElse("-");
    }

    private static void setCreatedOn(SW360Component component) {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String createdOn = dateFormat.format(new Date());
        component.setCreatedOn(createdOn);
    }

    public static void setName(SW360Component component, Artifact artifact) {
        component.setName(createComponentName(artifact));
    }

    private static void setComponentType(SW360Component component, Artifact artifact) {
        if (artifact.isProprietary()) {
            component.setComponentType(SW360ComponentType.INTERNAL);
        } else {
            component.setComponentType(SW360ComponentType.OSS);
        }
    }

    private static void setHomePage(SW360Component component, Artifact artifact) {
        artifact.askForGet(ArtifactSourceUrl.class)
                .ifPresent(component::setHomepage);
    }

    public static void prepareComponent(SW360Component component, Artifact artifact) {
        SW360ComponentAdapterUtils.setCreatedOn(component);
        SW360ComponentAdapterUtils.setName(component, artifact);
        SW360ComponentAdapterUtils.setComponentType(component, artifact);
        SW360ComponentAdapterUtils.setHomePage(component, artifact);
    }

    public static boolean isValidComponent(SW360Component component) {
        return component.getName() != null && !component.getName().isEmpty();
    }
}
