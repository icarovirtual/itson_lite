package br.not.sitedoicaro.android.util;

import org.junit.Assert;
import org.junit.Test;

public class WordUtilTest {

    /** A single word will become title cased. */
    @Test
    public void titleCase_word() throws Exception {
        Assert.assertEquals("Hello", WordUtil.titleCase("hello"));
    }

    /** If the word is already capitalized it remains unchanged. */
    @Test
    public void titleCase_wordCapitalized() throws Exception {
        Assert.assertEquals("Hello", WordUtil.titleCase("Hello"));
    }

    /** Symbols do not affect setting the title case. */
    @Test
    public void titleCase_wordWithSymbol() throws Exception {
        Assert.assertEquals("Hello!", WordUtil.titleCase("hello!"));
    }

    /** If the word is already all capitalized it gets title cased. */
    @Test
    public void titleCase_wordAllCapitalized() throws Exception {
        Assert.assertEquals("Hello", WordUtil.titleCase("HELLO"));
    }

    /** If the word only has numbers, it remains unchanged. */
    @Test
    public void titleCase_wordIsNumber() throws Exception {
        Assert.assertEquals("555", WordUtil.titleCase("555"));
    }

    /** If the word only has symbols, it remains unchanged. */
    @Test
    public void titleCase_wordIsSymbols() throws Exception {
        Assert.assertEquals("$!@?", WordUtil.titleCase("$!@?"));
    }

    /** A string with multiple words will become title cased. */
    @Test
    public void titleCase_Phrase() throws Exception {
        Assert.assertEquals("Hello my friend", WordUtil.titleCase("hello my friend"));
    }

    /** A string with multiple words with symbols will become title cased. */
    @Test
    public void titleCase_PhraseWithSymbols() throws Exception {
        Assert.assertEquals("Hello, what's up?", WordUtil.titleCase("hello, what's up?"));
    }

    /** If other words are capitalized, they become lower cased. */
    @Test
    public void titleCase_PhraseWithCapitalizedWords() throws Exception {
        Assert.assertEquals("Hello, i am michael", WordUtil.titleCase("hello, I am Michael"));
    }

    /** A string with multiple words where they are all capitalized gets title cased. */
    @Test
    public void titleCase_PhraseAllCapitalized() throws Exception {
        Assert.assertEquals("Hello my friend", WordUtil.titleCase("HELLO MY FRIEND"));
    }
}
