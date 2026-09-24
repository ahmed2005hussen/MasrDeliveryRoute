package entity;

import java.util.List;

public class Menu {

    private List<MenuItem> menuItemList;

    public Menu(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    public List<MenuItem> getMenuItemList() {
        return menuItemList;
    }

    public void addItem(MenuItem menuItem) {
        if (menuItem == null) throw new IllegalArgumentException("WRONG: item can not be null");

        menuItemList.add(menuItem);
    }

    public boolean removeItem(MenuItem menuItem) {
        return menuItemList.remove(menuItem);
    }

    public boolean isContain(MenuItem menuItem) {
        return menuItemList.contains(menuItem);
    }

    public int size() {
        return menuItemList.size();
    }

    public void displayMenu() {

        if (menuItemList.isEmpty()) {
            System.out.println("We don't have menu to share");
        }

        System.out.println("\nMenu Items: ");
        System.out.println("--------------");

        int count = 1;
        for (MenuItem m : menuItemList) {
            System.out.println(count++ + "- " + m);
        }
        System.out.println("-----------------------");
    }

    public void setMenuItemList(List<MenuItem> menuItemList) {
        this.menuItemList = menuItemList;
    }
}
