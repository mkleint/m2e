/*******************************************************************************
 * Copyright (c) 2008-2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Sonatype, Inc. - initial API and implementation
 *******************************************************************************/

package org.eclipse.m2e.core.internal.repository;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import org.eclipse.m2e.core.internal.Messages;
import org.eclipse.m2e.core.internal.jobs.IBackgroundProcessingQueue;

/**
 * RepositoryRegistryUpdateJob
 *
 * @author igor
 */
public class RepositoryRegistryUpdateJob extends Job implements IBackgroundProcessingQueue {
  
  private final RepositoryRegistry registry;

  private final ArrayList<Object> queue = new ArrayList<Object>();

  public RepositoryRegistryUpdateJob(RepositoryRegistry registry) {
    super(Messages.RepositoryRegistryUpdateJob_title);
    this.registry = registry;
  }

  public IStatus run(IProgressMonitor monitor) {
    synchronized(queue) {
      queue.clear();
    }
    try {
      registry.updateRegistry(monitor);
    } catch(CoreException ex) {
      return ex.getStatus();
    }
    return Status.OK_STATUS;
  }
  
  public boolean isEmpty() {
    synchronized(queue) {
      return queue.isEmpty();
    }
  }

  public void updateRegistry() {
    synchronized(queue) {
      queue.add(new Object());
      schedule(1000L);
    }
  }  
}
