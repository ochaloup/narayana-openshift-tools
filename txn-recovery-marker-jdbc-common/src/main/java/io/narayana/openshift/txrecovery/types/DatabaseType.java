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

package io.narayana.openshift.txrecovery.types;

/**
 * Set of defaults for database types. When particular database type
 * is used as argument '-y' the driver class and the hibernate dialect
 * is derived from the default values provided here.
 */
public enum DatabaseType {
    POSTGRESQL(
        "jdbc:postgresql:",
        "org.postgresql.Driver",
        "org.hibernate.dialect.PostgreSQLDialect",
        "jdbc:postgresql://{0}:{1,number,#}/{2}",
        "5432"),

    MYSQL(
        "jdbc:mysql:",
        "com.mysql.jdbc.Driver",
        "org.hibernate.dialect.MySQLDialect",
        "jdbc:mysql://{0}:{1,number,#}/{2}",
        "3306"),

    ORACLE(
            "jdbc:oracle:",
            "oracle.jdbc.driver.OracleDriver",
            "org.hibernate.dialect.OracleDialect",
            "jdbc:oracle:thin:@{0}:{1,number,#}:{2}",
            "1521"),

    DB2(
            "jdbc:db2:",
            "com.ibm.db2.jcc.DB2Driver",
            "org.hibernate.dialect.DB2Dialect",
            "jdbc:db2://{0}:{1,number,#}/{2}",
            "50000"),

    MARIADB(
            "jdbc:mariadb:",
            "org.mariadb.jdbc.Driver",
            "org.hibernate.dialect.MariaDBDialect",
            "jdbc:mariadb://{0}:{1,number,#}/{2}",
            "3306"),

    SYBASE(
            "jdbc:sybase:",
            "com.sybase.jdbc4.jdbc.SybDriver",
            "org.hibernate.dialect.SybaseDialect",
            "jdbc:sybase:Tds:{0}:{1,number,#}/{2}",
            "5000"),

    MSSQL(
            "jdbc:sqlserver:",
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            "org.hibernate.dialect.SQLServerDialect",
            "jdbc:sqlserver://{0}:{1,number,#};DatabaseName={2}",
            "1433"),

    POSTGRESPLUS(
            "jdbc:edb:",
            "com.edb.Driver",
            "org.hibernate.dialect.PostgresPlusDialect",
            "jdbc:edb://{0}:{1,number,#}/{2}",
            "5432"),

    H2(
        "jdbc:h2:",
        "org.h2.Driver",
        "org.hibernate.dialect.H2Dialect",
        "jdbc:h2:tcp://{0}:{1,number,#}/~/{2}",
        "8082"),

    UNKNOWN(null, null, null, null, null);



    private String dbNameMatch, jdbcUrlPattern;
    private String hibernateDialect, jdbcDriverClass, defaultPort;

    /**
     * Constructor of the Database type which requires several arguments that are used
     * during construction of connection URL string and data.
     *
     * @param dbNameMatch  string that is checked to be part of the url provided by argument '-l',
     *                     if the dbtype is not provided and the url is provided then based on the string match
     *                     the db type is tried to be derived
     * @param driverClass  driver class name. It's needed as the setup is dynamic and classpath is not searched
     *                     and verified to load all META-INF/services/java.sql.Driver classes
     * @param hibernateDialect      Hibernate dialect to be used for database communication
     * @param jdbcUrlPattern url pattern which consist of hostname, port, database name. The pattern is used to create
     *                       the connection JDBC URL if the URL is not provided and
     *                       there are only defined the arguments of host, port separetely
     * @param defaultPort   default port known for particular database. Used as default value when port is not defined as argument.
     */
    private DatabaseType(String dbNameMatch, String driverClass, String hibernateDialect, String jdbcUrlPattern, String defaultPort) {
        this.dbNameMatch = dbNameMatch;
        this.jdbcDriverClass = driverClass;
        this.hibernateDialect = hibernateDialect;
        this.jdbcUrlPattern = jdbcUrlPattern;
        this.defaultPort = defaultPort;
    }

    public String hibernateDialect() {
        return hibernateDialect;
    }

    public String port() {
        return defaultPort;
    }

    public String jdbcDriverClass() {
        return jdbcDriverClass;
    }

    /**
     * Format to take is:<br>
     * <code>MessageFormat.format(DatabaseType.jdbcUrlPattern(), host, port, dbName)</code>
     *
     * @return  jdbc url pattern based on the database type defined
     */
    public String jdbcUrlPattern() {
        return jdbcUrlPattern;
    }

    /**
     * Based on the provided JDBC URL string it tries to return
     * appropriate database type.
     *
     * @param jdbcUrl  jdbc url that will be verified if it matches particular database type
     * @return a database type instance,
     *         if instance is not estimated then {@link DatabaseType#UNKNOWN} is returned.
     */
    public static DatabaseType estimateType(String jdbcUrl) {
        if(jdbcUrl == null || jdbcUrl.isEmpty()) return DatabaseType.UNKNOWN;

        if (jdbcUrl.contains(DatabaseType.POSTGRESPLUS.dbNameMatch)) {
            return DatabaseType.POSTGRESPLUS;
        } else
        if (jdbcUrl.contains(DatabaseType.POSTGRESQL.dbNameMatch)) {
            return DatabaseType.POSTGRESQL;
        } else
        if (jdbcUrl.contains(DatabaseType.MYSQL.dbNameMatch)) {
            return DatabaseType.MYSQL;
        } else
        if (jdbcUrl.contains(DatabaseType.MSSQL.dbNameMatch)) {
            return DatabaseType.MSSQL;
        } else
        if (jdbcUrl.contains(DatabaseType.MARIADB.dbNameMatch)) {
            return DatabaseType.MARIADB;
        } else
        if (jdbcUrl.contains(DatabaseType.ORACLE.dbNameMatch)) {
            return DatabaseType.ORACLE;
        } else
        if (jdbcUrl.contains(DatabaseType.DB2.dbNameMatch)) {
            return DatabaseType.DB2;
        } else
        if (jdbcUrl.contains(DatabaseType.SYBASE.dbNameMatch)) {
            return DatabaseType.SYBASE;
        } else
        if (jdbcUrl.contains(DatabaseType.H2.dbNameMatch)) {
            return DatabaseType.H2;
        }

        return DatabaseType.UNKNOWN;
    }
}
