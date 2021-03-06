package com.bwfcwalshy.flarebot.github;

import com.arsenarsen.githubwebhooks4j.events.EventListener;
import com.arsenarsen.githubwebhooks4j.events.PushEvent;
import com.arsenarsen.githubwebhooks4j.objects.Commit;
import com.bwfcwalshy.flarebot.FlareBot;
import com.bwfcwalshy.flarebot.MessageUtils;
import sx.blah.discord.handle.obj.IChannel;

public class GithubListener implements EventListener<PushEvent> {

    private IChannel githubChannel;

    @Override
    public void handle(PushEvent e) {
        if(!FlareBot.getInstance().getClient().isReady())
            return;
        StringBuilder sb = new StringBuilder();
        sb.append("New commit by **")
                .append(e.getPusher().getName())
                .append("** in branch ")
                .append("`")
                .append(e.getRef().substring(e.getRef().lastIndexOf('/') + 1))
                .append("`\n");
        for(Commit commit : e.getCommits()){
            sb.append(":arrow_up: ").append("`")
                    .append(commit.getId().substring(0, 7)).append("`").append(" :pencil: ").append("`")
                    .append(commit.getMessage()).append("`")
                    .append('\n').append('<').append(commit.getUrl()).append('>').append('\n');

        }
        MessageUtils.sendMessage(FlareBot.getInstance().getClient().getChannelByID("229236239201468417"), sb.toString());
    }
}
