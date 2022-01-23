package com.mtapo.app.controller;

import com.mtapo.app.entity.*;
import com.mtapo.app.model.FormResponse;
import com.mtapo.app.model.Pagination;
import com.mtapo.app.model.UserRoleStatistic;
import com.mtapo.app.service.EventLogService;
import com.mtapo.app.service.RoleService;
import com.mtapo.app.service.StudentTopicService;
import com.mtapo.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class UsersController {

    private final UserService userService;
    private final StudentTopicService studentTopicService;
    private final RoleService roleService;
    private final EventLogService eventlogService;

    @Autowired
    public UsersController(
            EventLogService eventlogService,
            UserService userService,
            StudentTopicService studentTopicService,
            RoleService roleService) {
        this.eventlogService = eventlogService;
        this.userService = userService;
        this.studentTopicService = studentTopicService;
        this.roleService = roleService;
    }

    @RequestMapping("/users")
    public String users(Model model,
                        HttpServletRequest request,
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "9") Integer size,
                        @RequestParam(defaultValue = "lastName") String sort,
                        @RequestParam(defaultValue = "desc") String order) {
        String keywords = request.getParameter("keywords");
        String pageStr = request.getParameter("page");
        String sortBy = request.getParameter("sort");

        page = (pageStr == null || pageStr.isEmpty() || Integer.parseInt(pageStr) < 0)
                ? 0
                : Integer.parseInt(pageStr);

        if (sortBy != null) {
            switch (sortBy) {
                case "first-name-asc": {
                    sort = "firstName";
                    order = "asc";
                    break;
                }
                case "first-name-desc": {
                    sort = "firstName";
                    order = "desc";
                    break;
                }
                case "last-name-asc": {
                    sort = "lastName";
                    order = "asc";
                    break;
                }
                case "last-name-desc": {
                    sort = "lastName";
                    order = "desc";
                    break;
                }
                case "email-asc": {
                    sort = "email";
                    order = "asc";
                    break;
                }
                case "email-desc": {
                    sort = "email";
                    order = "desc";
                    break;
                }
            }
        }

        Pagination<User> userPagination = this.getUsers(page, size, sort, order, keywords);

        Optional<Role> student = roleService.findByName(Role.STUDENT);
        Optional<Role> teacher = roleService.findByName(Role.TEACHER);
        Optional<Role> admin = roleService.findByName(Role.ADMIN);

        UserRoleStatistic userRoleStatistic = new UserRoleStatistic();

        student.ifPresent(role -> userRoleStatistic.setStudents(userService.getCountByRoleId(role.getId())));
        teacher.ifPresent(role -> userRoleStatistic.setTeachers(userService.getCountByRoleId(role.getId())));
        admin.ifPresent(role -> userRoleStatistic.setAdmins(userService.getCountByRoleId(role.getId())));

        Map<Integer, Topic> userIdToTopicMap = new HashMap<>();
        userPagination.getElements().forEach(user -> {
            Optional<StudentTopic> assignedTopic =
                    studentTopicService.findAssignedByUserId(user.getId());
            assignedTopic.ifPresent(
                    studentTopic -> userIdToTopicMap.put(user.getId(), studentTopic.getTopic()));
        });

        model.addAttribute("userPagination", userPagination);
        model.addAttribute("userIdToTopicMap", userIdToTopicMap);
        model.addAttribute("userRoleStatistic", userRoleStatistic);

        return "users";
    }

    public Pagination<User> getUsers(Integer page,
                                     Integer size,
                                     String sort,
                                     String order,
                                     String keywords) {
        Pageable paging;
        // Set sort
        Sort pageSort = Sort.by(sort);
        if (order.equals("desc")) {
            pageSort = pageSort.descending();
        }

        // Set page
        paging = PageRequest.of(page, size, pageSort);

        Page<User> usersPage;
        usersPage = userService.findAllByNameOrEmail(keywords, paging);

        Pagination<User> userPagination = new Pagination<>();
        userPagination.setElements(usersPage.getContent());
        userPagination.setTotalElements(usersPage.getTotalElements());
        userPagination.setTotalPages(usersPage.getTotalPages());
        userPagination.setPageNumber(usersPage.getNumber());
        userPagination.setPageSize(usersPage.getSize());

        return userPagination;
    }

    @RequestMapping("/users/change-roles/{id}")
    public RedirectView changeRoles(
            @PathVariable("id") Integer userId,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        User sessionUser = (User) request.getSession().getAttribute("sessionUser");
        String roles = request.getParameter("roles");

        List<String> roleList = roles.contains(",")
                ? Arrays.asList(roles.split(","))
                : Collections.singletonList(roles);

        FormResponse formResponse = new FormResponse();
        try {

            Optional<User> user = userService.findById(userId);
            if (!user.isPresent()) {
                throw new UsernameNotFoundException("User with id: " + userId + " not found");
            }

            Set<Role> userRoles = new HashSet<>();
            roleList.forEach(item -> {
                Optional<Role> role = roleService.findByName(item);
                role.ifPresent(userRoles::add);
            });

            user.get().setRoles(userRoles);
            userService.saveOrUpdate(user.get());
            EventLog log = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_SUCCESS,
                    "CHANGE ROLE: student " + userId + ".",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setStatus(FormResponse.STATUS_SUCCESS);
            formResponse.setMessage("Roles successfully changed");
            formResponse.setDescription(
                    "Roles of user with id of " + "<a><b>#" + userId + "</b></a>" + " has been changed");

        } catch (Exception e) {
            e.printStackTrace();
            EventLog log = new EventLog(null,
                    EventLog.OPERATION_UPDATE,
                    EventLog.STATUS_FAILED,
                    "CHANGE ROLE: student " + userId + ".",
                    new Timestamp(System.currentTimeMillis()),
                    sessionUser);
            eventlogService.saveOrUpdate(log);
            formResponse.setMessage("Oops!");
            formResponse.setStatus(FormResponse.STATUS_ERROR);
            formResponse.setDescription("Something went wrong while processing your request");
        }

        redirectAttributes.addFlashAttribute("formResponse", formResponse);

        return new RedirectView("/users", true);
    }

    @ResponseBody
    @PostMapping(value = "/users/get-user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {

        Optional<User> user = userService.findById(id);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/users/get-roles")
    public ResponseEntity<List<Role>> getUserById() {

        List<Role> roles = roleService.findAll();

        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
