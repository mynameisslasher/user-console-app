package org.example;

import org.example.model.User;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("Starting application...");
        UserService userService = new UserService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Меню ===");
            System.out.println("1 - Создать пользователя");
            System.out.println("2 - Показать всех пользователей");
            System.out.println("3 - Удалить пользователя по ID");
            System.out.println("0 - Выход");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Введите имя: ");
                    String name = scanner.nextLine();
                    System.out.print("Введите email: ");
                    String email = scanner.nextLine();
                    System.out.print("Введите возраст: ");
                    int age = Integer.parseInt(scanner.nextLine());

                    try {
                        userService.createUser(name, email, age);
                        System.out.println("✅ Пользователь создан.");
                    } catch (RuntimeException e) {
                        System.out.println("❌ Не удалось создать пользователя: " + e.getMessage());
                    }
                }
                case "2" -> {
                    List<User> users = userService.getAllUsers();
                    if (users.isEmpty()) {
                        System.out.println("Нет пользователей.");
                    } else {
                        users.forEach(System.out::println);
                    }
                }
                case "3" -> {
                    System.out.print("Введите ID пользователя для удаления: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    try {
                        userService.deleteUserById(id);
                        System.out.println("✅ Пользователь удалён (если существовал).");
                    } catch (RuntimeException e) {
                        System.out.println("❌ Не удалось удалить: " + e.getMessage());
                    }
                }
                case "0" -> {
                    log.info("Exiting application.");
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("❌ Неверный ввод.");
            }
        }
    }
}
