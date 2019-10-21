package OwnLibs.Owls;

import OwnLibs.Owls.Views.Items.*;
import com.spvessel.spacevil.*;
import com.spvessel.spacevil.Core.InterfaceBaseItem;
import com.spvessel.spacevil.Core.MouseArgs;
import com.spvessel.spacevil.Flags.*;

import OwnLibs.Owls.Views.Windows.*;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Controller {
    final String rootFolder;
    private MainWindow owlWindow;
    private SettingsWindow setsWindow;
    private FileEntryTreeItem rootItem;
    private FileEntryTreeItem workItem;
    private FileEntryTreeItem workDirectory;
    private FileEntryTreeItem tmpItem;
    private KeyBindings helpWnd;
    private Map<FileEntryTab, FileEntryTreeItem> tabToOwls;

    private History historyStore;

    public Controller(MainWindow owlWindow) {
        String userDir = System.getProperty("user.dir");
        rootFolder = userDir + File.separator + "workspace";
        this.owlWindow = owlWindow;
        setsWindow = new SettingsWindow();
        InterfaceSupport.controller = this;
        tabToOwls = new HashMap<>();
        historyStore = new History(this);
    }

    public void start() {
        initialize();
        owlWindow.show();
    }

    private void initialize() {
        helpWnd = new KeyBindings(owlWindow);
        // TODO: подгрузка библиотеки или выбор директории
        // rootItem = InterfaceSupport.makeOwlItem(rootFolder.substring(2), rootFolder,
        // TreeItemType.BRANCH, null);
        rootItem = InterfaceSupport.makeOwlItem("workspace", rootFolder, TreeItemType.BRANCH, null);
        fillFilesTree(rootItem);
        rootItem = InterfaceSupport.filesTreeMaker(rootFolder, rootItem);
        tmpItem = null;
        workDirectory = rootItem; // Копировать значения или оставить ссылку?
        itemToDefault();
        // fillFields();

        owlWindow.eventKeyRelease.add((sender, args) -> {
            if (args.key == KeyCode.PAGEDOWN) {
                List<InterfaceBaseItem> list = ItemsLayoutBox.getLayoutItems(owlWindow.getWindowGuid());
                int result = 0;
                for (InterfaceBaseItem item : list) {
                    if (item.getBackground().getAlpha() > 0 && item.isDrawable() && item.isVisible()) {
                        result++;
                    }
                }
                System.out.println(result);
            }
        });
        // New file
        owlWindow.homePage.newFileLabel.eventMouseClick.add((sender, args) -> {
            if (args.button == MouseButton.BUTTON_LEFT)
                treeNewFileOrFolder(workDirectory, "file");
        });
        owlWindow.homePage.newFolderLabel.eventMouseClick.add((sender, args) -> {
            if (args.button == MouseButton.BUTTON_LEFT)
                treeNewFileOrFolder(workDirectory, "folder");
        });
        owlWindow.homePage.importFileLabel.eventMouseClick.add((sender, args) -> treeImportFile(workDirectory));
        owlWindow.homePage.settingsLabel.eventMouseClick.add((sender, args) -> setsWindow.show());
        owlWindow.homePage.quickTipsLabel.eventMouseClick.add((sender, args) -> helpWnd.show());

        owlWindow.newFileBtn.eventMouseClick.add((sender, args) -> treeNewFileOrFolder(workDirectory, "file"));
        owlWindow.miNewFile.eventMouseClick.add((sender,
                args) -> treeNewFileOrFolder((FileEntryTreeItem) owlWindow.filesTree.getSelectedItem(), "file"));

        // Save file
        owlWindow.saveBtn.eventMouseClick.add((sender, args) -> saveFile());

        owlWindow.eventKeyPress.add((sender, args) -> {
            if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.S) {
                saveFile();
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.contains(KeyMods.SHIFT) && args.mods.size() == 2
                    && args.key == KeyCode.S) {
                saveAllFiles();
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.E) {
                owlWindow.editBtn.eventToggle.execute(owlWindow.editBtn, new MouseArgs());
                TextArea current = getCurrentTextArea();
                if (current != null)
                    current.setFocus();

            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.K) {
                owlWindow.filePreferencesBtn.eventMouseClick.execute(owlWindow.filePreferencesBtn, new MouseArgs());
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.contains(KeyMods.SHIFT) && args.mods.size() == 2
                    && args.key == KeyCode.R) {
                owlWindow.filesTree.getArea().setFocus();
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.contains(KeyMods.SHIFT) && args.mods.size() == 2
                    && args.key == KeyCode.E) {

                if (!owlWindow.editBtn.isToggled()) {
                    owlWindow.editBtn.setToggled(true);
                    onEditBtnPressed(true);
                    // owlWindow.editBtn.eventToggle.execute(owlWindow.editBtn, new MouseArgs());
                }
                TextArea current = getCurrentTextArea();
                if (current != null)
                    current.setFocus();

            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.H) {
                helpWnd.show();
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.F) {
                owlWindow.searchBtn.eventMouseClick.execute(owlWindow.searchBtn, null);
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.I) {
                owlWindow.importBtn.eventMouseClick.execute(owlWindow.searchBtn, null);
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.size() == 1 && args.key == KeyCode.N) {
                owlWindow.newFileBtn.eventMouseClick.execute(owlWindow.searchBtn, null);
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.contains(KeyMods.SHIFT) && args.mods.size() == 2
                    && args.key == KeyCode.N) {
                owlWindow.newFolderBtn.eventMouseClick.execute(owlWindow.searchBtn, null);
            } else if (args.key == KeyCode.F11) {
                owlWindow.titleBar.getMaximizeButton().eventMouseClick.execute(owlWindow.titleBar, null);
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.contains(KeyMods.ALT) && args.mods.size() == 2
                    && args.key == KeyCode.LEFT) {
                List<Tab> tabs = owlWindow.workTabArea.getTabs();
                int index = tabs.indexOf(owlWindow.workTabArea.getSelectedTab());
                index--;
                if (index < 0)
                    return;
                owlWindow.workTabArea.selectTab(tabs.get(index));
                TextArea area = getCurrentTextArea();
                if (area != null)
                    area.setFocus();
            } else if (args.mods.contains(KeyMods.CONTROL) && args.mods.contains(KeyMods.ALT) && args.mods.size() == 2
                    && args.key == KeyCode.RIGHT) {
                List<Tab> tabs = owlWindow.workTabArea.getTabs();
                int index = tabs.indexOf(owlWindow.workTabArea.getSelectedTab());
                index++;
                if (index >= tabs.size())
                    return;
                owlWindow.workTabArea.selectTab(tabs.get(index));
                TextArea area = getCurrentTextArea();
                if (area != null)
                    area.setFocus();
            }
        });

        owlWindow.filesTree.eventKeyPress.add((sender, args) -> {
            if (args.key == KeyCode.MENU) {
                MouseArgs margs = new MouseArgs();
                margs.button = MouseButton.BUTTON_RIGHT;
                if (owlWindow.filesTree.getSelection() >= 0) {
                    InterfaceBaseItem selection = owlWindow.filesTree.getSelectedItem();
                    margs.position.setPosition(selection.getX() + 5, selection.getY() + selection.getHeight() - 3);
                } else
                    margs.position.setPosition(owlWindow.filesTree.getX() + 5, owlWindow.filesTree.getY() + 5);

                owlWindow.filesTree.eventMouseClick.execute(owlWindow.filesTree, margs);
            }
        });

        // Delete file
        owlWindow.deleteBtn.eventMouseClick.add((sender, args) -> {
            tmpItem = workItem;
            treeDeleteFile();
        });
        owlWindow.miTreeDelete.eventMouseClick.add((sender, args) -> {
            tmpItem = (FileEntryTreeItem) owlWindow.filesTree.getSelectedItem();
            treeDeleteFile();
        });

        // Edit mode
        owlWindow.editBtn.eventToggle.add((sender, args) -> onEditBtnPressed(owlWindow.editBtn.isToggled()));

        // Back to the files tree from search results
        owlWindow.backBtn.eventMouseClick.add((sender, args) -> {
            switchMode(true);
            // fillFilesTree(rootItem);
        });

        // Add key words
        owlWindow.filePreferencesBtn.eventMouseClick.add((sender, args) -> {
            owlWindow.filePreferencesContextMenu.show(sender, args);
        });

        owlWindow.miAddKeyWords.eventMouseClick.add((sender, args) -> {
            keyWordsInputDialog("New keywords", "add");
        });
        owlWindow.miShowAttachedContent.eventMouseClick.add((sender, args) -> {
            owlWindow.attachedContentSideArea.show();
        });

        // Delete key words
        owlWindow.miKWDelete.eventMouseClick.add((sender, args) -> {
            // System.out.println(sender.getItemName() + " " +
            // owlWindow.miKWDelete.getSender().getItemName());
            KeyWordItem kwSender = (KeyWordItem) owlWindow.miKWDelete.getSender();
            owlWindow.keyWordsStack.removeItem(kwSender);
            InterfaceSupport.removeKeyWord(workItem, kwSender.getText());
            setItemEdited(workItem, true);
        });

        // Delete all key words
        owlWindow.miKWUse.eventMouseClick.add((sender, args) -> {
            KeyWordItem kwSender = (KeyWordItem) owlWindow.miKWDelete.getSender();
            // owlWindow.keyWordsStack.clear();
            // InterfaceSupport.removeAllKeyWords(workItem);
            // InterfaceSupport.setEditing(workItem, true);
            if (args.button.equals(MouseButton.BUTTON_LEFT)) {
                searchFilesWithKeyWords(kwSender.getText());
            }
        });

        // Search
        owlWindow.searchBtn.eventMouseClick.add((sender, args) -> {
            keyWordsInputDialog("Find files with keywords", "search");
        });

        // Import existing file
        owlWindow.importBtn.eventMouseClick.add((sender, args) -> treeImportFile(workDirectory));
        owlWindow.miAddExist.eventMouseClick
                .add((sender, args) -> treeImportFile((FileEntryTreeItem) owlWindow.filesTree.getSelectedItem()));

        // New folder
        owlWindow.newFolderBtn.eventMouseClick.add((sender, args) -> treeNewFileOrFolder(workDirectory, "folder"));
        owlWindow.miNewFolder.eventMouseClick.add((sender,
                args) -> treeNewFileOrFolder((FileEntryTreeItem) owlWindow.filesTree.getSelectedItem(), "folder"));

        // Rename item
        owlWindow.miRename.eventMouseClick
                .add((sender, args) -> renameFile((FileEntryTreeItem) owlWindow.filesTree.getSelectedItem()));

        // Close app
        // owlWindow.titleBar.getCloseButton().eventMouseClick.clear();
        owlWindow.eventClose.clear();
        owlWindow.eventClose.add(() -> {
            // History.serialize(historyStore);
            historyStore.serialize();
            checkAndCloseWindow();

            // List<Tab> allTabs = owlWindow.workTabArea.getTabs();
            // for (Tab currentTab : allTabs) {
            // currentTab.getCloseButton().eventMouseClick.execute(currentTab, null);
            // }

            // if (owlWindow.workTabArea.getTabs().size() == 0) {
            // owlWindow.close();
            // }

            // if ((workItem != null) && (InterfaceSupport.isEditing(workItem))) {
            // MessageItem alertDialog = new MessageItem("", "Close Owls");
            // alertDialog.setMessageText("Do you want save changes in " +
            // workItem.getText() + " before close app?");
            // alertDialog.addUserButton(new ButtonCore("Don't save"), 2);
            // alertDialog.getOkButton().setText("Save");

            // alertDialog.onCloseDialog.add(() -> {
            // if (alertDialog.getResult()) { // Ok
            // treeSaveFile();
            // owlWindow.close();
            // } else {
            // if (alertDialog.getUserButtonResult() == 2) {// Don't save
            // owlWindow.close();
            // } else { // Cancel
            // // Do nothing
            // }
            // }
            // });

            // alertDialog.show(owlWindow);

            // } else {
            // // MessageItem alertDialog = new MessageItem("", "Close Owls");
            // // alertDialog.setMessageText("Close Own Libs?");
            // // alertDialog.getCancelButton().setVisible(false);
            // //
            // // alertDialog.onCloseDialog.add(() -> {
            // // if (alertDialog.getResult()) { //Ok
            // owlWindow.close();
            // // } else { //Cancel
            // // //Do nothing
            // // }
            // // });
            // //
            // // alertDialog.show(owlWindow.Handler);
            // }
        });

        owlWindow.settingsBtn.eventMouseClick.add((sender, args) -> {
            setsWindow.show();
        });

        owlWindow.eventOnStart.add(() -> {
            historyStore.deserialize();
            // historyStore = History.deserialize(historyStore._path);
            // for (String record : historyStore.getRecords()) {
            // historyAddRecordSetEvent(record);
            //// owlWindow.homePage.addItemToHistoryList(record);
            // }
        });

        owlWindow.refreshBtn.eventMouseClick.add((sender, args) -> {
            owlWindow.filesTree.clear();
            rootItem = InterfaceSupport.makeOwlItem("workspace", rootFolder, TreeItemType.BRANCH, null);
            fillFilesTree(rootItem);
            rootItem = InterfaceSupport.filesTreeMaker(rootFolder, rootItem);
            tmpItem = null;
            workDirectory = rootItem; // Копировать значения или оставить ссылку?
            itemToDefault();
        });
    }

    private void checkAndCloseWindow() {

        FileEntryTab selectedTab = getSelectedTab();

        if (selectedTab == null) {
            WindowManager.appExit();
            return;
        }

        if ((workItem != null) && (InterfaceSupport.isEditing(workItem))) {
            MessageItem alertDialog = new MessageItem("Do you want save changes in " + workItem.getText() + "?",
                    "Save file");
            alertDialog.addUserButton(new ButtonCore("Don't save"), 2);
            alertDialog.getOkButton().setText("Save");

            alertDialog.onCloseDialog.add(() -> {
                boolean res = fileUnsaved(selectedTab, alertDialog.getResult(), alertDialog.getUserButtonResult());
                if (res)
                    checkAndCloseWindow();
            });

            alertDialog.show(owlWindow);
        } else {
            // owlWindow.workTabArea.removeTab(tab);

            removeTab(selectedTab);
            checkAndCloseWindow();
        }
    }

    private void closeTabAlertDialog(FileEntryTab tab) {
        // TextArea selectedTextArea = getCurrentTextArea();
        // if (selectedTextArea != null &&
        // (InterfaceSupport.isEditing(selectedTextArea))) {
        if ((workItem != null) && (InterfaceSupport.isEditing(workItem))) {
            MessageItem alertDialog = new MessageItem("Do you want save changes in " + workItem.getText() + "?",
                    "Save file");
            alertDialog.addUserButton(new ButtonCore("Don't save"), 2);
            alertDialog.getOkButton().setText("Save");

            alertDialog.onCloseDialog.add(() -> {
                fileUnsaved(tab, alertDialog.getResult(), alertDialog.getUserButtonResult());
            });

            alertDialog.show(owlWindow);
        } else {
            removeTab(tab);
        }
    }

    private void switchMode(boolean mode) {
        // true - files tree mode, false - key words mode
        owlWindow.filesTree.setVisible(mode);
        owlWindow.kwResultsListBox.setVisible(!mode);
        owlWindow.kwResultsListBox.getArea().updateLayout();
    }

    private void fillFilesTree(FileEntryTreeItem sti) {
        // owlWindow.filesTree.clear();
        owlWindow.filesTree.setRootVisible(false);

        // owlWindow.backBtn.setVisible(!InterfaceSupport.getOwlMode());

        owlWindow.filesTree.addItem(sti);
        // owlWindow.treeStack.updateLayout();
    }

    void loadFileLauncher(FileEntryTreeItem loadingItem) {
        if (loadingItem.isDirectory()) {
            return;
        }

        FileEntryTab existingTab = getTabByOwlsTreeItem(loadingItem);

        if (existingTab != null) {
            owlWindow.workTabArea.selectTab(existingTab);
            checkNewSelectedTab();
            return;
        }

        // historyAddRecordSetEvent(loadingItem.getFullPath());
        historyStore.addRecord(loadingItem.getFullPath());

        FileEntryTab tab = addNewTabAndSelect(loadingItem);

        launchContinue(loadingItem, tab);

    }

    void historyAddRecordSetEvent(HistoryRecordItem hri, String path) {
        owlWindow.homePage.addItemToHistoryList(hri);
        hri.eventMouseClick.add((sender, args) -> {
            if (args.button == MouseButton.BUTTON_LEFT) {
                FileEntryTreeItem feti = InterfaceSupport.findFETIByAddress(path, rootItem);
                if (feti == null)
                    System.out.println("No such file");
                else
                    loadFileLauncher(feti);
            }
        });
    }

    private FileEntryTab getTabByOwlsTreeItem(FileEntryTreeItem oti) {
        FileEntryTab tab = null;
        for (Map.Entry<FileEntryTab, FileEntryTreeItem> entry : tabToOwls.entrySet()) {
            if (entry.getValue().equals(oti)) {
                tab = entry.getKey();
                // return tab;
                break;
            }
        }
        return tab;
    }

    private FileEntryTab addNewTabAndSelect(FileEntryTreeItem tabItem) {
        // создание и добавление вкладки
        FileEntryTab tab = ElementsFactory.getNewTab(tabItem.getText());
        owlWindow.workTabArea.addTab(tab);

        tab.getCloseButton().eventMouseClick.clear();
        // tab.getCloseButton().setPassEvents(false);
        tab.getCloseButton().eventMouseClick.add((sender, args) -> closeTabAlertDialog(tab));

        tab.eventMouseClick.add((sender, args) -> {
            checkNewSelectedTab();
        });

        tabToOwls.put(tab, tabItem);

        // owlWindow.workTabArea.updateLayout();

        TextArea textArea = ElementsFactory.getTextArea();
        // textArea.setText(""); // Переместить это в fillFields или
        // textArea.rewindText();
        owlWindow.workTabArea.addItemToTab(tab, textArea);
        // // одного вызова достаточно?

        // Check editing
        textArea.onTextChanged.add(() -> {
            if (workItem != null && owlWindow.editBtn.isToggled() && !InterfaceSupport.isEditing(workItem)) {
                setItemEdited(workItem, true);
            }
        });

        owlWindow.workTabArea.selectTab(tab);

        return tab;
    }

    private boolean fileUnsaved(FileEntryTab tab, boolean adres, int buttonId) { // OwlsTreeItem loadingItem,
        if (adres) { // save and close
            saveFile(); // сама меняет setEditing
        } else {
            if (buttonId == 2) { // close without saving
                InterfaceSupport.closeOwlFile(workItem);
            } else { // cancel closing
                // Do nothing
                return false;
            }
        }

        // if (owlWindow.editBtn.isToggled())
        // onEditBtnPressed(false);
        // launchContinue(loadingItem, tab);

        removeTab(tab);
        return true;
    }

    private void removeTab(FileEntryTab tab) {
        tab.removeTab();
        checkNewSelectedTab();
        tabToOwls.remove(tab);
    }

    private void checkNewSelectedTab() {
        FileEntryTab selectedTab = getSelectedTab();
        if (selectedTab == null)
            return;

        workItem = tabToOwls.get(selectedTab);
        workDirectory = (FileEntryTreeItem) workItem.getParentBranch();
        setLinksFlow();

        TextArea selectedTextArea = getCurrentTextArea();
        // System.out.println(selectedTextArea.isEditable());
        if (selectedTextArea == null) {
            owlWindow.editBtn.setToggled(false);
        } else {
            owlWindow.editBtn.setToggled(selectedTextArea.isEditable());
        }
    }

    private void launchContinue(FileEntryTreeItem loadingItem, FileEntryTab tab) {
        // areaCleaner();
        itemToDefault();

        // if (InterfaceSupport.isDirectory(loadingItem)) {
        // workDirectory = loadingItem;
        // } else {
        workItem = loadingItem;
        workDirectory = (FileEntryTreeItem) workItem.getParentBranch();
        setLinksFlow();

        // TextArea textArea = ElementsFactory.getTextArea();
        // owlWindow.workTabArea.addItemToTab(tab, textArea);
        // textArea.setText(InterfaceSupport.getFileText(workItem)); // Переместить это
        // в fillFields или
        getCurrentTextArea().setText(InterfaceSupport.getFileText(workItem));
        // // одного вызова достаточно?
        // textArea.rewindText();
        getCurrentTextArea().rewindText();
        // getCurrentTextArea().eventScrollUp.execute(getCurrentTextArea(), new MouseArgs());

        // // Check editing
        // textArea.onTextChanged.add(() -> {
        // if (workItem != null && owlWindow.editBtn.isToggled() &&
        // !InterfaceSupport.isEditing(workItem)) {
        // InterfaceSupport.setEditing(workItem, true);
        // }
        // });

        setItemEdited(workItem, false); // Загрузка сама считается изменением поля. Это нужно
        // обязательно
        // }

        fillFields();
    }

    // Save file
    private void saveFile() {
        if (workItem == null)
            return;

        InterfaceSupport.saveOwlFile(workItem, getCurrentTextArea().getText());
        setItemEdited(workItem, false, getTabByOwlsTreeItem(workItem));
    }

    private void saveAllFiles() {
        for (Map.Entry<FileEntryTab, FileEntryTreeItem> entry : tabToOwls.entrySet()) {
            if (InterfaceSupport.isEditing(entry.getValue())) {
                InterfaceSupport.saveOwlFile(entry.getValue(), getTextAreaByTab(entry.getKey()).getText());
                setItemEdited(entry.getValue(), false, entry.getKey()); // TODO что-нибудь с этим отстоем
            }
        }
    }

    private TextArea getCurrentTextArea() {
        TextArea selectedTextArea = null;
        FileEntryTab selectedTab = getSelectedTab();
        if (selectedTab == null)
            return selectedTextArea;

        return getTextAreaByTab(selectedTab);
    }

    private TextArea getTextAreaByTab(FileEntryTab tab) {
        TextArea textArea = null;
        List<InterfaceBaseItem> tabContent = owlWindow.workTabArea.getTabContent(tab);
        for (InterfaceBaseItem item : tabContent) {
            if (item instanceof TextArea) {
                textArea = (TextArea) item;
                break;
            }
        }
        return textArea;
    }

    private FileEntryTab getSelectedTab() {
        if (owlWindow.workTabArea.getTabs().size() == 0 || owlWindow.workTabArea.getSelectedTab() instanceof HomeTab) {
            return null;
        }

        return (FileEntryTab) owlWindow.workTabArea.getSelectedTab();
    }

    // Delete file
    private void treeDeleteFile() {
        if (tmpItem != null) {
            String title;
            String message;

            if (tmpItem.isDirectory()) {
                title = "Delete directory";
                message = "Delete directory \"" + tmpItem.getText() + "\" and all inner files?";
            } else {
                title = "Delete file";
                message = "Delete file \"" + tmpItem.getText() + "\"?";
            }

            MessageItem alertDialog = new MessageItem(message, title);

            alertDialog.onCloseDialog.add(() -> {
                if (alertDialog.getResult()) { // delete confirmed
                    // if (tmpItem.equals(workDirectory)) {
                    // // areaCleaner();
                    // } else if (tmpItem.equals(workItem)) {
                    // // areaCleaner();
                    // // itemToDefault();
                    // }

                    FileEntryTab existingTab = getTabByOwlsTreeItem(tmpItem);
                    if (existingTab != null) {
                        removeTab(existingTab);
                    }

                    InterfaceSupport.removeOwlFile(tmpItem); // workItem);
                }
            });

            alertDialog.show(owlWindow);
        }

    }

    private void itemToDefault() {
        // Поставить по умолчанию корневую директорию, файл по умолчанию сделать null,
        // поместить в поле адреса текующую
        // директорию, хотя, возможно, она оттуда и не удаляется
        if (workDirectory == null) {
            workDirectory = rootItem;
        }

        workItem = null;
    }

    // private void areaCleaner() {
    // // owlWindow.codeArea.clear();

    // owlWindow.keyWordsStack.clear();

    // if (owlWindow.editBtn.isToggled())
    // onEditBtnPressed(false);

    // owlWindow.setTitle("Owls. Your own libs");
    // }

    // private void changeEditMode(boolean state) {
    // TextArea selectedTextArea = getCurrentTextArea();

    // if (selectedTextArea == null)
    // return;

    // owlWindow.editBtn.setToggled(selectedTextArea.isEditable());

    // // if (state) {
    // // owlWindow.editBtn.setToggled(true);
    // // selectedTextArea.setEditable(true);
    // // } else {
    // // owlWindow.editBtn.setToggled(false);
    // // selectedTextArea.setEditable(false);
    // // }
    // }

    private void onEditBtnPressed(boolean state) {
        TextArea selectedTextArea = getCurrentTextArea();

        if (selectedTextArea == null)
            return;

        selectedTextArea.setEditable(state);
    }

    private void fillFields() {
        if (workItem != null) {
            owlWindow.setTitle("Own libs: " + InterfaceSupport.getFullPath(workItem));
        } else if (workDirectory != null) {
            owlWindow.setTitle("Own libs: " + InterfaceSupport.getFullPath(workDirectory));
        } else {
            owlWindow.setTitle("Owls. Your own libs");
        }
    }

    private void setLinksFlow() {
        owlWindow.keyWordsStack.clear();

        for (String s : workItem.getKeyWordsArr()) {
            KeyWordItem kwi = ElementsFactory.getKeyWord(s);
            kwi.eventMouseClick.add((sender, action) -> {
                owlWindow.keyWordsContextMenu.show(kwi, action);
            });
            kwi.eventMouseClick.add((sender, action) -> {
                if (action.button.equals(MouseButton.BUTTON_LEFT)) {
                    searchFilesWithKeyWords(kwi.getText());
                }

            });
            owlWindow.keyWordsStack.addItem(kwi);
        }
    }

    private boolean checkExistence(String name) {
        return checkExistence(name, workDirectory);
    }

    private boolean checkExistence(String name, FileEntryTreeItem parentItem) {
        String fp = InterfaceSupport.makeFullPath(parentItem, name);
        return (new File(fp).exists());
    }

    // Search
    private void searchFilesWithKeyWords(String keyWords) {
        switchMode(false);

        // owlWindow.kwResultsListBox.clear();

        List<InterfaceBaseItem> list = owlWindow.kwResultsListBox.getListContent();
        while (list.size() > 3) {
            owlWindow.kwResultsListBox.removeItem(list.get(3));
            list.remove(3);
        }

        owlWindow.kwResultLabel2.setText(keyWords);

        List<FileEntryTreeItem> kwFilesList = InterfaceSupport.findOwlFiles(keyWords, rootItem);
        for (FileEntryTreeItem oti : kwFilesList) {
            HorizontalStack hStack = new HorizontalStack();
            hStack.setSizePolicy(SizePolicy.EXPAND, SizePolicy.FIXED);
            hStack.setHeight(30);
            owlWindow.kwResultsListBox.addItems(hStack); // oti);
            ElementsFactory.searchResultItem(oti.getText(), hStack);

            hStack.eventMouseDoubleClick.add((s1, a1) -> loadFileLauncher(oti));
        }
    }

    private void treeImportFile(FileEntryTreeItem parentSti) {
        if (parentSti == null)
            return;

        if (InterfaceSupport.isDirectory(parentSti)) {
            workDirectory = parentSti;
        } else {
            workDirectory = (FileEntryTreeItem) parentSti.getParentBranch();
        }

        OpenEntryDialog opd = new OpenEntryDialog("Import File:", FileSystemEntryType.FILE,
                OpenEntryDialog.OpenDialogType.OPEN);
        // opd.addFilterExtensions("Text files (*.txt);*.txt", "Images (*.png, *.bmp,
        // *.jpg);*.png,*.bmp,*.jpg");
        opd.onCloseDialog.add(() -> {
            String path = opd.getResult();
            if (path != null) {
                File file = new File(path);
                FileEntryTreeItem sti = InterfaceSupport.importNewFile(file, workDirectory);
                if (sti == null) {
                    System.out.println("Файл с таким именем уже есть в этой директории, заменить?");
                } else {
                    // workItem = sti; //loadFileLauncher делает это.
                    loadFileLauncher(sti);
                }
            }
            // InterfaceSupport.refreshDir(workDirectory);
        });
        opd.show(owlWindow);
    }

    // New file or folder
    private void treeNewFileOrFolder(FileEntryTreeItem stitmp, String itemType) {
        // TODO: Нужна проверка на то, что там вообще что-то есть
        if (stitmp == null)
            return;

        if (InterfaceSupport.isDirectory(stitmp)) {
            workDirectory = stitmp;
        } else {
            workDirectory = (FileEntryTreeItem) stitmp.getParentBranch();
        }

        String defName, defType;
        if (itemType.equals("file")) {
            defName = "Untitled";
            defType = ".txt";
        } else { // folder
            defName = "New folder";
            defType = "";
        }

        String fn = defName + defType; // Надеюсь, прибавление пустой строки для папки никак не повлияет

        int inc = 0;
        while (checkExistence(fn)) {
            inc++;
            fn = defName + Integer.toString(inc) + defType;
        }

        callNameDialog("Enter new " + itemType + " name", itemType, fn);
    }

    // New file
    private void treeNewFile(String fnp) {
        // areaCleaner(); // ???

        // String fnp = checkNewName(stitmp, "Untitled", ".txt", "file");

        if (fnp != null) {
            workItem = InterfaceSupport.makeNewOwlFile(workDirectory, fnp);
            // workItem.getText();
            fillFields();
            addNewTabAndSelect(workItem);
            saveFile();
            // workItem = itemToDefault();

            // InterfaceSupport.setEditing(workItem, true);
            onEditBtnPressed(true); // Режим редактирования, но файл пока не требует сохранения
            checkNewSelectedTab();
            // InterfaceSupport.refreshDir(workDirectory);
        }

    }

    // New folder
    private void treeNewFolder(String fnp) {

        // String fnp = checkNewName(stitmp, "New folder", "", "folder");

        // workItem = intSupp.makeNewSTI(workDirectory, fn, fp);
        // fillFields();
        // treeSaveFile();
        if (fnp != null) {
            InterfaceSupport.makeNewOwlDir(workDirectory, fnp);
            // InterfaceSupport.refreshDir(workDirectory);
        }
    }

    private void callNameDialog(String dialogName, String label, String textField) {
        callNameDialog(dialogName, label, textField, true);
    }

    private void callNameDialog(String dialogName, String itemType, String textField, boolean isFirst) {
        // String out;
        // if (isFirst)
        // out = OwlDialogs.textInpDialog(dialogName, label, textField, true);
        // else
        // out = OwlDialogs.textInpDialog(dialogName, "This name already exists \nTry
        // another name", textField, false); //Над надписью нужно, наверное, поработать
        //
        // if ((out == null) || out.isEmpty()) return null; //Пустая строка - считаем,
        // что никто не хотел ничего создавать
        // if (checkExistence(out, parentItem)) {
        // out = callNameDialog(dialogName, label, textField, false, parentItem);
        // }
        // return out;

        FileEntryTreeItem parentItem = workDirectory;

        // TODO: different styles for the fist call and others
        InputDialog input = new InputDialog(dialogName, "Ok", textField);
        input.selectAll();
        input.onCloseDialog.add(() -> {
            String result = input.getResult();
            if (result == "") {
                // if (itemType.equals("file")) {
                //     treeNewFile(result);
                // } else if (itemType.equals("folder")) { // folder
                //     treeNewFolder(result);
                // } else { // rename
                //     renameFinished(result);
                // }
                return;
            }

            if (checkExistence(result, parentItem))
                callNameDialog(dialogName, itemType, textField, false);

            if (itemType.equals("file")) {
                treeNewFile(result);
            } else if (itemType.equals("folder")) { // folder
                treeNewFolder(result);
            } else { // rename
                renameFinished(result);
            }
            owlWindow.filesTree.sortBrunch(parentItem);
        });
        input.show(owlWindow);
    }

    private void keyWordsInputDialog(String title, String type) {
        InputDialog input = new InputDialog(title, "Ok", "");
        input.selectAll();
        input.onCloseDialog.add(() -> {
            String keyWords = input.getResult();

            if (type.equals("add")) {
                if ((keyWords != null) && (!keyWords.isEmpty())) {
                    keyWords = keyWords.replaceAll("\\s+", " ").trim();
                    if (InterfaceSupport.addKeyWords(keyWords, workItem)) {
                        setItemEdited(workItem, true);
                        setLinksFlow();
                    }
                }
            } else { // search
                if ((keyWords != null) && (!keyWords.isEmpty())) {
                    keyWords = keyWords.replaceAll("\\s+", " ").trim();
                    searchFilesWithKeyWords(keyWords);
                }
            }
        });
        input.show(owlWindow);

    }

    private void renameFile(FileEntryTreeItem renamedItem) {
        tmpItem = renamedItem;

        if (renamedItem.isRoot) {
            workDirectory = renamedItem;
        } else {
            workDirectory = (FileEntryTreeItem) renamedItem.getParentBranch();
        }

        String itemType;
        if (renamedItem.isDirectory())
            itemType = "directory";
        else
            itemType = "file";

        callNameDialog("Rename the " + itemType, "rename", renamedItem.getText());
    }

    private void renameFinished(String newName) {
        if (newName != null) {
            InterfaceSupport.renameOwlFile(tmpItem, newName);
        }
    }

    private void setItemEdited(FileEntryTreeItem itemEdit, boolean isEdited, FileEntryTab tab) {
        InterfaceSupport.setEditing(itemEdit, isEdited);
        changeTabState(tab, isEdited);
    }

    private void setItemEdited(FileEntryTreeItem itemEdit, boolean isEdited) {
        setItemEdited(itemEdit, isEdited, getSelectedTab());
    }

    void changeTabState(FileEntryTab tab, boolean state) {
        tab.setUnsaved(state);
    }
}