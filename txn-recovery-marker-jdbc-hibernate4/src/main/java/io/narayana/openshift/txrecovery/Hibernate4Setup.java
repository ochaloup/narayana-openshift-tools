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

import java.util.Map;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import io.narayana.openshift.txrecovery.hibernate.ApplicationRecoveryPod;
import io.narayana.openshift.txrecovery.hibernate.HibernateProperties;

/**
 * Utility methods to setup hibernate 4 standalone app.
 */
public final class Hibernate4Setup {
    private Hibernate4Setup() {
        // utility class
    }

    /**
     * Generate hibernate registry while filling it with properties.
     *
     * @param setupProperties  properties for connection
     * @return hibernate standard registry
     */
    @SuppressWarnings("rawtypes")
    public static ServiceRegistry getStandardRegistry(Properties setupProperties) {
        ServiceRegistryBuilder standardRegistryBuilder = new ServiceRegistryBuilder();
        standardRegistryBuilder.applySettings((Map) setupProperties);
        return standardRegistryBuilder.buildServiceRegistry();
    }

    /**
     * Returning current table name being used in the app for saving the recovery markers.
     *
     * @param setupProperties  properties to search for the db table name
     * @return name of table used in app
     */
    public static String getTableName(Properties setupProperties) {
        String appRecoveryPodTableName = HibernateProperties.getTableName(setupProperties);
        if(appRecoveryPodTableName == null) appRecoveryPodTableName = ApplicationRecoveryPod.TABLE_NAME;
        return appRecoveryPodTableName;
    }

    /**
     * Closing the Hibernate resources - {@link SessionFactory}
     * and the {@link Session}.
     *
     * @param sf  session factory to be closed
     * @param s  session to be closed
     */
    public static void close(SessionFactory sf, Session s) {
        if(s.isOpen()) s.close();
        if(!sf.isClosed()) sf.close();
    }
}
