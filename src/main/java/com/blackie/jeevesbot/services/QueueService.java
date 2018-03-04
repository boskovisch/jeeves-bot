package com.blackie.jeevesbot.services;

import java.util.Queue;

public interface QueueService {
    /**
     * Execute an action based on the event received.
     * @param event
     * @return
     */
    String addUserToMergeQueue(String userId, Queue mergeQueue);
    String removeUserFromMergeQueue(String userId, Queue mergeQueue);
    String kickUserFromMergeQueue(Queue mergeQueue);
    String getMergeQueueStatus(Queue mergeQueue);
}
