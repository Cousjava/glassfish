/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.test.jms.mdbdest.client;

import javax.naming.*;
import javax.jms.*;
import org.glassfish.test.jms.mdbdest.ejb.*;
import com.sun.ejte.ccl.reporter.SimpleReporterAdapter;

public class Client {
    private final static SimpleReporterAdapter STAT = new SimpleReporterAdapter("appserv-tests");

    public static void main (String[] args) {
        STAT.addDescription("mdbDestID");
        Client client = new Client(args);
        client.doTest();
        STAT.printSummary("mdbDestID");
    }

    public Client (String[] args) {
    }

    public void doTest() {
        String ejbName = "MySessionBean";
        String text = "Hello World!";
        int count = 0;
        int expectedCount = 6;
        try {
            Context ctx = new InitialContext();
            MySessionBeanRemote beanRemote = (MySessionBeanRemote) ctx.lookup(MySessionBeanRemote.RemoteJNDIName);
            beanRemote.sendMessage(text);

            count = beanRemote.checkMessage(text, expectedCount);
            if (count == expectedCount)
                STAT.addStatus("mdbDestID", STAT.PASS);
            else {
                System.out.println("Got " + count + " messages, but " + expectedCount + " are expected.");
                STAT.addStatus("mdbDestID", STAT.FAIL);
            }
        } catch(Exception e) {
            e.printStackTrace();
            STAT.addStatus("mdbDestID " + ejbName, STAT.FAIL);
        }
    }
}
