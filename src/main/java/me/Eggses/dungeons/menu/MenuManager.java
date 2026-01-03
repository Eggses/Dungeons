package me.Eggses.dungeons.menu;

import java.util.ArrayDeque;
import java.util.Deque;

public class MenuManager {

    private final Deque<Menu> menuStack = new ArrayDeque<>();

    public void addAndOpen(Menu menu) {
        menuStack.push(menu);
        menu.open();
    }

    public void removeAndOpen(int backAmount) {
        if (backAmount < 0) return;
        if (menuStack.isEmpty()) return;

        int removals = Math.min(backAmount, menuStack.size() - 1);
        for (int i = 0; i < removals; i++) {
            menuStack.pop();
        }

        Menu menu = menuStack.peek();
        if (menu != null) menu.open();
    }
}