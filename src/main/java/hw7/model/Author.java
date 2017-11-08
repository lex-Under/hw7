package hw7.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Author implements Serializable {

    public enum Gender {
        MALE,
        FEMALE
    }

    private String name;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private Gender gender;

    public Author(String name, LocalDate birthDate, LocalDate deathDate, Gender gender) {
        this.name = name;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.gender = gender;
    }

    public Author(String name, LocalDate birthDate, Gender gender) {
        this(name, birthDate, null, gender);
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return (deathDate == null)
                ? (int) ChronoUnit.YEARS.between(this.getBirthDate(), LocalDate.now())
                : (int) ChronoUnit.YEARS.between(this.getBirthDate(), this.getDeathDate());
    }

    public boolean isDead() {
        return (deathDate != null);
    }

    @Override
    public String toString() {
        return "name: " + name + ",\ndate of birth: " + birthDate + ",\ndate of death: " + deathDate + ",\nage: " + getAge() + ",\ngender: " + gender.toString();
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res
                + ((name != null)
                ? name.hashCode()
                : 0);
        res = 31 * res
                + ((birthDate != null)
                ? birthDate.hashCode()
                : 0);
        res = 31 * res
                + ((deathDate != null)
                ? deathDate.hashCode()
                : 0);
        res = 31 * res
                + ((gender != null)
                ? gender.hashCode()
                : 0);
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Author)) {
            return false;
        } else if (o == this) {
            return true;
        } else {
            Author oAuthor = (Author) o;
            return ((birthDate == null) ? oAuthor.getBirthDate() == null : birthDate.equals(oAuthor.getBirthDate()))
                    && ((deathDate == null) ? oAuthor.getDeathDate() == null : deathDate.equals(oAuthor.getDeathDate()))
                    && ((name == null) ? oAuthor.getName() == null : name.equals(oAuthor.getName()))
                    && ((gender == null) ? oAuthor.getGender() == null : gender.equals(oAuthor.getGender()));
        }
    }

}
