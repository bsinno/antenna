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
package org.eclipse.sw360.antenna.policy.testing.rules;

import org.eclipse.sw360.antenna.policy.engine.PolicyViolation;
import org.eclipse.sw360.antenna.policy.engine.RuleSeverity;
import org.eclipse.sw360.antenna.policy.engine.Ruleset;
import org.eclipse.sw360.antenna.policy.engine.SingleArtifactRule;
import org.eclipse.sw360.antenna.policy.engine.model.LicenseData;
import org.eclipse.sw360.antenna.policy.engine.model.ThirdPartyArtifact;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.eclipse.sw360.antenna.policy.engine.RuleUtils.artifactAppliesToRule;
import static org.eclipse.sw360.antenna.policy.engine.RuleUtils.artifactRaisesPolicyViolation;

public class CheckForGPL implements SingleArtifactRule {
    private final Ruleset ruleset;

    public CheckForGPL(final Ruleset ruleset) {
        this.ruleset = ruleset;
    }

    @Override
    public Optional<PolicyViolation> evaluate(ThirdPartyArtifact thirdPartyArtifact) {
        if (hasLicenses(thirdPartyArtifact, "GPL-2.0-only", "AGPL-3.0-or-later")) {
            return artifactRaisesPolicyViolation(this, thirdPartyArtifact);
        }
        return artifactAppliesToRule(this, thirdPartyArtifact);
    }

    private boolean hasLicenses(ThirdPartyArtifact artifact, String... licenses) {
        Collection<String> liclist = Arrays.asList(licenses);
        return artifact.getLicenses().stream()
                .map(LicenseData::getLicenseId)
                .filter(id -> liclist.contains(id))
                .count() > 0;
    }

    @Override
    public String getId() {
        return "T1";
    }

    @Override
    public String getName() {
        return "Check for GPL";
    }

    @Override
    public String getDescription() {
        return "A GPL license has been found";
    }

    @Override
    public RuleSeverity getSeverity() {
        return RuleSeverity.CRITICAL;
    }

    @Override
    public Ruleset getRuleset() {
        return ruleset;
    }
}
