/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.dbcleaner;

import org.junit.Test;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Scopes;
import org.sonar.core.purge.PurgeDao;
import org.sonar.plugins.dbcleaner.api.DbCleanerConstants;
import org.sonar.plugins.dbcleaner.period.DefaultPeriodCleaner;

import static org.mockito.Mockito.*;

public class DefaultPurgeTaskTest {
  @Test
  public void shouldNotDeleteHistoricalDataOfDirectories() {
    PurgeDao purgeDao = mock(PurgeDao.class);
    Settings settings = new Settings(new PropertyDefinitions(DefaultPurgeTask.class));
    settings.setProperty(DbCleanerConstants.PROPERTY_CLEAN_DIRECTORY, "false");
    DefaultPurgeTask task = new DefaultPurgeTask(purgeDao, settings, mock(DefaultPeriodCleaner.class));

    task.purge(1L);

    verify(purgeDao).purge(1L, new String[]{Scopes.FILE});
  }

  @Test
  public void shouldDeleteHistoricalDataOfDirectoriesByDefault() {
    PurgeDao purgeDao = mock(PurgeDao.class);
    Settings settings = new Settings(new PropertyDefinitions(DefaultPurgeTask.class));
    DefaultPurgeTask task = new DefaultPurgeTask(purgeDao, settings, mock(DefaultPeriodCleaner.class));

    task.purge(1L);

    verify(purgeDao).purge(1L, new String[]{Scopes.DIRECTORY, Scopes.FILE});
  }

  @Test
  public void shouldNotFailOnErrors() {
    PurgeDao purgeDao = mock(PurgeDao.class);
    when(purgeDao.purge(anyLong(), (String[]) any())).thenThrow(new RuntimeException());
    DefaultPurgeTask task = new DefaultPurgeTask(purgeDao, new Settings(), mock(DefaultPeriodCleaner.class));

    task.purge(1L);

    verify(purgeDao).purge(anyLong(), (String[]) any());
  }
}
