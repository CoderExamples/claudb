/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package tonivade.db.command.list;

import static org.hamcrest.CoreMatchers.is;
import static tonivade.db.data.DatabaseValue.listFromString;

import org.junit.Rule;
import org.junit.Test;

import tonivade.db.command.CommandRule;
import tonivade.db.command.CommandUnderTest;

@CommandUnderTest(LeftPushCommand.class)
public class LeftPushCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Test
    public void testExecute() throws Exception {
        rule.withParams("key", "a", "b", "c")
            .execute()
            .assertThat("key", is(listFromString("a", "b", "c")))
            .verify().addInt(3);

        rule.withParams("key", "d")
            .execute()
            .assertThat("key", is(listFromString("d", "a", "b", "c")))
            .verify().addInt(4);
    }

}
