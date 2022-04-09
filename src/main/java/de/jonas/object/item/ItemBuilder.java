package de.jonas.object.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.Map;

/**
 * Mit einer Instanz dieses {@link ItemBuilder} lässt sich ganz einfach ein {@link ItemStack} zusammenbauen.
 */
@NotNull
public final class ItemBuilder {

    //<editor-fold desc="LOCAL FIELDS">
    /** Das Material woraus dieser {@link ItemBuilder} den {@link ItemStack} zusammenbaut. */
    @NotNull
    private final Material material;
    /** Die Anzahl an Items, die der {@link ItemStack} später beinhalten soll. */
    @Range(from = 0, to = 64)
    private int amount;
    /** Der Name des {@link ItemStack}. */
    @NotNull
    private String name;
    /** Die einzelnen Zeilen der Lore, die der {@link ItemStack} haben soll. */
    @NotNull
    private String[] lore;
    /** Die Verzauberungen, die der {@link ItemStack} bekommen soll. */
    @Nullable
    private Map<Enchantment, Integer> enchantments;
    //</editor-fold>


    //<editor-fold desc="CONSTRUCTORS">

    /**
     * Erzeugt eine neue und vollständig unabhängige Instanz eines {@link ItemBuilder}. Mit einer Instanz dieses {@link
     * ItemBuilder} lässt sich ganz einfach ein {@link ItemStack} zusammenbauen.
     *
     * @param material Das Material, woraus dieser {@link ItemStack} später bestehen soll.
     */
    public ItemBuilder(@NotNull final Material material) {
        this.material = material;

        this.amount = 1;
        this.name = this.material.name().toLowerCase();
        this.lore = new String[]{""};
    }
    //</editor-fold>


    /**
     * Setzt den Namen dieses {@link ItemBuilder}.
     *
     * @param name Der Name dieses {@link ItemBuilder}.
     *
     * @return Die aktualisierte Instanz dieses {@link ItemBuilder}.
     */
    @NotNull
    public ItemBuilder setName(@NotNull final String name) {
        this.name = name;
        return this;
    }

    /**
     * Setzt die Lore dieses {@link ItemBuilder}.
     *
     * @param lore Die Lore dieses {@link ItemBuilder}.
     *
     * @return Die aktualisierte Instanz dieses {@link ItemBuilder}.
     */
    @NotNull
    public ItemBuilder setLore(@NotNull final String[] lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Setzt die Anzahl an Items dieses {@link ItemBuilder}.
     *
     * @param amount Die Anzahl an Items dieses {@link ItemBuilder}.
     *
     * @return Die aktualisierte Instanz dieses {@link ItemBuilder}.
     */
    @NotNull
    public ItemBuilder setAmount(@Range(from = 0, to = 64) final int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Setzt alle Verzauberungen dieses {@link ItemBuilder}.
     *
     * @param enchantments Alle Verzauberungen dieses {@link ItemBuilder}.
     *
     * @return Die aktualisierte Instanz dieses {@link ItemBuilder}.
     */
    @NotNull
    public ItemBuilder setEnchantments(@NotNull final Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    /**
     * Baut den {@link ItemStack} mit allen zuvor festgelegten Eigenschaften zusammen und gibt ihn zurück. Diese Methode
     * ist also die letzte Methode, die man aufruft, da mit dieser Methode der {@link ItemStack} zusammengebaut wird und
     * nicht mehr die aktualisierte und editierbare Instanz dieses {@link ItemBuilder}, sondern der fertige {@link
     * ItemStack} zurückgegeben wird.
     *
     * @return Der fertig zusammengebaute {@link ItemStack} mit allen zuvor festgelegten Eigenschaften.
     */
    @NotNull
    public ItemStack getStack() {
        final ItemStack stack = new ItemStack(this.material, this.amount);
        final ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(this.name);
        meta.setLore(Arrays.asList(this.lore));

        if (this.enchantments != null) {
            for (@NotNull final Map.Entry<Enchantment, Integer> enchantmentEntry : this.enchantments.entrySet()) {
                final Enchantment enchantment = enchantmentEntry.getKey();
                final int level = enchantmentEntry.getValue();

                meta.addEnchant(enchantment, level, true);
            }
        }

        stack.setItemMeta(meta);

        return stack;
    }

}
