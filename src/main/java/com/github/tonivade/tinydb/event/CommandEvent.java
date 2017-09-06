package com.github.tonivade.tinydb.event;

import static java.lang.String.format;

import com.github.tonivade.resp.protocol.SafeString;

class CommandEvent extends Event {
  
  private static final String KEYSPACE = "__keyspace__@%d__:%s";

  public CommandEvent(SafeString command, SafeString key, int schema) {
    super(command, key, schema);
  }
  
  @Override
  SafeString getValue() {
    return getCommand();
  }
  
  @Override
  String getChannel() {
    return format(KEYSPACE, getSchema(), getCommand());
  }
}
