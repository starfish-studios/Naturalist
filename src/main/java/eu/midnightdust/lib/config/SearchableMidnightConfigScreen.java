package eu.midnightdust.lib.config;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.lang.reflect.Field;

public class SearchableMidnightConfigScreen extends MidnightConfig.MidnightConfigScreen {
    private String lastQuery = "";
    private boolean showAllTabs = false;

    public SearchableMidnightConfigScreen(Screen parent, String modid) {
        super(parent, modid);
    }

    @Override
    public void init() {
        super.init();

        int boxWidth = 200;
        int boxHeight = 20;
        int barHeight = 24;
        int barTop = this.tabs.size() > 1 ? 32 : 20;
        int boxX = this.width / 2 - (boxWidth / 2);
        int boxY = barTop + (barHeight - boxHeight) / 2 - 3;
        int listTop = barTop + barHeight;

        Checkbox allTabs = getCheckbox(boxX, boxY, boxHeight);
        this.addRenderableWidget(allTabs);

        EditBox searchBox = new EditBox(this.font, boxX, boxY, boxWidth, boxHeight, Component.translatable("naturalist.midnightconfig.search"));
        searchBox.setHint(Component.translatable("naturalist.midnightconfig.search"));
        searchBox.setValue(lastQuery);
        searchBox.setResponder(this::onSearchChanged);
        this.addRenderableWidget(searchBox);

        double prevScroll = this.list.getScrollAmount();
        this.removeWidget(this.list);
        assert this.minecraft != null;
        this.list = new MidnightConfig.MidnightConfigListWidget(this.minecraft, this.width, this.height, listTop, this.height - 32, 25);
        if (this.minecraft.level != null) {
            this.list.setRenderBackground(false);
        }
        this.addWidget(this.list);
        this.fillList();
        this.list.setScrollAmount(prevScroll);
    }

    private @NotNull Checkbox getCheckbox(int boxX, int boxY, int boxHeight) {
        int checkboxWidth = 20;
        int gap = 6;
        int checkboxX = boxX - checkboxWidth - gap;

        return new Checkbox(checkboxX, boxY, checkboxWidth, boxHeight, Component.empty(), showAllTabs, false) {
            @Override
            public void onPress() {
                super.onPress();
                showAllTabs = this.selected();
                double prevScrollInner = list.getScrollAmount();
                list.clear();
                fillList();
                list.setScrollAmount(prevScrollInner);
            }
        };
    }

    private void onSearchChanged(String query) {
        this.lastQuery = query == null ? "" : query;
        this.list.clear();
        this.fillList();
    }

