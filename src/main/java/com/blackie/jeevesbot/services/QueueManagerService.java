package com.blackie.jeevesbot.services;

import me.ramswaroop.jbot.core.slack.models.Event;

public interface QueueManagerService {

    /**
     * Execute a command on a specific queue based on a incoming event.
     * @param event
     * @return
     */
    String executeCommand(Event event);
}
