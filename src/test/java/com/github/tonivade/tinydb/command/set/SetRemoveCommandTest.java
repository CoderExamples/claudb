/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.set;

import static com.github.tonivade.tinydb.DatabaseValueMatchers.isSet;
import static com.github.tonivade.tinydb.DatabaseValueMatchers.set;

import org.junit.Rule;
import org.junit.Test;

import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;

@CommandUnderTest(SetRemoveCommand.class)
public class SetRemoveCommandTest {

  @Rule
  public final CommandRule rule = new CommandRule(this);

  @Test
  public void testExecute()  {
    rule.withData("key", set("a", "b", "c"))
    .withParams("key", "a")
    .execute()
    .assertValue("key", isSet("b", "c"))
    .then(RedisToken.integer(1));

    rule.withParams("key", "a")
    .execute()
    .assertValue("key", isSet("b", "c"))
    .then(RedisToken.integer(0));
  }

}