    @Override
    public void fillList() {
        String queryLower = lastQuery.toLowerCase(Locale.ROOT).trim();
        for (MidnightConfig.EntryInfo info : getEntries()) {
            if (!info.id.equals(this.modid)) continue;
            if (!showAllTabs && info.tab != null && info.tab != this.tabManager.getCurrentTab()) continue;

            Component name = Objects.requireNonNullElseGet(info.name,
                    () -> Component.translatable(this.translationPrefix + info.field.getName()));

            if (!queryLower.isEmpty() && !name.getString().toLowerCase(Locale.ROOT).contains(queryLower)) {
                continue;
            }

            Button resetButton = Button.builder(Component.literal("Reset").withStyle(ChatFormatting.RED), button -> {
                info.value = info.defaultValue;
                info.tempValue = info.defaultValue.toString();
                info.index = 0;
                this.list.clear();
                this.fillList();
            }).bounds(this.width - 205, 0, 40, 20).build();

            if (info.widget instanceof Map.Entry<?, ?> widgetEntry) {
                @SuppressWarnings("unchecked")
                Map.Entry<Button.OnPress, Function<Object, Component>> widget =
                        (Map.Entry<Button.OnPress, Function<Object, Component>>) widgetEntry;

                if (info.field.getType().isEnum()) {
                    widget.setValue(value -> Component.translatable(
                            this.translationPrefix + "enum." + info.field.getType().getSimpleName() + "." + info.value));
                }

                this.list.addButton(
                        List.of(
                                Button.builder(widget.getValue().apply(info.value), widget.getKey())
                                        .bounds(this.width - 160, 0, 150, 20)
                                        .tooltip(MidnightConfig.getTooltip(info))
                                        .build(),
                                resetButton),
                        name,
                        info);
            } else if (info.field.getType() == List.class) {
                if (!this.reload) {
                    info.index = 0;
                }
                EditBox widget = new EditBox(this.font, this.width - 160, 0, 150, 20, Component.empty());
                widget.setMaxLength(info.width);
                if (info.index < ((List<?>) info.value).size()) {
                    widget.setValue(String.valueOf(((List<?>) info.value).get(info.index)));
                }
                @SuppressWarnings("unchecked")
                Predicate<String> processor = ((BiFunction<EditBox, Button, Predicate<String>>) info.widget).apply(widget, this.done);
                widget.setFilter(processor);
                resetButton.setWidth(20);
                resetButton.setMessage(Component.literal("R").withStyle(ChatFormatting.RED));
                Button indexButton = Button.builder(Component.literal(String.valueOf(info.index)).withStyle(ChatFormatting.GOLD), button -> {
                    ((List<?>) info.value).remove("");
                    info.index++;
                    if (info.index > ((List<?>) info.value).size()) {
                        info.index = 0;
                    }
                    this.list.clear();
                    this.fillList();
                }).bounds(this.width - 185, 0, 20, 20).build();
                widget.setTooltip(MidnightConfig.getTooltip(info));
                this.list.addButton(List.of(widget, resetButton, indexButton), name, info);
            } else if (info.widget != null) {
                MidnightConfig.Entry e = info.field.getAnnotation(MidnightConfig.Entry.class);
                Object widget = getWidget(info, e);

                if (widget instanceof EditBox textField) {
                    textField.setMaxLength(info.width);
                    textField.setValue(info.tempValue);
                    @SuppressWarnings("unchecked")
                    Predicate<String> processor = ((BiFunction<EditBox, Button, Predicate<String>>) info.widget).apply(textField, this.done);
                    textField.setFilter(processor);
                }

                ((AbstractWidget) widget).setTooltip(MidnightConfig.getTooltip(info));

                if (e.isColor()) {
                    resetButton.setWidth(20);
                    resetButton.setMessage(Component.literal("R").withStyle(ChatFormatting.RED));
                    Button colorButton = Button.builder(Component.literal("⬛"), button -> {
                    }).bounds(this.width - 185, 0, 20, 20).build();
                    try {
                        colorButton.setMessage(Component.literal("⬛").setStyle(Style.EMPTY.withColor(Color.decode(info.tempValue).getRGB())));
                    } catch (Exception ignored) {
                    }
                    info.colorButton = colorButton;
                    colorButton.active = false;
                    this.list.addButton(List.of((AbstractWidget) widget, resetButton, colorButton), name, info);
                } else {
                    this.list.addButton(List.of((AbstractWidget) widget, resetButton), name, info);
                }
            } else {
                this.list.addButton(List.of(), name, info);
            }
        }

        this.list.setScrollAmount(this.scrollProgress);
        this.updateResetButtons();
    }

    private @NotNull Object getWidget(MidnightConfig.EntryInfo info, MidnightConfig.Entry e) {
        Object widget;
        if (e.isSlider()) {
            widget = new MidnightConfig.MidnightSliderWidget(this.width - 160, 0, 150, 20,
                    Component.nullToEmpty(info.tempValue),
                    (Double.parseDouble(info.tempValue) - e.min()) / (e.max() - e.min()),
                    info);
        } else {
            widget = new EditBox(this.font, this.width - 160, 0, 150, 20, null, Component.nullToEmpty(info.tempValue));
        }
        return widget;
    }

    @SuppressWarnings("unchecked")
    private static List<MidnightConfig.EntryInfo> getEntries() {
        try {
            Field field = MidnightConfig.class.getDeclaredField("entries");
            field.setAccessible(true);
            return (List<MidnightConfig.EntryInfo>) field.get(null);
        } catch (Exception ignored) {
            return List.of();
        }
    }
}

