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

public enum DatabaseType {
    POSTGRESQL(
        "org.postgresql.Driver",
        "org.hibernate.dialect.PostgreSQL82Dialect",
        // timeout is in seconds
        "jdbc:postgresql://{0}:{1,number,#}/{2}?socketTimeout=10&loginTimeout=10"),

    MYSQL(
        "com.mysql.jdbc.Driver",
        "org.hibernate.dialect.MySQL5InnoDBDialect",
        // socket timeout is in milliseconds
        "jdbc:mysql://{0}:{1,number,#}/{2}?socketTimeout=2000&connectTimeout=2000"),

    H2(
        "org.h2.Driver",
        "org.hibernate.dialect.H2Dialect",
        // timeout is in seconds
        "jdbc:mysql://{0}:{1,number,#}/{2};SOCKET_CONNECT_TIMEOUT=2000");



    private String jdbcUrlPattern;
    private String dialect, jdbcDriverClass;

    private DatabaseType(String driverClass, String dialect, String jdbcUrlPattern) {
        this.jdbcDriverClass = driverClass;
        this.dialect = dialect;
        this.jdbcUrlPattern = jdbcUrlPattern;
    }

    public String dialect() {
        return dialect;
    }

    public String jdbcDriverClasss() {
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
}
