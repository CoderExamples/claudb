/*
 * Copyright (c) 2015, Antonio Gabriel Muñoz Conejo <antoniogmc at gmail dot com>
 * Distributed under the terms of the MIT License
 */

package com.github.tonivade.tinydb.command.set;

import static com.github.tonivade.tinydb.data.DatabaseKey.safeKey;
import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.Set;

import com.github.tonivade.resp.annotation.Command;
import com.github.tonivade.resp.annotation.ParamLength;
import com.github.tonivade.resp.command.IRequest;
import com.github.tonivade.resp.command.IResponse;
import com.github.tonivade.resp.protocol.SafeString;
import com.github.tonivade.tinydb.command.ITinyDBCommand;
import com.github.tonivade.tinydb.command.annotation.ParamType;
import com.github.tonivade.tinydb.command.annotation.ReadOnly;
import com.github.tonivade.tinydb.data.DataType;
import com.github.tonivade.tinydb.data.DatabaseKey;
import com.github.tonivade.tinydb.data.DatabaseValue;
import com.github.tonivade.tinydb.data.IDatabase;

@ReadOnly
@Command("sinter")
@ParamLength(2)
@ParamType(DataType.SET)
public class SetIntersectionCommand implements ITinyDBCommand {

    @Override
    public void execute(IDatabase db, IRequest request, IResponse response) {
        DatabaseValue first = db.getOrDefault(safeKey(request.getParam(0)), DatabaseValue.EMPTY_SET);
        Set<SafeString> result = new HashSet<>(first.<Set<SafeString>>getValue());
        for (DatabaseKey param : request.getParams().stream().skip(1).map((item) -> safeKey(item)).collect(toList())) {
            result.retainAll(db.getOrDefault(param, DatabaseValue.EMPTY_SET).<Set<SafeString>>getValue());
        }
        response.addArray(result);
    }

}
