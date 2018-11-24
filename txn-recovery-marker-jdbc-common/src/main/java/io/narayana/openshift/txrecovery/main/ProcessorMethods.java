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

import io.narayana.openshift.txrecovery.types.CommandType;

/**
 * <p>
 * Definition of business code to be process for particular command type {@link CommandType}.
 * <p>
 * <i>NOTE:</i> the consumer of the API expects the <code>List</code> to be the result
 * of all methods even for those which will do not return any data to consume.
 */
public interface ProcessorMethods {
    /**
     * Creating database table.
     */
    List<String> create();

    /**
     * Inserting data to database.
     */
    List<String> insert();

    /**
     * Deleting data from database.
     */
    List<String> delete();

    /**
     * Select list of application pod names saved in database.
     */
    List<String> selectApplication();

    /**
     * Select list of recovery pod names saved in database.
     */
    List<String> selectRecovery();

    /**
     * Dropping database table from database.
     */
    List<String> drop();
}
