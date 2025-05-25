package org.tutortalk.be.domain.welfare.util;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AgeCategoryMapper {

    // 생애주기와 그에 해당하는 나이 조건 정의
    private final List<AgeCategoryRule> rules = List.of(
            new AgeCategoryRule(0, 5, Set.of("영유아")),
            new AgeCategoryRule(6, 12, Set.of("아동")),
            new AgeCategoryRule(13, 18, Set.of("청소년")),
            new AgeCategoryRule(19, 34, Set.of("청년")),
            new AgeCategoryRule(35, 64, Set.of("중장년")),
            new AgeCategoryRule(65, Integer.MAX_VALUE, Set.of("노년")),
            new AgeCategoryRule(20, 49, Set.of("임신 · 출산")) // 중복 카테고리 가능
    );

    public Set<String> categories(int age) {
        Set<String> matched = new HashSet<>();
        for (AgeCategoryRule rule : rules) {
            if (rule.isInRange(age)) {
                matched.addAll(rule.getCategories());
            }
        }
        return matched;
    }

    private static class AgeCategoryRule {
        private final int minAge;
        private final int maxAge;
        private final Set<String> categories;

        public AgeCategoryRule(int minAge, int maxAge, Set<String> categories) {
            this.minAge = minAge;
            this.maxAge = maxAge;
            this.categories = categories;
        }

        public boolean isInRange(int age) {
            return age >= minAge && age <= maxAge;
        }

        public Set<String> getCategories() {
            return categories;
        }
    }
}
