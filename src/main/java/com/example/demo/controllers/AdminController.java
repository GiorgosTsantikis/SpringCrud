package com.example.demo.controllers;

import com.example.demo.entities.Role;
import com.example.demo.entities.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@CrossOrigin(origins="http://localhost:5173")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/hello")
    public ResponseEntity<String> sayHello(JwtAuthenticationToken token){
        return ResponseEntity.ok("hello");
    }

    @PostMapping("/makeAdmin/{username}")
    public ResponseEntity<User> makeUserAdmin(JwtAuthenticationToken token, @PathVariable String username){
        System.out.println(username);
        try{
            User user=userService.findByUsername(username);
            if(user!=null){
                user.setRole(Role.ROLE_ADMIN);
                userService.updateUser(user);
                return ResponseEntity.ok(user);
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return ResponseEntity.status(403).body(null);
    }


    @GetMapping("/allUsers")
    public List<User> getUsers(){
        var users= userService.getAllUsers();
        users.forEach(x-> x.getRole());
        return users;
    }
}
