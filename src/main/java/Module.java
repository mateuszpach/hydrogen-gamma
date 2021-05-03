public interface Module {
    Variable call(Variable... args);
}
//może wam się nie podobać "... args", ale module powinien móc przyjąć zarówno 1 zmienną (signum(x)), jak i wiele (całka oznaczona od a do b(f,a,b))
