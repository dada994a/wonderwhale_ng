package dev.twerklife.asm.ducks;

import net.minecraft.text.Text;

public interface IChatHud {
    void clientMessage(Text message, int id);
}