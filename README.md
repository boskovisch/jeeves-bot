# Jeeves bot

A simple SpringBoot Bot to Handle Git merging queues in Slack.

This application was developed due constant conflicts between team members, 
when merging a branch into the master.  

## The valid commands
```
MERGE - add yourself to the merging queue
DONE - remove yourself from the merging queue
KICK - kick current merging user from the queue
STATUS - print queue
HELP - list all available commands
```

## How to use it:

### Step 1
Create a bot in slack and add the slack bot token to `application.properties` file.  

### Step 2
```
# slack integrations
rtmUrl=${RTM_URL}
slackBotToken=${SLACK_BOT_TOKEN}
slashCommandToken=${SLASH_COMMAND_TOKEN}
slackIncomingWebhookUrl=${SLACK_INCOMING_WEBHOOK_URL}
```

### Step 3
Add jeeves bot to the channel where the merge communication with be done.

### Step 4
Tag jeeves and pass the desired command as a parameter:

`@jeeves merge`

Then Jeeves will reply valid commands. 

If the queue is empty the response will be:

`Hey @User, you are the first in line! You can go right ahead!` 

if not:

`Alright alright alright @User! Sit tight, and wait a bit until your turn`