package com.bwfcwalshy.flarebot.commands.music;

import com.bwfcwalshy.flarebot.MessageUtils;
import com.bwfcwalshy.flarebot.commands.Command;
import com.bwfcwalshy.flarebot.commands.CommandType;
import com.bwfcwalshy.flarebot.util.SQLController;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <br>
 * Created by Arsen on 29.10.16..
 */
public class DeleteCommand implements Command {
    @Override
    public void onCommand(IUser sender, IChannel channel, IMessage message, String[] args) {
        if(args.length == 0){
            MessageUtils.sendMessage(channel, "Usage: _delete [NAME]");
            return;
        }
        String name = "";
        for (String arg : args) name += arg + ' ';
        name = name.trim();
        channel.setTypingStatus(true);
        try {
            String finalName = name;
            SQLController.runSqlTask(connection -> {
                connection.createStatement().execute("CREATE TABLE IF NOT EXISTS playlist (\n" +
                        "  name VARCHAR(10),\n" +
                        "  guild VARCHAR(10),\n" +
                        "  list VARCHAR(80),\n" +
                        "  PRIMARY KEY(name, guild)\n" +
                        ")");
                PreparedStatement update = connection.prepareStatement("DELETE FROM playlist WHERE name = ? AND guild = ?");
                update.setString(1, finalName);
                update.setString(2, channel.getGuild().getID());
                if (update.executeUpdate() > 0) {
                    MessageUtils.sendMessage(channel, "Success!");
                } else MessageUtils.sendMessage(channel, "That name was never taken!");
            });
        } catch (SQLException e) {
            MessageUtils.sendException("**Database error!**", e, channel);
        }
    }

    @Override
    public String getCommand() {
        return "delete";
    }

    @Override
    public String getDescription() {
        return "Deletes a playlist";
    }

    @Override
    public CommandType getType() {
        return CommandType.MUSIC;
    }

    @Override
    public String getPermission() {
        return "flarebot.playlist.delete";
    }
}
