package by.epam.fitness.command.impl.admin;

import by.epam.fitness.command.ActionCommand;
import by.epam.fitness.service.CommentService;
import by.epam.fitness.service.ServiceException;
import by.epam.fitness.service.impl.CommentServiceImpl;
import by.epam.fitness.util.JspConst;
import by.epam.fitness.util.page.Page;
import by.epam.fitness.util.validation.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteCommentCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(DeleteCommentCommand.class);
    private CommentService commentService = new CommentServiceImpl();
    private static DataValidator dataValidator = new DataValidator();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        String commentIdString = request.getParameter(JspConst.COMMENT_ID);
        if (commentIdString==null || !dataValidator.isIdentifiableIdValid(commentIdString)) {
            log.info("incorrect comment id was received:" + commentIdString);
            return Page.ADMIN_COMMENTS_COMMAND;
        }
        long commentId = Long.parseLong(commentIdString);
        try {
            commentService.delete(commentId);
            log.info("comment with id = " + commentId + " was successfully deleted");
            request.setAttribute(JspConst.SUCCESS, true);
            page = Page.ADMIN_COMMENTS_COMMAND;
        } catch (ServiceException e) {
            log.error("Problem with service occurred!", e);
            page = Page.ADMIN_COMMENTS_COMMAND;
        }
        return page;
    }
}