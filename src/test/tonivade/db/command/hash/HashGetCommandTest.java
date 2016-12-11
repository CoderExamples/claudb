/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package tonivade.db.command.hash;

import static com.github.tonivade.resp.protocol.SafeString.safeString;
import static tonivade.db.DatabaseValueMatchers.entry;
import static tonivade.db.data.DatabaseValue.hash;

import org.junit.Rule;
import org.junit.Test;

import tonivade.db.command.CommandRule;
import tonivade.db.command.CommandUnderTest;

@CommandUnderTest(HashGetCommand.class)
public class HashGetCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Test
    public void testExecute() {
        rule.withData("a", hash(entry("key", "value")))
            .withParams("a", "key")
            .execute()
            .verify().addBulkStr(safeString("value"));
    }

}
