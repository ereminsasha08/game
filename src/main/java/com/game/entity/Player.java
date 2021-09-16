package com.game.entity;


import com.game.entity.Profession;
import com.game.entity.Race;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "player")

public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "title")
    private String title;
    @Column(name = "race")
    @Enumerated(EnumType.STRING)
    private Race race;
    @Column(name = "profession")
    @Enumerated(EnumType.STRING)
    private Profession profession;
    @Column(name = "experience")
    private Integer experience;
    @Column(name = "level")
    private Integer level;
    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;
    @Column(name = "birthday")
    private Date birthday;
    @Column(name = "banned")
    private Boolean banned;

    public Player(String name, String title, Race race, Profession profession, Integer experience, Date birthday, Boolean banned) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.experience = experience;
        this.birthday = birthday;
        this.banned = banned;
    }


    public static void main(String[] args) {


    }


    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null & name.length() <= 12 & name.length() != 0)
            this.name = name;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title != null & title.length() <= 30)
            this.title = title;

    }

    public Race getRace() {
        return this.race;
    }

    public void setRace(String race) {
        this.race = Race.valueOf(race);
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(String profession) {
        this.profession = Profession.valueOf(profession);
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        if (Integer.parseInt(experience) >= 0 & Integer.parseInt(experience) <= 10_000_000) {
            this.experience = Integer.parseInt(experience);
            setLevel(Integer.parseInt(experience));
            setUntilNextLevel(Integer.parseInt(experience));
        }
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer experience) {
        this.level = (int) Math.floor((Math.sqrt(2500 + 200 * experience) - 50)/100);
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer experience) {
        this.untilNextLevel = 50 * (getLevel() + 1) * (getLevel() + 2) - experience;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        Date birthDate = new Date(Long.parseLong(birthday));
        if (Long.parseLong(birthday) > 0 & birthDate.after(new Date(946684800000L)) & birthDate.before((new Date(32503680000000L))))
            this.birthday = birthDate;

    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(String banned) {
        if (banned.equals("true")) {
            this.banned = true;
        }
        else this.banned = false;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                ", birthday=" + birthday +
                ", banned=" + banned +
                '}';
    }
}
