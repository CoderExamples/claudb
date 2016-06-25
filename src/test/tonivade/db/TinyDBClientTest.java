/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package tonivade.db;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static tonivade.redis.protocol.SafeString.safeString;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import tonivade.redis.IRedisCallback;
import tonivade.redis.RedisClient;
import tonivade.redis.protocol.RedisToken;
import tonivade.redis.protocol.RedisTokenType;

public class TinyDBClientTest {

    @Rule
    public final TinyDBRule rule = new TinyDBRule();

    @Test
    public void testClient() throws Exception {
        ArgumentCaptor<RedisToken> captor = ArgumentCaptor.forClass(RedisToken.class);

        IRedisCallback callback = mock(IRedisCallback.class);
        RedisClient client = new RedisClient(ITinyDB.DEFAULT_HOST, ITinyDB.DEFAULT_PORT, callback);

        client.start();

        verify(callback, timeout(1000)).onConnect();

        client.send("ping");

        verify(callback, timeout(1000)).onMessage(captor.capture());

        RedisToken message = captor.getValue();

        assertThat(message.getType(), is(RedisTokenType.STATUS));
        assertThat(message.getValue(), is(safeString("PONG")));

        client.stop();

        verify(callback, timeout(1000)).onDisconnect();
    }

}
