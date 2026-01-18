package client.utils;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import commons.User;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Singleton  // one shared language service for the entire app
public class LanguageService {

    private final UserConfig userConfig;
    private Locale locale;
    private final StringProperty languageTag = new SimpleStringProperty("en");
    // used locale and not just the tag because it allows things like formating decimals or dates
    // (and with a universal tag, I can easily set a locale)

    @Inject
    public LanguageService(UserConfig userConfig) {
        this.userConfig = userConfig;

        String tag = userConfig.getLanguageTag();
        if (tag == null || tag.isEmpty()) tag = "en";

//        this.locale = Locale.forLanguageTag(tag);
//        Locale.setDefault(this.locale);

        applyTag(tag, false);
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguageTag() {
        return languageTag.get();
    }

    public void setLanguageTag(String tag){
        if (tag == null || tag.isEmpty()) tag = "en";
        if (tag.equalsIgnoreCase(languageTag.get())) return;

        applyTag(tag, true);
//
//        this.locale = Locale.forLanguageTag(tag);
//        Locale.setDefault(this.locale);
//
//        userConfig.setLanguageTag(tag);
//        userConfig.saveUser();
    }

    public void applyTag(String tag, boolean persist){
        this.locale = Locale.forLanguageTag(tag);
        Locale.setDefault(this.locale);
        this.languageTag.set(tag);

        if (persist) {
            userConfig.setLanguageTag(tag);
            userConfig.saveUser();
        }
    }

    public ResourceBundle bundle(){
        return ResourceBundle.getBundle("languages.messages", locale);
        // like java's built-in "dictionary" object
    }

    public static LanguageService defaultService() {
        UserStorage memStorage = new UserStorage() {
            private User user = new User();
            @Override
            public User load() {
                return user;
            }

            @Override
            public void save(User u) {
                user = u;
            }
        };

        UserConfig cfg = new UserConfig(memStorage);

        cfg.setLanguageTag("en");

        return new LanguageService(cfg);
    }

    public ReadOnlyStringProperty languageTagProperty() {
        return languageTag;
    }


    /**
     * Translates a key (each label/button in the app gets one) and fetch the corresponding
     * text in its language file.
     * Can take a variable number of arguments to format the text
     * (in the cases of variable bits, namely numbers and names).
     */
    public String translate(String key, Object... args){
        String raw;
        try {
            raw = bundle().getString(key);
        } catch (MissingResourceException e) {
            raw = "!!" + key + "!!"; // to note which keys are missing
        }
        if (args == null || args.length == 0) return raw;
        return new MessageFormat(raw, locale).format(args);
        // the MessageFormat takes the text (with placeholders,
        // formated with the "args") and formats it according to the locale
    }

    public String unitLabel(String unitValue) {
        if (unitValue == null || unitValue.isBlank()) return translate("unit.select");
        return translate("unit." + unitValue.toLowerCase());
    }

}
