package com.mtapo.app.controller;

import com.mtapo.app.entity.*;
import com.mtapo.app.model.Pagination;
import com.mtapo.app.model.TopicStatistic;
import com.mtapo.app.service.TagService;
import com.mtapo.app.service.TopicService;
import com.mtapo.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class TopicsController {

    private final TopicService topicService;
    private final UserService userService;
    private final TagService tagService;

    @Autowired
    public TopicsController(TopicService topicService,
                            UserService userService,
                            TagService tagService) {
        this.topicService = topicService;
        this.userService = userService;
        this.tagService = tagService;
    }

    @RequestMapping("/topics")
    public String topics(HttpServletRequest request,
                         Model model,
                         @RequestParam(defaultValue = "0") Integer page,
                         @RequestParam(defaultValue = "10") Integer size,
                         @RequestParam(defaultValue = "createdDate") String sort,
                         @RequestParam(defaultValue = "desc") String order) {
        String teacher = request.getParameter("teacher");
        String category = request.getParameter("category");
        String pageStr = request.getParameter("page");
        String tags = request.getParameter("tags");

        page = (pageStr == null || pageStr.isEmpty() || Integer.parseInt(pageStr) < 0)
                ? 0
                : Integer.parseInt(pageStr);

        if (teacher == null || teacher.isEmpty()) {
            teacher = null;
        }

        if (category == null || category.isEmpty()) {
            category = null;
        }

        Pagination<Topic> topicPagination =
                this.getTopics(page, size, sort, order, teacher, category, tags);
        List<Topic> topics = topicPagination.getElements();
        Map<Integer, Integer> assignedUsers = new HashMap<>();
        topics.forEach(topic -> assignedUsers.put(topic.getId(),
                userService.getCountByTopicIdAndStatus(topic.getId(),
                        StudentTopic.STATUS_APPROVED)));

        List<User> teachers = userService.getUsersByRole("teacher");

        TopicStatistic topicStatistic = new TopicStatistic();
        topicStatistic.setLicenseTopics(topicService.getCountByCategoryName(Category.LICENSE));
        topicStatistic.setMasterTopics(topicService.getCountByCategoryName(Category.MASTER));

        model.addAttribute("topicPagination", topicPagination);
        model.addAttribute("assignedUsers", assignedUsers);
        model.addAttribute("teachers", teachers);
        model.addAttribute("topicStatistic", topicStatistic);

        return "topics";
    }

    public Pagination<Topic> getTopics(Integer page,
                                       Integer size,
                                       String sort,
                                       String order,
                                       String teacher,
                                       String category,
                                       String tags) {
        Pageable paging;
        // Set sort
        Sort pageSort = Sort.by(sort);
        if (order.equals("desc")) {
            pageSort = pageSort.descending();
        }

        // Set page
        paging = PageRequest.of(page, size, pageSort);

        Page<Topic> topicPage;
        List<String> tagList = null;
        if (tags == null || tags.isEmpty()) {
            topicPage = topicService.findAllByTeacherAndCategory(teacher, category, paging);

        } else {
            tagList =
                    tags.contains(",")
                            ? Arrays.asList(tags.split(","))
                            : new ArrayList<>(Collections.singletonList(tags));
            topicPage = topicService.findAllByTeacherCategoryAndTags(teacher, category, tagList, paging);
        }

        Pagination<Topic> topicPagination = new Pagination<>();
        topicPagination.setElements(topicPage.getContent());
        topicPagination.setTotalElements(topicPage.getTotalElements());
        topicPagination.setTotalPages(topicPage.getTotalPages());
        topicPagination.setPageNumber(topicPage.getNumber());
        topicPagination.setPageSize(topicPage.getSize());

        return topicPagination;
    }

    @ResponseBody
    @PostMapping("/topics/get-all-tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.findAll();

        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
