/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.replication;

import static com.github.tonivade.resp.protocol.RedisToken.array;
import static com.github.tonivade.resp.protocol.RedisToken.nullString;
import static com.github.tonivade.resp.protocol.RedisToken.string;
import static com.github.tonivade.resp.protocol.SafeString.safeString;
import static com.github.tonivade.tinydb.data.DatabaseKey.safeKey;
import static com.github.tonivade.tinydb.data.DatabaseValue.set;
import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.resp.protocol.RedisToken.ArrayRedisToken;
import com.github.tonivade.resp.protocol.SafeString;
import com.github.tonivade.tinydb.ITinyDB;
import com.github.tonivade.tinydb.TinyDBServerState;
import com.github.tonivade.tinydb.data.Database;
import com.github.tonivade.tinydb.data.DatabaseKey;
import com.github.tonivade.tinydb.data.DatabaseValue;

public class MasterReplication implements Runnable {

  private static final Logger LOGGER = Logger.getLogger(MasterReplication.class.getName());

  private static final String SELECT_COMMAND = "SELECT";
  private static final String PING_COMMAND = "PING";
  private static final int TASK_DELAY = 2;

  private static final DatabaseKey SLAVES_KEY = safeKey(safeString("slaves"));

  private final ITinyDB server;
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

  public MasterReplication(ITinyDB server) {
    this.server = server;
  }

  public void start() {
    executor.scheduleWithFixedDelay(this, TASK_DELAY, TASK_DELAY, TimeUnit.SECONDS);
  }

  public void stop() {
    executor.shutdown();
  }

  public void addSlave(String id) {
    getAdminDatabase().merge(SLAVES_KEY, set(safeString(id)), (oldValue, newValue) -> {
      List<SafeString> merge = new LinkedList<>();
      merge.addAll(oldValue.getValue());
      merge.addAll(newValue.getValue());
      return set(merge);
    });
    LOGGER.info(() -> "new slave: " + id);
  }

  public void removeSlave(String id) {
    getAdminDatabase().merge(SLAVES_KEY, set(safeString(id)), (oldValue, newValue) -> {
      List<SafeString> merge = new LinkedList<>();
      merge.addAll(oldValue.getValue());
      merge.removeAll(newValue.getValue());
      return set(merge);
    });
    LOGGER.info(() -> "slave revomed: " + id);
  }

  @Override
  public void run() {
    List<ArrayRedisToken> commands = createCommands();

    for (SafeString slave : getSlaves()) {
      for (ArrayRedisToken command : commands) {
        server.publish(slave.toString(), command);
      }
    }
  }

  private Database getAdminDatabase() {
    return getServerState().getAdminDatabase();
  }

  private Set<SafeString> getSlaves() {
    return getAdminDatabase().getOrDefault(SLAVES_KEY, DatabaseValue.EMPTY_SET).getValue();
  }

  private List<ArrayRedisToken> createCommands() {
    List<ArrayRedisToken> commands = new LinkedList<>();
    commands.add(pingCommand());
    commands.addAll(commandsToReplicate());
    return commands;
  }

  private List<ArrayRedisToken> commandsToReplicate() {
    List<ArrayRedisToken> commands = new LinkedList<>();

    for (ArrayRedisToken command : server.getCommandsToReplicate()) {
      commands.add(selectCommand(command.getValue().stream().findFirst().orElse(nullString())));
      commands.add(array(command.getValue().stream().skip(1).collect(toList())));
    }
    return commands;
  }

  private ArrayRedisToken selectCommand(RedisToken<?> database) {
    return array(string(SELECT_COMMAND), database);
  }

  private ArrayRedisToken pingCommand() {
    return array(string(PING_COMMAND));
  }

  private TinyDBServerState getServerState() {
    return serverState().orElseThrow(() -> new IllegalStateException("mission server state"));
  }

  private Optional<TinyDBServerState> serverState() {
    return server.getValue("state");
  }
}
