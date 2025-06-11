package edu.upc.dsa.models;

import java.util.List;

public class BadgeListDTO {
    public List<Badge> badges;

    public BadgeListDTO() {}

    public BadgeListDTO(List<Badge> badges) {
        this.badges = badges;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public void setBadges(List<Badge> badges) {
        this.badges = badges;
    }
}