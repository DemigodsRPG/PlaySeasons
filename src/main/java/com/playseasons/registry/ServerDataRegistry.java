/*
 * Copyright 2015 Demigods RPG
 * Copyright 2015 Alexander Chauncey
 * Copyright 2015 Alex Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.playseasons.registry;

import com.demigodsrpg.util.datasection.DataSection;
import com.playseasons.impl.PlaySeasons;
import com.playseasons.model.ServerDataModel;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ServerDataRegistry extends AbstractRegistry<ServerDataModel> {
    public ServerDataRegistry(PlaySeasons backend) {
        super(backend, "misc", true);
    }

    public void put(String row, String column, String value) {
        // Remove the value if it exists already
        remove(row, column);

        // Create and save the timed value
        ServerDataModel timedData = new ServerDataModel();
        timedData.generateId();
        timedData.setDataType(ServerDataModel.DataType.PERSISTENT);
        timedData.setRow(row);
        timedData.setColumn(column);
        timedData.setValue(value);
        register(timedData);
    }

    public void put(String row, String column, boolean value) {
        // Remove the value if it exists already
        remove(row, column);

        // Create and save the timed value
        ServerDataModel timedData = new ServerDataModel();
        timedData.generateId();
        timedData.setDataType(ServerDataModel.DataType.PERSISTENT);
        timedData.setRow(row);
        timedData.setColumn(column);
        timedData.setValue(value);
        register(timedData);
    }

    public void put(String row, String column, Number value) {
        // Remove the value if it exists already
        remove(row, column);

        // Create and save the timed value
        ServerDataModel timedData = new ServerDataModel();
        timedData.generateId();
        timedData.setDataType(ServerDataModel.DataType.PERSISTENT);
        timedData.setRow(row);
        timedData.setColumn(column);
        timedData.setValue(value);
        register(timedData);
    }

    /*
     * Timed value
     */
    public void put(String row, String column, Object value, long time, TimeUnit unit) {
        // Remove the value if it exists already
        remove(row, column);

        // Create and save the timed value
        ServerDataModel timedData = new ServerDataModel();
        timedData.generateId();
        timedData.setDataType(ServerDataModel.DataType.TIMED);
        timedData.setRow(row);
        timedData.setColumn(column);
        timedData.setValue(value);
        timedData.setExpiration(unit, time);
        register(timedData);
    }

    public boolean contains(String row, String column) {
        return find(row, column) != null;
    }

    public Object get(String row, String column) {
        return find(row, column).getValue();
    }

    public long getExpiration(String row, String column) throws NullPointerException {
        return find(row, column).getExpiration();
    }

    ServerDataModel find(String row, String column) {
        if (findByRow(row) == null) return null;

        for (ServerDataModel data : findByRow(row))
            if (data.getColumn().equals(column)) return data;

        return null;
    }

    Set<ServerDataModel> findByRow(final String row) {
        return getFromDb().values().stream().filter(model -> model.getRow().equals(row)).collect(Collectors.toSet());
    }

    public void remove(String row, String column) {
        if (find(row, column) != null) remove(find(row, column).getKey());
    }

    /**
     * Clears all expired timed value.
     */
    public void clearExpired() {
        getFromDb().values().stream().filter(model -> ServerDataModel.DataType.TIMED.equals(model.getDataType()) &&
                model.getExpiration() <= System.currentTimeMillis()).map(ServerDataModel::getKey).
                collect(Collectors.toList()).forEach(this::remove);
    }

    @Override
    public ServerDataModel fromDataSection(String stringKey, DataSection data) {
        return new ServerDataModel(stringKey, data);
    }
}
