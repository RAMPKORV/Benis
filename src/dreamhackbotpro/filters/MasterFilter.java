package dreamhackbotpro.filters;

/**
 *
 * @author patrik
 */
public class MasterFilter extends CompositeMessageFilter {
    public MasterFilter() {
        addMessageFilter(new SpecialCharacterFilter());
        
        addMessageFilter(new WhiteSpaceCleanerFilter());
        
        addMessageFilter(new PriceSynonymFilter());
        
        addMessageFilter(new DelimeterFilter());
        
        addMessageFilter(new TradingSynonymFilter());
    }
}
