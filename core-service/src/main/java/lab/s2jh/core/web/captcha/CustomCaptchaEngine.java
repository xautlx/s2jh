package lab.s2jh.core.web.captcha;

import java.awt.Font;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.FontGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.textpaster.TextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

public class CustomCaptchaEngine extends ListImageCaptchaEngine {
    @Override
    protected void buildInitialFactories() {
        // filter number 0,1 and char O I
        WordGenerator wgen = new RandomWordGenerator("23456789abcdefghjklmnpqrstuvwxyz");
        RandomRangeColorGenerator cgen = new RandomRangeColorGenerator(new int[] { 0, 100 }, new int[] { 0, 100 },
                new int[] { 0, 100 });
        TextPaster textPaster = new RandomTextPaster(new Integer(4), new Integer(4), cgen, true);

        BackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(new Integer(90), new Integer(30));

        Font[] fontsList = new Font[] { new Font("Arial", 0, 10), new Font("Tahoma", 0, 10),
                new Font("Verdana", 0, 10), };

        FontGenerator fontGenerator = new RandomFontGenerator(new Integer(20), new Integer(20), fontsList);

        WordToImage wordToImage = new ComposedWordToImage(fontGenerator, backgroundGenerator, textPaster);
        this.addFactory(new GimpyFactory(wgen, wordToImage));
    }
}