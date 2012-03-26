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
package org.sonar.plugins.core.timemachine;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ViolationTrackingBlocksRecognizerTest {

  @Test
  public void test() {
    assertThat(compute(t("abcde"), t("abcde"), 3, 3), is(5));
    assertThat(compute(t("abcde"), t("abcd"), 3, 3), is(4));
    assertThat(compute(t("bcde"), t("abcde"), 3, 3), is(0));
    assertThat(compute(t("bcde"), t("abcde"), 2, 3), is(4));
  }

  private static int compute(String a, String b, int ai, int bi) {
    ViolationTrackingBlocksRecognizer rec = new ViolationTrackingBlocksRecognizer(a, b);
    return rec.computeLengthOfMaximalBlock(ai, bi);
  }

  private static String t(String text) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < text.length(); i++) {
      sb.append(text.charAt(i)).append('\n');
    }
    return sb.toString();
  }

}