package dreamhackbotpro.filters;

import dreamhackbotpro.Message;

/**
 * This filters splits sentences that are unrelated to each other.
 * "säljer wow acc med spel tid biligt har 3 85 warrior" is two sentences,
 * Where a period should be inserted between biligt and har.
 *
 * Basically the opposite of RelatedSentenceFilter
 *
 * @author patrik
 */
public class UnrelatedSentenceFilter implements MessageFilter{

    @Override
    public void filter(Message m) {
        String msg = m.getMessage();
        msg = msg.replaceAll("(?i)(skriv)?[ ]*bud",".");
        m.setMessage(msg);
    }

}
