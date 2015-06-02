package tonivade.db.command.string;

import java.util.ArrayList;
import java.util.List;

import tonivade.db.command.ICommand;
import tonivade.db.command.IRequest;
import tonivade.db.command.IResponse;
import tonivade.db.command.annotation.Command;
import tonivade.db.command.annotation.ParamLength;
import tonivade.db.data.DatabaseValue;
import tonivade.db.data.IDatabase;

@Command("mget")
@ParamLength(1)
public class MultiGetCommand implements ICommand {

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        List<DatabaseValue> result = new ArrayList<>(request.getLength());
        for (String key : request.getParams()) {
            result.add(db.get(key));
        }
        response.addArrayValue(result);
    }

}
