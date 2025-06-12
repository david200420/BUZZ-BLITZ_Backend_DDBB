package edu.upc.dsa.models;

import java.util.List;

public class ListFreqQuest {
    List<FreqQuest> faqs;

    public ListFreqQuest() {}
    public ListFreqQuest(List<FreqQuest> faqs) {
        this.faqs = faqs;
    }

    public void setFaqs(List<FreqQuest> faqs) {
        this.faqs = faqs;
    }
    public List<FreqQuest> getFaqs() {
        return this.faqs;
    }
}
