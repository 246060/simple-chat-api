# socket server 

## chat event name

### emit or listener name
- event.api.server
- event.chat.server
- event.chat.client

## chat server space_key naming
- all
- channel_xxxx
- user_xxxx

## routing type
- to.channel
- to.all
- to.user~~~~

## push event message

```json
{
	"routing" : {
		"type" : "to.channel",
		"targetId" : 123
	}, 
	"message" : {
		"eventType" : "channel.participant.join | channel.participant.exit",
		"channelId" : 123,
    "users" : [
      {"id" : 123, "name" : "홍길동"}
    ]
	}
}
```

```json
{
	"routing" : {
		"type" : "to.channel",
		"targetId" : 123
	}, 
	"message" : {
		"eventType" : "channel.message.new", 
		"channelId" : 123,
		"messageId" : 123,
		"type" : "short | long | file | link"
	}
}
```

```json
{
	"routing" : {
		"type" : "to.channel",
		"targetId" : 123
	}, 
	"message" : {
		"eventType" : "channel.message.deleted | channel.message.reaction",
		"channelId": 123,
		"messageId": 123
	}
}
```