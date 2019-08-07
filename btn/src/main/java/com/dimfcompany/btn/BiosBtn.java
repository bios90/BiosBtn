package com.dimfcompany.btn;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BiosBtn extends RelativeLayout
{
    private static final String TAG = "BiosBtn";

    private boolean gradient;
    private int gradient_direction;
    private int bg_color;
    private int color1;
    private int color2;
    private int corner_radius;
    private int stroke_width;
    private int stroke_color;
    private int text_color;
    private int text_size;
    private int text_padding_horizontal;
    private int padding;
    private Typeface typeface;
    private String text;
    private String faw_text;
    private int icon_padding_horizontal;
    private int icon_padding_vertical;
    private int icon_size;
    private int icon_color;

    private int btn_type;

    private StateListDrawable drawable;

    private int width;
    private int height;
    private View view;

    public BiosBtn(Context context)
    {
        super(context);
        init(null);
    }

    public BiosBtn(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    public BiosBtn(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BiosBtn(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs)
    {
        initAttributes(attrs);
        initDrawable(attrs);
        initView();
    }

    private void initAttributes(@Nullable AttributeSet attrs)
    {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BiosBtn);

        gradient = ta.getBoolean(R.styleable.BiosBtn_gradient, false);
        if (gradient)
        {
            if (ta.hasValue(R.styleable.BiosBtn_color_1))
            {
                color1 = ta.getColorStateList(R.styleable.BiosBtn_color_1).getDefaultColor();
            }
            else
            {
                color1 = Color.parseColor("#ee0979");
            }

            if (ta.hasValue(R.styleable.BiosBtn_color_2))
            {
                color2 = ta.getColorStateList(R.styleable.BiosBtn_color_2).getDefaultColor();
            }
            else
            {
                color2 = Color.parseColor("#ff6a00");
            }

            gradient_direction = ta.getInteger(R.styleable.BiosBtn_gradient_direction, 0);
        }
        else
        {
            if (ta.hasValue(R.styleable.BiosBtn_background_color))
            {
                bg_color = ta.getColorStateList(R.styleable.BiosBtn_background_color).getDefaultColor();
            }
            else
            {
                bg_color = Color.parseColor("#16BFFD");
            }
        }

        corner_radius = ta.getDimensionPixelSize(R.styleable.BiosBtn_corner_radius, dp2pxInt(8));
        stroke_width = ta.getDimensionPixelSize(R.styleable.BiosBtn_stroke_width, dp2pxInt(2));

        if (ta.hasValue(R.styleable.BiosBtn_stroke_color))
        {
            stroke_color = ta.getColorStateList(R.styleable.BiosBtn_stroke_color).getDefaultColor();
        }
        else
        {
            if (gradient)
            {
                stroke_color = ColorUtils.blendARGB(color1, color2, 0.5f);
            }
            else
            {
                stroke_color = manipulateColor(bg_color, 0.8f);
            }
        }

        if (ta.hasValue(R.styleable.BiosBtn_text_color))
        {
            text_color = ta.getColorStateList(R.styleable.BiosBtn_text_color).getDefaultColor();
        }
        else
        {
            text_color = Color.parseColor("#ffffff");
        }

        text_size = ta.getDimensionPixelSize(R.styleable.BiosBtn_android_textSize, dp2pxInt(14));
        text_padding_horizontal = ta.getDimensionPixelSize(R.styleable.BiosBtn_text_padding_horizontal, dp2pxInt(16));
        padding = ta.getDimensionPixelSize(R.styleable.BiosBtn_android_padding, dp2pxInt(10));

        typeface = Typeface.createFromAsset(getContext().getAssets(), "monbold.ttf");
        if (ta.hasValue(R.styleable.BiosBtn_btn_font))
        {
            try
            {
                String font_str = ta.getString(R.styleable.BiosBtn_btn_font);
                typeface = Typeface.createFromAsset(getContext().getAssets(), font_str);
            }
            catch (Exception e)
            {
                Log.e(TAG, "initAttributes: Error while setting font, will use default");
            }

        }


        text = ta.getString(R.styleable.BiosBtn_text);
        if (text == null)
        {
            text = "Super Btn";
        }

        btn_type = ta.getInt(R.styleable.BiosBtn_btn_type, 0);

        if (ta.hasValue(R.styleable.BiosBtn_faw_text))
        {
            faw_text = ta.getString(R.styleable.BiosBtn_faw_text);
        }
        else
        {
            faw_text = "\uf164";
        }

        icon_padding_horizontal = ta.getDimensionPixelSize(R.styleable.BiosBtn_icon_padding_horizontal, dp2pxInt(4));
        icon_padding_vertical = ta.getDimensionPixelSize(R.styleable.BiosBtn_icon_padding_vertical, dp2pxInt(4));
        icon_size = ta.getDimensionPixelSize(R.styleable.BiosBtn_icon_size_faw, dp2pxInt(18));
        if (ta.hasValue(R.styleable.BiosBtn_icon_color))
        {
            icon_color = ta.getColorStateList(R.styleable.BiosBtn_icon_color).getDefaultColor();
        }
        else
        {
            icon_color = Color.parseColor("#ffffff");
        }

        ta.recycle();
    }

    private void initDrawable(AttributeSet attrs)
    {
        drawable = new StateListDrawable();

        drawable.addState(new int[]{android.R.attr.state_focused}, getSingleDrawable(true));
        drawable.addState(new int[]{android.R.attr.state_pressed}, getSingleDrawable(true));
        drawable.addState(new int[]{}, getSingleDrawable(false));
    }

    private void initView()
    {
        TextView tv_icon_left;
        TextView tv_icon_right;
        TextView tv_text;
        RelativeLayout root_view;

        int icon_width = 0;

        switch (btn_type)
        {
            case 0:
                view = inflate(getContext(), R.layout.la_no_icon, this);
                tv_text = view.findViewById(R.id.tv_text);
                break;
            case 1:
                view = inflate(getContext(), R.layout.la_icon_end, this);

                tv_text = view.findViewById(R.id.tv_text);
                tv_icon_left = view.findViewById(R.id.tv_icon_left);
                tv_icon_right = view.findViewById(R.id.tv_icon_right);

                tv_icon_left.setTextColor(getTextIconColor(true));
                tv_icon_left.setText(faw_text);
                tv_icon_right.setText(faw_text);
                tv_icon_left.setPadding(icon_padding_horizontal, icon_padding_vertical, icon_padding_horizontal, icon_padding_vertical);
                tv_icon_right.setPadding(icon_padding_horizontal, icon_padding_vertical, icon_padding_horizontal, icon_padding_vertical);
                tv_icon_right.setVisibility(INVISIBLE);

                tv_icon_left.setTextSize(TypedValue.COMPLEX_UNIT_PX, icon_size);
                tv_icon_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, icon_size);
                tv_text.setPadding(text_padding_horizontal, 0, text_padding_horizontal, 0);
                break;

            case 2:
                view = inflate(getContext(), R.layout.la_icon_end, this);

                tv_text = view.findViewById(R.id.tv_text);
                tv_icon_left = view.findViewById(R.id.tv_icon_left);
                tv_icon_right = view.findViewById(R.id.tv_icon_right);

                tv_icon_right.setTextColor(getTextIconColor(true));
                tv_icon_right.setText(faw_text);
                tv_icon_left.setText(faw_text);
                tv_icon_right.setPadding(icon_padding_horizontal, icon_padding_vertical, icon_padding_horizontal, icon_padding_vertical);
                tv_icon_left.setPadding(icon_padding_horizontal, icon_padding_vertical, icon_padding_horizontal, icon_padding_vertical);
                tv_icon_left.setVisibility(INVISIBLE);

                tv_icon_left.setTextSize(TypedValue.COMPLEX_UNIT_PX, icon_size);
                tv_icon_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, icon_size);
                tv_text.setPadding(text_padding_horizontal, 0, text_padding_horizontal, 0);
                break;

            case 3:
                view = inflate(getContext(), R.layout.la_icon_center, this);

                tv_text = view.findViewById(R.id.tv_text);
                tv_icon_left = view.findViewById(R.id.tv_icon_left);
                tv_icon_right = view.findViewById(R.id.tv_icon_right);

                tv_icon_left.setTextColor(getTextIconColor(true));
                tv_icon_left.setText(faw_text);
                tv_icon_right.setText(faw_text);
                tv_icon_left.setPadding(icon_padding_horizontal, icon_padding_vertical, icon_padding_horizontal, icon_padding_vertical);
                tv_icon_right.setPadding(icon_padding_horizontal, icon_padding_vertical, icon_padding_horizontal, icon_padding_vertical);
                tv_icon_right.setVisibility(INVISIBLE);

                tv_icon_left.setTextSize(TypedValue.COMPLEX_UNIT_PX, icon_size);
                tv_icon_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, icon_size);
                tv_text.setPadding(text_padding_horizontal, 0, text_padding_horizontal, 0);
                break;

            case 4:
                view = inflate(getContext(), R.layout.la_icon_center, this);

                tv_text = view.findViewById(R.id.tv_text);
                tv_icon_left = view.findViewById(R.id.tv_icon_left);
                tv_icon_right = view.findViewById(R.id.tv_icon_right);

                tv_icon_right.setTextColor(getTextIconColor(true));
                tv_icon_right.setText(faw_text);
                tv_icon_left.setText(faw_text);
                tv_icon_right.setPadding(icon_padding_horizontal, icon_padding_vertical, icon_padding_horizontal, icon_padding_vertical);
                tv_icon_left.setPadding(icon_padding_horizontal, icon_padding_vertical, icon_padding_horizontal, icon_padding_vertical);
                tv_icon_left.setVisibility(INVISIBLE);

                tv_icon_left.setTextSize(TypedValue.COMPLEX_UNIT_PX, icon_size);
                tv_icon_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, icon_size);
                tv_text.setPadding(text_padding_horizontal, 0, text_padding_horizontal, 0);
                break;

            default:
                throw new RuntimeException("Eeoeoe type");
        }

        view.setBackground(drawable);


        tv_text.setText(text);

        tv_text.setTextColor(getTextIconColor(false));
        tv_text.setTypeface(typeface);
        tv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, text_size);

        view.setPadding(padding, padding, padding, padding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = view.getLayoutParams().width;
        int height = view.getLayoutParams().height;

        View rootview = view.findViewById(R.id.la_root);

        rootview.getLayoutParams().width = width;
        rootview.getLayoutParams().height = height;
    }

    private static int dp2pxInt(float dp)
    {
        return (int) dp2px(dp);
    }

    private static float dp2px(float dp)
    {
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public GradientDrawable getSingleDrawable(boolean dark)
    {
        GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.btn_bg);
        drawable.setCornerRadii(new float[]{corner_radius, corner_radius, corner_radius, corner_radius, corner_radius, corner_radius, corner_radius, corner_radius});
        drawable.setStroke(stroke_width, stroke_color);

        if (!gradient)
        {
            float factor = 1f;
            if (dark)
            {
                factor = 0.8f;
            }

            drawable.setColor(manipulateColor(bg_color, factor));
        }
        else
        {
            GradientDrawable.Orientation orientation = null;
            if (gradient_direction == 0)
            {
                orientation = GradientDrawable.Orientation.BOTTOM_TOP;
            }
            else
            {
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
            }

            int color1_fixed = color1;
            int color2_fixed = color2;

            if (dark)
            {
                color1_fixed = manipulateColor(color1, 0.8f);
                color2_fixed = manipulateColor(color2, 0.8f);
            }

            drawable.setOrientation(orientation);
            drawable.setColors(new int[]{color1_fixed, color2_fixed});
        }

        return drawable;
    }

    private ColorStateList getTextIconColor(boolean icon)
    {
        int color;
        if (icon)
        {
            color = icon_color;
        }
        else
        {
            color = text_color;
        }

        int[][] states = new int[][]
                {
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{}
                };


        int[] colors = new int[]
                {
                        manipulateColor(color, 0.8f),
                        manipulateColor(color, 0.8f),
                        color
                };

        return new ColorStateList(states, colors);
    }

    public static int manipulateColor(int color, float factor)
    {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    private static void setLeftRightMargin(View view, int left, int right)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        setMargins(view, left, params.topMargin, right, params.bottomMargin);
    }

    private static void setMargins(View view, int left, int top, int right, int bottom)
    {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.setMargins(left, top, right, bottom);
        view.setLayoutParams(params);
    }
}
