/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
