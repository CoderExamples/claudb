/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.hash;

import static com.github.tonivade.resp.protocol.RedisToken.array;
import static com.github.tonivade.resp.protocol.RedisToken.string;
import static com.github.tonivade.tinydb.DatabaseValueMatchers.entry;
import static com.github.tonivade.tinydb.data.DatabaseValue.hash;

import org.junit.Rule;
import org.junit.Test;

import com.github.tonivade.tinydb.command.CommandRule;
import com.github.tonivade.tinydb.command.CommandUnderTest;

@CommandUnderTest(HashValuesCommand.class)
public class HashValuesCommandTest {

  @Rule
  public final CommandRule rule = new CommandRule(this);

  @Test
  public void testExecute() {
    rule.withData("test",
                  hash(entry("key1", "value1"),
                       entry("key2", "value2"),
                       entry("key3", "value3")))
    .withParams("test")
    .execute()
    // FIXME: order
    .then(array(string("value1"), string("value2"), string("value3")));
  }

  @Test
  public void testExecuteNotExists() {
    rule.withParams("test")
    .execute()
    .then(array());
  }

}
