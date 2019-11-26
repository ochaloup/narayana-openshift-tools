/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2018, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package io.narayana.openshift.txrecovery.main;

import java.util.List;

import io.narayana.openshift.txrecovery.cliargs.ParsedArguments;

/**
 * Class processing the arguments and calling service to save, delete data in database.
 */
public class ProgramProcessor {
    private ProcessorMethods methods;

    public ProgramProcessor(ProcessorMethods methods) {
        this.methods = methods;
    }

    /**
     * Verify necessary arguments for processing particular command
     * and run logic provided in the {@link ProcessorMethods} implementation.
     *
     * @param parsedArguments  arguments to be passed to the program
     * @return list of string as output based on the command type
     */
    public List<String> process(ParsedArguments parsedArguments) {
        switch(parsedArguments.getCommand()) {
            case CREATE:
                return methods.create();
            case INSERT:
                verifyExistenceApplicationPod(parsedArguments);
                verifyExistenceRecoveryPod(parsedArguments);
                return methods.insert();
            case DELETE:
                return methods.delete();
            case SELECT_APPLICATION:
                return methods.selectApplication();
            case SELECT_RECOVERY:
                return methods.selectRecovery();
            case DROP:
                verifyExistenceTablename(parsedArguments);
                return methods.drop();
            default:
                throw new IllegalArgumentException("Unknown handler for command '" + parsedArguments.getCommand() + "'");
        }
    }

    private void verifyExistenceApplicationPod(ParsedArguments parsedArguments) {
        String appPodSelect = parsedArguments.getApplicationPodName();
        if(appPodSelect == null || appPodSelect.isEmpty()) {
            throw new IllegalArgumentException("For command '" + parsedArguments.getCommand().name()
                + "' application pod name has to be specified. Use cli argument '-a/--application_pod_name'."
                + " Arguments were: " + parsedArguments);
        }
    }

    private void verifyExistenceRecoveryPod(ParsedArguments parsedArguments) {
        String recPodSelect = parsedArguments.getRecoveryPodName();
        if(recPodSelect == null || recPodSelect.isEmpty()) {
            throw new IllegalArgumentException("For command '" + parsedArguments.getCommand().name()
                + "' recovery pod name has to be specified. Use cli argument '-r/--recovery_pod_name'."
                + " Arguments were : " + parsedArguments);
        }
    }

    private void verifyExistenceTablename(ParsedArguments parsedArguments) {
        String tableName = parsedArguments.getTableName();
        if(tableName == null || tableName.isEmpty()) {
            throw new IllegalArgumentException("For command '" + parsedArguments.getCommand().name()
                + "' table name has to be specified. Use cli argument '-t/--table_name'."
                + " Arguments were: " + parsedArguments);
        }
        if(tableName.matches(".*(\\.|;|/|\\\\|--).*")) {
            throw new IllegalArgumentException(String.format("For command '%s' table name '%s'"
                + "contains prohibited characters [.|;|/|\\\\|--].%nProgram arguments were: %s",
                parsedArguments.getCommand().name(), tableName, parsedArguments));
        }
    }
}
