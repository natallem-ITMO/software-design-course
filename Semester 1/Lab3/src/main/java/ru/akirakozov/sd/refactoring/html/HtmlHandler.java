package ru.akirakozov.sd.refactoring.html;

import java.util.ArrayList;
import java.util.List;

public class HtmlHandler {
    private static final String HTML_BEGIN_LINE = "<html><body>";
    private static final String HTML_END_LINE = "</body></html>";
    List<String> lines = new ArrayList<>();

    public HtmlHandler() {
        lines.add(HTML_BEGIN_LINE);
    }

    public void appendLine(String line) {
        lines.add(line);
    }

    public void appendNamePriceInfo(String name, int price) {
        lines.add(String.format("%1$s\t%2$s</br>", name, price));
    }

    public String constructBody() {
        lines.add(HTML_END_LINE);
        StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(line).append(System.lineSeparator());
        }
        return builder.toString();
    }
}
