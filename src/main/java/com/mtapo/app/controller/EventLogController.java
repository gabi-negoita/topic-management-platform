package com.mtapo.app.controller;

import com.mtapo.app.entity.EventLog;
import com.mtapo.app.model.Pagination;
import com.mtapo.app.service.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class EventLogController {
    private final EventLogService eventlogService;

    @Autowired
    public EventLogController(EventLogService eventlogService) {
        this.eventlogService = eventlogService;
    }

    @RequestMapping("/logs")
    public String logs(HttpServletRequest request,
                       Model model,
                       @RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "25") Integer size,
                       @RequestParam(defaultValue = "date") String sort,
                       @RequestParam(defaultValue = "desc") String order) {

        String operation = request.getParameter("operation");
        String status = request.getParameter("status");
        String pageStr = request.getParameter("page");
        String sortBy = request.getParameter("order");

        page = (pageStr == null || pageStr.isEmpty() || Integer.parseInt(pageStr) < 0)
                ? 0
                : Integer.parseInt(pageStr);

        if (operation == null || operation.isEmpty()) {
            operation = null;
        }
        if (status == null || status.isEmpty()) {
            status = null;
        }

        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = null;
        }
        if (sortBy != null) {
            if (sortBy.equals("asc")) {
                order = "asc";
            } else if (sortBy.equals("desc")) {
                order = "desc";
            }
        }

        Pagination<EventLog> logsPagination = this.getLogs(page, size, sort, order, operation, status);

        model.addAttribute("logsPagination", logsPagination);
        return "logs";
    }

    public Pagination<EventLog> getLogs(Integer page,
                                        Integer size,
                                        String sort,
                                        String order,
                                        String operation,
                                        String status) {
        Pageable paging;
        // Set sort
        Sort pageSort = Sort.by(sort);
        if (order.equals("desc")) {
            pageSort = pageSort.descending();
        }

        // Set page
        paging = PageRequest.of(page, size, pageSort);

        Page<EventLog> logsPage;
        logsPage = eventlogService.findAllByOperationAndStatus(operation, status, paging);

        Pagination<EventLog> logsPagination = new Pagination<>();
        logsPagination.setElements(logsPage.getContent());
        logsPagination.setTotalElements(logsPage.getTotalElements());
        logsPagination.setTotalPages(logsPage.getTotalPages());
        logsPagination.setPageNumber(logsPage.getNumber());
        logsPagination.setPageSize(logsPage.getSize());

        return logsPagination;
    }
}
