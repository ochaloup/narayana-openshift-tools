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

package io.narayana.openshift.txrecovery.hibernate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * DTO for gathering name of application pod and
 * the corresponding recovery pod name which the recovery pod works at.
 */
@Entity
@Table(name = ApplicationRecoveryPod.TABLE_NAME)
public class ApplicationRecoveryPod {
    public static final String TABLE_NAME = "RECOVERY_MARKER";

    @EmbeddedId private ApplicationRecoveryPodRecordId id;

    public ApplicationRecoveryPod() {
        // constructor needed by Hibernate
    }

    public ApplicationRecoveryPod(String applicationPodName, String recoveryPodName) {
        this.id = new ApplicationRecoveryPodRecordId();
        id.applicationPodName = applicationPodName;
        id.recoveryPodName = recoveryPodName;
    }

    ApplicationRecoveryPod setApplicationPodName(String applicationPodName) {
        this.id.applicationPodName = applicationPodName;
        return this;
    }
    ApplicationRecoveryPod setRecoveryPodName(String recoveryPodName) {
        this.id.recoveryPodName = recoveryPodName;
        return this;
    }

    public String getApplicationPodName() {
        return this.id.applicationPodName;
    }
    public String getRecoveryPodName() {
        return this.id.recoveryPodName;
    }

    @Override
    public String toString() {
        return String.format("app pod name: %s, recovery pod name: %s",
            getApplicationPodName(), getRecoveryPodName());
    }
}