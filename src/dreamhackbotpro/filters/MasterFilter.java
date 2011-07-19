package dreamhackbotpro.filters;

/**
 *
 * @author patrik
 */
public class MasterFilter extends CompositeMessageFilter {
    public MasterFilter() {
        addMessageFilter(new WhiteSpaceCleanerFilter());
    }
}
