package com.blackie.jeevesbot.services;

import com.blackie.jeevesbot.services.impl.QueueManagerServiceImpl;
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

    @Autowired
    private QueueManagerService queueManagerService;

    @Test
    public void testHelpMessage() {
        final Event event = new Event();
        final String eventText = "print help command list";
        event.setText(eventText);
        Assert.assertNotNull(queueManagerService.executeCommand(event));
    }

    @Test
    public void testNullEventText() {
        final Event event = new Event();
        Assert.assertEquals(StringUtils.EMPTY, queueManagerService.executeCommand(event));
    }

    @Test
    public void testEmptyEventText() {
        final Event event = new Event();
        event.setText(StringUtils.EMPTY);
        Assert.assertEquals(StringUtils.EMPTY, queueManagerService.executeCommand(event));
    }

    @Test
    public void testNullEvent() {
        Assert.assertEquals(StringUtils.EMPTY, queueManagerService.executeCommand(null));
    }

    public void setQueueManagerService(final QueueManagerServiceImpl queueManagerService) {
        this.queueManagerService = queueManagerService;
    }
}
