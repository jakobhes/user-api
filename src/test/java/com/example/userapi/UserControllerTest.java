package com.example.userapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;

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
        List<User> users = List.of(
                new User("John", "Wick", "wick@gmail.com", 30),
                new User("Alex", "Miller", "alex@yahoo.com", 34)
        );
        when(userService.getAllUsers()).thenReturn(users);

        // when
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(users);
        verify(userService).getAllUsers();
    }

    @Test
    public void testGetUserById() {
        // given
        User user = new User("Josephine", "Smith", "smith.josephine@web.de", 57);
        when(userService.getUserById(1)).thenReturn(user);

        // when
        ResponseEntity<User> response = userController.getUserById(1);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user);
        verify(userService).getUserById(1);
    }


    @Test
    public void testCreateUser() {
        // given
        User user = new User("Max", "Wesson", "wesson@gmail.com", 16);

        when(userService.createUser(user)).thenReturn(user);

        BeanPropertyBindingResult result = new BeanPropertyBindingResult(user, "user");

        // when
        ResponseEntity<User> response = userController.createUser(user, result);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).hasPath("/api/users/" + user.getId());
        assertThat(response.getBody()).isEqualTo(user);
        verify(userService).createUser(user);
    }




    @Test
    public void testUpdateUser() {
        // given
        User userDetails = new User("Moritz", "Bandit", "banditen.moritz@yahoo.com", 22);
        User user = new User("John", "Wick", "wickedjohn@mydomain.com", 30);
        when(userService.updateUser(1, userDetails)).thenReturn(user);

        BeanPropertyBindingResult result = new BeanPropertyBindingResult(userDetails, "userDetails");


        // when
        ResponseEntity<User> response = userController.updateUser(1, userDetails, result);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(user);
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