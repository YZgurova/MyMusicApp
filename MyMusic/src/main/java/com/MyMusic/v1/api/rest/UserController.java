package com.MyMusic.v1.api.rest;

import com.MyMusic.v1.api.rest.models.MusicCreatorDto;
import com.MyMusic.v1.api.rest.models.MusicCreatorInput;
import com.MyMusic.v1.api.rest.models.UserDto;
import com.MyMusic.v1.api.rest.models.UserInput;
import com.MyMusic.v1.auth.MyUser;
import com.MyMusic.v1.services.MusicCreatorService;
import com.MyMusic.v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private MusicCreatorService creatorService;

    @GetMapping
    public List<UserDto> listUsers( @RequestParam Integer lastShowedUser,
                                    @RequestParam Integer countStudents ) {
        return userService.listUsers(lastShowedUser,countStudents)
                .stream()
                .map(Mappers::fromUser)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        return Mappers.fromUser(userService.getUser(id));
    }

    //@PreAuthorize("hasAuthority('ADMIN')")
//    @PostMapping
//    public UserDto createUser(@RequestBody UserInput user) {
//        return Mappers.fromUser(userService.createUser(user.firstName, user.lastName, user.username, user.email, user.password));
//    }

    @PostMapping
    public void createCreator(@AuthenticationPrincipal MyUser user) {
        creatorService.createCreator(user.id);
    }

    @PutMapping("/update")
    public int updateUser(@RequestParam Integer id, @RequestBody UserInput user) {
        return userService.updateUser(id, user.firstName, user.lastName, user.username, user.email);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
