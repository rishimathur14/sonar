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
package org.sonar.core.persistence;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.hamcrest.core.Is;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.core.rule.RuleMapper;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class MyBatisTest {

  static {
    DerbyUtils.fixDerbyLogs();
  }

  private static MyBatis myBatis;
  private static InMemoryDatabase database;

  @BeforeClass
  public static void start() {
    database = new InMemoryDatabase();
    myBatis = new MyBatis(database.start());
    myBatis.start();
  }

  @AfterClass
  public static void stop() {
    database.stop();
  }

  @Test
  public void shouldConfigureMyBatis() {
    Configuration conf = myBatis.getSessionFactory().getConfiguration();
    assertThat(conf.isUseGeneratedKeys(), Is.is(true));
    assertThat(conf.hasMapper(RuleMapper.class), Is.is(true));
    assertThat(conf.isLazyLoadingEnabled(), Is.is(false));
  }

  @Test
  public void shouldOpenBatchSession() {
    SqlSession session = myBatis.openBatchSession();
    try {
      assertThat(session.getConnection(), notNullValue());
      assertThat(session.getMapper(RuleMapper.class), notNullValue());
    } finally {
      session.close();
    }
  }
}
