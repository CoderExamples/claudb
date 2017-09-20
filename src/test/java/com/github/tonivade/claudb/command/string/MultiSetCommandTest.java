/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.claudb.command.string;

import static com.github.tonivade.claudb.data.DatabaseValue.string;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;

import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.claudb.command.CommandRule;
import com.github.tonivade.claudb.command.CommandUnderTest;
import com.github.tonivade.claudb.command.string.MultiSetCommand;

@CommandUnderTest(MultiSetCommand.class)
public class MultiSetCommandTest {

  @Rule
  public final CommandRule rule = new CommandRule(this);

  @Test
  public void testExecute() {
    rule.withParams("a", "1", "b", "2", "c", "3")
    .execute()
    .assertValue("a", is(string("1")))
    .assertValue("b", is(string("2")))
    .assertValue("c", is(string("3")))
    .assertThat(RedisToken.status("OK"));

    assertThat(rule.getDatabase().size(), is(3));
  }

}
