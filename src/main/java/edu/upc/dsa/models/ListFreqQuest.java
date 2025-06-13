package edu.upc.dsa.models;

import java.util.ArrayList;
import java.util.List;

public class ListFreqQuest {
    private List<FreqQuest> faqs;

    public ListFreqQuest() {
        this.faqs = new ArrayList<>();
    }

    public ListFreqQuest(List<FreqQuest> faqs) {
        // Evita que faqs quede null
        if (faqs == null) {
            this.faqs = new ArrayList<>();
        } else {
            this.faqs = faqs;
        }
    }

    public void setFaqs(List<FreqQuest> faqs) {
        this.faqs = (faqs != null) ? faqs : new ArrayList<>();
    }

    public List<FreqQuest> getFaqs() {
        return this.faqs != null ? this.faqs : new ArrayList<>();
    }
}
