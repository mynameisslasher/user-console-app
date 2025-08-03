package org.example;

import org.example.model.User;
import org.example.service.UserService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
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

                    userService.createUser(name, email, age);
                    System.out.println("✅ Пользователь создан.");
                }
                case "2" -> {
                    List<User> users = userService.getAllUsers();
                    if (users.isEmpty()) {
                        System.out.println("Нет пользователей.");
                    } else {
                        System.out.println("Список пользователей:");
                        for (User user : users) {
                            System.out.println(user);
                        }
                    }
                }
                case "3" -> {
                    System.out.print("Введите ID пользователя для удаления: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    userService.deleteUserById(id);
                    System.out.println("✅ Пользователь удалён (если существовал).");
                }
                case "0" -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("❌ Неверный ввод.");
            }
        }
    }
}
