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
import java.util.stream.Collectors;

import io.narayana.openshift.txrecovery.types.OutputFormatType;

public class OutputPrinter {
    private OutputPrinter() {
        // utility class
    }

    public static void printToStandardOutput(List<String> dataToPrint, OutputFormatType printingFormat) {
        switch(printingFormat) {
            case LIST_COMMA:
                System.out.println(
                    dataToPrint.stream().collect(Collectors.joining(", ")));
                break;
            case RAW:
                System.out.println(dataToPrint);
                break;
            case LIST_SPACE:
            default:
                System.out.println(
                    dataToPrint.stream().collect(Collectors.joining(" ")));
        }
    }
}
