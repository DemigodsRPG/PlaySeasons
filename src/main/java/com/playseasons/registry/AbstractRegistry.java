/*
 * Copyright (c) 2015 Demigods RPG
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.playseasons.registry;

import com.demigodsrpg.util.datasection.DataSection;
import com.demigodsrpg.util.datasection.FJsonSection;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.playseasons.impl.*;

import java.io.*;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class AbstractRegistry<T extends Model> {
    protected final Cache<String, T> REGISTERED_DATA;

    // -- SQL -- //

    private String TABLE;

    // -- FILE -- //
    private File FOLDER;
    private boolean PRETTY;

    public AbstractRegistry(String table, int expireMins) {
        if (expireMins > 0) {
            REGISTERED_DATA =
                    CacheBuilder.newBuilder().concurrencyLevel(4).expireAfterAccess(expireMins, TimeUnit.MINUTES)
                            .build();
        } else {
            REGISTERED_DATA = CacheBuilder.newBuilder().concurrencyLevel(4).build();
        }
        TABLE = table;
    }

    public AbstractRegistry(PlaySeasons backend, String folder, boolean pretty, int expireMins) {
        if (expireMins > 0) {
            REGISTERED_DATA =
                    CacheBuilder.newBuilder().concurrencyLevel(4).expireAfterAccess(expireMins, TimeUnit.MINUTES)
                            .build();
        } else {
            REGISTERED_DATA = CacheBuilder.newBuilder().concurrencyLevel(4).build();
        }
        FOLDER = new File(backend.getDataFolder().getPath() + "/" + folder + "/");
        PRETTY = pretty;
    }

    public Optional<T> fromKey(String key) {
        if (!REGISTERED_DATA.asMap().containsKey(key)) {
            loadFromDb(key);
        }
        return Optional.ofNullable(REGISTERED_DATA.asMap().getOrDefault(key, null));
    }

    public T register(T value) {
        REGISTERED_DATA.put(value.getKey(), value);
        saveToDb(value.getKey());
        return value;
    }

    public T put(String key, T value) {
        REGISTERED_DATA.put(key, value);
        saveToDb(key);
        return value;
    }

    public void remove(String key) {
        REGISTERED_DATA.asMap().remove(key);
        if (!Setting.USING_PSQL) {
            removeFile(key);
        } else {
            // TODO
        }
    }

    private void createFile(File file) {
        try {
            FOLDER.mkdirs();
            file.createNewFile();
        } catch (Exception oops) {
            oops.printStackTrace();
        }
    }

    public void removeFile(String key) {
        File file = new File(FOLDER.getPath() + "/" + key + ".json");
        if (file.exists()) {
            file.delete();
        }
    }

    public void saveToDb(String key) {
        if (REGISTERED_DATA.asMap().containsKey(key)) {
            if (!Setting.USING_PSQL) {
                File file = new File(FOLDER.getPath() + "/" + key + ".json");
                if (!(file.exists())) {
                    createFile(file);
                }
                Gson gson = PRETTY ? new GsonBuilder().setPrettyPrinting().create() : new GsonBuilder().create();
                String json = gson.toJson(REGISTERED_DATA.asMap().get(key).serialize());
                try {
                    PrintWriter writer = new PrintWriter(file);
                    writer.print(json);
                    writer.close();
                } catch (Exception oops) {
                    oops.printStackTrace();
                }
            } else {
                // TODO
            }
        }
    }

    public void loadAllFromDb() {
        File[] files = FOLDER.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".json")) {
                    String key = fileName.replace(".json", "");
                    loadFromDb(key);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromDb(String key) {
        Gson gson = new GsonBuilder().create();
        if (!Setting.USING_PSQL) {
            try {
                File file = new File(FOLDER.getPath() + "/" + key + ".json");
                if (file.exists()) {
                    FileInputStream inputStream = new FileInputStream(file);
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    DataSection section = new FJsonSection(gson.fromJson(reader, Map.class));
                    REGISTERED_DATA.put(key, fromDataSection(key, section));
                    reader.close();
                }
            } catch (Exception oops) {
                oops.printStackTrace();
            }
        } else {
            // TODO
        }
    }

    @SuppressWarnings("ConstantConditions")
    public ConcurrentMap<String, T> getFromDb() {
        if (!Setting.USING_PSQL) {
            for (File file : FOLDER.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    String key = file.getName().replace(".json", "");
                    loadFromDb(key);
                }
            }
        } else {
            // TODO
        }
        return REGISTERED_DATA.asMap();
    }

    public void purge() {
        REGISTERED_DATA.asMap().keySet().forEach(this::removeFile);
        REGISTERED_DATA.asMap().clear();
    }

    protected abstract T fromDataSection(String key, DataSection section);
}
