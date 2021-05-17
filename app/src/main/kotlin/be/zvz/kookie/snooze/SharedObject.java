package be.zvz.kookie.snooze;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SharedObject {
    public final Lock lock = new ReentrantLock();
    public final Map<Integer, Integer> ids = new ConcurrentHashMap<>();
}
