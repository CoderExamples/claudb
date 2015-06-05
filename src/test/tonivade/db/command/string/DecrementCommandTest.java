/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package tonivade.db.command.string;

import org.junit.Rule;
import org.junit.Test;

import tonivade.db.command.CommandRule;
import tonivade.db.command.CommandUnderTest;
import tonivade.db.command.string.DecrementCommand;

@CommandUnderTest(DecrementCommand.class)
public class DecrementCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Test
    public void testExecute() {
        rule.withParams("a")
            .execute()
            .verify().addInt("-1");

        rule.withParams("a")
            .execute()
            .verify().addInt("-2");

        rule.withParams("a")
            .execute()
            .verify().addInt("-3");
    }

}
