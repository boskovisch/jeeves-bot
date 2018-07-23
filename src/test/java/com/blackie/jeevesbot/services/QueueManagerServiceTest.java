package com.blackie.jeevesbot.services;

import com.blackie.jeevesbot.components.Messages;
import com.blackie.jeevesbot.services.impl.QueueManagerServiceImpl;
import com.blackie.jeevesbot.utils.SlackMessageUtils;
import me.ramswaroop.jbot.core.slack.models.Event;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/context/spring-test-context.xml"})
public class QueueManagerServiceTest {

    private static final String MERGE = "merge";
    private static final String DONE = "done";
    private static final String HELP = "help";
    private static final String KICK = "kick";

    @Autowired
    private QueueManagerService queueManagerService;

    @Test
    public void when_ReceiveHelpCommand_Should_PrintCommandList() {
        final Event event = new Event();
        event.setText(HELP);
        Assert.assertNotNull(queueManagerService.executeCommand(event));
    }

    @Test
    public void when_ReceiveHelpCommandInsideText_Should_PrintCommandList() {
        final Event event = new Event();
        final String eventText = "print help command list";
        event.setText(eventText);
        Assert.assertNotNull(queueManagerService.executeCommand(event));
    }

    @Test
    public void when_ReceiveNullEventText_Should_ReturnEmpty() {
        final Event event = new Event();
        Assert.assertEquals(StringUtils.EMPTY, queueManagerService.executeCommand(event));
    }

    @Test
    public void when_ReceiveEmptyEventText_Should_ReturnEmpty() {
        final Event event = new Event();
        event.setText(StringUtils.EMPTY);
        Assert.assertEquals(StringUtils.EMPTY, queueManagerService.executeCommand(event));
    }

    @Test
    public void when_ReceiveNullEvent_Should_ReturnEmpty() {
        Assert.assertEquals(StringUtils.EMPTY, queueManagerService.executeCommand(null));
    }

    @Test
    public void when_SuccessfulyMerge_Should_ReturnSuccessMessage(){
        String channelId = "cha001";
        String userId = "jj002";
        Event event = createEvent(channelId, userId, MERGE);
        // Add user to queue.
        String expectedMergeMessage = String.format(Messages.get("firstInLine"), SlackMessageUtils.mentionUser(userId));
        String response = queueManagerService.executeCommand(event);
        Assert.assertEquals(expectedMergeMessage, response);
        // Remove user from queue.
        event.setText(DONE);
        String expectedSuccessMessage = String.format(Messages.get("mergedSuccessfully"), SlackMessageUtils.mentionUser(userId));
        response = queueManagerService.executeCommand(event);
        Assert.assertEquals(expectedSuccessMessage , response);
    }

    @Test
    public void when_MergeInProgress_Should_ReturnWaitMessage(){
        String channelId = "cha001";
        String firstUserId = "db001";
        String secondUserId = "jj002";
        Event firstEvent = createEvent(channelId, firstUserId, MERGE);
        Event secondEvent = createEvent(channelId, secondUserId, MERGE);
        queueManagerService.executeCommand(firstEvent);
        String expectedWaitMessage = String.format(Messages.get("waitForABit"), SlackMessageUtils.mentionUser(secondUserId));
        String response = queueManagerService.executeCommand(secondEvent);
        Assert.assertEquals(expectedWaitMessage, response);
    }


    private Event createEvent(final String channelId, final String userId, final String text){
        Event event = new Event();
        event.setChannelId(channelId);
        event.setUserId(userId);
        event.setText(text);
        return event;
    }

    public void setQueueManagerService(final QueueManagerServiceImpl queueManagerService) {
        this.queueManagerService = queueManagerService;
    }
}
