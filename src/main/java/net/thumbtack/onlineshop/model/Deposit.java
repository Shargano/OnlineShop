package net.thumbtack.onlineshop.model;

import java.util.Objects;

public class Deposit {
    private int value;
    private int version = 1;

    public Deposit() {
    }

    public Deposit(int value) {
        this.value = value;
    }

    public void add(int value) {
        this.value += value;
    }

    public void reduce(int value) {
        this.value -= value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deposit)) return false;
        Deposit deposit = (Deposit) o;
        return getValue() == deposit.getValue() &&
                getVersion() == deposit.getVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue(), getVersion());
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "value=" + value +
                ", version=" + version +
                '}';
    }
}