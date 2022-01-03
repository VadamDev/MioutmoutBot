package net.vadamdev.mioutmout.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class CMDEmbedMessage {
    private String title, text;
    private EmbedType embedType;

    public CMDEmbedMessage(String title, String text, EmbedType embedType) {
        this.title = title;
        this.text = text;
        this.embedType = embedType;
    }

    public MessageEmbed build() {
        return new EmbedBuilder().setTitle(title).setDescription(text).setColor(embedType.getColor()).build();
    }

    public enum EmbedType {
        SUCCES(Color.ORANGE),
        WARNING(Color.RED),
        ERROR(Color.RED);

        private Color color;

        EmbedType(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }
    }
}
