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

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.DefaultNamingStrategy;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.jboss.logging.Logger;

import io.narayana.openshift.txrecovery.cliargs.ArgumentParserException;
import io.narayana.openshift.txrecovery.cliargs.ParsedArguments;
import io.narayana.openshift.txrecovery.hibernate.ApplicationRecoveryPod;
import io.narayana.openshift.txrecovery.hibernate.HibernateProperties;
import io.narayana.openshift.txrecovery.main.OutputPrinter;
import io.narayana.openshift.txrecovery.main.ProcessorMethods;
import io.narayana.openshift.txrecovery.main.ProgramProcessor;

/**
 * Class processing the arguments and calling service to save, delete data in database.
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class);


    public static void main(String[] args) {
        ParsedArguments parsedArguments = null;
        try {
            parsedArguments = ParsedArguments.parse(args);
        } catch (ArgumentParserException ape) {
            log.debugf(ape, "Error on parsing arguments: %s", Arrays.asList(args));
            System.exit(1);
        }

        // Hibernate setup
        Properties setupProperties = HibernateProperties.setupPropertiesByParsedArguments(parsedArguments);
        // table creation is automatic for hibernate4, explicit schema export works strangely
        // org.hibernate.cfg.AvailableSettings
        setupProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        final String tableName = HibernateProperties.getTableName(setupProperties);

        Configuration configuration = new Configuration()
            .addAnnotatedClass(ApplicationRecoveryPod.class)
            .addProperties(setupProperties)
            .setNamingStrategy(new DefaultNamingStrategy() {
                private static final long serialVersionUID = 1L;
                @Override
                public String tableName(String originalTableName) {
                    if(originalTableName.equalsIgnoreCase(ApplicationRecoveryPod.TABLE_NAME)
                            && tableName != null && !tableName.isEmpty())
                        return tableName;
                    return originalTableName;
                }
            });
        ServiceRegistry builder = new ServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(builder);
        Session session = sessionFactory.openSession();

        ApplicationRecoveryPodHibernate4DAO dao = new ApplicationRecoveryPodHibernate4DAO(session);
        ProcessorMethods methods = new Hibernate4ProcessorMethods(dao, parsedArguments);
        List<String> outputListing = new ProgramProcessor(methods).process(parsedArguments);

        OutputPrinter.printToStandardOutput(outputListing, parsedArguments.getFormat());
    }
}
