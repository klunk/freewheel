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

package org.freewheelschedule.freewheel.schedule;

import org.freewheelschedule.freewheel.config.ServerConfig;
import org.freewheelschedule.freewheel.controlserver.ControlServer;
import org.freewheelschedule.freewheel.rest.RestServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScheduleLauncher {

    @Autowired
    private ControlServer controlServer;
    @Autowired
    private RestServices restService;


    private void runScheduleLauncher() {
        controlServer.runControlServer();
        restService.runRestService();
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ServerConfig.class);

        ScheduleLauncher server = (ScheduleLauncher) ctx.getBean("scheduleLauncher");
        server.runScheduleLauncher();
    }
}
