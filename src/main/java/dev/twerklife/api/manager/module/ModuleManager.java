package dev.twerklife.api.manager.module;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.twerklife.WonderWhale;
import dev.twerklife.api.manager.event.EventListener;
import dev.twerklife.api.utilities.IMinecraft;
import dev.twerklife.client.events.*;
import dev.twerklife.client.modules.client.*;
import dev.twerklife.client.modules.combat.ModuleHoleFill;
import dev.twerklife.client.modules.combat.ModuleOffhand;
import dev.twerklife.client.modules.combat.ModuleSurround;
import dev.twerklife.client.modules.miscellaneous.ModuleWelcomer;
import dev.twerklife.client.modules.movement.ModuleSprint;
import dev.twerklife.client.modules.movement.ModuleVelocity;
import dev.twerklife.client.modules.player.ModuleMultiTask;
import dev.twerklife.client.values.Value;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ModuleManager implements IMinecraft, EventListener {
    private final ArrayList<Module> modules;

    public ModuleManager() {
        WonderWhale.EVENT_MANAGER.register(this);
        this.modules = new ArrayList<>();
        // write modules here

        //Client
        this.register(new ModuleColor());
        this.register(new ModuleCommands());
        this.register(new ModuleGUI());
        this.register(new ModuleHUD());
        this.register(new ModuleHUDEditor());
        this.register(new ModuleParticles());
        this.register(new ModuleRotations());

        //Combat
        this.register(new ModuleHoleFill());
        this.register(new ModuleOffhand());
        this.register(new ModuleSurround());

        //Miscellaneous
        this.register(new ModuleWelcomer());

        //Movement
        this.register(new ModuleSprint());
        this.register(new ModuleVelocity());

        //Player
        this.register(new ModuleMultiTask());

        this.modules.sort(Comparator.comparing(Module::getName));
    }

    public void register(Module module) {
        try {
            for (Field field : module.getClass().getDeclaredFields()) {
                if (!Value.class.isAssignableFrom(field.getType())) continue;
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                module.getValues().add((Value)field.get(module));
            }
            module.getValues().add(module.tag);
            module.getValues().add(module.chatNotify);
            module.getValues().add(module.drawn);
            module.getValues().add(module.bind);
            this.modules.add(module);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public ArrayList<Module> getModules(Module.Category category) {
        return (ArrayList<Module>) this.modules.stream().filter(mm -> mm.getCategory().equals(category)).collect(Collectors.toList());
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.modules.stream().filter(mm -> mm.getName().equals(name)).findFirst().orElse(null);
        if (module != null) {
            return module.isToggled();
        }
        return false;
    }

    @Override
    public void onTick(EventTick event) {
        if (mc.player != null && mc.world != null) {
            this.modules.stream().filter(Module::isToggled).forEach(Module::onTick);
        }
    }

    @Override
    public void onMotion(EventMotion event) {
        if (mc.player != null && mc.world != null) {
            this.modules.stream().filter(Module::isToggled).forEach(Module::onUpdate);
        }
    }

    @Override
    public void onRender2D(EventRender2D event) {
        this.modules.stream().filter(Module::isToggled).forEach(m -> m.onRender2D(event));
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onRender3D(EventRender3D event) {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderSystem.defaultBlendFunc();
        RenderSystem.lineWidth(1.0f);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.modules.stream().filter(Module::isToggled).forEach(mm -> mm.onRender3D(event));
        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onLogin(EventLogin event) {
        this.modules.stream().filter(Module::isToggled).forEach(Module::onLogin);
    }

    @Override
    public void onLogout(EventLogout event) {
        this.modules.stream().filter(Module::isToggled).forEach(Module::onLogout);
    }
}
