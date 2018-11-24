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

import java.util.EnumSet;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import io.narayana.openshift.txrecovery.hibernate.ApplicationRecoveryPod;
import io.narayana.openshift.txrecovery.hibernate.HibernateProperties;
import io.narayana.openshift.txrecovery.logging.I18NLogger;

/**
 * Utility methods to setup hibernate standalone app.
 */
public final class Hibernate5Setup {
    private Hibernate5Setup() {
        // utility class
    }

    /**
     * Generate hibernate registry while filling it with properties.
     *
     * @param setupProperties  properties for connection
     * @return hibernate standard registry
     */
    @SuppressWarnings("rawtypes")
    public static StandardServiceRegistry getStandardRegistry(Properties setupProperties) {
        StandardServiceRegistryBuilder standardRegistryBuilder = new StandardServiceRegistryBuilder();
        standardRegistryBuilder.applySettings((Map) setupProperties);
        return standardRegistryBuilder.build();
    }
    /**
     * Setting up the Hibernate as standalone app. It uses  the {@link Metadata} filled from provided properties.
     *
     * @param setupProperties properties, probably taken from HibernateProperties#setupPropertiesByParsedArguments()
     * @param standardRegistry  hibernate registry to be used for being able to link table naming strategy
     * @return hibernate metadata to be used for {@link Session} creation
     */
    public static Metadata getHibernateStartupMetadata(Properties setupProperties, final ServiceRegistry standardRegistry) {
        // loading name of table that will be used for saving data, in null then value is not used
        final String programParamTableName = HibernateProperties.getTableName(setupProperties);

        MetadataSources sources = new MetadataSources(standardRegistry)
                .addAnnotatedClass(ApplicationRecoveryPod.class);
        MetadataBuilder metadataBuilder = sources.getMetadataBuilder();
        metadataBuilder.applyPhysicalNamingStrategy(new PhysicalNamingStrategyStandardImpl() {
            private static final long serialVersionUID = 1L;
            @Override
            public Identifier toPhysicalTableName(Identifier originalTableName, JdbcEnvironment jdbcEnvironment) {
                if(originalTableName.getCanonicalName().equalsIgnoreCase(ApplicationRecoveryPod.TABLE_NAME)
                        && programParamTableName != null && !programParamTableName.isEmpty())
                    return Identifier.toIdentifier(programParamTableName);
                return originalTableName;
            }
        });
        return metadataBuilder.build();
    }

    /**
     * Based on the provided Hibernate {@link Metadata} it runs schema export
     * to generate database schema aka. tables.
     *
     * @param metadata  hibernate metadata as base for db schema generation
     * @return  true if schema generation succeed without errors, false otherwise
     */
    public static boolean createTable(Metadata metadata) {
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.createOnly( EnumSet.of( TargetType.DATABASE ), metadata);
        if(schemaExport.getExceptions() != null && !schemaExport.getExceptions().isEmpty()) {
            I18NLogger.logger.error_schemaExportFailure(schemaExport.getExceptions());
            return false;
        }
        return true;
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
