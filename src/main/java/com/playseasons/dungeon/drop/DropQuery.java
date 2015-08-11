package com.playseasons.dungeon.drop;

import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class DropQuery {
    List<Entry> query;

    static class Entry {
        final String from;
        final double luck;
        final int stack;

        Entry(String from, double luck, int stack) {
            this.from = from;
            this.luck = luck;
            this.stack = stack;
        }
    }

    static class Modifier {
        Enchantment enchantment;
        int enchantLevel;
        String displayName;
        List<String> lore;
    }

    public DropQuery() {
        query = new ArrayList<>();
    }

    public DropQuery(String from, double luck, int stack) {
        this();
        query.add(new Entry(from, luck, stack));
    }

    public DropQuery add(String from, double luck, int stack) {
        return add(new Entry(from, luck, stack));
    }

    DropQuery add(Entry entry) {
        query.add(entry);
        return this;
    }
}
