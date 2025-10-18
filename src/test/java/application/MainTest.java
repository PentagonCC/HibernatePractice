package application;

import org.example.application.Main;
import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }


    @Test
    void checkNumber_correctNumber() {
        assertTrue(Main.checkNumber("123"));
        assertTrue(Main.checkNumber("0"));
        assertTrue(Main.checkNumber("-456"));
    }

    @Test
    void checkNumber_incorrectNumber() {
        assertFalse(Main.checkNumber("abc"));
        assertFalse(Main.checkNumber("123abc"));
        assertFalse(Main.checkNumber(""));
    }

    @Test
    void checkNumber_null() {
        assertFalse(Main.checkNumber(null));
    }

    @Test
    void createUserFromInputData_correctData() {
        String input = "fefe, nnhgng@list.com, 25";
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        assertDoesNotThrow(Main::createUserFromInputData);

        assertTrue(outContent.toString().contains("Введите последовательно Имя, email и возраст"));
    }

    @Test
    void createUserFromInputData_incorrectAge() {
        String input = "veve, veve@gmail.com, abc";
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        assertDoesNotThrow(Main::createUserFromInputData);

        assertTrue(outContent.toString().contains("Возраст должен быть числом!"));
    }

    @Test
    void createUserFromInputData_withoutAge() {
        String input = "fefe, fege@mail.ru";
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        assertDoesNotThrow(Main::createUserFromInputData);
    }

    @Test
    void searchUserById_incorrectId() {
        String input = "wvve";
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        User result = Main.searchUserById();

        assertNull(result);
        assertTrue(outContent.toString().contains("Некорректный id пользователя"));
    }

    @Test
    void searchUserById_correctId() {
        UserDao userDao = new UserDaoImpl();
        User user = userDao.create(new User("cdca", "cvevf@fd.ri", 324, LocalDateTime.now()));
        String input = String.valueOf(user.getId());
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        User result = Main.searchUserById();
        userDao.delete(user);

        assertNotNull(result);
    }

    @Test
    void updateUserById_userIncorrectAge() {
        UserDao userDao = new UserDaoImpl();
        User user = userDao.create(new User("cdca", "cvevf@fd.ri", 324, LocalDateTime.now()));
        String id = String.valueOf(user.getId());
        String input = id + "\nfefe, nnhgng@list.com, мввыаыв";
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        assertDoesNotThrow(Main::updateUserById);
        userDao.delete(user);

        assertTrue(outContent.toString().contains("Возраст должен быть числом!"));
    }

    @Test
    void updateUserById_userFound() {
        UserDao userDao = new UserDaoImpl();
        User user = userDao.create(new User("cdca", "cvevf@fd.ri", 324, LocalDateTime.now()));
        String id = String.valueOf(user.getId());
        String input = id + "\nfefe, nnhgng@list.com, 25";
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        assertDoesNotThrow(Main::updateUserById);

        assertNotEquals(user, userDao.findById(user.getId()));
        userDao.delete(user);

        assertTrue(outContent.toString().contains("Пользователь обновлен!"));
    }


    @Test
    void updateUserById_UserNotFound() {
        String input = "1\nfefe, nnhgng@list.com, 25";
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        assertDoesNotThrow(Main::updateUserById);

        assertTrue(outContent.toString().contains("Пользователь не найден!"));
    }

    @Test
    void deleteUserById_userNotFound() {
        String input = "1";
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        assertDoesNotThrow(Main::deleteUserById);

        String output = outContent.toString();
        assertTrue(output.contains("Пользователь не найден!"));
    }

    @Test
    void deleteUserById_userFound() {
        UserDao userDao = new UserDaoImpl();
        User user = userDao.create(new User("cdca", "cvevf@fd.ri", 324, LocalDateTime.now()));
        String input = String.valueOf(user.getId());
        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        assertDoesNotThrow(Main::deleteUserById);

        String output = outContent.toString();
        assertTrue(output.contains("Пользователь удален"));
    }

    @Test
    void testMenu() {
        Main.menu();
        String output = outContent.toString();
        assertTrue(output.contains("Добавить пользователя"));
        assertTrue(output.contains("Найти пользователя"));
        assertTrue(output.contains("Обновить пользователя"));
        assertTrue(output.contains("Удалить пользователя"));
        assertTrue(output.contains("ВЫХОД"));
    }

    @Test
    void mainTest() {
        String input = "1\n\n" +
                "2\n\n" +
                "3\n\n" +
                "4\n\n" +
                "0\n";

        Main.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        Thread thread = new Thread(() -> {
            Main.main(new String[]{});
        });
        thread.start();

        try {
            thread.join(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String output = outContent.toString();
        assertTrue(output.contains("Добавить пользователя"));
        assertTrue(output.contains("Найти пользователя"));
        assertTrue(output.contains("Обновить пользователя"));
        assertTrue(output.contains("Удалить пользователя"));
        assertTrue(output.contains("ВЫХОД"));
    }


}