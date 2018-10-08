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

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
class ApplicationRecoveryPodRecordId implements Serializable {
    private static final long serialVersionUID = 1L;

    String applicationPodName;
    String recoveryPodName;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((applicationPodName == null) ? 0 : applicationPodName.hashCode());
        result = prime * result + ((recoveryPodName == null) ? 0 : recoveryPodName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApplicationRecoveryPodRecordId other = (ApplicationRecoveryPodRecordId) obj;
        if (applicationPodName == null) {
            if (other.applicationPodName != null)
                return false;
        } else if (!applicationPodName.equals(other.applicationPodName))
            return false;
        if (recoveryPodName == null) {
            if (other.recoveryPodName != null)
                return false;
        } else if (!recoveryPodName.equals(other.recoveryPodName))
            return false;
        return true;
    }

}