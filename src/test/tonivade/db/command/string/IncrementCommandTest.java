package tonivade.db.command.string;

import org.junit.Rule;
import org.junit.Test;

import tonivade.db.command.impl.CommandRule;
import tonivade.db.command.impl.CommandUnderTest;
import tonivade.db.command.string.IncrementCommand;

@CommandUnderTest(IncrementCommand.class)
public class IncrementCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Test
    public void testExecute() {
        rule.withParams("a")
            .execute()
            .verify().addInt("1");

        rule.withParams("a")
            .execute()
            .verify().addInt("2");

        rule.withParams("a")
            .execute()
            .verify().addInt("3");
    }

}
