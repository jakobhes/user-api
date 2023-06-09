package com.example.userapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        User user = new User("Max", "Mustermann", "mustermann@gmail.com", 30);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        Assertions.assertEquals(user, createdUser);
    }

    @Test
    public void testGetUserById() {
        User user = new User("Erika", "Musterfrau", "musterfrau@yahoo.com", 30);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        Assertions.assertEquals(user, foundUser);
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User("Tim", "Rancher", "tim.rancher@example.com", 67);
        User user2 = new User("Mona", "Lisa", "smile@art.com", 66);
        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> foundUsers = userService.getAllUsers();

        Assertions.assertEquals(userList, foundUsers);
    }

    @Test
    public void testUpdateUser() {
        User user = new User("Corinna", "Meier", "corinna.meier@gmail.com", 77);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = new User("Peter", "Lustig", "peter.lustig@gmail.com", 56);
        userService.updateUser(1L, updatedUser);

        Assertions.assertEquals("Peter", updatedUser.getFirstName());
        Assertions.assertEquals("Lustig", updatedUser.getLastName());
        Assertions.assertEquals("peter.lustig@gmail.com", updatedUser.getEmail());
        Assertions.assertEquals(56, updatedUser.getAge());
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        User userToUpdate = new User("Maria", "Rilke", "mariarilke@web.de", 23);
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(userToUpdate.getId(), userToUpdate));
    }

    @Test
    public void testDeleteUser() {
        User user = new User("Anna", "Bar", "bar@gmail.com", 30);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        // Verify that the deleteById method of the repository is called with the correct user ID
        Mockito.verify(userRepository, Mockito.times(1)).deleteById(1L);
    }


    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }
}
