package model;

import model.Variable;

public interface Module<T extends Variable<?>, R extends Variable<?>> {
    T execute(R... args);
}
//TODO: there must be a way to convert it into a lambda
//TODO: what is the point of such an interface
//może wam się nie podobać "... args", ale module powinien móc przyjąć zarówno 1 zmienną (signum(x)), jak i wiele (całka oznaczona od a do b(f,a,b))