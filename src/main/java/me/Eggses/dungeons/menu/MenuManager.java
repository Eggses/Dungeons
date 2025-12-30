package me.Eggses.dungeons.menu;

import java.util.ArrayDeque;
import java.util.Deque;

public class MenuManager {

    private final Deque<Menu> menus = new ArrayDeque<>();

    public void addAndOpen(Menu menu) {
        menus.push(menu);
        menu.open();
    }

    public void removeAndOpen(int backAmount) {
        if (backAmount < 0) return;
        if (menus.isEmpty()) return;

        int removals = Math.min(backAmount, menus.size() - 1);
        for (int i = 0; i < removals; i++) {
            menus.pop();
        }

        Menu menu = menus.peek();
        if (menu != null) menu.open();
    }
}