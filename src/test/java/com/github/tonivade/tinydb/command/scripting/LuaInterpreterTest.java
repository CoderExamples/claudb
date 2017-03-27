/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.tinydb.command.scripting;

import static com.github.tonivade.resp.protocol.RedisToken.array;
import static com.github.tonivade.resp.protocol.RedisToken.integer;
import static com.github.tonivade.resp.protocol.RedisToken.string;
import static com.github.tonivade.resp.protocol.SafeString.safeString;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.github.tonivade.resp.command.ICommand;
import com.github.tonivade.resp.command.IRequest;
import com.github.tonivade.resp.command.IResponse;
import com.github.tonivade.resp.command.IServerContext;
import com.github.tonivade.resp.command.ISession;
import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.resp.protocol.SafeString;

@RunWith(MockitoJUnitRunner.class)
public class LuaInterpreterTest {
  @Mock
  private IServerContext context;
  @Mock
  private ISession session;

  private ICommand command = new ICommand()
  {
    @Override
    public void execute(IRequest request, IResponse response)
    {
      response.addSimpleStr("PONG");
    }
  };

  private LuaInterpreter interpreter;

  @Before
  public void setUp() {
    interpreter = new LuaInterpreter(new RedisBinding(context, session));
  }

  @Test
  public void keys() {
    RedisToken token = interpreter.execute(safeString("return KEYS[1]"),
                                           asList(safeString("key1")),
                                           emptyList());

    assertThat(token, equalTo(string("key1")));
  }

  @Test
  public void argv() {
    RedisToken token = interpreter.execute(safeString("return ARGV[1]"),
                                           asList(safeString("key1")),
                                           asList(safeString("value1")));

    assertThat(token, equalTo(string("value1")));
  }

  @Test
  public void keysAndArgv() {
    RedisToken token = interpreter.execute(safeString("return {KEYS[1], ARGV[1]}"),
                                           asList(safeString("key1")),
                                           asList(safeString("value1")));

    assertThat(token, equalTo(array(string("key1"), string("value1"))));
  }

  @Test
  public void number() {
    RedisToken token = interpreter.execute(safeString("return 1"),
                                           emptyList(),
                                           emptyList());

    assertThat(token, equalTo(integer(1)));
  }

  @Test
  public void boolTrue() {
    RedisToken token = interpreter.execute(safeString("return true"),
                                           emptyList(),
                                           emptyList());

    assertThat(token, equalTo(integer(1)));
  }

  @Test
  public void boolFalse() {
    RedisToken token = interpreter.execute(safeString("return false"),
                                           emptyList(),
                                           emptyList());

    assertThat(token, equalTo(string(SafeString.EMPTY_STRING)));
  }

  @Test
  public void ping() {
    when(context.getCommand("ping")).thenReturn(command);

    RedisToken token = interpreter.execute(safeString("return redis.call('ping')"),
                                           emptyList(),
                                           emptyList());

    assertThat(token, equalTo(string("PONG")));
  }

}
