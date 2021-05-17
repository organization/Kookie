/**
 *
 * _  __           _    _
 * | |/ /___   ___ | | _(_) ___
 * | ' // _ \ / _ \| |/ / |/ _ \
 * | . \ (_) | (_) |   <| |  __/
 * |_|\_\___/ \___/|_|\_\_|\___|
 *
 * A server software for Minecraft: Bedrock Edition
 *
 * Copyright (C) 2021 organization Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
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
