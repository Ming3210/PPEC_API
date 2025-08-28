package com.ra.base_spring_boot.model.constants;

import java.util.stream.IntStream;

public enum RoleName implements CharSequence {
    ADMIN, STUDENT, LECTURER, ASSISTANT, SERVICE_STAFF, SCHOOL_ADMIN,STAFF,CENTER;

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return CharSequence.super.isEmpty();
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    @Override
    public IntStream chars() {
        return CharSequence.super.chars();
    }

    @Override
    public IntStream codePoints() {
        return CharSequence.super.codePoints();
    }
}
