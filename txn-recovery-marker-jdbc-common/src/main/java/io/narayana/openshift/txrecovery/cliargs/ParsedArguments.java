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

package io.narayana.openshift.txrecovery.cliargs;

import java.text.MessageFormat;

import io.narayana.openshift.txrecovery.logging.I18NLogger;
import io.narayana.openshift.txrecovery.types.CommandType;
import io.narayana.openshift.txrecovery.types.DatabaseType;
import io.narayana.openshift.txrecovery.types.OutputFormatType;

public final class ParsedArguments {
    public static final String DEFAULT_TABLE_NAME = "JDBC_RECOVERY";
    public static final String DEFAULT_DB_TYPE = DatabaseType.UNKNOWN.name();
    public static final String DEFAULT_HOST = "localhost";
    public static final String DEFAULT_COMMAND = CommandType.SELECT_RECOVERY.name();

    private static Options ARGS_OPTIONS = new Options()
        .addOption("y", "type_db", true, I18NLogger.logger.msg_typeDb())
        .addOption("i", "hibernate_dialect", true, I18NLogger.logger.msg_hibernateDialect())
        .addOption("j", "jdbc_driver_class", true, I18NLogger.logger.msg_jdbcDriverClass())
        .addOption("l", "url", true, I18NLogger.logger.msg_url())
        .addOption("o","host", true, I18NLogger.logger.msg_host())
        .addOption("p","port", true, I18NLogger.logger.msg_port())
        .addOption("d","database", true, I18NLogger.logger.msg_database())
        .addRequiredOption("u","user", true, I18NLogger.logger.msg_user())
        .addRequiredOption("s","password", true, I18NLogger.logger.msg_password())
        .addOption("t","table_name", true, I18NLogger.logger.msg_tableName())
        .addOption("c","command", true, I18NLogger.logger.msg_command())
        .addOption("a","application_pod_name", true, I18NLogger.logger.msg_applicationPodName())
        .addOption("r","recovery_pod_name", true, I18NLogger.logger.msg_recoveryPodName())
        .addOption("f", "format", true, I18NLogger.logger.msg_format())
        .addOption("v", "verbose", false, I18NLogger.logger.msg_verbose())
        .addOption("h", "help", false, I18NLogger.logger.msg_help());

    private DatabaseType typeDb;
    private String hibernateDialect, jdbcDriverClass;
    private String jdbcUrl;
    private String host, database, user, password, tableName;
    private Integer port;
    private CommandType command;
    private String applicationPodName, recoveryPodName;
    private OutputFormatType format;
    private boolean isVerbose;


    /**
     * Use the static method for getting instance of parsed arguments.
     *
     * @param args  cli arguments
     * @return parser with getters containing the parsed values
     * @throws ArgumentParserException  error happens during error parsing
     */
    public static ParsedArguments parse(String... args) throws ArgumentParserException {
        return new ParsedArguments(args);
    }

    private ParsedArguments(String... args) throws ArgumentParserException {
        ArgumentParser parser = new ArgumentParser();

        try {
            parser.parse(ARGS_OPTIONS, args);

            if(parser.hasOption("help")) {
                printHelpStdErr();
                System.exit(2);
            }

            String value = parser.getOptionValue("type_db", DEFAULT_DB_TYPE);
            this.typeDb = DatabaseType.valueOf(value.toUpperCase());
            this.jdbcUrl = parser.getOptionValue("url");

            if(this.typeDb == DatabaseType.UNKNOWN) {
                this.typeDb = DatabaseType.estimateType(this.jdbcUrl);
            }

            this.hibernateDialect = parser.getOptionValue("hibernate_dialect", typeDb.hibernateDialect());
            this.jdbcDriverClass = parser.getOptionValue("jdbc_driver_class", typeDb.jdbcDriverClass());

            this.host = parser.getOptionValue("host", DEFAULT_HOST);
            value = parser.getOptionValue("port", typeDb.port());
            this.port = Integer.valueOf(value);
            this.database = parser.getOptionValue("database");

            if((jdbcUrl == null) && (host.isEmpty() || database == null)) {
                throw new IllegalArgumentException("Argument '-l/--url' is empty and there is not enough"
                   + " data for construction jdbc url. Please add at least the -d/--database and default/defined "
                   + host + ":" + port + " will be used.");
            }

            this.user = parser.getOptionValue("user");
            this.password = parser.getOptionValue("password");
            this.tableName = parser.getOptionValue("table_name", DEFAULT_TABLE_NAME);

            value = parser.getOptionValue("command", DEFAULT_COMMAND);
            this.command = CommandType.valueOf(value.toUpperCase());

            this.applicationPodName = parser.getOptionValue("application_pod_name");
            this.recoveryPodName = parser.getOptionValue("recovery_pod_name");

            value = parser.getOptionValue("format", OutputFormatType.LIST_SPACE.name());
            this.format = OutputFormatType.valueOf(value.toUpperCase());

            this.isVerbose = parser.hasOption("verbose");
        } catch(Exception pe) {
            System.err.println(pe.getMessage());
            printHelpStdErr();
            throw new ArgumentParserException(pe);
        }
    }

    void printHelpStdErr() {
        System.err.println(I18NLogger.logger.msg_errHelpMessage());
        ARGS_OPTIONS.printHelpToStdErr();
    }

    public static Options getARGS_OPTIONS() {
        return ARGS_OPTIONS;
    }

    public DatabaseType getTypeDb() {
        return typeDb;
    }

    public String getHibernateDialect() {
        return hibernateDialect;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getTableName() {
        return tableName;
    }

    public Integer getPort() {
        return port;
    }

    public CommandType getCommand() {
        return command;
    }

    public String getApplicationPodName() {
        return applicationPodName;
    }

    public String getRecoveryPodName() {
        return recoveryPodName;
    }

    public OutputFormatType getFormat() {
        return format;
    }

    public boolean isVerbose() {
        return isVerbose;
    }

    public String getJdbcDriverClass() {
        return jdbcDriverClass;
    }

    public String getJdbcUrl() {
        if(jdbcUrl != null) return jdbcUrl;
        if (typeDb.jdbcUrlPattern() == null || database == null) {
            throw new IllegalStateException("Unkonwn JDBC URL for database type " + typeDb.name() + ". Please define it with -l argument.");
        }
        return MessageFormat.format(typeDb.jdbcUrlPattern(), host, port.intValue(), database);
    }

    @Override
    public String toString() {
        return String.format("[command: %s, dbtype: %s, url: %s, host: %s, port: %s, db: %s, user: %s, pass: %s, table: %s,"
                + " dialect: %s, driver: %s, app: %s, recovery: %s, format: %s, verbose: %s]",
                command, typeDb, jdbcUrl, host, port, database, user, "******", tableName,
                hibernateDialect, jdbcDriverClass, applicationPodName, recoveryPodName, format, isVerbose ? "true" : "false");
    }
}
