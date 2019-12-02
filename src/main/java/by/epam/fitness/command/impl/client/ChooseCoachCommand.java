package by.epam.fitness.command.impl.client;

import by.epam.fitness.command.ActionCommand;
import by.epam.fitness.entity.Client;
import by.epam.fitness.entity.Coach;
import by.epam.fitness.service.CoachService;
import by.epam.fitness.service.ServiceException;
import by.epam.fitness.service.ClientService;
import by.epam.fitness.service.impl.CoachServiceImpl;
import by.epam.fitness.service.impl.ClientServiceImpl;
import by.epam.fitness.util.JspConst;
import by.epam.fitness.util.MembershipValidChecker;
import by.epam.fitness.util.SessionAttributes;
import by.epam.fitness.util.page.Page;
import by.epam.fitness.util.validation.DataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

import static by.epam.fitness.util.JspConst.*;

public class ChooseCoachCommand implements ActionCommand {
    private static Logger log = LogManager.getLogger(ChooseCoachCommand.class);
    private static DataValidator dataValidator = new DataValidator();
    private ClientService clientService = new ClientServiceImpl();
    private MembershipValidChecker membershipValidChecker = new MembershipValidChecker();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String coachIdString = request.getParameter(COACH_ID);
        if (coachIdString == null || !dataValidator.isIdentifiableIdValid(coachIdString)){
            log.info("invalid coach id format: coach_id:" + coachIdString);
            request.setAttribute(JspConst.INVALID_COACH, true);
            return Page.ALL_COACHES_COMMAND;
        }
        Long coachId = Long.valueOf(coachIdString);
        try {
            if (!isCoachIdExist(coachId)) {
                log.info("coach with id = " + coachId + " doesn't exist");
                request.setAttribute(JspConst.NOT_EXIST_ID, true);
                return Page.ALL_COACHES_COMMAND;
            }
            HttpSession session = request.getSession();
            long clientId = (long) session.getAttribute(SessionAttributes.ID);
            Optional<Client> user = clientService.findById(clientId);
            if (user.isPresent()) {
                if (!membershipValidChecker.isCurrentMembershipValid(clientId)) {
                    log.info("Membership isn't valid");
                    request.setAttribute(JspConst.MEMBERSHIP_VALID, false);
                    return Page.ALL_COACHES_COMMAND;
                } else {
                    request.setAttribute(MAX_NUMBER_SYMBOLS_ATTRIBUTE, MAX_NUMBER_SYMBOLS_VALUE);
                    request.setAttribute(JspConst.MEMBERSHIP_VALID, true);
                }
                user.get().setCoachId(coachId);
                clientService.save(user.get());
            }
            log.info("coach with id  = " + coachId + " was chosen");
            request.setAttribute(JspConst.COACH_CHOSEN, true);
            page = Page.WELCOME_PAGE;
        } catch (ServiceException e) {
            log.error("Problem with service occurred!", e);
            page = Page.ALL_COACHES_COMMAND;
        }
        return page;
    }

    private boolean isCoachIdExist(Long coachId) throws ServiceException {
        CoachService coachService = new CoachServiceImpl();
        Optional<Coach> coach = coachService.findById(coachId);
        return coach.isPresent();
    }
}
