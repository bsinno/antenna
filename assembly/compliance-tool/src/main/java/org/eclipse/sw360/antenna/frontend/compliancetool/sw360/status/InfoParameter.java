package org.eclipse.sw360.antenna.frontend.compliancetool.sw360.status;

import org.eclipse.sw360.antenna.sw360.client.adapter.SW360Connection;

import java.util.Collections;
import java.util.Set;

abstract public class InfoParameter {
    abstract public String getInfoParameter();

    boolean hasAdditionalParameters() {
        return getAdditionalParameters().size() > 0;
    }

    abstract String helpMessage();
    abstract boolean isValid();
    abstract Set<String> getAdditionalParameters();
    abstract void parseAdditionalParameter(Set<String> parameters);
    abstract Object execute(SW360Connection connection);
    abstract String[] printResult();
    abstract String getResultFileHeader();

    static InfoParameter emptyInfoParameter() {
        return new InfoParameter() {
            @Override
            public String getInfoParameter() {
                return "NON_VALID";
            }

            @Override
            String helpMessage() {
                return "The provided info parameter is not supported in this status reporter";
            }

            @Override
            boolean isValid() {
                return false;
            }

            @Override
            Set<String> getAdditionalParameters() {
                return Collections.emptySet();
            }

            @Override
            void parseAdditionalParameter(Set<String> parameters) {
                // no-op
            }

            @Override
            Object execute(SW360Connection connection) {
                return null;
            }

            @Override
            String[] printResult() {
                return new String[]{};
            }

            @Override
            String getResultFileHeader() {
                return "";
            }
        };
    }
}
