package be.zvz.kookie.snooze;

public class SleeperNotifier {
    private SharedObject sharedObject;
    private int sleeperId;

    public final void attachSleeper(SharedObject sharedObject, int id) {
        this.sharedObject = sharedObject;
        this.sleeperId = id;
    }

    public final int getSleeperId() {
        return this.sleeperId;
    }

    public final void wakeupSleeper() {
        SharedObject shared = sharedObject;
        assert(shared != null);
        int sleeperId = this.sleeperId;
        shared.ids.put(sleeperId, sleeperId);
        try {
            if (shared.lock.tryLock()) {
                shared.notify();
            }
        } finally {
            shared.lock.unlock();
        }
    }
}
