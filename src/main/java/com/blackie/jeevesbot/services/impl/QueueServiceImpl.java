package com.blackie.jeevesbot.services.impl;

import com.blackie.jeevesbot.components.Messages;
import com.blackie.jeevesbot.services.QueueService;
import com.blackie.jeevesbot.utils.SlackMessageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Queue;

@Service
public class QueueServiceImpl implements QueueService {

    @Override
    public String addUserToMergeQueue(final String userId, final Queue mergeQueue) {
        final StringBuilder sb = new StringBuilder();
        if (CollectionUtils.isEmpty(mergeQueue)) {
            sb.append(String.format(Messages.get("firstInLine"), SlackMessageUtils.mentionUser(userId)));
        } else {
            sb.append(String.format(Messages.get("waitForABit"), SlackMessageUtils.mentionUser(userId)));
        }
        mergeQueue.add(userId);
        return sb.toString();
    }

    @Override
    public String removeUserFromMergeQueue(final String userId, final Queue mergeQueue) {
        final String mergingUser = (String) mergeQueue.peek();
        if (mergingUser == null) {
            return Messages.get("noMergeInProcess");
        } else if (!StringUtils.equals(mergingUser, userId)) {
            return String.format(Messages.get("waitYourTurn"), SlackMessageUtils.mentionUser(mergingUser));
        } else {
            final StringBuilder sb = new StringBuilder();
            sb.append(String.format(Messages.get("mergedSuccessfully"), SlackMessageUtils.mentionUser(mergingUser)));
            mergeQueue.remove();
            sb.append(callNextUserInQueue(mergeQueue));
            return sb.toString();
        }
    }

    @Override
    public String kickUserFromMergeQueue(final Queue mergeQueue) {
        if (CollectionUtils.isEmpty(mergeQueue)) {
            return Messages.get("queueIsEmpty");
        }
        final StringBuilder sb = new StringBuilder();
        final String kickedUser = (String) mergeQueue.poll();
        sb.append(String.format(Messages.get("kickedFromQueue"), SlackMessageUtils.mentionUser(kickedUser)));
        sb.append(callNextUserInQueue(mergeQueue));
        return sb.toString();
    }

    @Override
    public String getMergeQueueStatus(final Queue mergeQueue) {
        if (CollectionUtils.isEmpty(mergeQueue)) {
            return Messages.get("queueIsEmpty");
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(Messages.get("showQueue"));
        sb.append(StringUtils.LF);
        mergeQueue.forEach(user -> {
            sb.append(SlackMessageUtils.mentionUser(user.toString()));
            sb.append(StringUtils.LF);
        });
        return sb.toString();
    }


    private String callNextUserInQueue(final Queue mergeQueue) {
        StringBuilder sb = new StringBuilder();
        if (mergeQueue.size() > 0) {
            sb.append(StringUtils.LF);
            sb.append(String.format(Messages.get("nextTurn"), SlackMessageUtils.mentionUser(mergeQueue.peek().toString())));
        }
        return sb.toString();
    }

}
