package top.wangudiercai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChatListView extends ListView {
    public ChatListView(Context context) {
        super(context);
    }

    public ChatListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
