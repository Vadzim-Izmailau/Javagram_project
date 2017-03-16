import contacts.ContactList;
import gui.Confirmation;
import gui.InputNumber;
import gui.MainScreen;
import gui.Registration;
import messages.MessagesForm;
import misc.BlueButton;
import misc.Helper;
import org.javagram.dao.*;
import org.javagram.dao.Dialog;
import org.javagram.dao.proxy.TelegramProxy;
import org.javagram.dao.proxy.changes.UpdateChanges;
import overlays.*;
import resources.Images;
import undecorated.BaseForm;
import undecorated.ComponentResizerAbstract;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by i5 on 22.04.2016.
 */
public class MyFrame extends JFrame
{
    private TelegramDAO telegramDAO;
    private TelegramProxy telegramProxy;
    private BaseForm baseForm;
    private InputNumber phoneNumber = new InputNumber();
    private Confirmation confirmCode = new Confirmation();
    private Registration registr = new Registration();
    private MainScreen mainScreen = new MainScreen();
    private ContactList contactList = new ContactList();

    private ProfileSettings profileSettings = new ProfileSettings();
    private AddContact addContact = new AddContact();
    private AddButton addButton = new AddButton();
    private EditContact editContact = new EditContact();
    private MyOverlayDialog overlayDialog = new MyOverlayDialog(mainScreen, profileSettings, addContact, editContact, addButton);
    private static final int MAIN_SCREEN = -1, PROFILE_FORM = 0, ADD_FORM = 1, EDIT_FORM = 2;

    private Timer timer;
    private int messagesFrozen;

