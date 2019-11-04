package by.epam.fitness.tag;

import by.epam.fitness.util.MembershipPriceReader;
import by.epam.fitness.util.UtilException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

public class PricesTag extends TagSupport {
    @Override
    public int doStartTag() throws JspException {
        try {
            Map<Integer, BigDecimal> prices = MembershipPriceReader.getInstance().getPrices();
            JspWriter out = pageContext.getOut();
            out.write("<option>Choose suitable offer</option>");
            for (Map.Entry<Integer, BigDecimal> price : prices.entrySet()) {
                out.write("<option value=\"" + price.getValue() + "\">" + price.getKey() + " days</option>");
            }
        } catch (UtilException | IOException e) {
            throw new JspException(e.getMessage(), e);
        }
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }
}
