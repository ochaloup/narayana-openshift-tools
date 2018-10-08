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

package io.narayana.openshift.txrecovery.logging;

import java.util.List;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.Message.Format;

import io.narayana.openshift.txrecovery.hibernate.ApplicationRecoveryPod;

import org.jboss.logging.annotations.MessageLogger;

@MessageLogger(projectCode = "OSTXNRECOVERY")
public interface I18NLogger {
    public static final I18NLogger logger = Logger.getMessageLogger(I18NLogger.class, "io.narayana.openshift.txrecovery");

    @SuppressWarnings("rawtypes")
    @Message(id = 00001, value = "Fail to export schema, cause: {0}", format = Format.MESSAGE_FORMAT)
    @LogMessage(level = Level.ERROR)
    public void error_schemaExportFailure(List reasons);

    @Message(id = 00002, value = "Cannot persist record: {0}", format = Format.MESSAGE_FORMAT)
    @LogMessage(level = Level.ERROR)
    public void error_cannotPersistRecord(ApplicationRecoveryPod record, @Cause Throwable cause);

    @Message(id = 00003, value = "Cannot remove record: {0}", format = Format.MESSAGE_FORMAT)
    @LogMessage(level = Level.ERROR)
    public void error_cannotRemoveRecord(ApplicationRecoveryPod record, @Cause Throwable cause);

    @Message(id = 00004, value = "Error on searching existence of table {0}", format = Format.MESSAGE_FORMAT)
    @LogMessage(level = Level.ERROR)
    public void error_dbTableDoesNotExist(String tableName, @Cause Throwable cause);



    // --------------------------------
    // ---- HELP MESSAGES ----
    // --------------------------------
    @Message(id = 0, value = "txn-recovery-marker-jdbc: creating and storing transaction recovery markers in database. Available command line arguments are:", format = Format.MESSAGE_FORMAT)
    public String msg_errHelpMessage();

    @Message(id = 0, value = "Database type the script will be working with", format = Format.MESSAGE_FORMAT)
    public String msg_typeDb();

    @Message(id = 0, value = "Hibernate dialect to be used", format = Format.MESSAGE_FORMAT)
    public String msg_hibernateDialect();

    @Message(id = 0, value = "Fully classified JDBC Driver class", format = Format.MESSAGE_FORMAT)
    public String msg_jdbcDriverClass();

    @Message(id = 0, value = "JDBC url which has precedence over configured host/port/database information", format = Format.MESSAGE_FORMAT)
    public String msg_url();

    @Message(id = 0, value = "Hostname where the database runs", format = Format.MESSAGE_FORMAT)
    public String msg_host();

    @Message(id = 0, value = "Port where the database runs", format = Format.MESSAGE_FORMAT)
    public String msg_port();

    @Message(id = 0, value = "Database name to connect to at the host and port", format = Format.MESSAGE_FORMAT)
    public String msg_database();

    @Message(id = 0, value = "Username at the database to connect to", format = Format.MESSAGE_FORMAT)
    public String msg_user();

    @Message(id = 0, value = "Password for the username at the database to connect to", format = Format.MESSAGE_FORMAT)
    public String msg_password();

    @Message(id = 0, value = "Table name to be working with", format = Format.MESSAGE_FORMAT)
    public String msg_tableName();

    @Message(id = 0, value = "Command to run in database available options are to create db schema to insert a record to delete the record and list recovery pod names", format = Format.MESSAGE_FORMAT)
    public String msg_command();

    @Message(id = 0, value = "Application pod name which will be either inserted/deleted onto database or by which query will be filtered", format = Format.MESSAGE_FORMAT)
    public String msg_applicationPodName();

    @Message(id = 0, value = "Recovery pod name which will be either inserted/deleted onto database or by which query will be filtered", format = Format.MESSAGE_FORMAT)
    public String msg_recoveryPodName();

    @Message(id = 0, value = "Output format", format = Format.MESSAGE_FORMAT)
    public String msg_format();

    @Message(id = 0, value = "Enable verbose logging", format = Format.MESSAGE_FORMAT)
    public String msg_verbose();

    @Message(id = 0, value = "Printing this help", format = Format.MESSAGE_FORMAT)
    public String msg_help();
}
