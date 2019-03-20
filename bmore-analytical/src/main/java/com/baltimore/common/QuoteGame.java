package com.baltimore.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by paul on 21.07.18.
 */
public class QuoteGame {
    List<Quote> quotes = new ArrayList<Quote>();
    String answer;

    public QuoteGame() {
        buildQuotes();
        answer = null;
    }

    private static int randomNumber(int seed) {
        Random random = new Random();
        return random.nextInt(seed);
    }

    private void buildQuotes() {
        quotes.add(new QuoteBuilder().setAuthor("Marla Daniels").setQuote("The tree that doesn’t bend, breaks. Bend too far, and you’re already broken.").build());
        quotes.add(new QuoteBuilder().setAuthor("Carver").setQuote("Girl, you can’t even think of calling this shit a war... Wars end.").build());
        quotes.add(new QuoteBuilder().setAuthor("Slim Charles").setQuote("Don’t matter who did what to who at this point. Fact is, we went to war, and now there ain’t no going back. I mean, shit, it’s what war is, you know? Once you in it, you in it. If it’s a lie, then we fight on that lie. But we gotta fight.").build());
        quotes.add(new QuoteBuilder().setAuthor("Bunk").setQuote("There you go, giving a fuck when it ain’t your turn to give a fuck").build());
        quotes.add(new QuoteBuilder().setAuthor("D'Angelo Barksdale").setQuote("The king stay the king.").build());
        quotes.add(new QuoteBuilder().setAuthor("Cedric Daniels").setQuote("There ain’t nothing you fear more than a bad headline, now, is there? You’d rather live in shit than let the world see you work a shovel.").build());
        quotes.add(new QuoteBuilder().setAuthor("Prop Joe").setQuote("Wanna know what kills more police than bullets and liquor? Boredom. They just can’t handle that shit. You keep it boring, String. You keep it dead f—ing boring").build());
        quotes.add(new QuoteBuilder().setAuthor("Omar").setQuote("You come at the king, you best not miss.").build());
        quotes.add(new QuoteBuilder().setAuthor("Avon Barksdale").setQuote("See, the thing is, you only got to fuck up once. Be a little slow, be a little late, just once. And how you ain’t gonna never be slow, never be late?").build());
        quotes.add(new QuoteBuilder().setAuthor("Clay Davis").setQuote("I’ll take any persons money if he giving it away").build());
        quotes.add(new QuoteBuilder().setAuthor("Clay Davis").setQuote("Shiiiiiiiiiiiiiiiiiiiiiiiiiiiiit").build());
        quotes.add(new QuoteBuilder().setAuthor("Buny Colvin").setQuote("I thought I might legalize drugs").build());
        quotes.add(new QuoteBuilder().setAuthor("Pryzbylewski").setQuote("You juke the stats enough and Majors become Colonels").build());
        quotes.add(new QuoteBuilder().setAuthor("Bubbles").setQuote("Ain't no shame in holding on to grief, as long as you make room for other things too").build());
        quotes.add(new QuoteBuilder().setAuthor("McNulty").setQuote("Cos when it came time for you to f*ck me, you were very gentle").build());
        quotes.add(new QuoteBuilder().setAuthor("Marlow").setQuote("My name is my name").build());
        quotes.add(new QuoteBuilder().setAuthor("Brother Mouzone").setQuote("What got you here is your word and your reputation. With that alone, you've still got an open line to New York. Without it, you're done.").build());

    }

    public String getQuestion() {
        Quote quote = randomQoute();
        answer = quote.author;
        StringBuilder question = new StringBuilder(question(quote.quote));
        List<String> authors = insertCorrectAuthor(randomAuthors(), quote.author);
        question.append("\n\n");
        String choices = authorsToString(authors);
        question.append(choices);
        question.append("\n");
        question.append("Enter choice (i suggest copy/paste): ");
        return question.toString();
    }

    private Quote randomQoute() {
        return quotes.get(randomNumber(quotes.size()));
    }

    private Set<String> randomAuthors() {
        Set<String> authors = new HashSet<String>();
        int i = 0;
        while (i < 4) {
            int index = randomNumber(quotes.size());
            authors.add(quotes.get(index).author);
            i++;
        }
        return authors;
    }

    private List<String> insertCorrectAuthor(Set<String> authors, String correctAuthor) {
        List<String> choices = new ArrayList<String>();

        if (authors.contains(correctAuthor))
            authors.remove(correctAuthor);

        choices.addAll(authors);
        int index = randomNumber(choices.size());
        choices.add(index, correctAuthor);

        return choices;
    }


    private String question(String text) {
        return String.format("What character in The Wire (HBO) said this statement - %s", text);
    }

    private String authorsToString(List<String> authors) {
        StringBuilder builder = new StringBuilder();
        for (String author : authors) {
            builder.append(author).append("\n");
        }

        return builder.toString();
    }

    class Quote {
        String author;
        String quote;
    }

    class QuoteBuilder {
        Quote q;

        QuoteBuilder() {
            q = new Quote();
        }

        QuoteBuilder setAuthor(String author) {
            q.author = author;
            return this;
        }

        QuoteBuilder setQuote(String quote) {
            q.quote = quote;
            return this;
        }

        Quote build() {
            return q;
        }


    }

}
