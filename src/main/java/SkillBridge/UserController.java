package SkillBridge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    UserRepository ur;


    @GetMapping("/h")
    public String home() {

        return "SkillBridge Backend Running";
    }


    @PostMapping("/register")
    public String register(
            @RequestBody Users u) {

        if (u.getUsername() == null ||
                u.getUsername().trim().isEmpty()) {

            return "Username is required";
        }


        if (u.getPassword() == null ||
                u.getPassword().trim().isEmpty()) {

            return "Password is required";
        }


        if (u.getEmail() == null ||
                u.getEmail().trim().isEmpty()) {

            return "Email is required";
        }


        if (u.getRole() == null ||
                u.getRole().trim().isEmpty()) {

            u.setRole("STUDENT");
        }


        String role =
                u.getRole().toUpperCase();


        if (role.equals("ADMIN")) {

            return "Admin registration is not allowed";
        }


        if (!role.equals("STUDENT") &&
                !role.equals("MENTOR")) {

            return "Invalid role";
        }


        u.setRole(role);


        String username =
                u.getUsername().trim();


        String email =
                u.getEmail().trim();


        if (ur.existsByUsername(
                username
        )) {

            return "Username Already Exists";
        }


        if (ur.existsByEmail(
                email
        )) {

            return "Email Already Exists";
        }


        u.setUsername(
                username
        );


        u.setEmail(
                email
        );


        ur.save(u);


        return "Registration Successful";
    }


    @PostMapping("/login")
    public Map<String, String> login(
            @RequestBody LoginData l) {

        Users user =
                ur.findByUsername(
                        l.getUsername()
                );


        Map<String, String> response =
                new HashMap<>();


        if (
                user != null &&
                user.getPassword() != null &&
                user.getPassword()
                        .equals(l.getPassword())
        ) {

            response.put(
                    "message",
                    "Login Successful"
            );


            response.put(
                    "username",
                    user.getUsername()
            );


            response.put(
                    "role",
                    user.getRole()
            );


            response.put(
                    "email",
                    user.getEmail()
            );

        } else {

            response.put(
                    "message",
                    "Invalid Username or Password"
            );
        }


        return response;
    }


    @GetMapping("/user/{username}")
    public Map<String, String> getUser(
            @PathVariable String username) {

        Users user =
                ur.findByUsername(username);


        if (user == null) {

            Map<String, String> response =
                    new HashMap<>();

            response.put(
                    "message",
                    "User Not Found"
            );

            return response;
        }


        Map<String, String> response =
                new HashMap<>();


        response.put(
                "username",
                user.getUsername()
        );


        response.put(
                "email",
                user.getEmail()
        );


        response.put(
                "fullName",
                user.getFullName()
        );


        response.put(
                "phone",
                user.getPhone()
        );


        response.put(
                "bio",
                user.getBio()
        );


        response.put(
                "interests",
                user.getInterests()
        );


        response.put(
                "role",
                user.getRole()
        );


        return response;
    }


    @PutMapping(
            "/user/update/{username}"
    )
    public String updateUser(
            @PathVariable String username,
            @RequestBody Users updatedUser) {

        Users user =
                ur.findByUsername(username);


        if (user == null) {

            return "User Not Found";
        }


        user.setFullName(
                updatedUser.getFullName()
        );


        user.setPhone(
                updatedUser.getPhone()
        );


        user.setBio(
                updatedUser.getBio()
        );


        user.setInterests(
                updatedUser.getInterests()
        );


        ur.save(user);


        return "Profile Updated Successfully";
    }
}