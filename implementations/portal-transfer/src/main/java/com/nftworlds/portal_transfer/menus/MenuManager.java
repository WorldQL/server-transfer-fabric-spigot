package com.nftworlds.portal_transfer.menus;

import com.nftworlds.portal_transfer.data.ServerLoader;
import com.nftworlds.portal_transfer.models.Server;

import java.util.List;

public class MenuManager {

    private static MenuManager instance;

    private MenuManager() {
        serverList = ServerLoader.getServerList();
        editorMenu = new EditorMenu();
        settingsMenu = new SettingsMenu();
        selectorMenu = new ServerSelectorMenu();
    }

    public static synchronized MenuManager getInstance() {
        if (instance == null)
            instance = new MenuManager();
        return instance;
    }

    public void init() {
        selectorMenu.initializeItems();
    }

    private final EditorMenu editorMenu;
    private final SettingsMenu settingsMenu;
    private final ServerSelectorMenu selectorMenu;

    public List<Server> serverList;

    public EditorMenu getEditorMenu() {
        return editorMenu;
    }

    public SettingsMenu getSettingsMenu() {
        return settingsMenu;
    }


    public ServerSelectorMenu getSelectorMenu() {
        return selectorMenu;
    }

}
