package dreamhackbotpro.filters;

/**
 * SpecialCharacterFilter is dependent on WhiteSpaceCleanerFilter. This class is only used for testing SpecialCharacterFilter
 * @author wasd
 */
public class SpecialCharacterAndWhitespaceFilter extends CompositeMessageFilter{
    
    public SpecialCharacterAndWhitespaceFilter(){
        addMessageFilter(new SpecialCharacterFilter());
        addMessageFilter(new WhiteSpaceCleanerFilter());
    }
    
}
