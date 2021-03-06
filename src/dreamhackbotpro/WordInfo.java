package dreamhackbotpro;

/**
 *
 * @author patrik
 */
public class WordInfo implements Comparable<WordInfo> {
    private String word;
    private int mentions;

    public WordInfo(String word, int mentions) {
        this.word = word;
        this.mentions = mentions;
    }

    public int getMentions() {
        return mentions;
    }

    public void setMentions(int mentions) {
        this.mentions = mentions;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public int compareTo(WordInfo t) {
        if(mentions > t.getMentions())
            return 1;
        if(mentions < t.getMentions())
            return -1;
        return word.compareTo(t.getWord());
    }
}
