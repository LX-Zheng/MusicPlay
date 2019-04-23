package com.example.apple.mytable.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.apple.mytable.R;

public class SideBar extends View {
    //侧边触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private static String[] b = {"A","B","C","D","E","F","G","H","I",
                                 "J","K","L","M","N","O","P","Q","R",
                                 "S","T","U","V","W","X","Y","Z","#"};
    private int choose = -1;
    private Paint paint = new Paint();
    private TextView textDialog;

    /**
     * 为SideBar设置显示字母的TextView
     * @param textDialog
     */
    public void setTextView(TextView textDialog){
        this.textDialog = textDialog;
    }

    public SideBar(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public SideBar(Context context){
        super(context);
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        //获取每个子母的高度
        int singleHeight = height/b.length;
        for(int i=0;i<b.length;i++){
            paint.setColor(Color.rgb(33,65,98));
            //paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(30);
            if(i == choose){
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            //x坐标等于中间-字符串宽度的一半
            float xPos = width/2 - paint.measureText(b[i])/2;
            float yPos = singleHeight * i +singleHeight;
            canvas.drawText(b[i],xPos,yPos,paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        //点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数
        final int c = (int)(y/getHeight()*b.length);
        switch (action){
            case MotionEvent.ACTION_UP:
                setBackground(new ColorDrawable(0x00000000));
                choose = -1;
                invalidate();
                if(textDialog != null){
                    textDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                setBackgroundResource(R.drawable.sidebar_background);
                if(oldChoose != c){
                    if(c>=0 && c<b.length){
                        if(listener != null){
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if(textDialog != null){
                            textDialog.setText(b[c]);
                            textDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 触摸事件
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener){
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener{
        void onTouchingLetterChanged(String s);
    }

}
