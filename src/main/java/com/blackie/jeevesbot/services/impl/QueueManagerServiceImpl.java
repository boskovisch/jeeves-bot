package com.blackie.jeevesbot.services.impl;

import com.blackie.jeevesbot.components.Messages;
import com.blackie.jeevesbot.enums.Commands;
import com.blackie.jeevesbot.services.QueueManagerService;
import com.blackie.jeevesbot.services.QueueService;
import com.blackie.jeevesbot.utils.SlackMessageUtils;
import me.ramswaroop.jbot.core.slack.models.Event;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueueManagerServiceImpl implements QueueManagerService{

    private static final Map<String, Queue> queueCollection = new HashMap<>();
    private final QueueService queueService;
    private final Messages messages;

    public QueueManagerServiceImpl(final QueueService queueService,
                                   final Messages messages) {
        this.queueService = queueService;
        this.messages = messages;
    }

    @Override
    public String executeCommand(final Event event) {
        final Commands command = parseCommandFromEvent(event);
        if(command == null){
            return StringUtils.EMPTY;
        }
        final String response;
        final Queue mergeQueue = getOrCreateQueue(event.getChannelId());
        switch (command) {
            case MERGE:
                response = queueService.addUserToMergeQueue(event.getUserId(), mergeQueue);
                break;
            case DONE:
                response = queueService.removeUserFromMergeQueue(event.getUserId(), mergeQueue);
                break;
            case KICK:
                response = queueService.kickUserFromMergeQueue(mergeQueue);
                break;
            case STATUS:
                response = queueService.getMergeQueueStatus(mergeQueue);
                break;
            case HELP:
                response = listCommands();
                break;
            default:
                response = StringUtils.EMPTY;
                break;
        }
        return response;
    }

    private Commands parseCommandFromEvent(final Event event) {
        return Arrays.asList(Commands.values())
                .stream()
                .filter(command -> event != null && StringUtils.containsIgnoreCase(event.getText(), command.name()))
                .findFirst()
                .orElse(null);
    }

    private Queue getOrCreateQueue(final String channelId) {
        if (!queueCollection.containsKey(channelId)) {
            queueCollection.put(channelId, new LinkedList<String>());
        }
        return queueCollection.get(channelId);
    }

    private String listCommands() {
        final StringBuilder sb = new StringBuilder();
        sb.append(messages.get("validCommands"));
        Arrays.asList(Commands.values())
                .stream()
                .forEach(command -> sb.append(StringUtils.LF + SlackMessageUtils.makeTextBold(command.name()) + StringUtils.SPACE + messages.get(command.name().toLowerCase())));
        return sb.toString();
    }
}
