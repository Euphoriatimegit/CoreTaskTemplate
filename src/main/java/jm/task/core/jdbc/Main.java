package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Misha", "Kamaz", (byte) 28);
        userService.saveUser("Vitia", "Raketa", (byte) 26);
        userService.saveUser("Grisha", "Sapyn", (byte) 22);
        userService.saveUser("Roma", "Baikanyr", (byte) 34);

        System.out.println(userService.getAllUsers());

        userService.cleanUsersTable();

        userService.dropUsersTable();


    }
}