    {
        setTitle("Javagram");
        baseForm = new BaseForm(this, ComponentResizerAbstract.KEEP_RATIO_CENTER);
        changeContentPanel(phoneNumber);
        mainScreen.setContactPanel(contactList);
        setContentPane(baseForm.getRootBasePanel());
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setMaximumSize(new Dimension(1000, 800));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        contactList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting() || messagesFrozen != 0)
                    return;
                if (telegramProxy == null) {
                    displayDialog(null);
                } else {
                    displayDialog(contactList.getSelectedValue());
                }
            }
        });

        timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkForUpdates(false);
            }
        });
        timer.start();

        // фокус и закрытие
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                phoneNumber.getNumber().requestFocusInWindow();
            }
            @Override
            public void windowClosing(WindowEvent e) {
                if(showQuestionMessage("Вы действительно хотите выйти?", "Вопрос"))
                    deAuth();
            }
        });

        // кнопка Продолжить на форме ввода номера телефона
        phoneNumber.getProceed().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputNumber();
            }
        });

        // нажатие Enter на форме ввода номера телефона
        phoneNumber.getNumber().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    inputNumber();
            }
        });

        // кнопка Завершить на форме регистрации
        registr.getComplete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputName();
            }
        });

        // нажатие Enter на форме регистрации
        registr.getFirstName().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    registr.getSurname().requestFocusInWindow();
            }
        });
        registr.getSurname().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    inputName();
            }
        });

        // кнопка Продолжить на форме ввода кода подтверждения
        confirmCode.getProceed().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputCode();
            }
        });

        // нажатие Enter на форме ввода кода подтверждения
        confirmCode.getPassField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    inputCode();
            }
        });

        //отправка сообщения
        mainScreen.getSendMessage().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Person person = contactList.getSelectedValue();
                String text = mainScreen.getMessageText().trim();
                if (telegramProxy != null && person != null && !text.isEmpty()) {
                    try {
                        telegramProxy.sendMessage(person, text);
                        mainScreen.setMessageText("");
                        checkForUpdates(true);
                    } catch (Exception e) {
                        showErrorMessage("Ошибка отправки сообщения", "Ошибка!");
                    }
                }
            }
        });

        //настройки профиля
        mainScreen.getSettingButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileSettings.setTelegramProxy(telegramProxy);
                overlayDialog.setIndex(PROFILE_FORM);
            }
        });
        profileSettings.getBack().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                overlayDialog.setIndex(MAIN_SCREEN);
            }
        });
        profileSettings.getExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToStart();
            }
        });

        //добавление контакта
        addButton.getAddButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Person person = contactList.getSelectedValue();
                if(person instanceof KnownPerson && !(person instanceof Contact))
                    addContact.setPhoneNumber(((KnownPerson) person).getPhoneNumber());
                overlayDialog.setIndex(ADD_FORM);
            }
        });
        addContact.getBack().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                overlayDialog.setIndex(MAIN_SCREEN);
            }
        });
        addContact.getAddContact().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryAddContact(addContact.getContactData());
            }
        });

        //редактирование контакта
        mainScreen.getEditButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Person person = contactList.getSelectedValue();
                if(person instanceof Contact) {
                    editContact.setContactData(new ContactData((Contact) person));
                    editContact.setPhoto(Helper.getPhoto(telegramProxy, person, false, true));
                    overlayDialog.setIndex(EDIT_FORM);
                }
            }
        });
        editContact.getBack().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                overlayDialog.setIndex(MAIN_SCREEN);
            }
        });
        editContact.getSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    tryUpdateContact(editContact.getContactData());
                } catch (ArrayIndexOutOfBoundsException e1) {
                    showErrorMessage("Пожалуйста, введите имя и/или фамилию", "Ошибка!");
                }
            }
        });
        editContact.getDelete().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(showQuestionMessage("Вы действительно хотите удалить пользователя?", "Удаление пользователя"))
                    tryDeleteContact(editContact.getContactData());
            }
        });

        //поиск
        mainScreen.getSearchButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchFor(mainScreen.getSearchText());
            }
        });
    }

    public MyFrame(TelegramDAO telegramDAO) throws Exception {
        this.telegramDAO = telegramDAO;
    }

    private void changeContentPanel(Container contentPanel) {
        baseForm.setContentPanel(contentPanel);
    }
    private void inputNumber() {
        try {
            String number = getPhoneNumber();
            try {
                telegramDAO.acceptNumber(number);
                if (telegramDAO.canSignIn())
                    getCode(number);
                else
                    openRegistrationForm();
            } catch (IOException e1) {
                showWarningMessage("Вы неверно ввели номер телефона", "Warning");
            }
        } catch (NullPointerException e){
            showWarningMessage("Вы неверно ввели номер телефона", "Warning");
        }
    }
    private String getPhoneNumber() {
        return (phoneNumber.getFormattedTextValue()).replaceAll("\\D+", "");
    }
    private void getCode(String number) {
        try {
            telegramDAO.sendCode();
            confirmCode.getNumber().setText("+" + number);
            changeContentPanel(confirmCode);
            confirmCode.getPassField().requestFocusInWindow();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    private void inputCode() {
        try {
            if (telegramDAO.canSignIn()) {
                telegramDAO.signIn(confirmCode.getPass());
                openMainScreen();
            }
            else {
                String firstName = registr.getFirstName().getText().trim();
                String lastName = registr.getSurname().getText().trim();
                telegramDAO.signUp(confirmCode.getPass(), firstName, lastName);
                openMainScreen();
            }
        } catch (IOException e1) {
            showErrorMessage("Неверный код", "Ошибка!");
        }
    }
    private void inputName() {
        if (registr.getFirstName().getText().trim().equals("Имя") ||
                registr.getSurname().getText().trim().equals("Фамилия")) {
            showWarningMessage("Вы ввели неполные данные. Введите имя и фамилию", "Warning");
        }
        else {
            try {
                String number = getPhoneNumber();
                getCode(number);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    private void backToStart() {
        try {
            destroyTelegramProxy();
            this.confirmCode.clear();
            this.phoneNumber.clear();
            overlayDialog.setIndex(MAIN_SCREEN);
            changeContentPanel(phoneNumber);
            phoneNumber.getNumber().requestFocusInWindow();
            if(!telegramDAO.logOut())
                throw new RuntimeException("Отказ сервера разорвать соединение");
        } catch (Exception e) {
            showErrorMessage("Продолжение работы не возможно", "Критическая ошибка!");
            abort(e);
        }
    }
    private void deAuth() {
        telegramDAO.close();
        System.exit(0);
    }
    private void abort(Throwable e) {
        if(e != null)
            e.printStackTrace();
        else
            System.err.println("Unknown Error");
        telegramDAO.close();
        System.exit(-1);
    }
    private void openRegistrationForm() {
        changeContentPanel(registr);
        registr.getFirstName().requestFocusInWindow();
    }
    private void openMainScreen() {
        changeContentPanel(overlayDialog);
        addButton.setVisible(true);
        createTelegramProxy();
    }
    private void createTelegramProxy() {
        telegramProxy = new TelegramProxy(telegramDAO);
        updateTelegramProxy();
    }
    private void destroyTelegramProxy() {
        telegramProxy = null;
        updateTelegramProxy();
    }
    private void updateTelegramProxy() {
        messagesFrozen++;
        try {
            contactList.setTelegramProxy(telegramProxy);
            contactList.setSelectedValue(null);
            createMessagesForm();
            displayDialog(null);
            displayMe(telegramProxy != null ? telegramProxy.getMe() : null);
        } finally {
            messagesFrozen--;
        }
        mainScreen.revalidate();
        mainScreen.repaint();
    }
    private MessagesForm createMessagesForm() {
        MessagesForm messagesForm = new MessagesForm(telegramProxy);
        mainScreen.setMessagePanel(messagesForm);
        mainScreen.revalidate();
        mainScreen.repaint();
        return messagesForm;
    }
    private void displayDialog(Person person) {
        try {
            MessagesForm messagesForm = getMessagesForm();
            messagesForm.display(person);
            displayPerson(person);
            revalidate();
            repaint();
        } catch (Exception e) {
            showErrorMessage("Нет связи с сервером", "Ошибка!");
        }
    }
    private MessagesForm getMessagesForm() {
        if (mainScreen.getMessagePanel() instanceof MessagesForm)
            return (MessagesForm) mainScreen.getMessagePanel();
        else
            return createMessagesForm();
    }
    private void updateContacts() {
        messagesFrozen++;
        try {
            Person person = contactList.getSelectedValue();
            contactList.setTelegramProxy(telegramProxy);
            contactList.setSelectedValue(person);
        } finally {
            messagesFrozen--;
        }
    }
    private void updateMessages() {
        displayDialog(contactList.getSelectedValue());
        mainScreen.revalidate();
        mainScreen.repaint();
    }
    protected void checkForUpdates(boolean force) {
        if (telegramProxy != null) {
            UpdateChanges updateChanges = telegramProxy.update(force ? TelegramProxy.FORCE_SYNC_UPDATE :
                    TelegramProxy.USE_SYNC_UPDATE);
            int photosChangedCount = updateChanges.getLargePhotosChanged().size() +
                    updateChanges.getSmallPhotosChanged().size() + updateChanges.getStatusesChanged().size();
            if (updateChanges.getListChanged())
                updateContacts();
            else if (photosChangedCount != 0)
                contactList.repaint();

            Person currentPerson = getMessagesForm().getPerson();
            Person targetPerson = contactList.getSelectedValue();
            Dialog currentDialog = currentPerson != null ? telegramProxy.getDialog(currentPerson) : null;
            if (!Objects.equals(targetPerson, currentPerson) ||
                    updateChanges.getDialogsToReset().contains(currentDialog) ||
                    //updateChanges.getDialogsChanged().getChanged().containsKey(currentDialog) ||
                    updateChanges.getDialogsChanged().getDeleted().contains(currentDialog))
                updateMessages();
            else if(updateChanges.getPersonsChanged().getChanged().containsKey(currentPerson) ||
                    updateChanges.getSmallPhotosChanged().contains(currentPerson) ||
                    updateChanges.getLargePhotosChanged().contains(currentPerson))
                displayPerson(targetPerson);
            if(updateChanges.getPersonsChanged().getChanged().containsKey(telegramProxy.getMe()) ||
                    updateChanges.getSmallPhotosChanged().contains(telegramProxy.getMe()) ||
                    updateChanges.getLargePhotosChanged().contains(telegramProxy.getMe()))
                displayMe(telegramProxy.getMe());
        }
    }
    private void searchFor(String text) {
        text = text.trim();
        if(text.isEmpty()) {
            return;
        }
        String[] words = text.toLowerCase().split("\\s+");
        List<Person> persons = telegramProxy.getPersons();
        Person person = contactList.getSelectedValue();
        person = searchFor(words, persons, person);
        contactList.setSelectedValue(person);
        if(person == null)
            showInformationMessage("Ничего не найдено", "Поиск");
    }
    private static Person searchFor(String[] words, List<? extends Person> persons, Person current) {
        int currentIndex = persons.indexOf(current);
        for(int i = 1; i <= persons.size(); i++) {
            int index = (currentIndex + i) % persons.size();
            Person person = persons.get(index);
            if(contains(person.getFirstName().toLowerCase(), words)
                    || contains(person.getLastName().toLowerCase(), words)) {
                return person;
            }
        }
        return null;
    }
    private static boolean contains(String text, String... words) {
        for(String word : words) {
            if(text.contains(word))
                return true;
        }
        return false;
    }
    private boolean tryAddContact(ContactData data) {
        String phone = data.getClearedNumber();
        if(phone.isEmpty()) {
            showWarningMessage("Пожалуйста, введите номер телефона", "Ошибка");
            return false;
        }
        if(data.getName().isEmpty() || data.getSurname().isEmpty()) {
            showWarningMessage("Пожалуйста, введите имя и/или фамилию", "Ошибка");
            return false;
        }
        for(Person person : telegramProxy.getPersons()) {
            if(person instanceof Contact) {
                if(((Contact) person).getPhoneNumber().replaceAll("\\D+", "").equals(phone)) {
                    showWarningMessage("Контакт с таким номером уже существует", "Ошибка");
                    return false;
                }
            }
        }
        if(!telegramProxy.importContact(data.getNumber(), data.getName(), data.getSurname())) {
            showErrorMessage("Ошибка на сервере при добавлении контакта", "Ошибка!");
            return  false;
        }
        overlayDialog.setIndex(MAIN_SCREEN);
        checkForUpdates(true);
        return true;
    }
    private boolean tryUpdateContact(ContactData data) {
        if(data.getName().isEmpty() || data.getSurname().isEmpty()) {
            showWarningMessage("Пожалуйста, введите имя и/или фамилию", "Ошибка");
            return false;
        }
        if(!telegramProxy.importContact(data.getNumber(), data.getName(), data.getSurname())) {
            showErrorMessage("Ошибка на сервере при изменении контакта", "Ошибка!");
            return  false;
        }
        overlayDialog.setIndex(MAIN_SCREEN);
        checkForUpdates(true);
        return true;
    }
    private boolean tryDeleteContact(ContactData data) {
        int id = data.getId();
        if(!telegramProxy.deleteContact(id)) {
            showErrorMessage("Ошибка на сервере при удалении контакта", "Ошибка!");
            return  false;
        }
        overlayDialog.setIndex(MAIN_SCREEN);
        checkForUpdates(true);
        return true;
    }
    private void displayMe(Me me) {
        if(me == null) {
            mainScreen.setMeText(null);
            mainScreen.setMePhoto(null);
        } else {
            mainScreen.setMeText(me.getFirstName() + " " + me.getLastName());
            mainScreen.setMePhoto(Helper.getPhoto(telegramProxy, me, true, true));
        }
    }
    private void displayPerson(Person person) {
        if(person == null) {
            mainScreen.setUserText(null);
            mainScreen.setUserPhoto(null);
            mainScreen.setEditEnabled(false);
        } else {
            mainScreen.setUserText(person.getFirstName() + " " + person.getLastName());
            mainScreen.setUserPhoto(Helper.getPhoto(telegramProxy, person, true, true));
            mainScreen.setEditEnabled(person instanceof Contact);
        }
    }

    private BlueButton[] okButton = BlueButton.createButtons(JOptionPane.DEFAULT_OPTION);
    private BlueButton[] yesNoButtons = BlueButton.createButtons(JOptionPane.YES_NO_OPTION);
    private void showErrorMessage(String text, String title) {
        BaseForm.showDialog(this, text, title, JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, Images.getErrorIcon(),
                okButton, okButton[0]);
    }
    private void showWarningMessage(String text, String title) {
        BaseForm.showDialog(this, text, title, JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, Images.getWarningIcon(),
                okButton, okButton[0]);
    }
    private void showInformationMessage(String text, String title) {
        BaseForm.showDialog(this, text, title, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, Images.getInfoIcon(),
                okButton, okButton[0]);
    }
    private boolean showQuestionMessage(String text, String title) {
        return BaseForm.showDialog(this, text, title, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, Images.getQuestionIcon(),
                yesNoButtons, yesNoButtons[0]) == 0;
    }
}
