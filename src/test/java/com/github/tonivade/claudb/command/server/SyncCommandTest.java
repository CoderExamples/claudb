/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.claudb.command.server;

import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import com.github.tonivade.claudb.TinyDBServerContext;
import com.github.tonivade.claudb.command.CommandRule;
import com.github.tonivade.claudb.command.CommandUnderTest;

@CommandUnderTest(SyncCommand.class)
public class SyncCommandTest {

  @Rule
  public final CommandRule rule = new CommandRule(this);

  @Test
  public void testExecute() throws IOException  {
    rule.execute()
    .verify(TinyDBServerContext.class).exportRDB(any());
  }

}
