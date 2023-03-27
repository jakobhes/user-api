package com.example.userapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        // given
        List<User> users = new ArrayList<>();
        User user1 = new User("John", "Wick", "wick@gmail.com", 30);
        User user2 = new User("Alex", "Miller", "alex@yahoo.com", 34);
        users.add(user1);
        users.add(user2);

        // mock userService
        when(userService.getAllUsers()).thenReturn(users);

        // when
        List<User> result = userController.getAllUsers();

        // then
        assertThat(result.size()).isEqualTo(users.size());
        assertThat(result.get(0).getFirstName()).isEqualTo(users.get(0).getFirstName());
        assertThat(result.get(0).getLastName()).isEqualTo(users.get(0).getLastName());
        assertThat(result.get(1).getFirstName()).isEqualTo(users.get(1).getFirstName());
        assertThat(result.get(1).getLastName()).isEqualTo(users.get(1).getLastName());
        verify(userService).getAllUsers();
    }

    @Test
    public void testGetUserById() {
        // given
        User user = new User("Josephine", "Smith", "smith.josephine@web.de", 57);

        // mock userService
        when(userService.getUserById(1)).thenReturn(user);

        // when
        User result = userController.getUserById(1);

        // then
        assertThat(result).isEqualTo(user);
        verify(userService).getUserById(1);
    }

    @Test
    public void testCreateUser() {
        // given
        User user = new User("Max", "Wesson", "wesson@gmail.com", 16);

        // mock userService
        when(userService.createUser(user)).thenReturn(user);

        // when
        User result = userController.createUser(user);

        // then
        assertThat(result).isEqualTo(user);
        verify(userService).createUser(user);
    }

    @Test
    public void testUpdateUser() {
        // given
        User userDetails = new User("Moritz", "Bandit", "banditen.moritz@yahoo.com", 22);
        User user = new User("John", "Wick", "wickedjohn@mydomain.com", 30);

        // mock userService
        when(userService.updateUser(1, userDetails)).thenReturn(user);

        // when
        User result = userController.updateUser(1, userDetails);

        // then
        assertThat(result).isEqualTo(user);
        verify(userService).updateUser(1, userDetails);
    }
    @Test
    public void testDeleteUserNotFound() {
        // mock userService to throw UserNotFoundException
        doThrow(UserNotFoundException.class).when(userService).deleteUser(1);

        // when
        try {
            userController.deleteUser(1);
            fail("Expected UserNotFoundException to be thrown");
        } catch (UserNotFoundException ex) {
            // then
            // verify that userService.deleteUser was called with the correct id
            verify(userService, times(1)).deleteUser(1);
        }
    }

    @Test
    public void testDeleteUserSuccess() {
        // when
        userController.deleteUser(1);

        // then
        // verify that userService.deleteUser was called with the correct id
        verify(userService, times(1)).deleteUser(1);
    }
}