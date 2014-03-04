/*******************************************************************************
 * Copyright (c) 2013 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package org.eclipsescout.demo.minicrm.server;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.server.scheduler.Scheduler;
import org.eclipse.scout.rt.server.services.common.node.IBackendService;
import org.eclipse.scout.rt.server.services.common.notification.INotificationService;
import org.eclipse.scout.service.CreateServiceImmediatelySchedulingRule;
import org.eclipse.scout.service.SERVICES;
import org.eclipsescout.demo.minicrm.server.services.notification.RegisterUserNotificationListener;
import org.eclipsescout.demo.minicrm.server.services.notification.UnregisterUserNotificationListener;

/**
 * Dummy application in order to manage server side product configurations in *.product files.
 * A typical config.ini for such a product has (among others) the following properties:
 * osgi.clean=true
 * osgi.console=
 * eclipse.consoleLog=true
 * org.eclipse.equinox.http.jetty.http.port=8080
 * org.eclipse.equinox.http.jetty.context.path=/minicrm_server
 * osgi.bundles=org.eclipse.equinox.common@2:start, org.eclipse.update.configurator@start,
 * org.eclipse.equinox.http.jetty@start, org.eclipse.equinox.http.registry@start, org.eclipse.core.runtime@start
 * osgi.bundles.defaultStartLevel=4
 * osgi.noShutdown=true
 * eclipse.ignoreApp=false
 * eclipse.product=org.eclipsescout.demo.minicrm.server.product
 */
public class ServerApplication implements IApplication {
  private static IScoutLogger logger = ScoutLogManager.getLogger(ServerApplication.class);

  @Override
  public Object start(IApplicationContext context) throws Exception {
    //start the scheduler

    Scheduler scheduler = new Scheduler(SERVICES.getService(IBackendService.class).getBackendSubject(), ServerSession.class);
    scheduler.start();
    Activator.getDefault().setScheduler(scheduler);

    Job job = new Job("Start distributed notification initialization job") {

      @Override
      protected IStatus run(IProgressMonitor monitor) {

        if (SERVICES.getService(INotificationService.class).register()) {
          return Status.OK_STATUS;
        }
        return Status.CANCEL_STATUS;

      }
    };
    job.setRule(new CreateServiceImmediatelySchedulingRule());
    job.schedule();

    SERVICES.getService(INotificationService.class).addDistributedNotificationListener(new RegisterUserNotificationListener());
    SERVICES.getService(INotificationService.class).addDistributedNotificationListener(new UnregisterUserNotificationListener());

    logger.info("minicrm server initialized");
    return EXIT_OK;
  }

  @Override
  public void stop() {

  }
}
