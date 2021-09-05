package hydrogengamma.model.modules.utils;

import hydrogengamma.utils.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface Collections {
    static List<Pair<String, String>> convertMapToListOfPairsOfStrings(Map<?, ?> lettersFreq) {
        return lettersFreq.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey().toString(), entry.getValue().toString()))
                .sorted(Comparator.comparing(x -> x.second))
                .sorted(Comparator.comparing(x -> x.first))
                .collect(Collectors.toList());
    }
}
