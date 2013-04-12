/*
 * Copyright (c) 2013 SixRQ Ltd.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.freewheelschedule.freewheel.services.rest;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.freewheelschedule.freewheel.controlserver.FreewheelAbstractRunnable;
import org.glassfish.grizzly.http.server.HttpServer;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class WebServiceRunner extends FreewheelAbstractRunnable {
    private final static Log log = LogFactory.getLog(WebServiceRunner.class);

    boolean continueWaiting = true;

    @Override
    public void run() {
        log.info("Starting the web service ...");
        ResourceConfig resourceConfig = new PackagesResourceConfig("com.freewheelschedule.freewheel.services.resources");
        HttpServer httpServer = null;
        try {
            httpServer = GrizzlyServerFactory.createHttpServer(getBaseURI(), resourceConfig);
        } catch (IOException e) {
            log.error("Unable to start Web Service", e);
            return;
        }
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.info("WebServiceRunner Thread Interrupted");
            }
        } while (continueWaiting);
        httpServer.stop();
    }

    public void setContinueWaiting(boolean continueWaiting) {
        this.continueWaiting = continueWaiting;
    }

    public URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost/").port(9998).build();
    }
}
