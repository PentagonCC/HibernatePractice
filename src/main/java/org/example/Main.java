package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.UserDaoImpl;
import org.example.models.User;
import org.example.services.UserService;

import java.util.Scanner;


public class Main {

    private static final Logger logger = LogManager.getLogger();

    static void menu() {
        System.out.println("""
                1.Добавить пользователя
                2.Найти пользователя
                3.Обновить пользователя
                4.Удалить пользователя
                0.ВЫХОД
                """);
    }

    public static void main(String[] args) {
        UserService userService = new UserService(new UserDaoImpl());
        User user;
        String input;
        Scanner scanner = new Scanner(System.in);
        boolean runFlag = true;
        while (runFlag) {
            menu();
            if (scanner.hasNextInt()) {
                int inputValue = scanner.nextInt();
                scanner.nextLine();
                switch (inputValue) {
                    case 1:
                        System.out.println("Введите последовательно Имя, email и возраст через запятую c пробелом");
                        input = scanner.nextLine();
                        try {
                            String[] userValues = input.split(", ");
                            if (checkNumber(userValues[2])) {
                                userService.createUser(userValues[0], userValues[1], Integer.parseInt(userValues[2]));
                            } else {
                                System.out.println("Возраст должен быть числом!");
                                logger.info("Введен некорректный возраст");
                            }
                        } catch (IndexOutOfBoundsException e) {
                            logger.error(e);
                        }
                        break;
                    case 2:
                        System.out.println("Введите id Пользователя");
                        input = scanner.nextLine();
                        if (checkNumber(input)) {
                            user = userService.findUser(Integer.parseInt(input));
                            if (user != null) {
                                System.out.println(user);
                            } else System.out.println("Пользователь не найден!");
                        } else {
                            System.out.println("Некорректный id пользователя");
                            logger.info("Введен некорректный id");
                        }
                        break;
                    case 3:
                        System.out.println("Введите id Пользователя, которого хотите обновить");
                        input = scanner.nextLine();
                        if (checkNumber(input)) {
                            user = userService.findUser(Integer.parseInt(input));
                            if (user != null) {
                                System.out.println("Введите последовательно новые - Имя, email и возраст через запятую c пробелом");
                                input = scanner.nextLine();
                                try {
                                    String[] newUserValues = input.split(", ");
                                    if (checkNumber(newUserValues[2])) {
                                        user.setName(newUserValues[0]);
                                        user.setEmail(newUserValues[1]);
                                        user.setAge(Integer.parseInt(newUserValues[2]));
                                        userService.updateUser(user);
                                    } else {
                                        System.out.println("Возраст должен быть числом!");
                                        logger.info("Введен некорректный возраст");
                                    }
                                } catch (IndexOutOfBoundsException e) {
                                    logger.error(e);
                                }
                            } else System.out.println("Пользователь не найден!");
                        } else {
                            System.out.println("Некорректный id пользователя");
                            logger.info("Введен некорректный id");
                        }
                        break;
                    case 4:
                        System.out.println("Введите id Пользователя, которого хотите удалить");
                        input = scanner.nextLine();
                        if (checkNumber(input)) {
                            user = userService.findUser(Integer.parseInt(input));
                            if (user != null) {
                                userService.deleteUser(user);
                            } else System.out.println("Пользователь не найден!");
                        } else {
                            System.out.println("Некорректный id пользователя");
                            logger.info("Введен некорректный id");
                        }
                        break;
                    case 0:
                        runFlag = false;
                }
            } else {
                System.out.println("Введите корректный пункт меню");
                scanner.next();
            }
        }

    }

    public static boolean checkNumber(String str) {
        if (str == null) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}