/*
 * Copyright (c) 2015-2017, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */
package com.github.tonivade.tinydb.command.pubsub;

import static java.util.Arrays.asList;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.annotation.ParamLength;
import com.github.tonivade.resp.command.Request;
import com.github.tonivade.resp.protocol.RedisToken;
import com.github.tonivade.resp.protocol.SafeString;
import com.github.tonivade.tinydb.command.TinyDBCommand;
import com.github.tonivade.tinydb.command.annotation.PubSubAllowed;
import com.github.tonivade.tinydb.command.annotation.ReadOnly;
import com.github.tonivade.tinydb.data.Database;

@ReadOnly
@Command("unsubscribe")
@ParamLength(1)
@PubSubAllowed
public class UnsubscribeCommand extends SubscriptionManager implements TinyDBCommand {

  private static final String UNSUBSCRIBE = "unsubscribe";

  @Override
  public RedisToken execute(Database db, Request request) {
    Database admin = getAdminDatabase(request.getServerContext());
    String sessionId = getSessionId(request);
    Collection<SafeString> channels = getChannels(request);
    int i = channels.size();
    List<Object> result = new LinkedList<>();
    for (SafeString channel : request.getParams()) {
      removeSubscription(admin, sessionId, channel);
      getSessionState(request.getSession()).removeSubscription(channel);
      result.addAll(asList(UNSUBSCRIBE, channel, --i));
    }
    return convert(result);
  }

  private String getSessionId(Request request) {
    return request.getSession().getId();
  }

  private Collection<SafeString> getChannels(Request request) {
    return getSessionState(request.getSession()).getSubscriptions();
  }

}
