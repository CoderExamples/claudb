package tonivade.db.command.string;

import static org.hamcrest.CoreMatchers.is;
import static tonivade.db.data.DatabaseValue.string;

import org.junit.Rule;
import org.junit.Test;

import tonivade.db.command.impl.CommandRule;
import tonivade.db.command.impl.CommandUnderTest;

@CommandUnderTest(SetCommand.class)
public class SetCommandTest {

    @Rule
    public final CommandRule rule = new CommandRule(this);

    @Test
    public void testExecute() {
        rule.withParams("a", "1")
            .execute()
            .assertThat("a", is(string("1")))
            .verify().addSimpleStr("OK");
    }

}
