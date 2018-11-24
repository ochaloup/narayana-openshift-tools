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

package io.narayana.openshift.txrecovery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.hibernate.boot.Metadata;
import org.jboss.logging.Logger;

import io.narayana.openshift.txrecovery.cliargs.ParsedArguments;
import io.narayana.openshift.txrecovery.hibernate.ApplicationRecoveryPod;
import io.narayana.openshift.txrecovery.hibernate.HibernateProperties;
import io.narayana.openshift.txrecovery.main.ProcessorMethods;
import io.narayana.openshift.txrecovery.types.CommandType;

public class Hibernate5ProcessorMethods implements ProcessorMethods {
    private static final Logger log = Logger.getLogger(Hibernate5ProcessorMethods.class);
    private static final List<String> EMPTY_RETURN = Collections.unmodifiableList(new ArrayList<String>());

    private ApplicationRecoveryPodHibernate5DAO dao;
    private Properties hibernateSetupProperties;
    private Metadata metadata;
    private ParsedArguments parsedArguments;

    public Hibernate5ProcessorMethods(ApplicationRecoveryPodHibernate5DAO dao, ParsedArguments parsedArguments, Properties hibernateProps, Metadata metadata) {
        this.dao = dao;
        this.hibernateSetupProperties = hibernateProps;
        this.metadata = metadata;
        this.parsedArguments = parsedArguments;
    }

    @Override
    public List<String> create() {
        String podTableName = HibernateProperties.getTableName(hibernateSetupProperties);
        if(!dao.tableExists(podTableName)) Hibernate5Setup.createTable(metadata);
        return EMPTY_RETURN;
    }

    @Override
    public List<String> drop() {
        String tableName = parsedArguments.getTableName();
        int numberDropped = dao.dropTable(tableName);
        log.info("Number ["  + numberDropped + "] of table names dropped. The table name to drop was: " + tableName);
        return EMPTY_RETURN;
    }

    @Override
    public List<String> insert() {
        String podTableName = HibernateProperties.getTableName(hibernateSetupProperties);
        String appPod = parsedArguments.getApplicationPodName();
        String recPod = parsedArguments.getRecoveryPodName();

        if(!dao.tableExists(podTableName)) Hibernate5Setup.createTable(metadata);
        if(!dao.saveRecord(appPod, recPod)) {
            throw new IllegalStateException("Error on saving data [" + appPod +"," + recPod + "] to db "
                + parsedArguments.getJdbcUrl() + " and table " + parsedArguments.getTableName());
        }
        return EMPTY_RETURN;
    }

    @Override
    public List<String> delete() {
        String appPod = parsedArguments.getApplicationPodName();
        String recPod = parsedArguments.getRecoveryPodName();

        int numberDeleted = dao.delete(appPod, recPod);
        log.info("Number ["  + numberDeleted + "] of records deleted while filtered at [application pod: "
            + appPod + ", recovery pod: " + recPod + "]");
        return EMPTY_RETURN;
    }

    @Override
    public List<String> selectApplication() {
        String appPod = parsedArguments.getApplicationPodName();
        String recPod = parsedArguments.getRecoveryPodName();

        List<String> outputListing = new ArrayList<String>();
        Collection<ApplicationRecoveryPod> dtos = dao.getRecords(appPod, recPod);
        for(ApplicationRecoveryPod dto: dtos) {
            if(parsedArguments.getCommand() == CommandType.SELECT_APPLICATION)
                outputListing.add(dto.getApplicationPodName());
            if(parsedArguments.getCommand() == CommandType.SELECT_RECOVERY)
                outputListing.add(dto.getRecoveryPodName());
        }
        return outputListing;
    }

    @Override
    public List<String> selectRecovery() {
        return selectApplication();
    }
}
