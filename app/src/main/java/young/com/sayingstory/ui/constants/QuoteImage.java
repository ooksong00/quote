package young.com.sayingstory.ui.constants;

import java.util.Random;

import young.com.sayingstory.R;

public class QuoteImage {

    public static final int BACKGROUND_IMG_COUNT = 60;
    public static final int COMMENT_USER_COUNT = 9;

    private static final Random RAMDOM = new Random();

    public static int getCommentUserDrawable() {
        switch (RAMDOM.nextInt(COMMENT_USER_COUNT)) {
            default:
            case 0:
                return R.drawable.bad_pig;
            case 1:
                return R.drawable.tuqui;
            case 2:
                return R.drawable.support;
            case 3:
                return R.drawable.meet;
            case 4:
                return R.drawable.rubber;
            case 5:
                return R.drawable.panda;
            case 6:
                return R.drawable.lion;
            case 7:
                return R.drawable.dog;
            case 8:
                return R.drawable.cat;
        }
    }

    public static final int[] quoteBackgroundDrawable = {
            R.drawable.quote_img_01, R.drawable.quote_img_02, R.drawable.quote_img_04, //1
            R.drawable.quote_img_05, R.drawable.quote_img_06, R.drawable.quote_img_72, //2
            R.drawable.quote_img_09, R.drawable.quote_img_11, R.drawable.quote_img_12, //3
            R.drawable.quote_img_14, R.drawable.quote_img_19, R.drawable.quote_img_71, //4
            R.drawable.quote_img_21, R.drawable.quote_img_22, R.drawable.quote_img_24, //5
            R.drawable.quote_img_25, R.drawable.quote_img_27, R.drawable.quote_img_73,//6
            R.drawable.quote_img_29, R.drawable.quote_img_30, R.drawable.quote_img_75, //7
            R.drawable.quote_img_31, R.drawable.quote_img_32, R.drawable.quote_img_33, //8
            R.drawable.quote_img_34, R.drawable.quote_img_37, R.drawable.quote_img_76,//9
            R.drawable.quote_img_38, R.drawable.quote_img_40, R.drawable.quote_img_74, //10

            R.drawable.quote_img_41, R.drawable.quote_img_42, R.drawable.quote_img_43, //1
            R.drawable.quote_img_44, R.drawable.quote_img_45, R.drawable.quote_img_46, //2
            R.drawable.quote_img_47, R.drawable.quote_img_48, R.drawable.quote_img_49,  //3
            R.drawable.quote_img_50, R.drawable.quote_img_51, R.drawable.quote_img_52,  //4
            R.drawable.quote_img_53, R.drawable.quote_img_54, R.drawable.quote_img_55,//5
            R.drawable.quote_img_56, R.drawable.quote_img_57, R.drawable.quote_img_58,//6
            R.drawable.quote_img_59, R.drawable.quote_img_60, R.drawable.quote_img_61,//7
            R.drawable.quote_img_62, R.drawable.quote_img_63, R.drawable.quote_img_64,//8
            R.drawable.quote_img_66, R.drawable.quote_img_72, R.drawable.quote_img_73, //9
            R.drawable.quote_img_68, R.drawable.quote_img_73, R.drawable.quote_img_76,//10

    };


}
